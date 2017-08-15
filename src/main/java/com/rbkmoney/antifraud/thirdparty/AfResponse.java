package com.rbkmoney.antifraud.thirdparty;

import java.util.HashMap;
import java.util.Map;

public class AfResponse {
    private final Status status;

    private final Integer resultCode;
    private final Integer declineCode;

    public AfResponse(Status status, Integer resultCode, Integer declineCode) {
        this.status = status;
        this.resultCode = resultCode;
        this.declineCode = declineCode;
    }

    public Status getStatus() {
        return status;
    }


    public Integer getResultCode() {
        return resultCode;
    }

    public Integer getDeclineCode() {
        return declineCode;
    }

    @Override
    public String toString() {
        return "AfResponse{" +
                "status=" + status +
                ", resultCode=" + resultCode +
                ", declineCode=" + declineCode +
                '}';
    }

    public enum Status {
        APPROVE("Approve"),
        DECLINE("Decline"),
        PRE_AUTHORIZATION("PreAuthorization");

        Status(String key) {
            this.key = key;
            Holder.MAP.put(key, this);
        }

        String key;

        public String getKey() {
            return key;
        }

        public static Status valueOfKey(String key) {
            return Holder.MAP.get(key);
        }

        private static class Holder {
            static Map<String, Status> MAP = new HashMap<>();
        }
    }
}
