package com.btourman.check.build.cpd;

import java.io.IOException;

import net.sourceforge.pmd.cpd.CPD;

import com.btourman.check.build.ILauncher;
import com.btourman.check.build.ModuleNameConst;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CPDLauncher implements ILauncher {

	@Override
	public int launch(String basedir, JsonNode confNode) {

		try {
			CPDConf conf = confNode != null ? new ObjectMapper().readValue(confNode.toString(), CPDConf.class) : new CPDConf();
			CPD.main(new String[] { "--files", basedir, "--minimum-tokens", String.valueOf(conf.getMinimumToken()), "--format", conf.getFormat() });
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public String getName() {
		return ModuleNameConst.CPD;
	}

}
