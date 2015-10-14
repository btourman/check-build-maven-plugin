package com.btourman.check.build.checkstyle;

import java.io.IOException;

import com.btourman.check.build.ILauncher;
import com.btourman.check.build.ModuleNameConst;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puppycrawl.tools.checkstyle.Main;

public class CheckstyleLauncher implements ILauncher {

	@Override
	public int launch(String basedir, JsonNode confNode) {
		try {
			CheckstyleConf conf = confNode != null ? new ObjectMapper().readValue(confNode.toString(), CheckstyleConf.class) : new CheckstyleConf();
			Main.main(new String[] { "-f", conf.getFormat(), basedir, "-c", conf.getConfigurationFile(), "-o",
					"checkbuild/checkstyle." + conf.getExtensionFile() });
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public String getName() {
		return ModuleNameConst.CHECKSTYLE;
	}

}
