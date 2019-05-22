package com.worldpay.prodmonitor.jmx;


import javax.management.Attribute;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public abstract class AbstractBaseApplicationMonitor implements MBeanRegistration, TestableApplicationMonitorInterface {

    public static final String className = AbstractBaseApplicationMonitor.class.getName();

    protected static final String APP_MON_MBEAN_TEST_TIMEOUT_MILLIS_PROP = "AppMonMbean.test.timeoutMillis";
    protected static final int DEFAULT_TEST_TIMEOUT_MILLIS = 60000;
    protected String shortName;
    protected ObjectName schedulerMBean ;
    protected String schedulerMBeanName;
    protected ObjectName objectName;
    protected MBeanServer server;
    protected ApplicationStatusImpl status;
    protected ApplicationMonitorHelper helper;

    int testTimeout = DEFAULT_TEST_TIMEOUT_MILLIS;
    // it is up to the subclass to set its own threadhold
    // using its own vap.context properties.
    private int maxThresholdMillis = DEFAULT_TEST_TIMEOUT_MILLIS;

    // default to noRetries, will be overwritten for OLTP, UI and IBC tests.
    private int maxTestRetries = 0;

    private static final int DEFAULT_TEST_FREQUENCY = 300;
    private double testAgeMultiplier = 1.25;
    // Use a generous min age as PAT testing has shown JBoss to be not accurate in
    // adhering to test frequences (2 minute intervals could take up to 3.5 minutes)
    static final int DEFAULT_TEST_AGE_MIN_SECS = 8*60;
    int testAgeMinSec = DEFAULT_TEST_AGE_MIN_SECS;
    protected String baseName;
    private Properties props;

    public AbstractBaseApplicationMonitor() {
    }

    public void init(Properties props) {
        this.props = props;
        //testAgeMultiplier = Double.valueOf(props.getProperty("applicationStatus.testAgeMultiplier", "1.25"));
        testAgeMinSec = Integer.valueOf(props.getProperty("applicationStatus.testAgeMinSecs",
                                                          String.valueOf(DEFAULT_TEST_AGE_MIN_SECS)));
        testTimeout = Integer.valueOf(props.getProperty(APP_MON_MBEAN_TEST_TIMEOUT_MILLIS_PROP,
                                                        String.valueOf(DEFAULT_TEST_TIMEOUT_MILLIS)));
        helper = new ApplicationMonitorHelper(props);
        status = new ApplicationStatusImpl();
    }

    protected Properties getProps() {
        return props;
    }

    /* *******************************************************************************
     *
     * JBoss service interface methods
     *
     * *******************************************************************************
     */

    public void create() throws Exception {
        // default to no-op
    }

    public void start() throws Exception {
        status = initStatus();
        System.out.println ("***** Started "+ baseName + " ******") ;
    }

    public void stop() {
        // default to no-op
    }

    public void destroy() {
        // default to no-op
    }

    protected ApplicationStatusImpl getApplicationStatusImple() {
        return new ApplicationStatusImpl();
    }
    public ApplicationStatusImpl initStatus()  {
        ApplicationStatusImpl newStatus = getApplicationStatusImple();
        DetailedApplicationStatus detailedStatus =  new DetailedApplicationStatus();
        newStatus.setDetailStatus(detailedStatus) ;
        newStatus.init();
        newStatus.setApplicationName(shortName);
        newStatus.setMbeanName( getMbeanName() ) ;

        helper = new ApplicationMonitorHelper(getProps());

        return newStatus;
    }


    /* *******************************************************************************
     *
     * MBeanRegistration interface
     *
     * *******************************************************************************
     */
    @Override
    public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
        this.server = server;
        this.objectName = name;

        baseName = objectName.getKeyProperty("name");
        this.schedulerMBeanName = "vap.scheduler:name=" + baseName;
        this.schedulerMBean = new ObjectName(this.schedulerMBeanName);

        return name;
    }

    @Override
    public void postRegister(Boolean arg0) {
        try {
            this.start();
        } catch (Exception e) {

        }
    }
    @Override
    public void preDeregister() throws Exception {    }
    @Override
    public void postDeregister() {
        try {
            this.stop();
        } catch (Exception e) {

        }
    }


    /* *******************************************************************************
     *
     * methods to implement in derived classes
     *
     * *******************************************************************************
     */
    //public abstract ApplicationStatus test();

    public abstract DetailedApplicationStatus getDetailedStatus();

    public String getMbeanName()  {
        return objectName.getCanonicalName();
    }

    protected abstract String getClassName();


    /* *******************************************************************************
     *
     * TestableApplicationMonitorInterface interface
     *
     * *******************************************************************************
     */
    public void startApp() {
        // default to no-op
    }

    public void shutdownApp() {
        // default to no-op
    }

    @Override
    public void pauseTest() {
        Object args[] = { Boolean.TRUE };
        String types[] = { Boolean.TYPE.getName() };

        try {
            server.invoke(schedulerMBean, "stopSchedule", args, types);
        }
        catch (Exception e) {
            logEvent(e, ".pauseTest");
        }
    }


    @Override
    public void resumeTest() {
        try {
            server.invoke(schedulerMBean, "restartSchedule", null, null);
        }
        catch (Exception e) {
            logEvent(e, ".resumeTest");
        }
    }

    @Override
    public int getTestFrequencyInSeconds() {
        Long retval = null;
        try {
            retval = (Long)server.getAttribute( schedulerMBean, "SchedulePeriod");
        }
        catch (Exception e) {
            logEvent(e, ".getTestFrequencyInSeconds");
            return DEFAULT_TEST_FREQUENCY;
        }

        return retval.intValue() / 1000 ;
    }

    @Override
    public void setTestFrequencyInSeconds(int testFrequencyInSeconds) {
        Attribute attr = new Attribute("SchedulePeriod", new Long(testFrequencyInSeconds*1000) );
        try {
            server.setAttribute(schedulerMBean, attr);
        }
        catch (Exception e) {
            logEvent(e, ".setTestFrequencyInSeconds");
        }
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String name) {
        shortName = name;
        status.setApplicationName(name);
    }

    public synchronized ApplicationStatusImpl getStatus() {
        if (helper.getTestStatus() == null) {
            return status;
        }
        else {
            return helper.createTestStatus(status);
        }
    }

    @Override
    public void setTestStatus(String newStatus) {
        helper.setTestStatus(newStatus);
    }

    public String getTestStatus() {
        return helper.getTestStatus();
    }

    public void reset() {
        stop();
        try {
            start();
        }
        catch (Exception e) {
            logEvent(e, ".reset");
        }
    }


    public void checkStatus(ApplicationStatusImpl status) {
        if (status == null) {
            // throw exception?
            return;
        }

        ApplicationStateEnum state = status.getState();
        if ( state.equals(ApplicationStateEnum.UP) || state.equals(ApplicationStateEnum.OUT_OF_ROTATION) || state.equals(ApplicationStateEnum.PRIORITY) ) {
            String eventCode = getClassName() + ".statusIsUp";
            String message = "Application " + baseName + " passed test" ;
            if (status.getDetailStatus() != null) {
                message += " Event Details: " + status.getDetailStatus().getEventDetails();
//                if (status.getDetailStatus() instanceof  UIDetailedApplicationStatus) {
//                    message += " Reply Details: " + ((UIDetailedApplicationStatus) status.getDetailStatus()).getReply();
//                }
            }
            System.err.println(message);
//            Event evt = new Event(eventCode, message);
//            logEvent(evt);
        }
        else if ( state.equals(ApplicationStateEnum.DOWN) ) {
            String eventCode = getClassName() + ".statusIsDown";

            if(status.exceededTimeoutTime()) {
                eventCode = eventCode + "-exceededTimeoutTime";
            }
            else if(status.exceededThresholdTime()){
                eventCode = eventCode + "-exceededThresholdTime";
            }
            String message = status.getErrorMessage();
            if (status.getDetailStatus() != null) {
                message += " Event Details: " + status.getDetailStatus().getEventDetails();
//                if (status.getDetailStatus() instanceof  UIDetailedApplicationStatus) {
//                    message += " Reply Details: " + ((UIDetailedApplicationStatus) status.getDetailStatus()).getReply();
//                }
            }
            System.err.println(message);
//            Event evt = new Event(eventCode, message);
//            logEvent(evt);
            downStatusHandler();
        }
        else if ( state.equals(ApplicationStateEnum.WARNING)) {
            String eventCode = getClassName() + ".statusIsWarning";
            String message = status.getErrorMessage();
            if (status.getDetailStatus() != null) {
                message += " Event Details: " + status.getDetailStatus().getEventDetails();
//                if (status.getDetailStatus() instanceof  UIDetailedApplicationStatus) {
//                    message += " Reply Details: " + ((UIDetailedApplicationStatus) status.getDetailStatus()).getReply();
//                }
            }
            System.err.println(message);
//            Event evt = new Event(eventCode, message);
//            logEvent(evt);
        }
        else if ( state.equals(ApplicationStateEnum.NA)) {
            // do nothing: the test is not applicable.
            return;
        }

        checkStatusAge(status);
    }

    /**
     * Override this method to put in test-specific actions when the status is set to down
     */
    public void downStatusHandler() {
    	// default is no-op
    }


    /**
     * Checks whether the last update time of the status field
     * is older then a configured threshold.
     * Sets the expiration time on the status using this threshold
     * for the purpose of marking monitors offline in case
     * they're stuck.
     *
     * @param status
     */
    private void checkStatusAge(ApplicationStatusImpl status) {
        long lastUpdate = status.getUpdateTime().getTime();
        long now = System.currentTimeMillis();
        long oldTestAgeThreshold = getOldTestAgeThresHold();

        status.setExpirationTime(new Date(now + oldTestAgeThreshold));

        if (now - lastUpdate > oldTestAgeThreshold) {
            status.setState(ApplicationStateEnum.WARNING);
            String message =
                "No new test run since " + status.getUpdateTime() + "<br/>" +
                "Expected test to have ran before " + new Date(lastUpdate + oldTestAgeThreshold) ;
            status.setErrorMessage(message) ;
        }
    }

    /**
     * Calculates the old test age threshold as testAgeMinSec +
     * testFrequencySecs * testAgeMultiplier.
     *
     * @return threshold interval in seconds
     */
    protected long getOldTestAgeThresHold() {
        long testFrequencySecs = getTestFrequencyInSeconds() ;
        long oldTestAgeThreshold = (long)((testAgeMinSec
                + testFrequencySecs * testAgeMultiplier) * 1000);
        return oldTestAgeThreshold;
    }


    public void setStatus(ApplicationStatusImpl newStatus) {
        status = newStatus;
    }

    public int getTestTimeoutMillis() {
        return testTimeout;
    }

    public void setTestTimeout(int testTimeout) {
        this.testTimeout = testTimeout;
    }

    public int getMaxThresholdMillis() {
        return maxThresholdMillis;
    }

    public void setMaxThresholdMillis(int maxThresholdMillis) {
        this.maxThresholdMillis = maxThresholdMillis;
    }

    public boolean exceededMaxThresholdMillis(long timeMillis) {
        return timeMillis > maxThresholdMillis;
    }

    public int getMaxTestRetries() {
        return maxTestRetries;
    }

    public void setMaxTestRetries(int maxTestRetries) {
        this.maxTestRetries = maxTestRetries;
    }

