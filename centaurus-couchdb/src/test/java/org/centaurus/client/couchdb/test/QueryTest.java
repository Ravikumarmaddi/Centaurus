package org.centaurus.client.couchdb.test;

import java.util.Date;

import org.centaurus.Session;
import org.centaurus.SessionFactory;
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
	
	@Test
	public void insert_should_insert_successfuly_new_user(){
		Student student = new Student("TestId", 99, "Test", new Date());
		session.insert(student);
		Assert.assertNotNull(student);
	}
}
