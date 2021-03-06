package com.btourman.check.build;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.ServiceLoader;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.versions.DisplayDependencyUpdatesMojo;

import com.btourman.check.build.utils.FolderDeleteNIO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Goal which touches a timestamp file.
 *
 * @goal start
 * @phase generate-sources
 */
public class App extends AbstractMojo {

    /**
     * @parameter property="project"
     */
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException {
        String basedirPath    = project.getBasedir().getPath();
        Path   checkbuildPath = Paths.get(basedirPath, ".checkbuild");

        Path checkbuildDir = Paths.get(basedirPath, "checkbuild");
        if (Files.exists(checkbuildDir)) {
            try {
                FolderDeleteNIO.delete(checkbuildDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Files.createDirectory(checkbuildDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<ILauncher> loader = ServiceLoader.load(ILauncher.class).iterator();
        if (Files.exists(checkbuildPath)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode root = mapper.readTree(checkbuildPath.toFile());
                JsonNode checkbuild = root.get(ModuleNameConst.CHECKBUILD);
                CheckbuildConf conf = checkbuild != null ? mapper.readValue(checkbuild.toString(), CheckbuildConf.class) : new CheckbuildConf();

                int exit = 0;
                while (loader.hasNext() && (conf.isContinueOnError() || (!conf.isContinueOnError() && exit == 0))) {
                    ILauncher launcher = loader.next();
                    if (conf.isEnable(launcher.getName())) {
                        int status = launcher.launch(basedirPath, root.get(launcher.getName()));
                        if (exit != 1) {
                            exit = status;
                        }
                    }
                }

                if (!conf.isKeepFiles()) {
                    FolderDeleteNIO.delete(checkbuildDir);
                }

                if (conf.isAllowFailures())
                    System.exit(0);
                else
                    System.exit(exit);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
