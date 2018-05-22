package com.oracle.csm.extn.datasecurity.source;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.oracle.csm.extn.datasecurity.model.DataSecurityObjects;
import com.oracle.csm.extn.datasecurity.model.FndGrant;
import com.oracle.csm.extn.datasecurity.utils.DSLoggerUtil;

public class SourceCsmValidator {
	private static Logger logger = DSLoggerUtil.getLogger();

	public static boolean validateSourceData(
			final Map<String, Map<DataSecurityObjects, List<Object>>> customObjectMap) {
		// Write logics for Source data validations
		logger.log(Level.INFO, "Validating Source CSM data........[Mainly Custom Object validation]");

		// Make two separate threads and run zmmNotesValidation and custom validation in
		// parallel
		// Check whether we need to use Executer service to do it here

		final boolean[] result = new boolean[2];

		Thread zmmThread = new Thread(new Runnable() {
			@Override
			public void run() {
				result[0] = zmmNotesValidation(customObjectMap);

			}
		}, "ZMMNOTES_THREAD");

		Thread customObjectThread = new Thread(new Runnable() {
			@Override
			public void run() {
				result[1] = customObjectValidation(customObjectMap);

			}
		}, "CUSTOM_OBJECTS_THREAD");

		zmmThread.start();
		customObjectThread.start();

		try {
			zmmThread.join();
			customObjectThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// result[0] = zmmNotesValidation();
		// result[1] = customObjectValidation();
		return result[0] && result[1];

	}

	private static boolean customObjectValidation(Map<String, Map<DataSecurityObjects, List<Object>>> customObjectMap) {
		// to be changed
		logger.log(Level.INFO, "Validating CustomObjects instance set and menu dependencies");

		boolean customObjectValid = true;

		Set<String> validGrantNames = new HashSet<String>();
		Set<String> invalidGrantNames = new HashSet<String>();

		List<FndGrant> fndGrants = new ArrayList<>();

		int objCount = customObjectMap.keySet().size();
		logger.log(Level.INFO, "Total number of custom Objects : " + objCount);

		for (Map.Entry<String, Map<DataSecurityObjects, List<Object>>> entry : customObjectMap.entrySet()) {

			int grantSize = entry.getValue().get(DataSecurityObjects.GRANTS).size();

			logger.log(Level.FINE, "Object : " + entry.getKey() + " has " + grantSize + " grants");
			if(entry.getValue().get(DataSecurityObjects.GRANTS).size()>0 && entry.getValue().get(DataSecurityObjects.GRANTS)!=null)
				for (Object obj : entry.getValue().get(DataSecurityObjects.GRANTS)) {
					if(obj!=null)
						fndGrants.add((FndGrant) obj);
				}

			// put a warning here instead of considering as invalid

			for (FndGrant fndGrant : fndGrants) {

				String instanceSetName = fndGrant.getInstanceSetName();
				String menuName = fndGrant.getMenuName();

				if (instanceSetName != null && !instanceSetName.equals("")) {
					if (!(DataSecurityProccessor.getInstanceSetNameMap().containsKey(instanceSetName)
							&& DataSecurityProccessor.getMenuNameMap().containsKey(menuName))) {

						customObjectValid = false;
						// break;
						invalidGrantNames.add(fndGrant.getGrantGuid());
					} else {
						validGrantNames.add(fndGrant.getGrantGuid());
					}
				} else {
					if (!DataSecurityProccessor.getMenuNameMap().containsKey(menuName)) {
						customObjectValid = false;
						// break;
						invalidGrantNames.add(fndGrant.getGrantGuid());
					} else {
						validGrantNames.add(fndGrant.getGrantGuid());
					}
				}
			}

		}
		logger.log(Level.INFO, "Invalid Grant Object size : " + invalidGrantNames.size());
		logger.log(Level.INFO, "valid Grant Object size : " + validGrantNames.size());
		logger.log(Level.INFO,
				"Invalid Grant Object Names [Cutom Object Validation] : " + invalidGrantNames.toString());
		// logger.log(Level.INFO, "Valid Grant Object Names [Custom Object Validation] :
		// " + validGrantNames.toString());

		return customObjectValid;
	}

	private static boolean zmmNotesValidation(Map<String, Map<DataSecurityObjects, List<Object>>> customObjectMap) {

		logger.log(Level.INFO, "Validating ZMM_NOTES dependenciess");
		boolean zmmValid = true;
		List<FndGrant> fndGrants = null;

		if (DataSecurityProccessor.getZmmAppCompNotesGrants() != null) {
			fndGrants = DataSecurityProccessor.getZmmAppCompNotesGrants();
		} else {
			logger.log(Level.INFO, "No ZMM/AppCmmnCompNotes/FndGrantsSD.xml exists");
			return false;
		}

		Set<String> validCustNames = new HashSet<String>();
		Set<String> InValidCustNames = new HashSet<String>();

		for (Map.Entry<String, Map<DataSecurityObjects, List<Object>>> entry : customObjectMap.entrySet()) {
			boolean foundRelatedFndGrant = false;
			boolean foundRelMenuAndIS = false;
			String instanceSetName;
			String menuName;
			String custObjName = entry.getKey();

			for (Object obj : fndGrants) {
				FndGrant fndGrant = (FndGrant) obj;
				if (fndGrant.getName().toLowerCase().contains(custObjName.toLowerCase())) {

					foundRelatedFndGrant = true;

					instanceSetName = fndGrant.getInstanceSetName();
					menuName = fndGrant.getMenuName();

					if (instanceSetName != null && !instanceSetName.equals("")) {
						if (DataSecurityProccessor.getInstanceSetNameMap().containsKey(instanceSetName)
								&& DataSecurityProccessor.getMenuNameMap().containsKey(menuName)) {
							foundRelMenuAndIS = true;
							// validCustNames.add(custObjName);
						}
					} else {
						if (DataSecurityProccessor.getMenuNameMap().containsKey(menuName)) {
							foundRelMenuAndIS = true;
							// validCustNames.add(custObjName);
						}
					}

					break;
				}
			}

			if (!(foundRelatedFndGrant && foundRelMenuAndIS)) {
				// return false;
				InValidCustNames.add(custObjName);
				zmmValid = false;

			} else {
				validCustNames.add(custObjName);
			}

		}
		logger.log(Level.INFO, "Invalid Custom Object size : " + InValidCustNames.size());
		logger.log(Level.INFO, "valid Custom Object size : " + validCustNames.size());

		if (InValidCustNames.size() > 0) {
			logger.log(Level.INFO,
					"!!!!!WARNING!!!!!  CSM has issue with ZMM validation, all the custom objects present in CSM does not ahve valid entry in ZMM_NOTES ");
			logger.log(Level.INFO, "Invalid Custom Object Names [ZMM Validation] : " + InValidCustNames.toString());
		}

		return true;
	}
}
