package com.ignidata.api.plugin;


import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Date;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public class IgnidataSurveyApi {

    //public static final String IGNIDATA_BASE_URL = "http://10.0.2.2:5000";
    public static final String IGNIDATA_BASE_URL = "http://surveys.ignidata.com";
    // public static final String IGNIDATA_BASE_URL = "http://192.168.3.116:9000";
    public static final String POSTAL_CODE_URL = "http://maps.googleapis.com/maps/api/geocode/json?sensor=true";

    // NOTE TO SELF
    // a void method is asynchronous
    // with return type, is synchronous
    public interface IgnidataSurveyApiInterface {
        @GET("/api/plugin")
        void getEndpoints(Callback<JsonObject> callback);


        @GET("/maps/api/geocode/json?sensor=true&address={address}")
        void getInformationPostalCode(@Query("address")String address, Callback<JSONObject>callback);

        @GET("/api/plugin/clients")
        void getClient(@Body Client client, Callback<Client> callback);

        //@POST("/getMobileSurvey")
        //  void postClient(@Body Client client,1
        //Callback<Client> callback);

        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        @FormUrlEncoded
        @POST("/getMobileSurvey")
            //void postClient(@Body Client client, Callback<Response> callback);

        void postClient(@Field("birthdate")String age,@Field("gender")String gender,@Field("language")String language,@Field("profession") String profession,@Field("postalcode") String postalCode,@Field("city") String city,@Field("country") String country,Callback<Response>callback);



        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        @FormUrlEncoded
        @POST("/getMobileSurvey")
            //void postClient(@Body Client client, Callback<Response> callback)
        void postClientWithParticipantID(@Field("participant_id") String participant_id, Callback<Response>callback);


        //@GET("/getMobileSurvey")
        //@GET("/api/plugin/surveys") // for local testing, fool
        // void protoGetSurvey(Callback<Response> callback);
        //@POST("/getMobileSurvey")
        //void postMobileSurvey(Callback<Response> callback);

        @GET("/api/plugin/surveys")
        void getSurvey(@Body Survey survey, Callback<Survey> callback);

        @PUT("/api/plugin/surveys")
        void putSurvey(@Body Survey survey, Callback<Survey> callback);

        @POST("/api/plugin/surveys")
        void postSurvey(@Body Survey survey, Callback<Survey> callback);




    }

}
