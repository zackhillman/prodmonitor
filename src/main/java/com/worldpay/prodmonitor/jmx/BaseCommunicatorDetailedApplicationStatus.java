package com.worldpay.prodmonitor.jmx;

import java.beans.ConstructorProperties;


public class BaseCommunicatorDetailedApplicationStatus extends DetailedApplicationStatus {

    protected String testUrl;
    protected String request;
    protected String reply;
    protected String expectedResults;
    protected long responseTime;

    public BaseCommunicatorDetailedApplicationStatus() {

    }

    @ConstructorProperties({"jmxConsoleUrl", "exceptionMessage", "stackTrace", "exceededThresholdTime", "numRetries", "testUrl", "request", "reply", "expectedResults", "responseTime", "exceededTimeoutTime"})
    public BaseCommunicatorDetailedApplicationStatus(String jmxConsoleUrl, String exceptionMessage, String stackTrace, boolean exceededThresholdTime,
            int numRetries, String testUrl, String request, String reply, String expectedResults, long responseTime, boolean exceededTimeoutTime) {
        super(jmxConsoleUrl, exceptionMessage, stackTrace, exceededThresholdTime, numRetries, exceededTimeoutTime);
        this.testUrl = testUrl;
        this.request = request;
        this.reply = reply;
        this.expectedResults = expectedResults;
        this.responseTime = responseTime;
    }

    public String getExpectedResults() {
        return expectedResults;
    }
    public void setExpectedResults(String expectedResults) {
        this.expectedResults = expectedResults;
    }
    public String getReply() {
        return reply;
    }
    public void setReply(String reply) {
        this.reply = reply;
    }
    public String getRequest() {
        return request;
    }
    public void setRequest(String request) {
        this.request = request;
    }
    public long getResponseTime() {
        return responseTime;
    }
    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }
    public String getTestUrl() {
        return testUrl;
    }
    public void setTestUrl(String testUrl) {
        this.testUrl = testUrl;
    }

    @Override
    public String addEventDetails() {
        String retVal =
            "<responseTime>" + getResponseTime() + "</responseTime>\n"
            +"<testUrl>" + getTestUrl() + "<testUrl>";
        return retVal;
    }
    @Override
    public String toString() {
        String sup = super.toString();
        String retVal =
            "testUrl=" + testUrl + "\n" +
            "request=\n" + request + "\n" +
            "reply=\n" + reply + "\n" +
            "expectedResults=\n" + expectedResults + "\n" +
            "responseTime=" + responseTime + "\n" +
            "exceptionMessage=" + getExceptionMessage() + "\n";
        return sup+retVal;
    }


}
