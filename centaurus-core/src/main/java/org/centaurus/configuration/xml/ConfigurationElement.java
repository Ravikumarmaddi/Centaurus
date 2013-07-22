package org.centaurus.configuration.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Vladislav Socolov
 */
@XmlRootElement(name = "centaurus-configuration")
public class ConfigurationElement implements Serializable {

	private static final long serialVersionUID = 7773455146251877498L;

	private List<PropertyElement> propertyElements;
	private List<MappingElement> mappingElement;

	public ConfigurationElement() {
		super();
	}

	@XmlElement(name = "mapping")
	public List<MappingElement> getMappingElement() {
		return mappingElement;
	}

	public void setMappingElement(List<MappingElement> mappingElement) {
		this.mappingElement = mappingElement;
	}

	@XmlElementWrapper(name = "properties")
	@XmlElement(name = "property")
	public List<PropertyElement> getPropertyElements() {
		return propertyElements;
	}

	public void setPropertyElements(List<PropertyElement> propertyElements) {
		this.propertyElements = propertyElements;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConfigurationElement [propertyElements=");
		builder.append(propertyElements);
		builder.append(", mappingElement=");
		builder.append(mappingElement);
		builder.append("]");
		return builder.toString();
	}
}
