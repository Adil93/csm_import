package com.oracle.csm.extn.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.JarInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class XMLUtil {
	
	public  static Document parseXML(InputStream stream)
			throws RuntimeException
		{
			Document result = null;
			try {
				DocumentBuilder builder = getDocumentBuilder();
				InputSource source = new InputSource(stream);
				result = builder.parse(source);
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			return result;
		}
	
	private static DocumentBuilder getDocumentBuilder() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				//DocumentBuilderFactory.newInstance("oracle.xml.jaxp.JXDocumentBuilderFactory",null);
			factory.setNamespaceAware(true);
			return factory.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace();;
			throw new RuntimeException(e);
		}
	}
	
}
