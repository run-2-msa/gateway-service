package com.example.gatewayservice.common.utils;

public class StringUtils{
    public static boolean isNullOrBlankOrEmpty(String object){
        return (object == null && object.isEmpty() && object.isBlank());
    }
}
