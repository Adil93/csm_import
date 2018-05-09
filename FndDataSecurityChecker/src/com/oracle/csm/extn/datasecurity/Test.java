package com.oracle.csm.extn.datasecurity;

import java.util.Arrays;
import java.util.List;

public class Test {

	private static List<String> modules = Arrays.asList("ACO", "AFS", "AHE", "AHT", "ATC", "CSH", "CSO", "EMC", "EMK",
			"EMV", "EWM", "FOD", "FATP", "HZ", "MCT", "MKL", "LOY", "MKS", "MKT", "MOO", "MOP", "MOT", "MOW", "OKC",
			"OSS", "QOC", "QSC", "SVC", "ZBS", "ZCA", "ZCC", "ZCH", "ZCM", "ZCP", "ZCQ", "ZCX", "ZMC", "ZMM", "ZMS",
			"ZOC", "ZON", "ZPM", "ZPS", "ZQP", "ZSF", "ZSO", "ZSO", "ZVC", "TMP","FUN");
	
	private static boolean containsPattern(String name) {
		// TODO Auto-generated method stub
		Boolean found = false;
		
		for(String module : modules)
		{
			if(name.contains(module)) {
				found =true;
				break;
			}
		}
		
		return found;
	}
	
	public static void main(String[] args) {
		
		String name= "/datasecurity/CN/fndObjects.xml";
		String name1= "/datasecurity/MKL/fndObjects.xml";
		
		System.out.println(containsPattern(name));
		System.out.println(containsPattern(name1));
	}
}
