package com.sinoautodiagnoseos.entity.LogBean;

/**
 * Created by Lanye on 2017/2/20.
 */

public class LogBean {

    public String message;
    public String exceptionMessage;
    public String stackTrace;
    public String applicationName;
    public int logLevel;
    public String sourceName;
    public String timestamp;


    public void setMessage(String message) {
        this.message = message;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getMessage() {

        return message;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public int getLogLevel() {
        return logLevel;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
