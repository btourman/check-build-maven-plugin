package com.btourman.check.build.cpd;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.sourceforge.pmd.cpd.CPD;

import com.btourman.check.build.ILauncher;
import com.btourman.check.build.ModuleNameConst;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CPDLauncher implements ILauncher {

    @Override
    public int launch(String basedir, JsonNode confNode) {

        try {
            final CPDConf conf = confNode != null ? new ObjectMapper().readValue(confNode.toString(), CPDConf.class) : new CPDConf();
            final Path outputFile = Paths.get("checkbuild/cpd." + conf.getExtensionFile());
            System.setOut(new PrintStream(System.out) {
                @Override
                public void println(String x) {
                    try {
                        Files.write(outputFile, x.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            System.setProperty("net.sourceforge.pmd.cli.noExit", "true");
            CPD.main(new String[]{"--files", basedir, "--minimum-tokens", String.valueOf(conf.getMinimumToken()),
                    "--format", conf.getFormat()});
            return Files.exists(outputFile) ? 1 : 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }

    @Override
    public String getName() {
        return ModuleNameConst.CPD;
    }

}
