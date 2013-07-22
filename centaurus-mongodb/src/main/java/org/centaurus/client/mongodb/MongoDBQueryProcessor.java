package org.centaurus.client.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.centaurus.client.QueryProcessor;
import org.centaurus.dql.Expression;
import org.centaurus.dql.Projection;
import org.centaurus.dql.QueryData;
import org.centaurus.enums.Junction;
import org.centaurus.enums.Operator;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

/**
 * 
 * @author Vladislav Socolov
 */
@SuppressWarnings("unchecked")
public class MongoDBQueryProcessor<T extends DBObject> implements QueryProcessor<T> {

	public T processWhereClause(QueryData queryData) {	
		List<Expression> expressionList = queryData.getExpressionList();
		List<T> filters = new ArrayList<T>();
		for (Expression expression : expressionList) {
			if(expression.getDescendents() != null){
				filters.add(processNestedLogicalOperators(expression));//process descendents
			} else {
				if(expression.getOperator() == Operator.eq){
					filters.add((T) new BasicDBObject(expression.getField(), expression.getValue()));
				} else {
					filters.add((T) new BasicDBObject(expression.getField(), new BasicDBObject("$" + expression.getOperator().name(), expression.getValue())));
				}
			}
		}
		
		return 	(T) QueryBuilder.start().and(filters.toArray(new BasicDBObject[0])).get();
	}

	public Number processOffsetClause(QueryData queryData) {
		return queryData.getOffset();
	}

	public Number processLimitClause(QueryData queryData) {
		return queryData.getLimit();
	}

	public T processSortClause(QueryData queryData) {
		return (T) new BasicDBObject(queryData.getSortOption().getField(), queryData.getSortOption().getSorting().getValue());	
	}
	
	public T processProjectionClause(QueryData queryData) {
		BasicDBObject dbObject = new BasicDBObject();
		for (Projection projection : queryData.getProjectionList()) {
			dbObject.append(projection.getField(), projection.getProjectionType().getValue());
		}
		return (T) dbObject;
	}
	
	private T processNestedLogicalOperators(Expression expression){
		Junction junction = expression.getJunction();
		List<T> nestedFilters = new ArrayList<T>();
		for (Expression expr : expression.getDescendents()) {
			if(expr.getDescendents() != null){
				nestedFilters.add(processNestedLogicalOperators(expr)); //Recursive process
			} else {
				if(expr.getOperator() == Operator.eq){
					nestedFilters.add((T) new BasicDBObject(expr.getField(), expr.getValue()));
				} else {
					nestedFilters.add((T) new BasicDBObject(expr.getField(), new BasicDBObject("$" + expr.getOperator().name(), expr.getValue())));
				}
			}
		}
		
		if(junction == Junction.AND){
			return (T) QueryBuilder.start().and(nestedFilters.toArray(new BasicDBObject[0])).get();
		}
		return (T) QueryBuilder.start().or(nestedFilters.toArray(new BasicDBObject[0])).get();
	}
	
}
