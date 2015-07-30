/**
 * 
 */
package com.ignidata.api.plugin;


import java.util.ArrayList;
import java.util.List;


/**
 * A question in the survey, with all the relevant data
 * @author Ignidata Lda.
 */
public class Question {
	public enum QuestionType {
		ChooseOne, ChooseMany, FreeValue,
		//ChooseN, StarRating, Slider, FreeValue
	}
	
	protected QuestionType type;
	public QuestionType getType() {
		return type;
	}
	
	protected String title;
	public String getTitle() {
		return title;
	}
	
	protected List<Answer> answers;
	public List<Answer> getAnswers() {
		return answers;
	}
	
	protected int id;
	public int getId() {
		return id;
	}

	/*
	protected Survey parent;
	public Survey getParent() {
		return parent;
	}
	
	public boolean isFirstQuestion() { 
		return id==0; 
	}
	public boolean isLastQuestion() { 
		return id==(getParent().getQuestionCount() - 1);
	}
	*/
	
	protected List<Integer> selected;
	public List<Integer> getSelected() {
		return selected;
	}

	
	/**
	 * 
	 */
	public Question(QuestionType type, int id, String title, List<Answer> answers) {
		this();
		this.type = type;
		this.id = id;
		this.title = title;
		this.answers = answers;
	}
	private Question() {
		// Empty constructor
		selected = new ArrayList<Integer>();
	}
	

/*	public static Question newInstance(QuestionModel qm) {
		Question q = new Question();
		ArrayList<Answer> answers = new ArrayList<Answer>();
		for(AnswerModel am : qm.answers){
			answers.add(new Answer(am.text, am.answerId));
		}
		q.answers = answers;
		q.id = qm.questionId;
		q.title = qm.title;
		q.type = QuestionType.valueOf(qm.type);
		return q;
	}*/
}
