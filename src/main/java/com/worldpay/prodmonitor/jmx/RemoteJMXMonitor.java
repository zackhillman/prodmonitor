package com.worldpay.prodmonitor.jmx;

public interface RemoteJMXMonitor  {
    /**
     * Gets the current connection state, the values should be same as the type specified by JMXConnectionNotification.
     * @return
     */
    public String getJMXConnetionState();

    public void setMaxInitConnectionRetires(int num);
    public int getMaxInitConnectionRetires();
}
