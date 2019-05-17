package com.rbkmoney.antifraud.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Component
public class PartyFilterService {

    private Set<String> partyIds = new ConcurrentSkipListSet<>();

    public void updateParties(List<String> partyIds) {
        this.partyIds.clear();
        this.partyIds.addAll(partyIds);
    }

    public boolean filter(String partyId) {
        return partyIds.contains(partyId);
    }

}
