package org.centaurus.configuration;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.centaurus.annotations.ConfigProperty;
import org.centaurus.enums.DBType;

/**
 * 
 * @author Vladislav Socolov
 */
public class CentaurusConfig implements Serializable {
	private static final long serialVersionUID = -7283971125264935572L;

	private static CentaurusConfig instance;
	
	private String configFileName;
	private List<String> classMappingList;
	private List<String> packageMappingList;
	
	//DB connection proprietes
	@ConfigProperty(name = "connection.db_type")
	private String dbType;
	@ConfigProperty(name = "connection.host")
	private String  dbHost;
	@ConfigProperty(name = "connection.db")
	private String dbName;
	@ConfigProperty(name = "connection.port")
	private String dbPort;
	@ConfigProperty(name = "connection.user")
	private String dbUsername;
	@ConfigProperty(name = "connection.password")
	private String dbPassword;
	
	private Set<Class<?>> mappedClasses;
	
	private CentaurusConfig(){
		super();
	}

	public static CentaurusConfig getInstance() {
		if(instance == null){
			instance = new CentaurusConfig();
		}
		return instance;
	}

	public String getConfigFileName() {
		return configFileName;
	}

	public void setConfigFileName(String configFileName) {
		this.configFileName = configFileName;
	}

	public List<String> getClassMappingList() {
		return classMappingList;
	}

	public void setClassMappingList(List<String> classMappingList) {
		this.classMappingList = classMappingList;
	}

	public List<String> getPackageMappingList() {
		return packageMappingList;
	}

	public void setPackageMappingList(List<String> packageMappingList) {
		this.packageMappingList = packageMappingList;
	}

	public String getDbType() {
		return dbType;
	}

	public DBType getDbTypeEnum() {
		return DBType.valueOf(dbType);
	}
	
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getDbHost() {
		return dbHost;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbPort() {
		return dbPort;
	}

	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public Set<Class<?>> getMappedClasses() {
		if(mappedClasses == null){
			mappedClasses = new HashSet<Class<?>>();
		}
		return mappedClasses;
	}

	public void setMappedClasses(Set<Class<?>> mappedClasses) {
		this.mappedClasses = mappedClasses;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CentaurusConfig [configFileName=");
		builder.append(configFileName);
		builder.append(", classMappingList=");
		builder.append(classMappingList);
		builder.append(", packageMappingList=");
		builder.append(packageMappingList);
		builder.append(", dbType=");
		builder.append(dbType);
		builder.append(", dbHost=");
		builder.append(dbHost);
		builder.append(", dbName=");
		builder.append(dbName);
		builder.append(", dbPort=");
		builder.append(dbPort);
		builder.append(", dbUsername=");
		builder.append(dbUsername);
		builder.append(", dbPassword=");
		builder.append(dbPassword);
		builder.append(", mappedClasses=");
		builder.append(mappedClasses);
		builder.append("]");
		return builder.toString();
	}

}
