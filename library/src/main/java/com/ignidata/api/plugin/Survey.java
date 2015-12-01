/**
 * 
 */
package com.ignidata.api.plugin;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Survey {

    public Survey() {
        questions = new LinkedList<>();
    }

	Object id;

    // my own interpretation
    double pricepoint;
    String userId;
    String partnerId;

    String surveyId; // ????
    boolean isValid; // will check if survey fits criteria in the end

    // end my own interpretation

    String surveyName;
	int numberOfSurveys;
    int numberOfQuestions;
    String targetCountry;
    double surveyPrice;
    //TODO Customer user;
    Date createDate;
    String gender;
    String age;
    Date beginDate;
    Date finishDate;
    boolean active;
    List<Question> questions;
    String paymillToken;
}
