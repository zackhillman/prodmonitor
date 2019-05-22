package com.worldpay.prodmonitor.jmx;


public interface TestableApplicationMonitorInterface {

    public ApplicationStatusImpl test();

    public void pauseTest();

    public void resumeTest();

    public void setTestFrequencyInSeconds(int sec) ;

    public int getTestFrequencyInSeconds();

    public void setTestStatus(String newStatus);

}
