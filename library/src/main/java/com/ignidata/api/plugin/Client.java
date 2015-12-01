package com.ignidata.api.plugin;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Client implements Serializable {
	Object id;
	String userId;
	String userAgent; // new requirement for first-time users correct id
	List<Partner> partner;
	String gender;
	String country;
	String city;
	String profession;
	String postalCode;
	String language;
	String age;
	String surveyID;


	Client(String age, String gender, String profession, String city, String country, String postalCode, String language,String userID) {
		this.age = age;
		this.gender = gender;
		this.profession = profession;
		this.city = city;
		this.language = language;
		this.country = country;
		this.postalCode = postalCode;
		this.userId = userID;
	}


}

