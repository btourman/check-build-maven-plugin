package com.btourman.check.build.pmd;

import java.io.Serializable;

public class PMDConf implements Serializable {

    private static final long serialVersionUID = -5050533123452085276L;

    private int priority;
    private String format;
    private String rulesets;

    public PMDConf() {
        this.priority = 2;
        this.format = "text";
        this.rulesets = "rulesets/internal/all-java.xml";
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getExtensionFile() {
        String extension;
        switch (this.getFormat()) {
            case "xml":
                extension = "xml";
                break;

            case "text":
            default:
                extension = "txt";
                break;
        }

        return extension;
    }

    public String getRulesets() {
        return rulesets;
    }

    public void setRulesets(String rulesets) {
        this.rulesets = rulesets;
    }
}
