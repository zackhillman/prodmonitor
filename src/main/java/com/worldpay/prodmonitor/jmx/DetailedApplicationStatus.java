package com.worldpay.prodmonitor.jmx;


import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.Properties;

public class DetailedApplicationStatus implements Serializable {
    static final long serialVersionUID = -2612238746777564425L;

    private String jmxConsoleUrl;
    private String exceptionMessage;
    private String stackTrace;
    private boolean exceededThresholdTime;
    private int numRetries;
    private boolean exceededTimeoutTime;

    @ConstructorProperties({"jmxConsoleUrl", "exceptionMessage", "stackTrace", "exceededThresholdTime", "numRetries", "exceededTimeoutTime"})
    public DetailedApplicationStatus(String jmxConsoleUrl, String exceptionMessage, String stackTrace, boolean exceededThresholdTime,
            int numRetries, boolean exceededTimeoutTime) {
        super();
        this.jmxConsoleUrl = jmxConsoleUrl;
        this.exceptionMessage = exceptionMessage;
        this.stackTrace = stackTrace;
        this.exceededThresholdTime = exceededThresholdTime;
        this.numRetries = numRetries;
        this.exceededTimeoutTime = exceededTimeoutTime;
    }

    public DetailedApplicationStatus() {

    }

    public String getJmxConsoleUrl() {
        return jmxConsoleUrl;
    }

    public void setJmxConsoleUrl(String jmxConsoleUrl) {
        this.jmxConsoleUrl = jmxConsoleUrl;
    }



    public String getStackTrace() {
        return stackTrace;
//        return PHXException.getStackTraceFromException(exception);
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    @Override
    public String toString() {
        return "jmxConsoleUrl=" + jmxConsoleUrl + "\n" ;

    }

    public static void main(String[] args) {
        System.out.println ("name=" + System.getProperty("os.name"));
    }

    /**
     * @return a string that will be logged with any event associated with an AppMon.
     * Specific implementations of this class should override this method to give information relevant to the AppMon test or general status.
     */
    public final String getEventDetails( ) {
        return "<eventDetails>" + addEventDetails() +
             (numRetries > 0 ? "<numRetries>" + numRetries + "</numRetries>" : "") + "</eventDetails>";
    }

    public String addEventDetails() {
        return "";
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public boolean isExceededThresholdTime() {
        return exceededThresholdTime;
    }
    public void setExceededThresholdTime(boolean exceededThresholdTime) {
        this.exceededThresholdTime = exceededThresholdTime;
    }

    public int getNumRetries() {
        return numRetries;
    }

    public void setNumRetries(int numRetries) {
        this.numRetries = numRetries;
    }

    public boolean isExceededTimeoutTime() {
        return exceededTimeoutTime;
    }

    public void setExceededTimeoutTime(boolean exceededTimeoutTime) {
        this.exceededTimeoutTime = exceededTimeoutTime;
    }
}
