package com.rbkmoney.antifraud.thirdparty;

import com.rbkmoney.antifraud.domain.tables.pojos.Payment;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModelConverter {
    public static Map<String, Object> convertToPreAuthorization(Payment preAuthReq) {
        Map<String, Object> model = new HashMap<>();
        model.put("merchant", convertToMerchantModel(preAuthReq));
        Object customer = convertToCustomerModel(preAuthReq);
        if (customer != null) {
            model.put("customer", customer);
        }
        Object device = convertToDeviceModel(preAuthReq);
        if (device != null) {
            model.put("device", device);
        }
        model.put("card", convertToCardModel(preAuthReq));
        model.put("order", convertToOrderModel(preAuthReq));
        return model;
    }

    public static Map<String, Object> convertToMerchantModel(Payment payment) {
        Map<String, Object> model = new HashMap<>();
        model.put("name", payment.getShopName());
        model.put("siteUrl", payment.getShopUrl());
        model.put("processingMerchantId", payment.getShopId());
        return model;
    }

    public static Map<String, Object> convertToCustomerModel(Payment payment) {
        Map<String, Object> model = new HashMap<>();
        Optional.ofNullable(payment.getClientEmail()).ifPresent(v -> model.put("email", v));
        Optional.ofNullable(payment.getClientIp()).ifPresent(v -> model.put("ip", v));
        return model.isEmpty() ? null : model;
    }

    public static Map<String, Object> convertToDeviceModel(Payment payment) {
        Map<String, Object> model = new HashMap<>();
        Optional.ofNullable(payment.getClientFingerprint()).ifPresent(v -> model.put("machineId", v));
        return model.isEmpty() ? null : model;
    }

    public static Map<String, Object> convertToCardModel(Payment payment) {
        Map<String, Object> model = new HashMap<>();
        model.put("number", payment.getCardToken());
        model.put("maskNumber", payment.getCardMask());
        return model;
    }

    public static Map<String, Object> convertToOrderModel(Payment payment) {
        Map<String, Object> model = new HashMap<>();
        model.put("id", payment.getInvoiceId() + "." + payment.getPaymentId());
        model.put("description", Optional.ofNullable(payment.getDescription()).orElse(""));
        model.put("amount", new BigDecimal(payment.getAmount()).movePointLeft(2));//todo get minor value from DM conf
        model.put("currency", "RUR");//Third party old processing bug, must be RUB but it's not recognised. Must be fixed in new processing version
        return model;
    }

    public static AfResponse convertFromPreAuthorization(Map<String, Object> preAuthResp) {
        Map<String, Object> narrowedMap = preAuthResp.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toLowerCase(), e -> e.getValue()));
        String tmp = String.valueOf(narrowedMap.get("response"));
        AfResponse.Status respStatus = AfResponse.Status.valueOfKey(tmp);
        Integer resultCode = Optional.ofNullable(narrowedMap.get("resultcode")).map(rc -> Integer.parseInt(String.valueOf(rc))).orElse(null);
        Integer declineCode = Optional.ofNullable(narrowedMap.get("declinecode")).map(rc -> Integer.parseInt(String.valueOf(rc))).orElse(null);
        return new AfResponse(respStatus, resultCode, declineCode);
    }
}
