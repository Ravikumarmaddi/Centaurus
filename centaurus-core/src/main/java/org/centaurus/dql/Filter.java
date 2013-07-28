package org.centaurus.dql;

import java.util.Arrays;

import org.centaurus.enums.Junction;
import org.centaurus.enums.MatchPlace;
import org.centaurus.enums.Operator;

/**
 * 
 * @author Vladislav Socolov
 */
public final class Filter {

	private Filter(){
		super();
	}
	
	public static Expression eq(String field, Object value) {
		return new Expression(field, value, Operator.eq);
	}

	public static Expression ne(String field, Object value) {
		return new Expression(field, value, Operator.ne);
	}

	public static Expression gt(String field, Object value) {
		return new Expression(field, value, Operator.gt);
	}

	public static Expression gte(String field, Object value) {
		return new Expression(field, value, Operator.gte);
	}

	public static Expression lt(String field, Object value) {
		return new Expression(field, value, Operator.lt);
	}

	public static Expression lte(String field, Object value) {
		return new Expression(field, value, Operator.lte);
	}
	
	public static Expression like(String field, Object value, MatchPlace matchPlace) {
		String endValue = value != null ? value.toString().trim() : "";
		switch (matchPlace) {
		case START:
			endValue = endValue + "%";
			break;
		case END:
			endValue = "%" + endValue;
			break;
		default:
			endValue = "%" + endValue + "%";
			break;
		}
		return new Expression(field, endValue, Operator.like);
	}

	public static Expression and(Expression... expressions) {
		return new Expression(Junction.AND).addDescendents(Arrays.asList(expressions));
	}

	public static Expression or(Expression... expressions) {
		return new Expression(Junction.OR).addDescendents(Arrays.asList(expressions));
	}
}
