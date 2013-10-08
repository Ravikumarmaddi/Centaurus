package org.centaurus.client.couchdb.test;

import org.centaurus.client.Mapper;
import org.centaurus.client.couchdb.CouchDBMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CouchDBMapperTest {

	private Mapper mapper;
	
	@Before
	public void init() {
		mapper = new CouchDBMapper();
		Assert.assertNotNull(mapper);
	}
	
	@Test
	public void documentToDBObject_sholud_not_return_null_object(){
		//TODO not finished method
	}
}
