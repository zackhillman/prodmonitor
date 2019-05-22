package com.worldpay.prodmonitor;

import com.worldpay.prodmonitor.jmx.ApplicationStatusImpl;

import javax.management.*;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

public class Controller {

    private static Properties tomeeAuthProperties;

    public static final String TOMEE_NAMING_PROVIDER_PREFIX = "service:jmx:rmi:///jndi/rmi://";
    public static final String TOMEE_NAMING_PROVIDER_SUFFIX = "/jmxrmi";

    public static void main(String[] args) throws IOException, MalformedObjectNameException, IntrospectionException, InstanceNotFoundException, ReflectionException, InterruptedException, ExecutionException {

        String file = System.getProperty("user.home") + "/tomee.auth.properties";
        tomeeAuthProperties = new Properties();



        try (FileInputStream in = new FileInputStream(file)) {
            tomeeAuthProperties.load(in);
        } catch (IOException e) {


        }
        System.out.println(tomeeAuthProperties.getProperty("tomee"));

        String[] hosts = args;

        List<Callable<List<String>>> taskList = new ArrayList<>();

        for (String hostport : hosts) {
            String[] h = hostport.split(":");
            taskList.add(new ConnectServerCallable(h[0], h[1]));
        }

        ExecutorService executor = Executors.newFixedThreadPool(hosts.length);
        long startTime = Instant.now().toEpochMilli();

        List<Future<List<String>>> results = executor.invokeAll(taskList);

        for (Future<List<String>> result : results) {
            System.out.println(result.get().toString());
            long endTime2 = Instant.now().toEpochMilli();
            long timeElapsed2 = endTime2 - startTime;
        }
        long endTime = Instant.now().toEpochMilli();

        long timeElapsed = endTime - startTime;

        System.out.println("TOTAL Time: "+ timeElapsed);

        executor.shutdown();


    }

    private static JMXConnector setTomeeConnection(String url) throws IOException {
        Map<String, String[]> environ = getJmxCreds();

        JMXServiceURL serviceUrl = new JMXServiceURL(url);
        return JMXConnectorFactory.connect(serviceUrl, environ);
    }

    private static String getUrl(String host, String port) {
        StringBuffer sb = new StringBuffer();
        sb.append(TOMEE_NAMING_PROVIDER_PREFIX);
        sb.append(host);
        sb.append(":");
        sb.append(port);
        sb.append(TOMEE_NAMING_PROVIDER_SUFFIX);
        return sb.toString();
    }

    private static Map<String, String[]> getJmxCreds() {
        Map<String, String[]> creds = new HashMap<>();
        String user =  "tomee";
        String tomeeAuth = "stgfoo"; //tomeeAuthProperties.getProperty("tomee");
        String[] userInfo = {user, tomeeAuth};
        creds.put(JMXConnector.CREDENTIALS, userInfo);

        return creds;
    }

    private static List<String> connectToServer(String host, String port) throws IOException, MalformedObjectNameException, ReflectionException, InstanceNotFoundException, InterruptedException, AttributeNotFoundException, MBeanException {
        long startTime = Instant.now().toEpochMilli();
        List<String> output = new ArrayList<>();
        JMXConnector connector = setTomeeConnection(getUrl( host,  port));
        output.add("Time Connecting to connector: " + (Instant.now().toEpochMilli() - startTime));

        MBeanServerConnection server = connector.getMBeanServerConnection();
        output.add("Time Connecting to Server: " + (Instant.now().toEpochMilli() - startTime));

        output.add(host + ":" + port);
        Set<ObjectName> mbeanNames = server.queryNames(new ObjectName("phx.monitor.*:category=*,*"), null);

        for (ObjectName objName : mbeanNames) {

            CompositeDataSupport status = (CompositeDataSupport) server.getAttribute(objName, "Status");

//            CompositeDataSupport detailStatus = (CompositeDataSupport) status.get("detailStatus");
            CompositeDataSupport detailStatusSep = (CompositeDataSupport) server.getAttribute(objName, "DetailedStatus");

/*
            String diff = "";

            if (detailStatus == null) {
                diff = "detailStatis Null";
                if (detailStatusSep == null) {
                    diff = "both Null";
                }
            } else if (detailStatusSep == null){
                diff = "detailStatusSep Null";
            } else {
                diff = (detailStatus.values().size() <= detailStatusSep.values().size()) +"";
            }


            if (!diff.contains("true")) {

            }*/
            output.add( objName.getKeyProperty("name")+"\n");

        }

        long endTime = Instant.now().toEpochMilli();

        long timeElapsed = endTime - startTime;

        output.add("TOTAL Time: "+ timeElapsed);
        return output;

    }

    static class ConnectServerCallable implements Callable<List<String>> {

        String host;
        String port;


        public ConnectServerCallable(String host, String port) {

            this.host = host;
            this.port = port;

        }

        @Override
        public List<String> call() throws Exception {

            return connectToServer(this.host, this.port);

        }
    }

    static class ConnectMBeanCallable implements Callable<String> {

        ObjectName mbeanName;
        MBeanServerConnection server;


        public ConnectMBeanCallable(ObjectName mbeanName, MBeanServerConnection server) {

            this.mbeanName = mbeanName;
            this.server = server;

        }

        @Override
        public String call() throws Exception {

            server.getAttributes(mbeanName, new String[] {"Status"}).toString();
            return mbeanName.getCanonicalName() + "\n";
        }
    }


}


