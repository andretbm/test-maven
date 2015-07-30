package com.ignidata.api.plugin;


import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;

public class IgnidataSurveyApi {

    //public static final String IGNIDATA_BASE_URL = "http://10.0.2.2:5000";
    public static final String IGNIDATA_BASE_URL = "http://services.ignidata.com";


    // NOTE TO SELF
    // a void method is asynchronous
    // with return type, is synchronous
    public interface IgnidataSurveyApiInterface {
        @GET("/api/plugin")
        void getEndpoints(Callback<JsonObject> callback);

        @GET("/api/plugin/clients")
        void getClient(@Body Client client, Callback<Client> callback);

        @POST("/api/plugin/clients")
        void postClient(@Body Client client,
                        Callback<Client> callback);

        // TODO this burns my eyes, please take it out ASAP
        @GET("/getMobileSurvey")
        //@GET("/api/plugin/surveys") // for local testing, fool
        void protoGetSurvey(Callback<Response> callback);

        @GET("/api/plugin/surveys")
        void getSurvey(@Body Survey survey, Callback<Survey> callback);

        @PUT("/api/plugin/surveys")
        void putSurvey(@Body Survey survey, Callback<Survey> callback);

        @POST("/api/plugin/surveys")
        void postSurvey(@Body Survey survey, Callback<Survey> callback);


    }

}
