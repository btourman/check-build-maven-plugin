package com.btourman.check.build.pmd;

import java.io.IOException;

import net.sourceforge.pmd.PMD;

import com.btourman.check.build.ILauncher;
import com.btourman.check.build.ModuleNameConst;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PMDLauncher implements ILauncher {

    @Override
    public int launch(String basedir, JsonNode confNode) {

        int status = 1;
        try {
            PMDConf conf = confNode != null ? new ObjectMapper().readValue(confNode.toString(), PMDConf.class) : new PMDConf();
            status = PMD.run(new String[]{"-d", basedir, "-l", "java", "-R", conf.getRulesets(),
                    "-shortnames", "-f", conf.getFormat(),
                    "-min", String.valueOf(conf.getPriority()), "-r", "checkbuild/pmd." + conf.getExtensionFile()});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return status > 0 ? 1 : 0;
    }

    @Override
    public String getName() {
        return ModuleNameConst.PMD;
    }

}
