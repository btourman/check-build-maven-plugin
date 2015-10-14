package com.btourman.check.build;

import com.fasterxml.jackson.databind.JsonNode;

public interface ILauncher {

	public int launch(String basedir, JsonNode confNode);

	public String getName();

}
