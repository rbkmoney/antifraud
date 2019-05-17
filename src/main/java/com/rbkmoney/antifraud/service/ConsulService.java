package com.rbkmoney.antifraud.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orbitz.consul.KeyValueClient;
import com.orbitz.consul.cache.KVCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

@Slf4j
@Service
@ConditionalOnProperty(value = "consul.enabled", havingValue = "true")
public class ConsulService {

    private final KeyValueClient keyValueClient;

    private final ObjectMapper objectMapper;

    private final String keyPath;

    private final KVCache cache;

    private final PartyFilterService partyFilterService;

    public ConsulService(
            KeyValueClient keyValueClient,
            ObjectMapper objectMapper,
            PartyFilterService partyFilterService,
            @Value("${consul.keyPath}") String keyPath,
            @Value("${consul.configuration}") Resource defaultConfigResource
    ) {
        this.keyValueClient = keyValueClient;
        this.objectMapper = objectMapper;
        this.partyFilterService = partyFilterService;
        this.keyPath = keyPath;
        this.cache = KVCache.newCache(this.keyValueClient, keyPath);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        cache.addListener(
                newValues -> newValues
                        .values()
                        .stream()
                        .filter(value -> value.getKey().equals(keyPath))
                        .findFirst()
                        .ifPresent(value -> value.getValueAsString().ifPresent(this::uploadFilters))
        );
        cache.start();
    }

    private void uploadFilters(String filters) {
        try {
            log.info("Update filters='{}'", filters);
            partyFilterService.updateParties(objectMapper.readValue(filters, new TypeReference<List<String>>() {
            }));
            log.info("New filters='{}' has been applied!", filters);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @PreDestroy
    public void stop() {
        cache.stop();
    }

}
