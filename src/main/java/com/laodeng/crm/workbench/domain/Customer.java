package com.laodeng.crm.workbench.domain;

public class Customer {

    private String id;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String owner;
    private String name;
    private String website;
    private String phone;
    private String create_by;
    private String create_time;
    private String edit_by;
    private String edit_time;
    private String contact_summary;
    private String next_contact_time;
    private String description;
    private String address;


}
