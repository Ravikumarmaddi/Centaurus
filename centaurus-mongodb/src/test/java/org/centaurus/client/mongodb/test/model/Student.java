package org.centaurus.client.mongodb.test.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.centaurus.annotations.Array;
import org.centaurus.annotations.Document;
import org.centaurus.annotations.Embedded;
import org.centaurus.annotations.Field;
import org.centaurus.annotations.Id;

@Document(collection = "students")
public class Student implements Serializable {

	private static final long serialVersionUID = 1202453819656115328L;

	@Id(defaultId = true)
	private String id;
	@Field(name = "age")
	private Integer age;
	@Field(name = "name")
	private String name;
	@Field(name = "created")
	private Date created;
	@Array(name = "arr")
	private String[] array;
	//@Array(name = "arr")
	private List<String> arrayList;
	@Array(name = "arrEmb")
	private EmbeddedStudent[] arrayEmb;
	//@Array(name = "arrEmb")
	private List<EmbeddedStudent> arrayListEmb;
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

	public Student(Integer age, String name, String[] array) {
		this.age = age;
		this.name = name;
		this.array = array;
	}
	
	public Student(Integer age, String name, List<String> arrayList) {
		this.age = age;
		this.name = name;
		this.arrayList = arrayList;
	}
	
	public Student(String id, Integer age, String name, EmbeddedStudent[] arrayEmb) {
		this.id = id;
		this.age = age;
		this.name = name;
		this.arrayEmb = arrayEmb;
	}
	
	public Student(String id, Integer age, String name, List<EmbeddedStudent> arrayListEmb) {
		this.id = id;
		this.age = age;
		this.name = name;
		this.arrayListEmb = arrayListEmb;
	}
	
	public Student(String id, Integer age, String name, EmbeddedStudent embeddedStudent) {
		this.id = id;
		this.age = age;
		this.name = name;
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

	public String[] getArray() {
		return array;
	}

	public void setArray(String[] array) {
		this.array = array;
	}

	public List<String> getArrayList() {
		return arrayList;
	}

	public void setArrayList(List<String> arrayList) {
		this.arrayList = arrayList;
	}

	public EmbeddedStudent[] getArrayEmb() {
		return arrayEmb;
	}

	public void setArrayEmb(EmbeddedStudent[] arrayEmb) {
		this.arrayEmb = arrayEmb;
	}

	public List<EmbeddedStudent> getArrayListEmb() {
		return arrayListEmb;
	}

	public void setArrayListEmb(List<EmbeddedStudent> arrayListEmb) {
		this.arrayListEmb = arrayListEmb;
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
		builder.append(", array=");
		builder.append(Arrays.toString(array));
		builder.append(", arrayList=");
		builder.append(arrayList);
		builder.append(", arrayEmb=");
		builder.append(Arrays.toString(arrayEmb));
		builder.append(", arrayListEmb=");
		builder.append(arrayListEmb);
		builder.append(", embeddedStudent=");
		builder.append(embeddedStudent);
		builder.append("]");
		return builder.toString();
	}
	
}
