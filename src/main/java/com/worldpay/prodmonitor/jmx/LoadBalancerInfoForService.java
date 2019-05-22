package com.worldpay.prodmonitor.jmx;

public interface LoadBalancerInfoForService {

    String getDelimitedStr();

    LoadBalancerForServiceEnum getLoadBalancer();

    String getPool();

    String getFakeService();

}