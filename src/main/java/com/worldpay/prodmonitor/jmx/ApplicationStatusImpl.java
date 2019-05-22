package com.worldpay.prodmonitor.jmx;



import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.Date;

public class ApplicationStatusImpl implements ApplicationStatus, Serializable {
    public static final String className = ApplicationStatusImpl.class.getName();
    static final long serialVersionUID = -6292835364919203995L;

    private Date updateTime = new Date();
    private Date expirationTime = null;
    private String errorMessage = "";
    private ApplicationStateEnum state = ApplicationStateEnum.NA;

    private String mbeanName;
    private String applicationName;
    private String shortDetails ;
    private DetailedApplicationStatus detailStatus;
    private boolean disabled;


    @ConstructorProperties({"updateTime", "expirationTime", "errorMessage", "state", "mbeanName", "applicationName", "shortDetails", "detailStatus", "disabled"})
    public ApplicationStatusImpl(Date updateTime, Date expirationTime, String errorMessage, ApplicationStateEnum state,
            String mbeanName, String applicationName, String shortDetails, DetailedApplicationStatus detailStatus,
            boolean disabled) {
        super();
        this.updateTime = updateTime;
        this.expirationTime = expirationTime;
        this.errorMessage = errorMessage;
        this.state = state;
        this.mbeanName = mbeanName;
        this.applicationName = applicationName;
        this.shortDetails = shortDetails;
        this.detailStatus = detailStatus;
        this.disabled = disabled;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public ApplicationStatusImpl() { }

    public void init() {
        state = ApplicationStateEnum.WARNING;
        errorMessage = "NO TEST RUN YET" ;
    }

    public ApplicationStatusImpl(String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public String getErrorMessage() {
        checkExpiration();
        return errorMessage;
    }
    public void setErrorMessage(String statusMessage) {
        this.errorMessage = statusMessage;
    }
    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }
    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public ApplicationStateEnum getState() {
        checkExpiration();
        return state;
    }

    public void setState(ApplicationStateEnum state) {
        this.state = state;
        updateTime = new Date();
    }

    public void resetToUpState() {
        state = ApplicationStateEnum.UP;
        updateTime = new Date();
        errorMessage = "";
    }

    public void resetToDownState(String errorMsg) {
        state = ApplicationStateEnum.DOWN;
        updateTime = new Date();
        errorMessage = errorMsg;
    }

    public void resetToWarnState(String desc) {
        state = ApplicationStateEnum.WARNING;
        updateTime = new Date();
        errorMessage = desc;
    }

    public void resetToOutOfRotation() {
        state = ApplicationStateEnum.OUT_OF_ROTATION;
        updateTime = new Date();
        errorMessage = "";
    }

    public void resetToPriorityProcessing() {
        state = ApplicationStateEnum.PRIORITY;
        updateTime = new Date();
        errorMessage = "";
    }

    @Override
    public String toString() {
        String statusMsg = state.toString();
        String statusDisplayString;
        if(state == ApplicationStateEnum.UP || state == ApplicationStateEnum.NA || state == ApplicationStateEnum.OUT_OF_ROTATION || state == ApplicationStateEnum.PRIORITY) {
            statusDisplayString = applicationName + " is " + statusMsg + " on " + updateTime.toString();
        }
        else {
            statusDisplayString = applicationName + " is " + statusMsg + " on " + updateTime.toString() +
                ". Error Message:" + errorMessage;
        }
        return statusDisplayString;
    }

    public void setShortDetails(String s) {
        shortDetails = s;
    }

    @Override
    public String getShortDetails() {
        return shortDetails;
    }

    @Override
    public DetailedApplicationStatus getDetailStatus() {
        return detailStatus;
    }

    public void setDetailStatus(DetailedApplicationStatus detailStatus) {
        this.detailStatus = detailStatus;
    }

    @Override
    public String getMbeanName() {
        return mbeanName;
    }

    public void setMbeanName(String mbeanName) {
        this.mbeanName = mbeanName;
    }

    public boolean exceededThresholdTime(){
        return detailStatus != null && detailStatus.isExceededThresholdTime();
    }

    public boolean exceededTimeoutTime() {
        return detailStatus != null && detailStatus.isExceededTimeoutTime();
    }

    protected void checkExpiration() {
        if ( (state == null || state.equals(ApplicationStateEnum.UP) || state.equals(ApplicationStateEnum.OUT_OF_ROTATION) || state.equals(ApplicationStateEnum.PRIORITY))
        && expirationTime != null
        && System.currentTimeMillis() > expirationTime.getTime()) {
            state = ApplicationStateEnum.WARNING;
            errorMessage = "last test at " + updateTime + " is too old.<br/>" +
                    "Expected to have a new run before " + expirationTime ;


        }
    }
}
