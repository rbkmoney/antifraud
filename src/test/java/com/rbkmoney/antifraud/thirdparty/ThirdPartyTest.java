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

    @Test
    public void test() {
        AfService service = new AfService("https://rbkmoneytest.antifraud.link/PreAuthorization.ashx", "ya.arc2011@yandex.ru", "Rbkmoney_2016", 1, 1000, 7000);
        Payment payment = new Payment();
        payment.setDescription("drugs guns murder");
        payment.setClientFingerprint("11111111111111111111111111111111111111");
        payment.setClientIp("192.42.116.16");
        payment.setClientEmail("v.pankrashkin@rbkmoney.com");
        payment.setCardMask("424242******4242");
        //payment.setCardMask("411111******1111");
        payment.setCardToken("477bba133c182267fe5f086924abdc5db71f77bfc27f01f2843f2cdc69d89f05");
        payment.setAmount(9000000000000000000L);
        payment.setShopId("2035728a");
        payment.setPartyId("");
        payment.setShopName("pizza-sushi");
        payment.setShopUrl("http://www.pizza-sushi.com/");
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
