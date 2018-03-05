package com.oracle.csm.extn.datasecurity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import com.oracle.csm.extn.datasecurity.model.DataSecurityObjects;
import com.oracle.csm.extn.datasecurity.source.CSMJarReader;
import com.oracle.csm.extn.datasecurity.source.DSObjectsProcessor;
import com.oracle.csm.extn.datasecurity.utils.ConfigurationReaderUtil;
import com.oracle.csm.extn.datasecurity.utils.DSLoggerUtil;

/**
 * 
 * @author adilmuthukoya
 *
 */
public class DSMain {

	static {
		ConfigurationReaderUtil.loadConfigurations();
	}

	private static Map<String, Map<DataSecurityObjects, List<Object>>> ootbObjectMap;
	private static Map<String, Map<DataSecurityObjects, List<Object>>> customObjectMap;
	private static Logger logger = DSLoggerUtil.getLogger();

	/**
	 * @param args
	 * @throws JAXBException
	 */
	public static void main(String[] args) throws JAXBException {
		try {
			long start = System.currentTimeMillis();

			// Read all the source object
			String csmJarFilePath = "/Volumes/DATA/Adil_Work/OVM/Fus152/CSM_Jars/CS_NEW_DBS_CSM_18082017_V2.jar";
			logger.log(Level.INFO, "Reading the CSM jar file");

			// Reading and processing the datasecurity XML files
			CSMJarReader.read(new URL("jar:file:/" + csmJarFilePath + "!/"));

			// Contains Object name as key and all the depended related objects as the value
			logger.log(Level.INFO, "Proccessing Objects and its related dependent seed datas");
			DSObjectsProcessor.proccessMaps();

			ootbObjectMap = DSObjectsProcessor.getOotbObjectMap();
			customObjectMap = DSObjectsProcessor.getCustomObjectMap();

			if (DSObjectsProcessor.validateSourceData(customObjectMap)) {
				logger.log(Level.INFO, "Source CSM data is Valid");
			} else {
				logger.log(Level.INFO, "Source CSM data is Not Valid..!! Please check");
			}

			logger.log(Level.INFO,
					"Total time taken for execution " + (System.currentTimeMillis() - start) / 1000 + " seconds");

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Read all the target objects

	// Read all the source grants

	// Read all the target grants

	// Read all the
}
