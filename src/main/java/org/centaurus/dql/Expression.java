package org.centaurus.dql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.centaurus.enums.Junction;
import org.centaurus.enums.Operator;

/**
 * 
 * @author Socolov Vladislav
 */
public class Expression implements Serializable {
	private static final long serialVersionUID = -4764922849082909423L;

	private String field;
	private Object value;
	private Junction junction;
	private Operator operator;
	private List<Expression> descendents;

	public Expression(Junction junction) {
		this.junction = junction;
	}
	
	public Expression(String field, Object value, Operator operator) {
		this.field = field;
		this.value = value;
		this.operator = operator;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Junction getJunction() {
		return junction;
	}

	public void setJunction(Junction junction) {
		this.junction = junction;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public List<Expression> getDescendents() {
		return descendents;
	}

	public Expression addDescendent(Expression descendent) {
		if (this.descendents == null) {
			this.descendents = new ArrayList<Expression>();
		}
		this.descendents.add(descendent);
		return this;
	}
	
	public Expression addDescendents(List<Expression> descendents) {
		if (this.descendents == null) {
			this.descendents = new ArrayList<Expression>();
		}
		this.descendents.addAll(descendents);
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Expression [field=");
		builder.append(field);
		builder.append(", value=");
		builder.append(value);
		builder.append(", junction=");
		builder.append(junction);
		builder.append(", operator=");
		builder.append(operator);
		builder.append(", descendents=");
		builder.append(descendents);
		builder.append("]");
		return builder.toString();
	}
	
}
