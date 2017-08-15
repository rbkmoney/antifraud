package com.rbkmoney.antifraud.dao;

import com.rbkmoney.antifraud.domain.tables.pojos.Payment;

public interface PaymentDao {
    boolean savePayment(Payment payment) throws DaoException;
    Payment getPayment(String invoiceId, String paymentId) throws DaoException;
}
