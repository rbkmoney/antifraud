package com.rbkmoney.antifraud.service;

import com.rbkmoney.antifraud.domain.tables.pojos.Payment;
import com.rbkmoney.antifraud.thirdparty.AfResponse;
import com.rbkmoney.antifraud.thirdparty.AfService;
import com.rbkmoney.antifraud.thirdparty.ThirdPartyException;
import com.rbkmoney.damsel.base.InvalidRequest;
import com.rbkmoney.damsel.domain.RiskScore;
import com.rbkmoney.damsel.proxy_inspector.Context;
import com.rbkmoney.damsel.proxy_inspector.InspectorProxySrv;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AntifraudHandler implements InspectorProxySrv.Iface{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final AfService service;

    public AntifraudHandler(AfService service) {
        this.service = service;
    }

    @Override
    public RiskScore inspectPayment(Context context) throws InvalidRequest, TException {
        try {
            log.info("New inspection request for invoice:{}, payment: {}", context.getPayment().getInvoice().getId(), context.getPayment().getPayment().getId());
            ProtocolValidator.validate(context);
            Payment payment = ProtocolConverter.convertToDomain(context);
            AfResponse afResponse = service.inspect(payment);
            log.info("Third party result: {}", afResponse);
            RiskScore riskScore;
            if (afResponse.getStatus() == AfResponse.Status.APPROVE && Integer.valueOf(0).equals(afResponse.getResultCode())) {
                riskScore = RiskScore.low;
            } else if (afResponse.getStatus() == AfResponse.Status.DECLINE) {
                riskScore = RiskScore.fatal;
            } else {
                riskScore = RiskScore.high;
            }
            log.info("Inspection result: {}", riskScore);
            return riskScore;
        } catch (Exception e) {
            log.error("Failed to process request: {}", context, e);
            throw e;
        }
    }
}
