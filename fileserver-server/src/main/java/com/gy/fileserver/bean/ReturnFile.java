package com.gy.fileserver.bean;

public class ReturnFile {
    private String originName;
    private String DigitEnvelop;
    private String type;
    private String data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getDigitEnvelop() {
        return DigitEnvelop;
    }

    public void setDigitEnvelop(String digitEnvelop) {
        DigitEnvelop = digitEnvelop;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ReturnFile{" +
                "originName='" + originName + '\'' +
                ", DigitEnvelop='" + DigitEnvelop + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
