package com.worldpay.prodmonitor.jmx;

/**
 * Defines an interface for a managable application.
 *
 * @author developer
 *
 */
public interface ApplicationMonitor {

    /**
     * Starts the app.
     */
    public void startApp();

    /**
     * Shuts down the app.
     */
    public void shutdownApp();

    /**
     * Gets the application status.
     *
     * @return
     */
    public ApplicationStatusImpl getStatus();

    /**
     * Gets the short name for this application. Application should define a default short name.
     *
     * @return the short name.
     */
    public String getShortName();

    /**
     * Sets the short name.
     *
     * @param name
     */
    public void setShortName(String name);

    /**
     * Resets the app, i.e. restart.
     */
    public void reset();

}
