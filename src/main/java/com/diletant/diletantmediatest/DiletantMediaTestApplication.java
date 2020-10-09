package com.diletant.diletantmediatest;

import com.diletant.diletantmediatest.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class DiletantMediaTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiletantMediaTestApplication.class, args);
    }

}
