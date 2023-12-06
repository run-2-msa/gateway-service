package com.example.gatewayservice.common.utils;

public class StringUtils{
    public static boolean isNotNullOrNotBlank(String object){
        return (object != null && !object.isBlank());
    }
}