//    AppProperties appprops = null;
//
//    Task getTaskNewInstance() throws TaskException {
//        Task aTask = TaskFactory.newTaskWithProps(appprops);
//        if(aTask==null) {
//          aTask = TaskFactory.newTask();
//        }
//        else {
//          appprops = (AppProperties) aTask.getAppProperties();
//          }
//        return aTask;
//    }

    public boolean isDisabled() {
//        try {
//            Task localTask = getTaskNewInstance();
//            String contextName = localTask.getAppPropertyStringWithDefault("contextName", "frontend");
//            if(contextName.equalsIgnoreCase("ib")) {
//                String ibNeededMBeans = localTask.getAppPropertyStringWithDefault("ib.needed.mbeans", StringUtils.EMPTY);
//                List<String> list = Arrays.asList(ibNeededMBeans.split(","));
//                if(!list.contains(this.getClass().getSimpleName())) {
//                    if(status == null) {
//                        status = new ApplicationStatusImpl();
//                    }
//                    status.setState(ApplicationStateEnum.NA);
//                    status.setShortDetails("Not configured to run on this host");
//                    status.setDisabled(true);
//                    return true;
//                }
//            }
//            else if(localTask.isFrontend() && localTask.isSecondarySiteMode()) {
//                String excludedMBeans = localTask.getAppPropertyStringWithDefault("fe.secondary.exclude.monitor.mbeans", StringUtils.EMPTY);
//                List<String> list = Arrays.asList(excludedMBeans.split(","));
//                if(list.contains(this.getClass().getSimpleName())) {
//                    if(status == null) {
//                        status = new ApplicationStatusImpl();
//                    }
//                    status.setState(ApplicationStateEnum.NA);
//                    status.setShortDetails("Not configured to run on this host");
//                    status.setDisabled(true);
//                    return true;
//                }
//            }
//            if(localTask.isSasiEnvironment()) {
//                String excludedMBeans = localTask.getAppPropertyStringWithDefault("monitor.dashboard.beans.exclude", StringUtils.EMPTY);
//                if(this.getClass().getSimpleName().matches(excludedMBeans)) {
//                    if(status == null) {
//                        status = new ApplicationStatusImpl();
//                    }
//                    status.setState(ApplicationStateEnum.NA);
//                    status.setShortDetails("Not configured to run on this host");
//                    status.setDisabled(true);
//                    return true;
//                }
//            }
//        } catch(TaskException e) {
//            MessageLogger.logError(e);
//        }
        return false;
    }

    public abstract ApplicationStatusImpl doTest();

    @Override
    public final ApplicationStatusImpl test() {

        logStartEvent();

        ApplicationStatusImpl localStatus = null;
        if(isDisabled()) {
            localStatus = status;
        } else {
            localStatus = doTest();
        }

        // if the test is disabled, eg. F5ProxyMonitor in development
        // then make this test a no-op.
        if(localStatus.isDisabled()){
            return localStatus;
        }
        int numRetries = 0;

        while(((localStatus.exceededThresholdTime() || localStatus.exceededTimeoutTime()) && numRetries < getMaxTestRetries())){
            numRetries ++;
            localStatus = doTest();
            localStatus.getDetailStatus().setNumRetries(numRetries);
        }
        checkStatus(localStatus);
        return localStatus;
    }

    protected void logStartEvent() {
        String eventCode = getClassName() + ".beginTest";
        String message = "Application " + baseName + " test is starting" ;
//        Event evt = new Event(eventCode, message);
//        evt.setSeverity(Event.NORMAL);
//
//        logEvent(evt);
    }

//
//    protected abstract void logEvent(Event evt);

    protected void logEvent(Exception e, String postFix) {
        logEvent(e, className, postFix);
    }
    protected void logEvent(Exception e, String myClassName, String postFix) {
//        PHXException pe = new PHXException();
//        String msg = e.getMessage();
//        if(StringUtils.isEmpty(msg)) {
//            msg = "Logging random exception as an event";
//        }
//        else {
//            msg = "";
//        }
//        pe.initChangeTypeAndEventCode(e, msg, myClassName + postFix);
//        logEvent(new Event(pe));
    }

}
