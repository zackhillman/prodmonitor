package com.worldpay.prodmonitor.jmx;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class LoadBalancerInfoForServiceImpl implements Serializable, LoadBalancerInfoForService {

    /**
     *
     */
    private static final long serialVersionUID = 9134846460420793855L;
    private String delimitedStr;
    private LoadBalancerForServiceEnum loadBalancer;
    private String pool;
    private String fakeService;

    @ConstructorProperties({"delimitedStr", "loadBalancer", "pool", "fakeService"})
    public LoadBalancerInfoForServiceImpl(String delimitedStr, LoadBalancerForServiceEnum loadBalancer, String pool, String fakeService) {
        super();
        this.delimitedStr = delimitedStr;
        this.loadBalancer = loadBalancer;
        this.pool = pool;
        this.fakeService = fakeService;
    }

    public static LoadBalancerInfoForServiceImpl createNewInstance(String delimitedStr) {
        String[] tmp = delimitedStr.split("\\|");

        LoadBalancerInfoForServiceImpl lb = new LoadBalancerInfoForServiceImpl(
                delimitedStr,
                LoadBalancerForServiceEnum.valueOf(tmp[0]),
                tmp[1],
                (tmp.length>2?tmp[2]:"unknown")        );

        return lb;
    }

    @Override
    public String getDelimitedStr() {
        return delimitedStr;
    }

    @Override
    public LoadBalancerForServiceEnum getLoadBalancer() {
        return loadBalancer;
    }

    @Override
    public String getPool() {
        return pool;
    }

    @Override
    public String getFakeService() {
        return fakeService;
    }

}
