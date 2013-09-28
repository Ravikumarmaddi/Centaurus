package org.centaurus.client.mongodb.test;

import java.util.List;

import junit.framework.Assert;

import org.centaurus.Session;
import org.centaurus.SessionFactory;
import org.centaurus.client.mongodb.test.model.Student;
import org.centaurus.dql.Filter;
import org.centaurus.dql.Projection;
import org.centaurus.dql.Query;
import org.centaurus.enums.ProjectionType;
import org.centaurus.enums.Sorting;
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
	
	@SuppressWarnings("unchecked")
	@Test
	public void list_should_return_not_empty_list(){
		List<Student> list = (List<Student>) query.list();
		Assert.assertEquals(false, list.isEmpty());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void sort_should_return_notempty_list(){
		List<Student> list = (List<Student>) query.sort("_id", Sorting.DESC).list();
		Assert.assertEquals(false, list.isEmpty());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void limit_should_return_2items_in_list(){
		List<Student> list = (List<Student>) query.limit(2).list();
		Assert.assertEquals(2, list.size());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void offset_should_return_notempty_list(){
		List<Student> list = (List<Student>) query.offset(2).list();
		Assert.assertEquals(false, list.isEmpty());
	}
	
	@Test
	public void first_should_return_not_null_object(){
		Student student = (Student) query.first();
		Assert.assertNotNull(student);
	}
	
	@Test
	public void last_should_return_not_null_object(){
		Student student = (Student) query.last();
		Assert.assertNotNull(student);
	}
	
	@Test
	public void count_should_return_number(){
		Long count = (Long) query.count();
		if(count == 0L)
			Assert.assertFalse(true);
		Assert.assertFalse(false);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void where_should_return_not_empty_list(){
		List<Student> list = (List<Student>) query.where(Filter.gt("age", 20), Filter.eq("name", "Victor"))
													.project(Projection.set("_id", ProjectionType.EXCLUDE))
													.list();
		Assert.assertEquals(false, list.isEmpty());
	}
}
