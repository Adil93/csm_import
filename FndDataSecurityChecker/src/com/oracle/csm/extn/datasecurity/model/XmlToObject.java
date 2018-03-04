package com.oracle.csm.extn.datasecurity.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlToObject {

	public static void main(String[] args) {
		
		try {
			JAXBContext jc =JAXBContext.newInstance(SeedData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			
			List<FndGrant> fndGrants = new ArrayList<FndGrant>();
//			fndGrants = (ArrayList<FndGrant>)unmarshaller.unmarshal(new File("/Volumes/DATA/Adil_Work/OVM/Fus152/CSM_Jars/20_12_2017/FndGrantsSD.xml"));
			
//			System.out.println(fndGrants.get(0).getGrantGuid());
			
//			FndGrant obj = (FndGrant) unmarshaller.unmarshal(new File("/Volumes/DATA/Adil_Work/OVM/Fus152/CSM_Jars/20_12_2017/FndGrantsSD.xml"));
			
			SeedData seedData = (SeedData) unmarshaller.unmarshal(new File("/Volumes/DATA/Adil_Work/OVM/Fus152/CSM_Jars/20_12_2017/FndGrantsSD.xml"));
			
			System.out.println("Grant Guid :"+seedData.getFndGrants().get(0).getGrantGuid());
			System.out.println("Grant Guid :"+seedData.getFndGrants().get(1).getGrantGuid());
			
			SeedData seedData_object = (SeedData) unmarshaller.unmarshal(new File("/Volumes/DATA/Adil_Work/OVM/Fus152/CSM_Jars/20_12_2017/FndObjectsSD.xml"));
			
			System.out.println("Object Name: " + seedData_object.getFndObjects().get(0).getObjName());
			System.out.println("Instanceset Name1: "
					+ seedData_object.getFndObjects().get(0).getFndObjectInstanceSets().get(0).getInstanceSetName());
			
			SeedData seedData_menu = (SeedData) unmarshaller.unmarshal(new File("/Volumes/DATA/Adil_Work/OVM/Fus152/CSM_Jars/20_12_2017/FndMenusSD.xml"));
			
			System.out.println("Menu Name: "+seedData_menu.getFndMenus().get(0).getMenuName());
			System.out.println("Menu Name: "+seedData_menu.getFndMenus().get(1).getMenuName());
			
			SeedData seedData_function = (SeedData) unmarshaller.unmarshal(new File("/Volumes/DATA/Adil_Work/OVM/Fus152/CSM_Jars/20_12_2017/FndFormFunctionsSD.xml"));
			
			
			System.out.println("Function Name: "+seedData_function.getFndFormFunctions().get(0).getFunctionName());
			System.out.println("Funciton Name: "+seedData_function.getFndFormFunctions().get(1).getFunctionName());
			
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
