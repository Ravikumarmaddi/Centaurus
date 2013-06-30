package org.centaurus.client;

import java.io.Serializable;

/**
 * 
 * @author Vladislav Socolov
 */
public interface DBClient extends Serializable {

	public <T> T insert(T document);

	public <T> T insertOrUpdate(T document);

	public <T> T update(T document);

	public <T> void delete(T document);

	public void clear();

	public void close();
	
	public Mapper getMapper();

	public void setMapper(Mapper mapper);
}
