package com.oracle.csm.extn.datasecurity.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class DSLoggerUtil {

	private static Logger logger;

	private static enum LogLevel {
		Trace(Level.FINER), Debug(Level.FINE), Info(Level.INFO), Warn(Level.WARNING), Error(Level.SEVERE);

		private LogLevel(Level level) {
			m_level = level;
		}

		public Level getLevel() {
			return m_level;
		}

		private final Level m_level;

	}

	// Returns the logger object.
	public static Logger getLogger() {
		if (null == logger) {
			logger = Logger.getLogger("oracle.csm.extn.datasecurity");
			setFileHandlerAndLogLevel();
		}
		return logger;
	}

	/**
	 * Creates a new File Handler and set the loglevel from the config file.
	 */
	private static void setFileHandlerAndLogLevel() {
		// FileHandler handler = null;
		ConsoleHandler consoleHandler = null;
		try {
			// Date date = new Date();
			// String logDirPath = getLogDirPath();
			// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			// handler = new FileHandler(logDirPath + "/log" + dateFormat.format(date) + "_"
			// + "%u%g.txt", 10485760, 10,true);
			consoleHandler = new ConsoleHandler();
			Level level = getLogLevel();

			consoleHandler.setLevel(level);
			consoleHandler.setFormatter(new SimpleFormatter());

			// handler.setLevel(level);
			// handler.setFormatter(new SimpleFormatter());

			logger.addHandler(consoleHandler);
			logger.setLevel(level);
			logger.setUseParentHandlers(false);
		} catch (SecurityException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

	}

	// Creates the log directory if not present. Returnrs the directory path if
	// present.
	/**
	private static String getLogDirPath() {
		File file = new File("/Volumes/DATA/Adil_Work/WORKSPACES/csm_import/FNDDataSecurityChecker/logs");
		if (!file.exists()) {
			boolean bCreateDir = file.mkdir();
			if (!bCreateDir) {
				throw new IllegalStateException("Unable to create log directory");
			}

		}
		return file.getAbsolutePath();
	}**/

	// Reads the LOGLEVEL parameter from config file and finds corresponding
	// java.util.logging.Level

	private static Level getLogLevel() {
		String loglevel = ConfigurationReaderUtil.getConfigurationProperty("LOGLEVEL"); // "INFO";//
		Level level = Level.ALL;
		for (LogLevel loglevel1 : LogLevel.values()) {

			if (loglevel1.toString().equalsIgnoreCase(loglevel)) {
				level = loglevel1.getLevel();
				break;
			}
		}
		return level;
	}

}
