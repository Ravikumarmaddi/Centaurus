package org.centaurus.client.mongodb.test.model;

import java.io.Serializable;

import org.centaurus.annotations.Document;
import org.centaurus.annotations.Field;

@Document
public class EmbeddedStudent implements Serializable{

	private static final long serialVersionUID = -3424400955937406520L;

	@Field
	private Integer date;
	@Field
	private Integer month;
	@Field
	private Integer year;
	
	public EmbeddedStudent(){
		super();
	}
	
	public EmbeddedStudent(Integer date, Integer month, Integer year){
		this.date = date;
		this.month = month;
		this.year = year;
	}

	public Integer getDate() {
		return date;
	}

	public void setDate(Integer date) {
		this.date = date;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EmbeddedStudent [date=");
		builder.append(date);
		builder.append(", month=");
		builder.append(month);
		builder.append(", year=");
		builder.append(year);
		builder.append("]");
		return builder.toString();
	}
}
