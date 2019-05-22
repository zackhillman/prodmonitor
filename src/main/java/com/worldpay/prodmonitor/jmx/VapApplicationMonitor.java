package com.worldpay.prodmonitor.jmx;

public interface VapApplicationMonitor extends ApplicationMonitor {
    
    public DetailedApplicationStatus getDetailedStatus();
    
    public String getHostName();
    
    //
    //  for integration test purposes
    //
    public void setTestStatus(String status) ;
    
    public String getTestStatus() ;
    
}
