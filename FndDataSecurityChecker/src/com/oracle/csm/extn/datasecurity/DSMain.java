package com.oracle.csm.extn.datasecurity;

import java.io.File;
import java.io.FileNotFoundException;
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
import com.oracle.csm.extn.datasecurity.model.FndFormFunction;
import com.oracle.csm.extn.datasecurity.source.CSMJarReader;
import com.oracle.csm.extn.datasecurity.source.DataSecurityProccessor;
import com.oracle.csm.extn.datasecurity.source.SourceCsmValidator;
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
		TargetValidator.truncateExistingFiles();
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

			// Testing purpose
			String testObjName = "ZMM_NOTES";
			ootbSourceObjectMap_test.put(testObjName, null);

			String csmJarFilePath = args[0];
			// "/Volumes/DATA/Adil_Work/OVM/Fus73/csm_jars/vamsi/CS_GSE_1802GA_04082018_144638633755336.jar";
			if (csmJarFilePath == null || "".equals(csmJarFilePath) || !new File(csmJarFilePath).exists()) {
				logger.log(Level.INFO, "Provide a valid CSM path");
				throw new FileNotFoundException("CSM Jar path is not valid");
			}
			logger.log(Level.INFO, "Reading the CSM jar file");

			// Reading and processing the data security XML files
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

			// if (DataSecurityProccessor.validateSourceData(customObjectSourceMap)) {
			if (SourceCsmValidator.validateSourceData(customObjectSourceMap)) {
				logger.log(Level.INFO, "Source CSM data is Valid");
			} else {
				logger.log(Level.INFO, "Source CSM data is Not Valid..!! Please check");

			}

			ootbTargetObjectMap = DSObjectsTargetProcessor.extractFndObjects(ootbSourceObjectMap);

			// testing
			for (Object obj : ootbSourceObjectMap.get(testObjName).get(DataSecurityObjects.FORM_FUNCIONS)) {
				FndFormFunction fndFormFunction = (FndFormFunction) obj;
				if (fndFormFunction.getFunctionName().equals("OKC_MANAGE_CONTRACT_DATA")) {
					System.out.println(fndFormFunction.getFunctionName() + "\n");
				}
			}

			logger.log(Level.INFO, "Target OOTB object Size : " + ootbTargetObjectMap.keySet().size());

			// Compare Source and target data and create insert queries to rectify target
			TargetValidator.validate(ootbSourceObjectMap, ootbTargetObjectMap);

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
