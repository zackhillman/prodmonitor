package com.worldpay.prodmonitor.jmx;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class ApplicationStateEnum implements Serializable {
    static final long serialVersionUID = -1934399981331293898L;

    private final String state;

    public String getState() {
        return this.state;
    }

    @Override
    public int hashCode() {
        return this.state.hashCode();
    }

    @ConstructorProperties({ "state" })
    public ApplicationStateEnum(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        ApplicationStateEnum applicationState = null;
        if (!(obj instanceof ApplicationStateEnum)) {
            return false;
        } else {
            applicationState = (ApplicationStateEnum) obj;
        }

        return this.state.equals(applicationState.getState());
    }

    @Override
    public String toString() {
        return this.state;
    }

    public static final ApplicationStateEnum UP = new ApplicationStateEnum("up");
    public static final ApplicationStateEnum DOWN = new ApplicationStateEnum("down");
    public static final ApplicationStateEnum WARNING = new ApplicationStateEnum("warning");
    public static final ApplicationStateEnum NA = new ApplicationStateEnum("not applicable");
    public static final ApplicationStateEnum OUT_OF_ROTATION = new ApplicationStateEnum("out of rotation");
    public static final ApplicationStateEnum PRIORITY = new ApplicationStateEnum("priority ibc");
}
