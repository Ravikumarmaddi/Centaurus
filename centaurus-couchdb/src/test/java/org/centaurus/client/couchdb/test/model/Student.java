package org.centaurus.client.couchdb.test.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import org.centaurus.annotations.Array;
import org.centaurus.annotations.Document;
import org.centaurus.annotations.Embedded;
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
	@Field(name = "created")
	private Date created;
	@Array(name = "courses")
	private String[] courses;
	@Embedded(name  = "embedded")
	private EmbeddedStudent embeddedStudent;
	
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
	
	public Student(String id, Integer age, String name, Date created) {
		this.id = id;
		this.age = age;
		this.name = name;
		this.created = created;
	}
	
	public Student(String id, Integer age, String name, Date created, String[] courses) {
		this.id = id;
		this.age = age;
		this.name = name;
		this.created = created;
		this.courses = courses;
	}
	
	public Student(String id, Integer age, String name, Date created, String[] courses, EmbeddedStudent embeddedStudent) {
		this.id = id;
		this.age = age;
		this.name = name;
		this.created = created;
		this.courses = courses;
		this.embeddedStudent = embeddedStudent;
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
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String[] getCourses() {
		return courses;
	}

	public void setCourses(String[] courses) {
		this.courses = courses;
	}
	
	public EmbeddedStudent getEmbeddedStudent() {
		return embeddedStudent;
	}

	public void setEmbeddedStudent(EmbeddedStudent embeddedStudent) {
		this.embeddedStudent = embeddedStudent;
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
		builder.append(", created=");
		builder.append(created);
		builder.append(", courses=");
		builder.append(Arrays.toString(courses));
		builder.append(", embeddedStudent=");
		builder.append(embeddedStudent);
		builder.append("]");
		return builder.toString();
	}
}
