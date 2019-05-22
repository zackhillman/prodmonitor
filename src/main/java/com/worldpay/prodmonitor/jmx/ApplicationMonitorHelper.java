package com.worldpay.prodmonitor.jmx;


import java.util.Date;
import java.util.Properties;

public class ApplicationMonitorHelper {

    private String testStatus;
    private Properties props;
        
    public static final String className = ApplicationMonitorHelper.class.getName();
    

    public ApplicationMonitorHelper(Properties props) {
        this.props = props;
    }
    
    public String getTestStatus() {
        return testStatus;
    }

    
    public void setTestStatus(String newStatus) {
        try {
            String env = props.getProperty("environment") ;
            boolean isPAT = Boolean.valueOf(props.getProperty("is.pat.environment", "false"));
            
            // we don't want to allow setting this in production,
            // however, we do want to allow it in PAT
            if (env.equals("prod") && !isPAT) {
                return ;
            }
        }
        catch (Exception e) {
            // just return
            return;
        }
        
        if (newStatus==null || newStatus.trim().length()==0 || newStatus.trim().toLowerCase().equals("null")) {
            testStatus = null;
        }
        else {
            testStatus = newStatus.trim().toLowerCase();
        }
    }


    public ApplicationStatusImpl createTestStatus(ApplicationStatusImpl status) {
        if (testStatus == null) {
            return status;
        }
        
        ApplicationStatusImpl fakeStatus = new ApplicationStatusImpl(status.getApplicationName()) ;
        fakeStatus.setMbeanName(status.getMbeanName());
        
        if (testStatus.equals("up")) {
            fakeStatus.resetToUpState();
            fakeStatus.setErrorMessage("test status set to up") ;
        }
        else if (testStatus.equals("down")) {
            fakeStatus.resetToDownState("test status set to down") ;
        }
        else if (testStatus.equals("warning")) {
            fakeStatus.setState(ApplicationStateEnum.WARNING) ;
            fakeStatus.setErrorMessage("test status set to warning") ;
            fakeStatus.setUpdateTime(new Date());
        }
        
        return fakeStatus ;
    }
    
    
    
}
