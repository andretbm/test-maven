package com.ignidata.api.plugin;


import java.util.HashMap;
import java.util.Map;

public class Globals {

    // for now, not to be disclosed
    private static String apiKey = "";
    private static double pricepoint = 0.0;
    private static String debug = "";

    public static Map<String, String> endpoints = new HashMap<>();
    // public static Client client;

    // Request codes
    public static final int REQUEST_SURVEY = 1;

    // Result codes
    public static final int RESULT_SUCCESS = 100;
    public static final int RESULT_CANCEL = 101;
    public static final int RESULT_ERROR = 102;
    public static final int RESULT_INVALID = 103;


    // Known origins
    public static final int USING_ANDROID = 200;
    public static final int USING_UNITY3D = 201;

}
