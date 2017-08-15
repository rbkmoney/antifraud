/*
 * This file is generated by jOOQ.
*/
package com.rbkmoney.antifraud.domain.tables;


import com.rbkmoney.antifraud.domain.Af;
import com.rbkmoney.antifraud.domain.Keys;
import com.rbkmoney.antifraud.domain.tables.records.PaymentRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Payment extends TableImpl<PaymentRecord> {

    private static final long serialVersionUID = 1611622717;

    /**
     * The reference instance of <code>af.payment</code>
     */
    public static final Payment PAYMENT = new Payment();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PaymentRecord> getRecordType() {
        return PaymentRecord.class;
    }

    /**
     * The column <code>af.payment.invoice_id</code>.
     */
    public final TableField<PaymentRecord, String> INVOICE_ID = createField("invoice_id", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>af.payment.payment_id</code>.
     */
    public final TableField<PaymentRecord, String> PAYMENT_ID = createField("payment_id", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>af.payment.party_id</code>.
     */
    public final TableField<PaymentRecord, String> PARTY_ID = createField("party_id", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>af.payment.shop_id</code>.
     */
    public final TableField<PaymentRecord, String> SHOP_ID = createField("shop_id", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>af.payment.client_ip</code>.
     */
    public final TableField<PaymentRecord, String> CLIENT_IP = createField("client_ip", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>af.payment.client_email</code>.
     */
    public final TableField<PaymentRecord, String> CLIENT_EMAIL = createField("client_email", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>af.payment.client_fingerprint</code>.
     */
    public final TableField<PaymentRecord, String> CLIENT_FINGERPRINT = createField("client_fingerprint", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>af.payment.card_mask</code>.
     */
    public final TableField<PaymentRecord, String> CARD_MASK = createField("card_mask", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>af.payment.card_token</code>.
     */
    public final TableField<PaymentRecord, String> CARD_TOKEN = createField("card_token", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>af.payment.amount</code>.
     */
    public final TableField<PaymentRecord, Long> AMOUNT = createField("amount", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>af.payment.currency</code>.
     */
    public final TableField<PaymentRecord, String> CURRENCY = createField("currency", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>af.payment.description</code>.
     */
    public final TableField<PaymentRecord, String> DESCRIPTION = createField("description", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>af.payment.shop_name</code>.
     */
    public final TableField<PaymentRecord, String> SHOP_NAME = createField("shop_name", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>af.payment.shop_url</code>.
     */
    public final TableField<PaymentRecord, String> SHOP_URL = createField("shop_url", org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * Create a <code>af.payment</code> table reference
     */
    public Payment() {
        this("payment", null);
    }

    /**
     * Create an aliased <code>af.payment</code> table reference
     */
    public Payment(String alias) {
        this(alias, PAYMENT);
    }

    private Payment(String alias, Table<PaymentRecord> aliased) {
        this(alias, aliased, null);
    }

    private Payment(String alias, Table<PaymentRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Af.AF;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<PaymentRecord> getPrimaryKey() {
        return Keys.PAYMENT_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<PaymentRecord>> getKeys() {
        return Arrays.<UniqueKey<PaymentRecord>>asList(Keys.PAYMENT_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Payment as(String alias) {
        return new Payment(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Payment rename(String name) {
        return new Payment(name, null);
    }
}
