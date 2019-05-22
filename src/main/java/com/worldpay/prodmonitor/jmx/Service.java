package com.worldpay.prodmonitor.jmx;

public interface Service {

    public abstract void create() throws Exception;

    public abstract void start() throws Exception;

    public abstract void stop();

    public abstract void destroy();
}
