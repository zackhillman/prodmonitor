package com.worldpay.prodmonitor.jmx;

//import com.oracle.coherence.patterns.pushreplication.PublishingService.State;

import java.beans.ConstructorProperties;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReplicationServiceStatusImpl extends ApplicationStatusImpl {

////    private Map<String, State> publishingStatusMap = new HashMap<String, State>();
//
//    public ReplicationServiceStatusImpl() {
//
//    }
//
//    @ConstructorProperties({ "updateTime", "expirationTime", "errorMessage", "state", "mbeanName", "applicationName",
//            "shortDetails", "detailStatus", "disabled", "publishingStatusMap" })
//    public ReplicationServiceStatusImpl(Date updateTime, Date expirationTime, String errorMessage,
//            ApplicationStateEnum state, String mbeanName, String applicationName, String shortDetails,
//            DetailedApplicationStatus detailStatus, boolean disabled, Map<String, State> publishingStatusMap) {
//        super(updateTime, expirationTime, errorMessage, state, mbeanName, applicationName, shortDetails, detailStatus,
//                disabled);
//        this.publishingStatusMap = publishingStatusMap;
//    }
//
//    public Map<String, State> getPublishingStatusMap() {
//        return publishingStatusMap;
//    }
//
//    public void setPublishingStatus(String publishing, State status) {
//        this.publishingStatusMap.put(publishing, status);
//    }
}
