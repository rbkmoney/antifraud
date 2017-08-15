package com.rbkmoney.antifraud.thirdparty;

import com.rbkmoney.antifraud.domain.tables.pojos.Payment;
import org.junit.Ignore;
import org.junit.Test;

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
}
