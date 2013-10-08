package org.centaurus.client.couchdb.test.model;

import java.io.Serializable;

import org.centaurus.annotations.Document;
import org.centaurus.annotations.Field;
import org.centaurus.annotations.Id;

@Document(collection = "students")
public class Student implements Serializable {

	private static final long serialVersionUID = -5266511816374301596L;
	
	@Id(defaultId = true)
	private String id;
	@Field(name = "age")
	private Integer age;
	@Field(name = "name")
	private String name;
	
	public Student() {
		super();
	}

	public Student(Integer age, String name) {
		this.age = age;
		this.name = name;
	}

	public Student(String id, Integer age, String name) {
		this.id = id;
		this.age = age;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Student [id=");
		builder.append(id);
		builder.append(", age=");
		builder.append(age);
		builder.append(", name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}
	
}
