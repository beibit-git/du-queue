package kz.dulaty.queue.core.configs.webSocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Пул потоков для heartbeat-планировщика.
     * Увеличен с 1 до 4 — не создаёт узкое место при большом числе подключений.
     */
    @Bean
    public ThreadPoolTaskScheduler wsTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(4);
        scheduler.setThreadNamePrefix("ws-heartbeat-");
        scheduler.initialize();
        return scheduler;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic")
                .setTaskScheduler(wsTaskScheduler())
                // 25s heartbeat — лучше переживает мобильные сети и прокси
                .setHeartbeatValue(new long[]{25000, 25000});
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Native WebSocket endpoint
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");

        // SockJS fallback (для браузеров/прокси без нативного WS)
        registry.addEndpoint("/ws-sockjs")
                .setAllowedOriginPatterns("*")
                .withSockJS()
                .setHeartbeatTime(25000)
                // Retry таймаут SockJS до reconnect
                .setDisconnectDelay(5000);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        // Макс. размер сообщения 128 KB (достаточно для TicketDto)
        registration.setMessageSizeLimit(128 * 1024);
        // Буфер отправки 512 KB
        registration.setSendBufferSizeLimit(512 * 1024);
        // Таймаут отправки 20s (не блокирует поток бесконечно)
        registration.setSendTimeLimit(20_000);
    }
}
