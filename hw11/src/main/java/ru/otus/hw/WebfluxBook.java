package ru.otus.hw;


import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;


@SpringBootApplication
@EnableConfigurationProperties
public class WebfluxBook {
    public static void main(String[] args) {
     SpringApplication.run(WebfluxBook.class);
    }

}

