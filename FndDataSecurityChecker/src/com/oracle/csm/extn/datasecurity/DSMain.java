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
import com.oracle.csm.extn.datasecurity.source.DataSecurityProccessor;
import com.oracle.csm.extn.datasecurity.target.TargetValidator;
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

			// /Volumes/DATA/Adil_Work/pruthvi/datasecurity.jar
			String csmJarFilePath = "/Volumes/DATA/Adil_Work/pruthvi/csm_jars/CS_CSM_exportedFromSmc127_1023524188740792.jar";
			// "/Volumes/DATA/Adil_Work/pruthvi/datasecurity.jar"
			
			//"/Volumes/DATA/Adil_Work/OVM/Fus152/CSM_Jars/CS_NEW_DBS_CSM_18082017_V2.jar";
			logger.log(Level.INFO, "Reading the CSM jar file");

			// Reading and processing the datasecurity XML files
			CSMJarReader.read(new URL("jar:file:/" + csmJarFilePath + "!/"));

			// Contains Object name as key and all the depended related objects as the value
			logger.log(Level.INFO, "Proccessing Objects and its related dependent seed datas");
			DataSecurityProccessor.proccessMaps();

			ootbObjectMap = DataSecurityProccessor.getOotbObjectMap();
			customObjectMap = DataSecurityProccessor.getCustomObjectMap();

			if (DataSecurityProccessor.validateSourceData(customObjectMap)) {
				logger.log(Level.INFO, "Source CSM data is Valid");
			} else {
				logger.log(Level.INFO, "Source CSM data is Not Valid..!! Please check");
			}
			
			
//			TargetValidator.validate(ootbObjectMap, targetOootbMap);
			
			
			// number of grants  in ZCA_EXP_OBJECTS object
			int num = ootbObjectMap.get("ZCA_EXP_OBJECTS").get(DataSecurityObjects.GRANTS).size();
			logger.log(Level.INFO, "Number of grants for ZCA_EXP_OBJECTS : "+num + "");

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
