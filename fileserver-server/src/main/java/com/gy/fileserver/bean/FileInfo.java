package com.gy.fileserver.bean;

public class FileInfo {
    private String  uuid;
    private Integer size;
    private String type;
    private String originName;
    private String createTime;//yyyy-MM-dd hh:mm:ss:SS
    private String saveAddress;
    private String digitEnvelop;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSaveAddress() {
        return saveAddress;
    }

    public void setSaveAddress(String saveAddress) {
        this.saveAddress = saveAddress;
    }

    public String getDigitEnvelop() {
        return digitEnvelop;
    }

    public void setDigitEnvelop(String digitEnvelop) {
        this.digitEnvelop = digitEnvelop;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "uuid='" + uuid + '\'' +
                ", size=" + size +
                ", type='" + type + '\'' +
                ", originName='" + originName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", saveAddress='" + saveAddress + '\'' +
                ", digitEnvelop='" + digitEnvelop + '\'' +
                '}';
    }
}
