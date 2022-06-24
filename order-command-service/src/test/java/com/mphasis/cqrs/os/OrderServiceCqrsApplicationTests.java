package com.mphasis.cqrs.os;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

@SpringBootTest
@Slf4j
class OrderServiceCqrsApplicationTests {

    @Test
    void contextLoads() {
        log.info("Starting context load tests....");
        OrderServiceCqrsApplication.main(new String[]{});
    }

    @Bean
    public DozerBeanMapper dozerBeanMapper() {
        return new DozerBeanMapper();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
