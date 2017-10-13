package com.rbkmoney.antifraud.service;

import com.rbkmoney.damsel.base.InvalidRequest;
import com.rbkmoney.damsel.proxy_inspector.Context;

import java.util.Arrays;

/**
 * Created by vpankrashkin on 13.10.17.
 */
public class ProtocolValidator {
    public static void validate(Context context) throws InvalidRequest {
        if (ProtocolConverter.getBankCard(context) == null) {
            throw new InvalidRequest(Arrays.asList("BankCard is not specified"));
        }
    }
}
