package com.rbkmoney.antifraud.thirdparty;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.antifraud.domain.tables.pojos.Payment;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AfService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final TypeReference<HashMap<String, Object>> RESP_TYPE_REF = new TypeReference<HashMap<String, Object>>() {
    };


    private final TPClient client;

    public AfService(TPClient client) {
        this.client = client;
    }

    public AfResponse inspect(Payment payment) throws ThirdPartyException {
        try {
            Map<String, Object> reqModel = ModelConverter.convertToPreAuthorization(payment);
            ObjectMapper mapper = new ObjectMapper();
            String requestStr = mapper.writeValueAsString(reqModel);
            String responseStr = client.callInspection(requestStr);
            Map<String, Object> respModel = mapper.readValue(responseStr, RESP_TYPE_REF);
            return ModelConverter.convertFromPreAuthorization(respModel);
        } catch (Exception e) {
            throw new ThirdPartyException(e);
        }
    }

    public interface TPClient {
        String callInspection(String request) throws Exception;
    }

    public static class OKHttpTPClient implements TPClient {
        private final Logger log = LoggerFactory.getLogger(this.getClass());

        private final String preAuthUrl;
        private final OkHttpClient client;

        public OKHttpTPClient(String preAuthUrl, String user, String password, int maxIdleConnections, int keepAliveDuration, int timeout) {
            this.preAuthUrl = preAuthUrl;
            client = new OkHttpClient.Builder()
                    .addInterceptor(new BasicAuthInterceptor(user, password))
                    .connectionPool(new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.MILLISECONDS))
                    .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                    .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                    .readTimeout(timeout, TimeUnit.MILLISECONDS)
                    .build();
        }


        @Override
        public String callInspection(String requestStr) throws Exception {
            log.info("Sending request: {} to url {}", requestStr, preAuthUrl);
            Response response = client.newCall(
                    new Request.Builder()
                            .url(preAuthUrl)
                            .post(RequestBody.create(MediaType.parse("application/json"), requestStr)).build()
            ).execute();
            String responseStr = response.body().string();//todo add resp size limits
            log.info("Received response: {}", responseStr);
            return responseStr;
        }
    }


    private static class BasicAuthInterceptor implements Interceptor {

        private String credentials;

        public BasicAuthInterceptor(String user, String password) {
            this.credentials = Credentials.basic(user, password);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request authenticatedRequest = request.newBuilder()
                    .header("Authorization", credentials).build();
            return chain.proceed(authenticatedRequest);
        }

    }
}
