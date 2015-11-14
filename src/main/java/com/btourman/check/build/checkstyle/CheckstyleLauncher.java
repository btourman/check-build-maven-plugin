package com.btourman.check.build.checkstyle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.btourman.check.build.ILauncher;
import com.btourman.check.build.ModuleNameConst;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puppycrawl.tools.checkstyle.Main;

public class CheckstyleLauncher implements ILauncher {

    @Override
    public int launch(final String basedir, final JsonNode confNode) {
        try {
            CheckstyleConf conf = confNode != null ? new ObjectMapper().readValue(confNode.toString(), CheckstyleConf.class) : new CheckstyleConf();
            final String outputFile = "checkbuild/checkstyle." + conf.getExtensionFile();
            MainCheckstyle.main(new String[]{"-f", conf.getFormat(), basedir, "-c", conf.getConfigurationFile(), "-o",
                    outputFile});
            return Files.exists(Paths.get(outputFile)) ? 1 : 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }

    @Override
    public String getName() {
        return ModuleNameConst.CHECKSTYLE;
    }
}
