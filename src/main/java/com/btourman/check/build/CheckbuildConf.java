package com.btourman.check.build;

import java.io.Serializable;

public class CheckbuildConf implements Serializable {

	private static final long	serialVersionUID	= 7768952155062501484L;

	private String[]			enable;
	private boolean				continueOnError;
	private boolean				allowFailures;

	public CheckbuildConf() {
		this.continueOnError = true;
		this.allowFailures = false;
	}

	public String[] getEnable() {
		return enable;
	}

	public void setEnable(String[] enable) {
		this.enable = enable;
	}

	public boolean isContinueOnError() {
		return continueOnError;
	}

	public void setContinueOnError(boolean continueOnError) {
		this.continueOnError = continueOnError;
	}

	public boolean isAllowFailures() {
		return allowFailures;
	}

	public void setAllowFailures(boolean allowFailures) {
		this.allowFailures = allowFailures;
	}

	public boolean isEnable(String module) {
		boolean isEnable = false;

		for (String value : enable) {
			if (value.equals(module)) {
				isEnable = true;
				break;
			}
		}

		return isEnable;
	}

}
