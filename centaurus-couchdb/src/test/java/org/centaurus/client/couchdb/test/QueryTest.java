package org.centaurus.client.couchdb.test;

import java.util.Date;
import java.util.List;

import org.centaurus.Session;
import org.centaurus.SessionFactory;
import org.centaurus.client.couchdb.test.model.EmbeddedStudent;
import org.centaurus.client.couchdb.test.model.Student;
import org.centaurus.dql.Query;
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
}
