package com.oracle.csm.extn.datasecurity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

//import com.oracle.csm.extn.datasecurity.domain.FndObject;
import com.oracle.csm.extn.datasecurity.model.DataSecurityObjects;
import com.oracle.csm.extn.datasecurity.source.CSMJarReader;
import com.oracle.csm.extn.datasecurity.source.DataSecurityProccessor;
import com.oracle.csm.extn.datasecurity.target.DSObjectsTargetProcessor;
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

	private static Logger logger = DSLoggerUtil.getLogger();

	private static Map<String, Map<DataSecurityObjects, List<Object>>> ootbSourceObjectMap;
	private static Map<String, Map<DataSecurityObjects, List<Object>>> ootbSourceObjectMap_test = new HashMap<>();
	private static Map<String, Map<DataSecurityObjects, List<Object>>> customObjectSourceMap;
	private static Map<String, Map<DataSecurityObjects, List<Object>>> ootbTargetObjectMap;

	/**
	 * @param args
	 * @throws JAXBException
	 */
	public static void main(String[] args) throws JAXBException {
		try {
			long start = System.currentTimeMillis();

			String testObjName = "ZSF_FCST_ITEM_DETAIL"; // "SVC_SERVICE_REQUESTS"
			String testObjName1 = "ZCA_REF_ENTITIES";
			String testObjName2 = "MOT_REF_ENTITIES";
			String testObjName3 = "ZBS_REFERENCE_PROFILES_XM";

			// Testing purpose

			ootbSourceObjectMap_test.put(testObjName, null);
			ootbSourceObjectMap_test.put(testObjName1, null);
			ootbSourceObjectMap_test.put(testObjName2, null);
			ootbSourceObjectMap_test.put(testObjName3, null);

			String csmJarFilePath = "/Volumes/DATA/Adil_Work/OVM/Fus152/CSM_Jars/14_03_2018/CS_STRCSM1SRCGSIR121_R13.17.11_PB14_PRE_UPG_BI_ENABLED_2018_0208_0826PST_957591183009343.jar";

			logger.log(Level.INFO, "Reading the CSM jar file");

			// Reading and processing the datasecurity XML files
			CSMJarReader.read(new URL("jar:file:/" + csmJarFilePath + "!/"));

			logger.log(Level.INFO, "Proccessing Objects and its related dependent seed datas");
			DataSecurityProccessor.proccessMaps();

			logger.log(Level.INFO, "Total time taken for reading / proccessing the entire CSM : "
					+ (System.currentTimeMillis() - start) / 1000 + " seconds");

			// Contains Object name as key and all the depended related objects as the value
			ootbSourceObjectMap = DataSecurityProccessor.getOotbObjectMap();
			customObjectSourceMap = DataSecurityProccessor.getCustomObjectMap();

			logger.log(Level.INFO, "Source Custom object Size : " + customObjectSourceMap.keySet().size());
			logger.log(Level.INFO, "Source OOTB object Size : " + ootbSourceObjectMap.keySet().size());

			if (DataSecurityProccessor.validateSourceData(customObjectSourceMap)) {
				logger.log(Level.INFO, "Source CSM data is Valid");
			} else {
				logger.log(Level.INFO, "Source CSM data is Not Valid..!! Please check");
				// need to throw an exception
			}

			ootbTargetObjectMap = DSObjectsTargetProcessor.extractFndObjects(ootbSourceObjectMap);

			logger.log(Level.INFO, "Target OOTB object Size : " + ootbTargetObjectMap.keySet().size());

			// Compare Source and target data and create insert queries to rectify target
			TargetValidator.validate(ootbSourceObjectMap, ootbTargetObjectMap);

			// number of grants in ZCA_EXP_OBJECTS object
			int num = ootbSourceObjectMap.get(testObjName).get(DataSecurityObjects.GRANTS).size();
			logger.log(Level.INFO, "Number of grants for " + testObjName + ": " + num);

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

}
