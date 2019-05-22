package com.worldpay.prodmonitor.jmx;

import javax.management.ObjectName;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ApplicationStatusComparator implements Comparator<String> {

    private Map<String, Integer> mbeans;

    private String[] mbeanOrder = {
            "phx.monitor.app:name=PaypageApplicationMonitor",
            "phx.monitor.app:category=FrontEnd,name=OnlineCommunicatorHHPortApplicationMonitor",
            "phx.monitor.app:category=FrontEnd,name=OnlineCommunicatorApplicationMonitor",
            "phx.monitor.app:category=FrontEnd,name=OnlineCommunicatorCacheApplicationMonitor",
            "phx.monitor.app:category=FrontEnd,name=ApmWebServicesMonitor",
            "phx.monitor.app:category=FrontEnd,name=CredentialServiceApplicationMonitor",
            "phx.monitor.app:category=FrontEnd,name=BatchInboundCommunicatorApplicationMonitor",
            "phx.monitor.app:category=Internal,name=AdminUIMonitor",
            "phx.monitor.app:category=BackEnd,name=IQMonitor",
            "phx.monitor.app:category=BackEnd,name=WSMonitor",//Web Service
            "phx.monitor.app:category=Payfac,name=MpMonitor",  //Merchant Provisioner
            "phx.monitor.app:category=Payfac,name=MpmMonitor", //Merchant Profile Manager
            "phx.monitor.app:category=Payfac,name=MpmInternalMonitor", //Merchant Profile Manager Internal
            "phx.monitor.app:category=Payfac,name=PayfacPortalMonitor", //Payfac Portal aka Metaui
            "phx.monitor.app:category=FrontEnd,name=PassthroughCommunicatorApplicationMonitor",
            "phx.monitor.app:category=FrontEnd,name=FtpCommunicatorApplicationMonitor",
            "phx.monitor.app:category=ExternalConnectivity,name=AuthSwitchMonitor",
            "phx.monitor.app:category=FrontEnd,name=USMGCommunicatorApplicationMonitor",
            "phx.monitor.app:category=FrontEnd,name=DomesticNetworkStatisticsInfo",
            "phx.monitor.app:category=FrontEnd,name=InternationalNetworkStatisticsInfo",
            "phx.monitor.app:category=Internal,name=EventMonitorJBossService",
            "phx.monitor.app:category=Internal,name=TomcatCacheMonitor",
            "phx.monitor.app:category=Internal,name=CacheRehashMonitor",
            "phx.monitor.app:category=Internal,name=WorkflowMode",
            "phx.monitor.app:category=Internal,name=PersistenceMode",
            "phx.monitor.app:category=Internal,name=SkybotAgentMonitor",
            "phx.monitor.app:name=ProfileDataMembersMonitor",
            "phx.monitor.app:name=NonProfileDataMembersMonitor",
            "phx.monitor.app:name=FirstLevelMembersMonitor",
            "phx.monitor.app:name=SecondLevelMembersMonitor",
            "phx.monitor.app:name=ManagementMembersMonitor",
            "phx.monitor.app:name=DupeCheckMembersMonitor",
            "phx.monitor.app:name=TokenMembersMonitor",
            "phx.monitor.app:name=ReplicationMembersMonitor",
            "phx.monitor.app:name=AutoNocMembersMonitor",
            "phx.monitor.app:name=CardRepairMembersMonitor",
            "phx.monitor.app:name=AuthFraudVelocityMembersMonitor",
            "phx.monitor.app:name=AuthFraudMembersMonitor",
            "phx.monitor.app:name=AuthRecycleMembersMonitor",
            "phx.monitor.app:name=ApmRecurringMandateMembersMonitor",
            "phx.monitor.app:name=FiBalanceCheckMembersMonitor",
            "phx.monitor.app:category=ExternalConnectivity,name=EtwsMonitor",
            "phx.monitor.app:category=ExternalConnectivity,name=EwsMonitorV4",
            "phx.monitor.app:category=FrontEnd,name=BatchInboundCommunicatorSSLApplicationMonitor",
            "phx.monitor.app:category=Internal,name=QuartzAgentMonitor",
            "phx.monitor.app:name=ZookeeperMonitor",
            "phx.monitor.app:name=KafkaMonitor",
            "phx.monitor.app:name=SchemaRegistryMonitor",
            "phx.monitor.app:category=ExternalConnectivity,name=NTSMonitor"
    };

    public ApplicationStatusComparator() {
        mbeans = new HashMap<String, Integer>();
        for (int i = 0; i < mbeanOrder.length; i++) {
            mbeans.put(mbeanOrder[i], new Integer(i));
        }
    }

    @Override
    public int compare(String arg0, String arg1) {

        String mbean0 = null;
        String mbean1 = null;

        try {
            mbean0 = new ObjectName(arg0).getCanonicalName();

        } catch (Exception e) {

        }
        try {
            mbean1 = new ObjectName(arg1).getCanonicalName();
        } catch (Exception e) {

        }

        Integer index0 = mbeans.get(mbean0);
        Integer index1 = mbeans.get(mbean1);


        final int at_the_end = 2 * mbeans.size();


        // this should make items that aren't in the list above
        // sort toward the end
        return (index0 == null ? at_the_end : index0) - (index1 == null ? at_the_end : index1);
    }
}
