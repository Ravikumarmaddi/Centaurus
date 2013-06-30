package org.centaurus.utils;

import java.io.File;
import java.io.Serializable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * 
 * @author Vladislav Socolov
 */
public class XmlUtils {

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T unmarshalXmlFile(File file, Class<?> clazz) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return (T) jaxbUnmarshaller.unmarshal(file);
	}

}
