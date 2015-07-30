/**
 * 
 */
package models;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * @author IGNIDATA Lda.
 * @version 1.1
 * @see 
 *
 * // Store data of survey
 */
public class Survey {

	@SerializedName("_id")
	Object id;
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
    SurveyParams params;    // this has to be added
}
