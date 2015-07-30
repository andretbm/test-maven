package models;

import com.google.gson.annotations.SerializedName;

public class SurveyParams {
	@SerializedName("_id")
	ObjectId id;
	String userHash;
	Double pricepoint;
	ObjectId providedSurveyId;
	String device;
	Double locationLat;
	Double locationLong;
}
