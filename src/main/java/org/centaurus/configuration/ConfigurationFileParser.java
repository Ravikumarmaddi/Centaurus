package org.centaurus.configuration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.centaurus.annotations.ConfigProperty;
import org.centaurus.configuration.xml.MappingElement;
import org.centaurus.configuration.xml.PropertyElement;
import org.centaurus.configuration.xml.ConfigurationElement;
import org.centaurus.exceptions.ConfigurationException;
import org.centaurus.utils.XmlUtils;

/**
 * 
 * @author Vladislav Socolov
 */
public class ConfigurationFileParser {
	private static Logger log = Logger.getLogger(ConfigurationFileParser.class);
	private final String propertyFileName = "centaurus.properties";
	
	public void parseConfigFile() throws ConfigurationException {
		try {
			Properties prop = new Properties();
			prop.load(ConfigurationFileParser.class.getClassLoader().getResourceAsStream(propertyFileName));
			CentaurusConfig.getInstance().setConfigFileName(prop.getProperty("configFileName"));
		} catch (IOException e) {
			throw new ConfigurationException(String.format("Cannot find %s file in project classpath.", propertyFileName));
		}
		
		URL url = this.getClass().getResource(CentaurusConfig.getInstance().getConfigFileName());
		if(url == null){
			throw new ConfigurationException(String.format("Cannot find %s file in project classpath.", CentaurusConfig.getInstance().getConfigFileName()));
		}

		try {
			File file = new File(url.getPath());
			ConfigurationElement configElement = XmlUtils.unmarshalXmlFile(file, ConfigurationElement.class);
			
			if(configElement == null){
				throw new ConfigurationException("Cannot read configuration file. File is empty.");
			}
			
			List<String> classMappingList = new ArrayList<String>();
			List<String> packageMappingList = new ArrayList<String>();
			List<MappingElement> mappingElements = configElement.getMappingElement();
			if(mappingElements != null){
				for (MappingElement mapping : configElement.getMappingElement()) {
					if(mapping.getPackagePath() != null){
						packageMappingList.add(mapping.getPackagePath());
					} else {
						classMappingList.add(mapping.getClassPath());
					}
				}
				CentaurusConfig.getInstance().setClassMappingList(classMappingList);
				log.info("Read configuration file. Set mapping.class property.");
				CentaurusConfig.getInstance().setPackageMappingList(packageMappingList);
				log.info("Read configuration file. Set mapping.package property.");
			}
			
			if(configElement.getPropertyElements() != null){
				for (PropertyElement property : configElement.getPropertyElements()) {
					Field[] declaredFields = CentaurusConfig.getInstance().getClass().getDeclaredFields();
					for (Field field : declaredFields) {
						ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
						if(annotation != null && annotation.name().equals(property.getName())){
							field.setAccessible(true);
							Class<?> type = field.getType();
							try {
								field.set(CentaurusConfig.getInstance(), type.cast(property.getValue()));	
								log.info(String.format("Read configuration file. Set %s property.", property.getName()));
							} catch (Exception e) {
								throw new ConfigurationException(String.format("Cannot set %s property.", property.getName()), e);
							}
						}
					}
				}	
			} else {
				throw new ConfigurationException("Cannot find proprietes elements in centaurus.cfg.xml file.");
			}
		} catch (JAXBException e) {
			throw new ConfigurationException(String.format("Cannot find %s file in project classpath.", CentaurusConfig.getInstance().getConfigFileName()));
		}
	}
	
}
