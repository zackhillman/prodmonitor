package com.worldpay.prodmonitor.jmx;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ApplicationStatusList implements Serializable, Iterable<ApplicationStatusImpl> {
    static final long serialVersionUID = 8081094393393649270L;

    List<ApplicationStatusImpl> statusList = new ArrayList<ApplicationStatusImpl>();

    public ApplicationStatusList() {
    }

    @ConstructorProperties({"statusList"})
    public ApplicationStatusList(List<ApplicationStatusImpl> statusList) {
        super();
        this.statusList.addAll(statusList);
    }

    public List<ApplicationStatusImpl> getStatusList() {
        return statusList;
    }

    public void add(ApplicationStatusImpl status) {
        statusList.add(status);
    }

    @Override
    public Iterator<ApplicationStatusImpl> iterator() {
        return statusList.iterator();
    }

    public int size() {
        return statusList.size();
    }

    public ApplicationStatusImpl get(int index) {
        return statusList.get(index);
    }

    public void addAll(ApplicationStatusList list) {
        statusList.addAll(list.statusList);
    }

    public void clear() {
        statusList.clear();
    }

    public void set(int i, ApplicationStatusImpl status) {
        statusList.set(i, status);
    }

    public ApplicationStatusImpl[] toArray(){
        ApplicationStatusImpl[] array = new ApplicationStatusImpl[statusList.size()];
        return statusList.toArray(array);
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (ApplicationStatusImpl status : statusList) {
            buf.append(status.toString() + "<BR>");
        }

        return buf.toString();
    }
}
