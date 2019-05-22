package com.worldpay.prodmonitor.jmx;



public enum LoadBalancerForServiceEnum {
    NOT_LOAD_BALANCED("none"),
    F5_LOAD_BALANCED("f5");

    private final String  lbForService;

    @Override
    public String toString() {
        return getLbForService();
    }
    public String getLbForService() {
        return this.lbForService;
    }

    private LoadBalancerForServiceEnum(String lbForService) {
        this.lbForService = lbForService;
    }

//    public static LoadBalancerForServiceEnum fromCode(String code) {
//        try {
//            return EnumUtil.getEnumFromCode(LoadBalancerForServiceEnum.class, code);
//        } catch (ObjectNotFoundException e) {
//            return NOT_LOAD_BALANCED;
//        }
//    }

}
