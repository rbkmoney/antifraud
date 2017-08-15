package com.rbkmoney.antifraud.service;

import com.rbkmoney.antifraud.domain.tables.pojos.Payment;
import com.rbkmoney.damsel.domain.BankCard;
import com.rbkmoney.damsel.domain.Cash;
import com.rbkmoney.damsel.domain.ClientInfo;
import com.rbkmoney.damsel.domain.ContactInfo;
import com.rbkmoney.damsel.proxy_inspector.Context;

public class ProtocolConverter {
    public static Payment convertToDomain(Context context) {
        Payment afPayment = new Payment();
        Cash cash = context.getPayment().getPayment().getCost();
        afPayment.setAmount(cash.getAmount());
        afPayment.setCurrency(cash.getCurrency().getSymbolicCode());
        BankCard card = context.getPayment().getPayment().getPayer().getPaymentTool().getBankCard();
        afPayment.setCardToken(card.getToken());
        afPayment.setCardMask(card.getMaskedPan());//todo pan + bin?

        ContactInfo contactInfo = context.getPayment().getPayment().getPayer().getContactInfo();
        afPayment.setClientEmail(contactInfo.getEmail());

        ClientInfo clientInfo = context.getPayment().getPayment().getPayer().getClientInfo();
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
}
