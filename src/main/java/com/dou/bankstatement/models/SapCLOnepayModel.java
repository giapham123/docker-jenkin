package com.dou.bankstatement.models;

public class SapCLOnepayModel {
    private String no;
    private String bank_id;
    private String merchant_id;
    private String tran_date;
    private String original_date;
    private String transaction_id;
    private String merchant_trans_ref;
    private String order_ref;
    private String card_number;
    private String amount;
    private String original_amount;
    private String transaction_type;
    private String response_code;
    private String transaction_state;
    private String file_name;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getBank_id() {
        return bank_id;
    }

    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getOriginal_date() {
        return original_date;
    }

    public void setOriginal_date(String original_date) {
        this.original_date = original_date;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getMerchant_trans_ref() {
        return merchant_trans_ref;
    }

    public void setMerchant_trans_ref(String merchant_trans_ref) {
        this.merchant_trans_ref = merchant_trans_ref;
    }

    public String getOrder_ref() {
        return order_ref;
    }

    public void setOrder_ref(String order_ref) {
        this.order_ref = order_ref;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOriginal_amount() {
        return original_amount;
    }

    public void setOriginal_amount(String original_amount) {
        this.original_amount = original_amount;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getTransaction_state() {
        return transaction_state;
    }

    public void setTransaction_state(String transaction_state) {
        this.transaction_state = transaction_state;
    }

    public String getTran_date() {
        return tran_date;
    }

    public void setTran_date(String tran_date) {
        this.tran_date = tran_date;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}
