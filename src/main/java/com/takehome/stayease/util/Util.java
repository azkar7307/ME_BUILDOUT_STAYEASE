package com.takehome.stayease.util;

public class Util {
    public static String mask(String str) {
        if (str == null || str.length() < 4) {
            return str;
        }
        
        return str.replaceAll("(?<=.{3}).", "*");
    }
}
