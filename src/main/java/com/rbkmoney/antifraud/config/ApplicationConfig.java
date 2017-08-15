package com.rbkmoney.antifraud.config;

import com.rbkmoney.antifraud.service.AntifraudHandler;
import com.rbkmoney.antifraud.thirdparty.AfService;
import com.rbkmoney.damsel.proxy_inspector.InspectorProxySrv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    AfService antifraudService(@Value("${af.url}") String url, @Value("${af.username}") String username, @Value("${af.password}") String password, @Value("${af.conn.maxidle}")int maxIdle, @Value("${af.conn.keepalive}")int keepAlive, @Value("${af.conn.timeout}")int timeout) {
        return new AfService(url, username, password, maxIdle, keepAlive, timeout);
    }

    @Bean
    InspectorProxySrv.Iface antifraudHandler(AfService afService) {
        return new AntifraudHandler(afService);
    }
}
