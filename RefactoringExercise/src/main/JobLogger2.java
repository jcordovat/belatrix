package main;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import bean.DataBaseParam;
import enums.TypeOfLog;
import enums.TypeOfMessage;
import exception.DataBaseException;
import exception.FileHandlerException;
import exception.FileLogException;
import exception.LogMessageException;

/**
 * Esta clase fue creada a partir JobLogger fue refactorizada 
 * para que el funcionamiento de los mensaje de aplicacion 
 * sean mas efectivos 
 * @author jorgecordova
 *
 */
public class JobLogger2 {
	
	
	private static TypeOfLog typeOfLog;
	private boolean initialized;
	private static DataBaseParam dbParams;
	private static Logger logger;
	
	
	/**
	 * Metodo constructor 1,para el caso de dbparams
	 * @param logParam
	 * @param dbParamsMap
	 */
	public JobLogger2(TypeOfLog logParam, DataBaseParam dbParamsMap) {
				
				logger = Logger.getLogger("MyLog");
				typeOfLog = logParam;
				dbParams = dbParamsMap;
	}
	
	/**
	 * Metodo constructor 2, sin acceso a datos 
	 * @param logParam
	 */
	public JobLogger2(TypeOfLog logParam){
			logger = Logger.getLogger("MyLog");
			typeOfLog = logParam;
		
	}
	
	
	/**
	 * Metodo que retorna la conexion de BD
	 * @return
	 * @throws DataBaseException
	 */
	public static Connection getConnection() throws DataBaseException{
		
		if (dbParams == null){
			throw new DataBaseException("DataBase params are null");
		}
		
		if (dbParams.getUserName() == null || dbParams.getUserName() == "" ){
			throw new DataBaseException("DataBase param username is null");
		}
		
		if (dbParams.getPassword() == null || dbParams.getPassword() == ""){
			throw new DataBaseException("DataBase param password is null");
		}
		
		if (dbParams.getDbms() == null || dbParams.getDbms() == ""){
			throw new DataBaseException("DataBase param dbms is null");
		}
		
		if (dbParams.getServerName() == null || dbParams.getServerName() == ""){
			throw new DataBaseException("DataBase param servername is null");
		}
		
		if (dbParams.getPortNumber() == null || dbParams.getPortNumber() == ""){
			throw new DataBaseException("DataBase param portnumber is null");
		}
		
		
		Connection connection = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", dbParams.getUserName());
		connectionProps.put("password", dbParams.getPassword());
		try {
			connection = DriverManager.getConnection("jdbc:" + dbParams.getDbms() + "://" +
			dbParams.getServerName() + ":" + dbParams.getPortNumber() + "/", connectionProps);
		} catch (SQLException e) {
			throw new DataBaseException(e.getMessage());
		}
		
		return connection;
		
		
	}
	/**
	 * Metodo que valida si esta creado el file del log
	 * @throws FileLogException
	 */
	public static void validateFileLog() throws FileLogException{
		
		if (dbParams.getLogFileFolder() == null || dbParams.getLogFileFolder() == ""){
			throw new FileLogException("File folder is null");
		}
		
		File logFile = new File(dbParams.getLogFileFolder() + "/logFile.txt");
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				throw new FileLogException(e.getMessage());
			}
		}

		
	}
	/**
	 * Metodo que retorna el fileHander, para el caso de Files
	 * @return
	 * @throws FileHandlerException
	 */
	
	public static FileHandler getFileHandler() throws FileHandlerException{
		
		if (dbParams.getLogFileFolder() == null || dbParams.getLogFileFolder() == ""){
			throw new FileHandlerException("File folder is null");
		}
		
		FileHandler fh;
		try {
			fh = new FileHandler(dbParams.getLogFileFolder() + "/logFile.txt");
		} catch (SecurityException se) {
			throw new FileHandlerException(se.getMessage());
		} catch (IOException io) {
			throw new FileHandlerException(io.getMessage());
		}
		
		return fh;
		
		
	}

	/**
	 * 
	 * Metodo que realiza la validacion de los mensajes
	 * a mostrar y permite almacernarlos de acuerdo
	 * a ciertos condiciones y el tipo de mensaje a mostrar.
	 * 
	 * @param messageText
	 * @param typeOfMessage
	 * @throws LogMessageException
	 * @throws DataBaseException
	 * @throws FileHandlerException
	 * @throws SQLException
	 */
	public static void LogMessage(String messageText, TypeOfMessage typeOfMessage) throws LogMessageException, DataBaseException, FileHandlerException, SQLException {
		
		if (messageText == null || messageText.length() == 0) {
			return;
		}
		
		messageText.trim();
		
		if (typeOfLog == null){
			throw new LogMessageException("Invalid configuration");
		}
		
		if (typeOfMessage == null){
			
			throw new LogMessageException("Error or Warning or Message must be specified");
		}
		
		StringBuffer msj = new StringBuffer();
		int t = 0;
		
		switch (typeOfMessage) {
		case MESSAGE:
			t = 1;
			msj.append("message ");
			break;
		case ERROR:
			t = 2;
			msj.append("error ");
			break;
		
		case WARNING:
			t = 3;
			msj.append("warning ");
		default:
			break;
			}
		
		msj.append(DateFormat.getDateInstance(DateFormat.LONG).format(new Date()));
		msj.append(" "+messageText);
		
		switch (typeOfLog){
		
		case LOG_TO_FILE:
			FileHandler fh = JobLogger2.getFileHandler();
			logger.addHandler(fh);
			logger.log(Level.INFO, msj.toString());
			break;
		case LOG_TO_CONSOLE:
			ConsoleHandler ch = new ConsoleHandler();
			logger.addHandler(ch);
			logger.log(Level.INFO, msj.toString());
			break;
		case LOG_TO_DATABASE:
			Connection conn = JobLogger2.getConnection();
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("insert into Log_Values('" + typeOfMessage.MESSAGE + "', " + String.valueOf(t) +
				")");
			break;
		default:
			break;
			
			
		}
		
			
		}
	

}
