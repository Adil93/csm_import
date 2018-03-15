package com.oracle.csm.extn.datasecurity.source;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

import javax.xml.bind.JAXBException;

import com.oracle.csm.extn.datasecurity.utils.DSLoggerUtil;

/**
 * @author dvijayan
 *
 */

public class CSMJarReader {

	private static Logger logger = DSLoggerUtil.getLogger();
	private static List<String> modules = Arrays.asList("ACO", "AFS", "AHE", "AHT", "ATC", "CSH", "CSO", "EMC", "EMK",
			"EMV", "EWM", "FOD", "FATP", "HZ", "MCT", "MKL", "LOY", "MKS", "MKT", "MOO", "MOP", "MOT", "MOW", "OKC",
			"OSS", "QOC", "QSC", "SVC", "ZBS", "ZCA", "ZCC", "ZCH", "ZCM", "ZCP", "ZCQ", "ZCX", "ZMC", "ZMM", "ZMS",
			"ZOC", "ZON", "ZPM", "ZPS", "ZQP", "ZSF", "ZSO", "ZSO", "ZVC", "TMP");

	private static byte[] copyStream(InputStream in, ZipEntry entry) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		long size = entry.getSize();
		if (size > -1) {
			byte[] buffer = new byte[1024 * 4];
			int n = 0;
			long count = 0;
			while (-1 != (n = in.read(buffer)) && count < size) {
				baos.write(buffer, 0, n);
				count += n;
			}
		} else {
			while (true) {
				int b = in.read();
				if (b == -1) {
					break;
				}
				baos.write(b);
			}
		}
		baos.close();
		return baos.toByteArray();
	}

	private static void readStream(InputStream inputStream, String name) throws IOException, JAXBException {
		if (inputStream instanceof JarInputStream) {
			JarEntry nextJarEntry = ((JarInputStream) inputStream).getNextJarEntry();
			while (nextJarEntry != null) {
				name = nextJarEntry.getName();
				logger.log(Level.FINE, "Reading jar entry " + name);

				if (name.endsWith(".jar") && name.contains("CS_ADF_") && !name.contains("CS_ADF_MDS_")
						&& !name.contains("CS_ADF_JEDI_") && !name.contains("CS_ADF_EXT_")) {
					byte[] byteArray = copyStream(inputStream, nextJarEntry);
					logger.log(Level.FINE, "Reading jar entry " + name);
					readStream(new JarInputStream(new ByteArrayInputStream(byteArray)), name);
				} else if (name.contains("datasecurity") && name.endsWith(".xml")
						&& modules.parallelStream().anyMatch(name::contains)) {

					logger.log(Level.FINE, "Reading file " + name);
					byte[] byteArray = copyStream(inputStream, nextJarEntry);

					if (byteArray != null) {
						DataSecurityProccessor.proccessXml(new ByteArrayInputStream(byteArray), name);
					}

				}
				nextJarEntry = ((JarInputStream) inputStream).getNextJarEntry();
			}
		}

	}

	public static void read(URL url) throws IOException, JAXBException {

		if (url != null) {
			if (url.toString().endsWith(".jar!/")) {
				String jarPath = url.getPath().substring(5);
				String[] paths = jarPath.split("!");
				logger.log(Level.INFO, "CSM Jar Location: " + paths[0]);
				InputStream jarFileInputStream = new FileInputStream(paths[0]);
				readStream(new JarInputStream(jarFileInputStream), url.toString());
			}

		}
	}

}