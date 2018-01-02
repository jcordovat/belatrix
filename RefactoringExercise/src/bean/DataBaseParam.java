package bean;

import java.io.Serializable;


/**
 * Bean de conexion a db
 * @author jorgecordova
 *
 */
public class DataBaseParam implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private String password;
	private String dbms;
	private String serverName;
	private String portNumber;
	private String logFileFolder;
	
	
	public DataBaseParam(String userName, String password, String dbms, String serverName, String portNumber,String logFileFolder) {
		this.userName = userName;
		this.password = password;
		this.dbms = dbms;
		this.serverName = serverName;
		this.portNumber = portNumber;
		this.logFileFolder = logFileFolder;
	}
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDbms() {
		return dbms;
	}
	public void setDbms(String dbms) {
		this.dbms = dbms;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}


	public String getLogFileFolder() {
		return logFileFolder;
	}


	public void setLogFileFolder(String logFileFolder) {
		this.logFileFolder = logFileFolder;
	}
	
	
	
	
	
	

}
