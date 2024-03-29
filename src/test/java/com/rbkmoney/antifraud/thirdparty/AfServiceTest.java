package com.rbkmoney.antifraud.thirdparty;

import com.rbkmoney.antifraud.AntifraudApplication;
import com.rbkmoney.antifraud.config.ApplicationConfig;
import com.rbkmoney.damsel.base.InvalidRequest;
import com.rbkmoney.damsel.domain.*;
import com.rbkmoney.damsel.payment_processing.Customer;
import com.rbkmoney.damsel.proxy_inspector.*;
import com.rbkmoney.damsel.proxy_inspector.Invoice;
import com.rbkmoney.damsel.proxy_inspector.InvoicePayment;
import com.rbkmoney.damsel.proxy_inspector.Party;
import com.rbkmoney.damsel.proxy_inspector.Shop;
import com.rbkmoney.woody.thrift.impl.http.THSpawnClientBuilder;
import org.apache.thrift.TException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = AfServiceTest.Config.class)
public class AfServiceTest {

    @Profile("test")
    @Configuration
    @Import(AntifraudApplication.class)
    public static class Config {

        @Bean
        @Primary
        public AfService.TPClient mockedTPClient() {
            return (request -> "{\"response\": \"Approve\", \"resultcode\":0}");
        }
    }

    @Value("${local.server.port}")
    protected int port;



    @Ignore
    @Test
    public void integrationTest() throws URISyntaxException, TException {
        InspectorProxySrv.Iface client = new THSpawnClientBuilder().withAddress(new URI("http://localhost:" + port + "/inspector")).withNetworkTimeout(0).build(InspectorProxySrv.Iface.class);
        RiskScore riskScore = client.inspectPayment(createContext());
        Assert.assertEquals(RiskScore.low, riskScore);
    }

    @Ignore
    @Test
    public void testLoad() throws URISyntaxException {
        InspectorProxySrv.Iface client = new THSpawnClientBuilder().withAddress(new URI("http://localhost:" + port + "/inspector")).withNetworkTimeout(0).build(InspectorProxySrv.Iface.class);
        for (int i = 0; i < 1000; i++) {
            try {
                RiskScore riskScore = client.inspectPayment(createContext());
            } catch (TException e) {
                e.printStackTrace();
            }

        }
    }

    @Test(expected = InvalidRequest.class)
    public void testPaymentToolValidation() throws TException, URISyntaxException {
        InspectorProxySrv.Iface client = new THSpawnClientBuilder().withAddress(new URI("http://localhost:" + port + "/inspector")).withNetworkTimeout(0).build(InspectorProxySrv.Iface.class);
        Context context = createContext();
        context.getPayment().getPayment().getPayer().getPaymentResource().getResource().getPaymentTool().setPaymentTerminal(new PaymentTerminal(TerminalPaymentProvider.euroset));
        client.inspectPayment(context);
    }

    @Test
    public void testCustomer() throws URISyntaxException, TException {
        InspectorProxySrv.Iface client = new THSpawnClientBuilder().withAddress(new URI("http://localhost:" + port + "/inspector")).withNetworkTimeout(0).build(InspectorProxySrv.Iface.class);
        Context context = createContext();
        context.getPayment().getPayment().setPayer(createCustomerPayer());
        RiskScore riskScore = client.inspectPayment(context);
        Assert.assertEquals(RiskScore.low, riskScore);
    }

    @Test
    public void testRecurrent() throws URISyntaxException, TException {
        InspectorProxySrv.Iface client = new THSpawnClientBuilder().withAddress(new URI("http://localhost:" + port + "/inspector")).withNetworkTimeout(0).build(InspectorProxySrv.Iface.class);
        Context context = createContext();
        context.getPayment().getPayment().setPayer(createRecurrentPayer());
        RiskScore riskScore = client.inspectPayment(context);
        Assert.assertEquals(RiskScore.low, riskScore);
    }

    public static Payer createRecurrentPayer() {
        return Payer.recurrent(new RecurrentPayer(createBankCard(), new RecurrentParentPayment("invoiceId", "paymentId"), new ContactInfo()));
    }

    public static Payer createCustomerPayer() {
        return Payer.customer(new CustomerPayer("custId", "1", "rec_paym_tool", createBankCard(), new ContactInfo()));

    }

    public static PaymentTool createBankCard() {
        return new PaymentTool() {{
            setBankCard(new BankCard(
                    "477bba133c182267fe5f086924abdc5db71f77bfc27f01f2843f2cdc69d89f05",
                    BankCardPaymentSystem.mastercard,
                    "424242",
                    "4242"
            ));
        }};
    }

    public static Context createContext() {
        return new Context(
                new PaymentInfo(
                        new Shop("2035728",
                                new Category("pizza", "no category"),
                                new ShopDetails("pizza-sushi"),
                                new ShopLocation() {{
                                    setUrl("http://www.pizza-sushi.com/");
                                }}
                        ),
                        new InvoicePayment("pId",
                                "",
                                Payer.payment_resource(
                                        new PaymentResourcePayer(new DisposablePaymentResource(createBankCard()), new ContactInfo() {{
                                            setEmail("v.pankrashkin@rbkmoney.com");
                                        }})
                                ),
                                new Cash(
                                        9000000000000000000L,
                                        new CurrencyRef("RUB")
                                )),
                        new Invoice(
                                "iId",
                                "",
                                "",
                                new InvoiceDetails("drugs guns murder")),
                        new Party("ptId")
                )
        );
    }
}
