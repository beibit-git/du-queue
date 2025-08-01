package kz.dulaty.queue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DuQueueApplication {

    public static void main(String[] args) {
        SpringApplication.run(DuQueueApplication.class, args);
    }

}
