package org.centaurus.configuration.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * @author Vladislav Socolov
 */
@XmlType(name = "mapping")
public class MappingElement implements Serializable {

	private static final long serialVersionUID = -5778143667045145297L;

	private String classPath;
	private String packagePath;

	public MappingElement() {
		super();
	}

	@XmlAttribute(name = "class")
	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	@XmlAttribute(name = "package")
	public String getPackagePath() {
		return packagePath;
	}

	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MappingElement [classPath=");
		builder.append(classPath);
		builder.append(", packagePath=");
		builder.append(packagePath);
		builder.append("]");
		return builder.toString();
	}

}
