package org.centaurus;

import org.apache.log4j.Logger;
import org.centaurus.configuration.CentaurusConfig;
import org.centaurus.configuration.ConfigurationFileParser;
import org.centaurus.exceptions.ConfigurationException;

/**
 * 
 * @author Vladislav Socolov
 */
public class Scanner extends ScannerTemplate {
	private static Logger log = Logger.getLogger(Scanner.class);

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
