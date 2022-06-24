package com.mphasis.cqrs.os;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mphasis.cqrs.os.command.exceptions.OrderServiceEventsExceptionHandler;
import org.axonframework.config.EventProcessingConfigurer;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OrderServiceCqrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceCqrsApplication.class, args);
    }

    @Bean
    public DozerBeanMapper dozerBeanMapper() {
        return new DozerBeanMapper();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Autowired
    public void configure(EventProcessingConfigurer configurer) {
        configurer.registerListenerInvocationErrorHandler("order-create", configuration -> new OrderServiceEventsExceptionHandler());
    }
}
