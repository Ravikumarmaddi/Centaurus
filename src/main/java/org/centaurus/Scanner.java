package org.centaurus;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.centaurus.annotations.Document;
import org.centaurus.configuration.CentaurusConfig;
import org.centaurus.configuration.ConfigurationFileParser;
import org.centaurus.exceptions.ConfigurationException;
import org.scannotation.AnnotationDB;

/**
 * 
 * @author Vladislav Socolov
 */
public class Scanner extends ScannerTemplate{
	private static Logger log = Logger.getLogger(Scanner.class);
	private static final String TARGET_PREFIX = "target/classes/";
	
	private ConfigurationFileParser fileParser;
	
	public Scanner(){
		fileParser = new ConfigurationFileParser();
	}

	@Override
	protected void scanConfigurationFile() throws ConfigurationException {
    	log.info("Scanner step 1: Scanning configuration file.");
		fileParser.parseConfigFile();
	}

	@Override
	protected void scanPackages() throws ConfigurationException{  
		AnnotationDB db = new AnnotationDB();
		Set<URL> urlPaths = new HashSet<URL>();

		log.info("Scanner step 2: Scanning configuration packages.");
		if(CentaurusConfig.getInstance().getPackageMappingList() != null){
			for (String path : CentaurusConfig.getInstance().getPackageMappingList()) {
				String pathString = TARGET_PREFIX.concat(path).replace('.', '/');
				try {
					urlPaths.add(new File(pathString).toURI().toURL());
				} catch (MalformedURLException e) {
					log.error(String.format("Cannot cast mapping package path:(%s) to file URL.", pathString));
				}
			}	
		}
		
		String className = null;
		try {
			db.scanArchives((URL[])urlPaths.toArray(new URL[0]));
			if(db.getAnnotationIndex().get(Document.class.getName()) != null){
				String[] entityClasses = (String[]) db.getAnnotationIndex().get(Document.class.getName()).toArray(new String[0]);
				for(int i = 0; i < entityClasses.length; i++) {
					try{
						className = entityClasses[i];
						if(!CentaurusConfig.getInstance().getMappedClasses().contains(className)){
							Class<?> clazz = Class.forName(className);
							CentaurusConfig.getInstance().getMappedClasses().add(clazz);
							log.info(String.format("Scanned %s class.", className));
						}
					} catch (ClassNotFoundException e) {
						log.error(String.format("Cannot extract class type of name: %s", className));
					}
				}
			}
			
		} catch (IOException e) {
			throw new ConfigurationException("Cannot scan mapping packages in project classpath.", e);
		} 
	}
	
	@Override
	protected void scanClasses() {
		log.info("Scanner step 3: Scanning configuration classes.");
		if(CentaurusConfig.getInstance().getClassMappingList() != null){
			for (String className: CentaurusConfig.getInstance().getClassMappingList()) {
				if(!CentaurusConfig.getInstance().getMappedClasses().contains(className)){
					try {
						CentaurusConfig.getInstance().getMappedClasses().add(Class.forName(className));
						log.info(String.format("Scanned %s class.", className));
					} catch (ClassNotFoundException e) {
						log.error(String.format("Cannot extract class type of name: %s", className));
					}
				}
			}	
		}
	}
}
