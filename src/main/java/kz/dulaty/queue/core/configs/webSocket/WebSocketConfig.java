package kz.dulaty.queue.core.configs.webSocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Bean
    public ThreadPoolTaskScheduler wsTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(8);
        scheduler.setThreadNamePrefix("ws-heartbeat-");
        scheduler.initialize();
        return scheduler;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic")
                .setTaskScheduler(wsTaskScheduler())
                .setHeartbeatValue(new long[]{25000, 25000});
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");

        // Local dev: /ws-sockjs (Spring Boot serves directly)
        registry.addEndpoint("/ws-sockjs")
                .setAllowedOriginPatterns("*")
                .withSockJS()
                .setHeartbeatTime(25000)
                .setDisconnectDelay(5000);

        // Production: /api/ws-sockjs (Nginx proxies /api/ws-sockjs → Spring Boot keeping full path)
        registry.addEndpoint("/api/ws-sockjs")
                .setAllowedOriginPatterns("*")
                .withSockJS()
                .setHeartbeatTime(25000)
                .setDisconnectDelay(5000);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(64 * 1024);       // 64 KB — достаточно для TicketDto
        registration.setSendBufferSizeLimit(512 * 1024);   // 512 KB буфер на клиента
        registration.setSendTimeLimit(15_000);              // 15s таймаут отправки
    }

    /**
     * Входящий канал — поток для обработки сообщений от клиентов.
     * При 2000+ подключениях нужен достаточный пул.
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.taskExecutor(inboundExecutor());
    }

    /**
     * Исходящий канал — поток для отправки сообщений клиентам.
     * Критически важен при broadcast на 2000+ клиентов.
     */
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor(outboundExecutor());
    }

    @Bean
    public ThreadPoolTaskExecutor inboundExecutor() {
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        ex.setCorePoolSize(8);
        ex.setMaxPoolSize(32);
        ex.setQueueCapacity(1000);
        ex.setThreadNamePrefix("ws-in-");
        ex.initialize();
        return ex;
    }

    @Bean
    public ThreadPoolTaskExecutor outboundExecutor() {
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        ex.setCorePoolSize(16);   // Больше потоков на отправку — broadcast идёт параллельно
        ex.setMaxPoolSize(64);
        ex.setQueueCapacity(5000);
        ex.setThreadNamePrefix("ws-out-");
        ex.initialize();
        return ex;
    }
}
