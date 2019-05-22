package com.worldpay.prodmonitor.jmx;

public interface DetailedAppStatus extends ApplicationStatus {

    /**
     * Gets the JMX console URL if applicable.
     * 
     * @return URL string or null if there's no JMX console set up.
     */
    public abstract String getJmxConsoleUrl();

    /**
     * Gets the stack trace if an exception has occurred.
     * 
     * @return the stack trace.
     */
    public abstract String getStackTrace();

    /**
     * @return a string that will be logged with any event associated with an AppMon. Specific implementations of this
     *         class should ovverride this method to give information relevant to the AppMon test or general status.
     */
    public abstract String getEventDetails();

    /**
     * Gets the exception occurred if any.
     * 
     * @return exception.
     */
    public abstract Exception getException();

    // public abstract int getNumRetries();

}
