package com.rbkmoney.antifraud.config;

import com.rbkmoney.damsel.proxy_inspector.InspectorProxySrv;
import com.rbkmoney.woody.thrift.impl.http.THClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class ExternalServiceConfig {

    @Value("${fraud.inspect.url}")
    private String SERVICE_URL;

    @Bean
    public InspectorProxySrv.Iface inspectorProxySrv() throws URISyntaxException {
        THClientBuilder clientBuilder = new THClientBuilder()
                .withAddress(new URI(SERVICE_URL))
                .withNetworkTimeout(300000);
        return clientBuilder.build(InspectorProxySrv.Iface.class);
    }

}
