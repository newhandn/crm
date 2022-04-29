package com.laodeng.crm.workbench.domain;

public class Tran {

    private String owner; // 。。。。。。。。。。。。。。。。。。外键 关联tbl_user
    private String money; // 交易金额
    private String name; // 交易名称
    private String expected_date; // 预计成交日期
    private String customer_id; // 。。。。。。。。。。。。。。。。。。外键 关联tbl_customer
    private String stage; // 交易阶段
    private String type; // 交易类型
    private String source; // 交易来源
    private String activity_id; // 。。。。。。。。。。。。。。。。。。外键 关联tbl_activity
    private String contacts_id; // 。。。。。。。。。。。。。。。。。。外键 关联tbl_contacts
    private String create_by;
    private String create_time;
    private String edit_by;
    private String edit_time;
    private String description;
    private String contact_summary; // 联系纪要
    private String next_contact_time; // 下次联系时间
    private String id;

    private String possibility; // 可能性

    public String getPossibility(){

        return possibility;

    }

    public void setPossibility(String possibility){

        this.possibility = possibility;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpected_date() {
        return expected_date;
    }

    public void setExpected_date(String expected_date) {
        this.expected_date = expected_date;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getContacts_id() {
        return contacts_id;
    }

    public void setContacts_id(String contacts_id) {
        this.contacts_id = contacts_id;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getEdit_by() {
        return edit_by;
    }

    public void setEdit_by(String edit_by) {
        this.edit_by = edit_by;
    }

    public String getEdit_time() {
        return edit_time;
    }

    public void setEdit_time(String edit_time) {
        this.edit_time = edit_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact_summary() {
        return contact_summary;
    }

    public void setContact_summary(String contact_summary) {
        this.contact_summary = contact_summary;
    }

    public String getNext_contact_time() {
        return next_contact_time;
    }

    public void setNext_contact_time(String next_contact_time) {
        this.next_contact_time = next_contact_time;
    }



}
