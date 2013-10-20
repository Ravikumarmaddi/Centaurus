package org.centaurus.client.couchdb.test;

import org.centaurus.Session;
import org.centaurus.SessionFactory;
import org.centaurus.client.Mapper;
import org.centaurus.client.couchdb.CouchDBMapper;
import org.centaurus.client.couchdb.test.model.Student;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CouchDBMapperTest {

	private Mapper mapper;
	private Session session;
	
	@Before
	public void init() {
		session = SessionFactory.openSession();
		mapper = new CouchDBMapper();
		Assert.assertNotNull(mapper);
		Assert.assertNotNull(session);
	}
	
	@Test
	public void documentToDBObject_sholud_not_return_null_object(){
		try {
			Student student = new Student("TestId", 99, "Test");
			Object obj = mapper.documentToDBObject(student);
			Assert.assertNotNull(obj);	
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
}
