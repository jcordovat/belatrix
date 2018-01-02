package test;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import enums.TypeOfLog;
import enums.TypeOfMessage;
import exception.DataBaseException;
import exception.FileHandlerException;
import exception.LogMessageException;
import main.JobLogger2;

/**
 * Metodo que realiza las pruebas unitarias
 * @author jorgecordova
 *
 */
 
public class TestJobLogger {
	
	private TypeOfLog logParam;
	private JobLogger2 jobLogger2;
	private TypeOfMessage typeOfMessage;
	
	
	@Before
	public void setUp(){
		
		logParam = TypeOfLog.LOG_TO_CONSOLE;
		jobLogger2 = new JobLogger2(logParam);
	}

	@Test
	public void test() throws LogMessageException, DataBaseException, FileHandlerException, SQLException {
		String msj = "Esto es una prueba";
		typeOfMessage = TypeOfMessage.MESSAGE;
		jobLogger2.LogMessage(msj, typeOfMessage);

	}

}
