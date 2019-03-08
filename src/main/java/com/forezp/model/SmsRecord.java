package com.forezp.model;

public class SmsRecord {
    private String mtDate;
    private String terminalId;
    private String content;
    private String sender;


    public String getMtDate() {
        return mtDate;
    }

    public void setMtDate(String mtDate) {
        this.mtDate = mtDate;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
