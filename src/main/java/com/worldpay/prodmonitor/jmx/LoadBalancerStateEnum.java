package com.worldpay.prodmonitor.jmx;





/**
 * //TODO This and ServerMonitorHelper should be moved to prodMonitor module
 * Represents an application server's load balancer information.
 */
public enum LoadBalancerStateEnum {

    /**
     * Represents that the server application is not receiving traffic from the load balancer when it should be.
     */
    INACTIVE("inactive", "Not Receiving", false, true, "lb-inactive"),

    /**
     * Represents that the server application has been intentionally taken offline by the load balancer.
     */
    OFFLINE("offline", "Offline", false, true, "lb-offline"),

    /**
     * Represents that the server application has been re-assigned to priority traffic by the load balancer.
     */
    PRIORITY("priority", "Re-Assigned", true, true, "lb-priority"),

    /**
     * Represents that the server application is receiving no traffic from the load balancer due to being de-prioritized.
     */
    DEPRIORITIZED("deprioritized", "No Traffic", true, true, "lb-deprioritized"),

    /**
     * Represents that the server application is receiving traffic from the load balancer.
     */
    ACTIVE("active", "Receiving", true, true, "lb-active"),

    /**
     * Represents that the server application is intentionally configured not to receive load balancer traffic.
     */
    OUT_OF_ROTATION("out-of-rotation", "Not Configured", false, false, "lb-out-of-rotation"),

    /**
     * Represents an absence of load balancer information.
     */
    NA("na", "N/A", false, false, "lb-none");

    private final String label;
    private final String trafficMessage;
    private final boolean isHealthyForMain;
    private final boolean isCounted;
    private final String cssClass;

    LoadBalancerStateEnum(String label, String trafficMessage, boolean isHealthyForMain, boolean isCounted, String cssClass) {
        this.label = label;
        this.trafficMessage = trafficMessage;
        this.isHealthyForMain = isHealthyForMain;
        this.isCounted = isCounted;
        this.cssClass = cssClass;
    }

    @Override
    public String toString() {
        return label;
    }

    /**
     * Gets the traffic message associated with the load balancer state.
     *
     * @return the traffic message
     */
    public String getTrafficMessage() {
        return trafficMessage;
    }

    /**
     * Checks whether the load balancer state indicates the server is online.
     *
     * @return true if the state counts as online
     */
    public boolean isHealthyForMain() {
        return isHealthyForMain;
    }

    /**
     * Checks whether the load balancer state indicates the server should be counted toward capacity and health states.
     *
     * @return true if the state should be counted
     */
    public boolean isCounted() {
        return isCounted;
    }

    public String getCssClass(){
        return cssClass;
    }
}
