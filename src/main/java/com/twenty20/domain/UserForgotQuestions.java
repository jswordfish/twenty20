package com.twenty20.domain;

import javax.persistence.Embeddable;

@Embeddable
public class UserForgotQuestions {
	
	
	String question1 = "What was your childhood nickname?";
	
	String question2 = "What was the name of your best friend at school?";
	
	String question3 = "What is your Maternal Grandmother's maiden name?";
	
	String question4 = "In what city does your nearest sibling live?";
	
	
	String question5 = "In what city or town was your first job?";
	
	String question6 = "Where did you meet your partner?";
	
	String question7 = "What is your favourite holiday destination?";
	
	String questionChosen = "question1";
	
	String answer;

	public String getQuestion1() {
		return question1;
	}

	public void setQuestion1(String question1) {
		this.question1 = question1;
	}

	public String getQuestion2() {
		return question2;
	}

	public void setQuestion2(String question2) {
		this.question2 = question2;
	}

	public String getQuestion3() {
		return question3;
	}

	public void setQuestion3(String question3) {
		this.question3 = question3;
	}

	public String getQuestion4() {
		return question4;
	}

	public void setQuestion4(String question4) {
		this.question4 = question4;
	}

	public String getQuestion5() {
		return question5;
	}

	public void setQuestion5(String question5) {
		this.question5 = question5;
	}

	public String getQuestion6() {
		return question6;
	}

	public void setQuestion6(String question6) {
		this.question6 = question6;
	}

	public String getQuestion7() {
		return question7;
	}

	public void setQuestion7(String question7) {
		this.question7 = question7;
	}

	public String getQuestionChosen() {
		return questionChosen;
	}

	public void setQuestionChosen(String questionChosen) {
		this.questionChosen = questionChosen;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	

}
