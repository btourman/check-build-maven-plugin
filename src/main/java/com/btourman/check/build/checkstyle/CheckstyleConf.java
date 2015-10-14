package com.btourman.check.build.checkstyle;

import java.io.Serializable;

public class CheckstyleConf implements Serializable {

	private static final long	serialVersionUID	= -4660289070675170443L;

	private String				format;
	private String				configurationFile;

	public CheckstyleConf() {
		this.format = "plain";
		this.configurationFile = "/sun_checks.xml";
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getConfigurationFile() {
		return configurationFile;
	}

	public void setConfigurationFile(String configurationFile) {
		this.configurationFile = configurationFile;
	}

	public String getExtensionFile() {
		String extension;
		switch (this.getFormat()) {
		case "xml":
			extension = "xml";
			break;

		case "plain":
		default:
			extension = "txt";
			break;
		}

		return extension;
	}

}
