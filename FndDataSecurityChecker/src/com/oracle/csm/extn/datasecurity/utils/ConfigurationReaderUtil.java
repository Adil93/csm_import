package com.oracle.csm.extn.datasecurity.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
* @author adilmuthukoya
* Reads configuration from config/config.properties
*
*/
public class ConfigurationReaderUtil {

private static Properties properties = new Properties();
private static String propFileName = "config/config.properties";


//Reads configurations from config.properties file.

public static void loadConfigurations() {

try {
properties.load(getStreamfile(propFileName));

} catch (Exception e) {
throw new IllegalStateException("Could not load configuration properties", e);
}
}


public static String getConfigurationProperty(String key) {
return properties.getProperty(key);
}


public static InputStream getStreamfile(String fileName) throws FileNotFoundException,
   UnsupportedEncodingException {

InputStream inputStream = new FileInputStream(fileName);

return inputStream;
}
}
