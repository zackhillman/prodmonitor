package com.worldpay.prodmonitor.jmx;


import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public abstract class BaseApplicationMonitor extends AbstractBaseApplicationMonitor implements MBeanRegistration, TestableApplicationMonitorInterface {

    protected static final String DISABLED_HOSTS_SUFFIX = ".lb.disabled.hosts";
    protected static final int DEFAULT_MAX_TEST_RETRIES = 2;



    public BaseApplicationMonitor() {
        this("");
    }

    public BaseApplicationMonitor(String applicationName) {
        super();
//        try {
//            task = TaskFactory.newTask();
//            task.setApplication(applicationName);
//            init(task.getAppProperties());
//       }
//        catch (TaskException e) {
//            //do nothing
//        }

    }


    @Override
    public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
        super.preRegister(server, name);
//        task.setApplication(baseName);

        return name;
    }

    @Override
    public void postRegister(Boolean arg0) {
        super.postRegister(arg0);
    }

    //
    // misc
    //
    public ObjectName getObjectName() {
        return objectName;
    }

    public String getSchedulerMbeanName() {
        return schedulerMBeanName;
    }
//
//
//
    //
    // file handling
    //


    public String getExpectedResultsFile(String contextFile) throws Exception {
        Properties props = getContext(contextFile) ;
        String filename = getExpectedResultsFilename(props) ;
        return getFile(filename) ;
    }

    public String getTestFile(String contextFile) throws Exception {
        Properties props = getContext(contextFile) ;
        String filename = getTestFilename(props) ;
        return getFile(filename) ;
    }

    protected Properties getContext(String contextFile) throws Exception {
        Properties props = new Properties();
        InputStream is = this.getClass().getResourceAsStream(contextFile);
        if( is == null ) {
            throw new IOException( "Unable to load context file for test from classpath with URI [" + contextFile +"]" );
        }
        props.load(is);
        return props;
    }


    protected String getFile(String name) throws IOException {
        StringBuffer buf  = new StringBuffer();
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(name));
            String line = null;

            while ((line = in.readLine()) != null) {
                buf.append(line).append(System.getProperty("line.separator"));
            }
        }
        finally {
            in.close();
        }

        return buf.toString();
    }


    private String getExpectedResultsFilename(Properties props) {
        String dir = props.getProperty("com.phoenix.vap.integrationTest.RunATestFile.expectedResultsDir") ;
        String name = props.getProperty("com.phoenix.vap.integrationTest.RunATestFile.expectedResultsFileName") ;
        String filename = dir + File.separator + name ;

        return filename;
    }


    private String getTestFilename(Properties props) {
        String dir = props.getProperty("com.phoenix.vap.integrationTest.RunATestFile.testDataDir") ;
        String name = props.getProperty("com.phoenix.vap.integrationTest.RunATestFile.inputFileName") ;
        String filename = dir + File.separator + name ;

        return filename;
    }






    protected String getApplicationName() {
        return "";
    }



    protected String getDisableHostsPropertyName(String appName) {
        return appName.toLowerCase() + DISABLED_HOSTS_SUFFIX;
    }

}
