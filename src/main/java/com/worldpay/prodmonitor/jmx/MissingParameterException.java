package com.worldpay.prodmonitor.jmx;

public class MissingParameterException extends Exception {
    String parameterName;
    
    public MissingParameterException(String parameter) {
        super("The request miss the parameter <" + parameter + ">");
        parameterName = parameter;
    }
    
    public String toString() {
        return "The request miss the parameter <" + parameterName + ">";
    }
}
