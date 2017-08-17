package com.rbkmoney.antifraud.thirdparty;

import com.rbkmoney.antifraud.domain.tables.pojos.Payment;
import com.rbkmoney.damsel.domain.RiskScore;
import com.rbkmoney.damsel.proxy_inspector.InspectorProxySrv;
import com.rbkmoney.woody.thrift.impl.http.THSpawnClientBuilder;
import org.apache.thrift.TException;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static com.rbkmoney.antifraud.thirdparty.AfServiceTest.createContext;

@Ignore
public class ThirdPartyTest {
    AfService service = new AfService("https://rbkmoney.antifraud.link/PreAuthorization.ashx", "ya.arc2011@yandex.ru", "Rbkmoney_2016__", 1, 1000, 7000);
    @Test
    public void  test1() {
        testLgbtNew();
        //testLgbtNew();
        //testLgbtNew();
    }

    public void testLgbtOld() {
        Payment payment = new Payment();
        payment.setInvoiceId(System.currentTimeMillis()+"");
        payment.setPaymentId(System.currentTimeMillis()+"");
        payment.setDescription("gays donation");
        payment.setClientFingerprint("11111111111111111111111111111111111112");
        payment.setClientIp("192.42.116.16");
        payment.setClientEmail("v.pankrashkin@rbkmoney.com");
        payment.setCardMask("424242******4242");
        //payment.setCardMask("411111******1111");
        payment.setCardToken("477bba133c182267fe5f086924abdc5db71f77bfc27f01f2843f2cdc69d89f05");
        payment.setAmount(90000l);
        payment.setShopId("");
        payment.setPartyId("2033985");
        payment.setShopName("lgbtnet.org");
        payment.setShopUrl("https://www.lgbtnet.org/");
        payment.setCurrency("RUB");

        System.out.println(service.inspect(payment)
        );
    }

    public void testLgbtNew() {
        Payment payment = new Payment();
        payment.setInvoiceId(System.currentTimeMillis()+"");
        payment.setPaymentId(System.currentTimeMillis()+"");
        payment.setDescription("gays donation");
        payment.setClientFingerprint("11111111111111111111111111111111111113");
        payment.setClientIp("192.42.116.17");
        payment.setClientEmail("123@rbkmoney.com");
        payment.setCardMask("424242******4243");
        //payment.setCardMask("411111******1111");
        payment.setCardToken("477bba133c182267fe5f086924abdc5db71f77bfc27f01f2843f2cdc69d89f0f");
        payment.setAmount(90000l);
        payment.setShopId("2");
        payment.setPartyId("6954b4d1-f39f-4cc1-8843-eae834e6f849");
        payment.setShopName("help.lgbtnet.org");
        payment.setShopUrl("https://help.lgbtnet.org/");
        payment.setCurrency("RUB");

        System.out.println(service.inspect(payment)
        );
    }

    @Test
    public void testLoad() throws URISyntaxException {
        InspectorProxySrv.Iface client = new THSpawnClientBuilder().withAddress(new URI("http://localhost:" + 8022 + "/inspector")).withNetworkTimeout(0).build(InspectorProxySrv.Iface.class);
        for (int i = 0; i < 1000; i++) {
            try {
                RiskScore riskScore = client.inspectPayment(createContext());
            } catch (TException e) {
                e.printStackTrace();
            }

        }
    }
}
