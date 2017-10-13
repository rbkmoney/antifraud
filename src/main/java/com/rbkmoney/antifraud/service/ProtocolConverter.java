package com.rbkmoney.antifraud.service;

import com.rbkmoney.antifraud.domain.tables.pojos.Payment;
import com.rbkmoney.damsel.domain.*;
import com.rbkmoney.damsel.proxy_inspector.Context;

public class ProtocolConverter {
    public static Payment convertToDomain(Context context) {
        Payment afPayment = new Payment();
        Cash cash = context.getPayment().getPayment().getCost();
        afPayment.setAmount(cash.getAmount());
        afPayment.setCurrency(cash.getCurrency().getSymbolicCode());
        BankCard card = getBankCard(context);
        afPayment.setCardToken(card.getToken());
        afPayment.setCardMask(card.getBin() + "******" + card.getMaskedPan());

        ContactInfo contactInfo = getContactInfo(context);
        afPayment.setClientEmail(contactInfo.getEmail());

        ClientInfo clientInfo = getClientInfo(context);
        afPayment.setClientIp(clientInfo.getIpAddress());
        afPayment.setClientFingerprint(clientInfo.getFingerprint());

        afPayment.setInvoiceId(context.getPayment().getInvoice().getId());
        afPayment.setPaymentId(context.getPayment().getPayment().getId());

        afPayment.setDescription(context.getPayment().getInvoice().getDetails().getDescription());
        afPayment.setPartyId(context.getPayment().getParty().getPartyId());
        afPayment.setShopId(String.valueOf(context.getPayment().getShop().getId()));

        afPayment.setShopName(context.getPayment().getShop().getDetails().getName());
        afPayment.setShopUrl(context.getPayment().getShop().getLocation().getUrl());
        return afPayment;
    }

    public static BankCard getBankCard(Context context) {
        Payer payer = context.getPayment().getPayment().getPayer();
        if (payer.isSetCustomer()) {
            if (payer.getCustomer().getPaymentTool().isSetBankCard()) {
                return payer.getCustomer().getPaymentTool().getBankCard();
            }
        } else if (payer.isSetPaymentResource()) {
            if (payer.getPaymentResource().getResource().getPaymentTool().isSetBankCard()) {
                return payer.getPaymentResource().getResource().getPaymentTool().getBankCard();
            }
        }
        return null;
    }

    public static ContactInfo getContactInfo(Context context) {
        if (context.getPayment().getPayment().getPayer().isSetPaymentResource()) {
            return context.getPayment().getPayment().getPayer().getPaymentResource().getContactInfo();
        }
        return null;
    }

    public static ClientInfo getClientInfo(Context context) {
        if (context.getPayment().getPayment().getPayer().isSetPaymentResource()) {
            return context.getPayment().getPayment().getPayer().getPaymentResource().getResource().getClientInfo();
        }
        return null;
    }
}
