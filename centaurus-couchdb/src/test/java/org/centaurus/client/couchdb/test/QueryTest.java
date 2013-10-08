package org.centaurus.client.couchdb.test;

import org.centaurus.Session;
import org.centaurus.SessionFactory;
import org.centaurus.client.couchdb.test.model.Student;
import org.centaurus.dql.Query;
import org.junit.Assert;
import org.junit.Before;

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
}
