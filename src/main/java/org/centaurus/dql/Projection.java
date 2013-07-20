package org.centaurus.dql;

import org.centaurus.enums.ProjectionType;


/**
 * 
 * @author Vladislav Socolov
 */
public final class Projection {
	
	private String field;
	private ProjectionType projectionType;
	
	public Projection(String field, ProjectionType projectionType){
		this.field = field;
		this.projectionType = projectionType;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public ProjectionType getProjectionType() {
		return projectionType;
	}

	public void setProjectionType(ProjectionType projectionType) {
		this.projectionType = projectionType;
	}

	public static Projection set(String field, ProjectionType projectionType){
		return new Projection(field, projectionType);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Projection [field=");
		builder.append(field);
		builder.append(", projectionType=");
		builder.append(projectionType);
		builder.append("]");
		return builder.toString();
	}

}
