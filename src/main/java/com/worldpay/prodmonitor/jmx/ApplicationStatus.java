package com.worldpay.prodmonitor.jmx;

import java.util.Date;

public interface ApplicationStatus {

    /**
     * Check to see if status update for this application is currently disabled.
     *
     * @return true if status update for the application is disabled
     */
    public abstract boolean isDisabled();

    /**
     * Gets current error message if any.
     *
     * @return the error message.
     */
    public abstract String getErrorMessage();

    /**
     * @return the last status update time.
     */
    public abstract Date getUpdateTime();

    /**
     * Gets the name of the application.
     *
     * @return name of the application.
     */
    public abstract String getApplicationName();

    /**
     * Gets the last known state of the application.
     *
     * @return the last known state.
     */
    public abstract ApplicationStateEnum getState();

    /**
     * Gets a short discri
     * ption of the detail status.
     *
     * @return short detail status.
     */
    public abstract String getShortDetails();

    /**
     * Gets the name of the MBean for this application.
     *
     * @return the name of the MBean.
     */
    public abstract String getMbeanName();

    public DetailedApplicationStatus getDetailStatus();

}
