package org.centaurus.client.couchdb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.centaurus.client.QueryProcessor;
import org.centaurus.dql.Expression;
import org.centaurus.dql.QueryData;
import org.centaurus.enums.Junction;

/**
 * 
 * @author Vladislav Socolov
 */
@SuppressWarnings("unchecked")
public class CouchDBQueryProcessor <T extends CharSequence> implements QueryProcessor<T> {

	public T processWhereClause(QueryData queryData) {
		List<Expression> expressionList = queryData.getExpressionList();
		List<String> clauses = new ArrayList<String>();
		StringBuilder builder = new StringBuilder("if(");
		
		for (Expression expression : expressionList) {
			if(expression.getDescendents() != null){
				clauses.add(processNestedLogicalOperators(expression).toString());//process descendents
			} else {
				clauses.add(new StringBuilder().append("doc." + expression.getField()).append(expression.getOperator().getValue()).append(convertValuesToString(expression.getValue())).toString());
				convertValuesToString(expression.getValue());
			}
		}
		
		for (int i = 0; i < clauses.size(); i++) {
			if(i == 0){
				builder.append(clauses.get(i));
			} else {
				builder.append("&&").append(clauses.get(i));
			}
		}
		builder.append(")");
		
		return (T) builder.toString();
	}

	public Number processOffsetClause(QueryData queryData) {
		return queryData.getOffset();
	}

	public Number processLimitClause(QueryData queryData) {
		return queryData.getLimit();
	}

	public T processSortClause(QueryData queryData) {
		// TODO Auto-generated method stub
		return null;
	}

	public T processProjectionClause(QueryData queryData) {
		// TODO Auto-generated method stub
		return null;
	}

	private T processNestedLogicalOperators(Expression expression){
		Junction junction = expression.getJunction();
		StringBuilder builder = new StringBuilder("(");
		List<String> clauses = new ArrayList<String>();
		
		for (Expression expr : expression.getDescendents()) {
			if(expr.getDescendents() != null){
				clauses.add(processNestedLogicalOperators(expr).toString()); //Recursive process
			} else {
				clauses.add(new StringBuilder().append("doc." + expr.getField()).append(expr.getOperator().getValue()).append(convertValuesToString(expr.getValue())).toString());
			}
		}
		
		for (int i = 0; i < clauses.size(); i++) {
			if(i == 0){
				builder.append(clauses.get(i));
			} else {
				builder.append(junction.getValue()).append(clauses.get(i));
			}
		}
		builder.append(")");
		
		return (T) builder.toString();
	}
	
	private Object convertValuesToString(Object object){
		Class<? extends Object> clazz = object.getClass();

		if(clazz.equals(String.class)) {
			return String.format("'%s'", object);
		} else if(clazz.equals(Date.class)) {
			SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.ROOT);
			return String.format("'%s'", formatter.format(object));
		} else {
			return object; // return numbers or booleans
		}
	}
	
}
