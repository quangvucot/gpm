package com.vdqgpm.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
	private Properties properties;

	public ConfigReader(String configFileName) {
		properties = new Properties();
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFileName)) {
			if (inputStream != null) {
				properties.load(inputStream);
			} else {
				throw new RuntimeException("Config file " + configFileName + " not found");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}
}
