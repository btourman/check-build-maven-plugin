package com.btourman.check.build.cpd;

import java.io.Serializable;

public class CPDConf implements Serializable {

	private static final long	serialVersionUID	= -3877218165406777135L;

	private String				format;
	private int					minimumToken;

	public CPDConf() {
		this.format = "text";
		this.minimumToken = 5;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getMinimumToken() {
		return minimumToken;
	}

	public void setMinimumToken(int minimumToken) {
		this.minimumToken = minimumToken;
	}

	public String getExtensionFile() {
		String extension;
		switch (this.getFormat()) {
		case "xml":
			extension = "xml";
			break;

		case "text":
		default:
			extension = "txt";
			break;
		}

		return extension;
	}

}
