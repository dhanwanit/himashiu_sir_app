package com.arcticfoxappz.walmartcoupon.bean;

public class Coupons {
    private String description;
    private String code;
    private String url;

    public Coupons(String description, String code, String url) {
        this.description = description;
        this.code = code;
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}