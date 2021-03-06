package com.actiknow.rokad.utils;

public class AppConfigURL {
    
    public static String version = "v0.11";
    //    public static String BASE_URL = "https://project-rokad-cammy92.c9users.io/api/" + version + "/";
    public static String BASE_URL = "http://www.actipatient.com/livfreight/api/" + version + "/";
    
    public static String URL_LOGIN = BASE_URL + "login";
    public static String URL_TRUCK_ENTRY = BASE_URL + "add/truck_entry";
    public static String URL_INIT = BASE_URL + "init/application";
    
    public static String URL_GET_DESTINATIONS = BASE_URL + "destination";
    public static String URL_GET_TRUCKS = BASE_URL + "truck";
    public static String URL_GET_PARTIES = BASE_URL + "party";
    
}