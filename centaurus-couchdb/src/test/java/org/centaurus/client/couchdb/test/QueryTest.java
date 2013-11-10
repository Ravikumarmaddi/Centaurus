package org.centaurus.client.couchdb.test;

import java.util.Date;
import java.util.List;

import org.centaurus.Session;
import org.centaurus.SessionFactory;
import org.centaurus.client.couchdb.test.model.EmbeddedStudent;
import org.centaurus.client.couchdb.test.model.Student;
import org.centaurus.dql.Filter;
import org.centaurus.dql.Query;
import org.centaurus.enums.Sorting;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class QueryTest {
	private Session session;
	private Query<?> query;

	@Before
	public void init() {
		session = SessionFactory.openSession();
		query = session.createQuery(Student.class);
		Assert.assertNotNull(session);
		Assert.assertNotNull(query);
	}
	
	//@Test
	public void insert_should_insert_successfuly_new_user(){
		Student student = new Student("TestId", 99, "Test", new Date(), new String[]{"English", "Math"}, new EmbeddedStudent(11, 11, 2011));
		Student newStudent = session.insert(student);
		Assert.assertNotNull(newStudent);
	}
	
	//@Test
	public void update_should_update_successfuly_user(){
		Student student = new Student("TestId","1-b6d0c491777bcae12d6e4881844bdb4c", 999, "Testic");
		Student newStudent = session.update(student);
		Assert.assertNotNull(newStudent);
	}
	
	//@Test
	public void delete_should_delete_successfuly_user(){
		Student student = new Student("TestId","1-4a4a9f7b06b7f8616c5b80ad7e3e07ad", 999, "Testic");
		session.delete(student);
	}
	
	@Test
	public void list_should_return_not_empty_list(){
		List<?> list = query.list();
		Assert.assertEquals(false, list.isEmpty());
	}
	
	@Test
	public void first_should_not_be_null(){
		Student first = (Student) query.first();
		Assert.assertNotNull(first);
	}
	
	@Test
	public void last_should_not_be_null(){
		Student last = (Student) query.last();
		Assert.assertNotNull(last);
	}
	
	@Test
	public void count_should_not_be_null(){
		Number count = query.count();
		Assert.assertNotNull(count);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void where_should_return_not_empty_list(){
		List<Student> list = (List<Student>) query.where(Filter.or(
															Filter.and(Filter.eq("age", 23), Filter.eq("name", "Vlad")),
															Filter.and(Filter.eq("age", 19), Filter.eq("name", "Tina"))
															)).list();
		Assert.assertEquals(false, list.isEmpty());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void wherelimit_should_not_return_empty_list(){
		List<Student> list = (List<Student>) query.where(Filter.gt("age", 10)).limit(1).list();
		Assert.assertEquals(1, list.size());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void sort_should_not_return_empty_list(){
		List<Student> list = (List<Student>) query.where(Filter.gt("age", 10)).sort("age", Sorting.DESC).list();
		Assert.assertEquals(4, list.size());
	}
	
	@Test
	public void count_should_not_return_4(){
		Number count = query.where(Filter.gt("age", 23)).count();
		Assert.assertEquals(2, count.intValue());
	}
	
}
