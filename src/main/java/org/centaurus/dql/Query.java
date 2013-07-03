package org.centaurus.dql;

import java.util.List;

import org.centaurus.enums.Sorting;

/**
 * 
 * @author Vladislav Socolov
 */
public interface Query<T> {

	public Query<T> where(Expression expression);

	public Query<T> where(Expression... expression);

	public Query<T> offset(Long offset);

	public Query<T> limit(Long limit);

	public Query<T> sort(String field, Sorting sorting);

	public List<T> list();

	public T first();

	public T last();
}
