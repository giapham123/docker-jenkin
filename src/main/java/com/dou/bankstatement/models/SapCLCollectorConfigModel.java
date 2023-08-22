package com.dou.bankstatement.models;

import java.util.Date;

public class SapCLCollectorConfigModel {
    private String table_name;
    private String column_name;
    private int row_num;
    private int index_num;
    private Date update_date;
    private String note;

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public int getRow_num() {
        return row_num;
    }

    public void setRow_num(int row_num) {
        this.row_num = row_num;
    }

    public int getIndex_num() {
        return index_num;
    }

    public void setIndex_num(int index_num) {
        this.index_num = index_num;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
