package com.actiknow.rokad.utils;

public class AppConfigURL {
    public static String version = "v1";
    public static String BASE_URL = "https://project-rokad-cammy92.c9users.io/api/" + version + "/";
// public static String BASE_URL = "http://famdent.indiasupply.com/isdental/api/" + version + "/";


    public static String URL_LOGIN = BASE_URL + "user/login";

    public static String URL_TRUCK_ENTRY = BASE_URL + "truck_entry";

    public static String URL_INIT = BASE_URL + "init/application";

    public static String URL_COMPANY_LIST = BASE_URL + "company";
    public static String URL_COMPANY_DETAILS = BASE_URL + "company";

    public static String URL_EVENT_LIST = BASE_URL + "event";
    public static String URL_EVENT_DETAILS = BASE_URL + "event";

    public static String URL_ORGANISER_DETAILS = BASE_URL + "organiser";
}