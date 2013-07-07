package org.centaurus.client.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.centaurus.client.QueryProcessor;
import org.centaurus.dql.Expression;
import org.centaurus.dql.QueryData;
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
		List<BasicDBObject> filters = new ArrayList<BasicDBObject>();
		for (Expression expression : expressionList) {
			if(expression.getDescendents() != null){
				//TODO call recursive logic operator processor
			} else {
				if(expression.getOperator() == Operator.eq){
					filters.add(new BasicDBObject(expression.getField(), expression.getValue()));
				} else {
					filters.add(new BasicDBObject(expression.getField(), new BasicDBObject("$" + expression.getOperator().name(), expression.getValue())));
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

	private BasicDBObject processNestedLogicalOperators(Expression expression){
		
		return null;
	}
}
