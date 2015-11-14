package com.btourman.check.build.checkstyle;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.*;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import org.apache.commons.cli.*;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class MainCheckstyle {

    private static final Options OPTS = new Options();

    private MainCheckstyle() {
    }

    public static void main(String[] args) {
        PosixParser clp = new PosixParser();
        CommandLine line = null;

        try {
            line = clp.parse(OPTS, args);
        } catch (ParseException var12) {
            usage();
        }

        assert line != null;

        if (line.hasOption("v")) {
            System.out.println("Checkstyle version: " + Main.class.getPackage().getImplementationVersion());
            System.exit(0);
        }

        Properties props = line.hasOption("p") ? loadProperties(new File(line.getOptionValue("p"))) : System.getProperties();
        if (!line.hasOption("c")) {
            System.out.println("Must specify a config XML file.");
            usage();
        }

        Configuration config = loadConfig(line, props);
        Object out = null;
        boolean closeOut = false;
        if (line.hasOption("o")) {
            String listener = line.getOptionValue("o");

            try {
                out = new FileOutputStream(listener);
                closeOut = true;
            } catch (FileNotFoundException var11) {
                System.out.println("Could not find file: \'" + listener + "\'");
                System.exit(1);
            }
        } else {
            out = System.out;
            closeOut = false;
        }

        AuditListener listener1 = createListener(line, (OutputStream) out, closeOut);
        List files = getFilesToProcess(line);
        Checker c = createChecker(config, listener1);
        int numErrs = c.process(files);
        c.destroy();
    }

    private static Checker createChecker(Configuration config, AuditListener nosy) {
        Checker c = null;

        try {
            c = new Checker();
            ClassLoader e = Checker.class.getClassLoader();
            c.setModuleClassLoader(e);
            c.configure(config);
            c.addListener(nosy);
        } catch (Exception var4) {
            System.out.println("Unable to create Checker: " + var4.getMessage());
            System.exit(1);
        }

        return c;
    }

    private static List<File> getFilesToProcess(CommandLine line) {
        LinkedList files = Lists.newLinkedList();
        String[] remainingArgs = line.getArgs();
        String[] arr$ = remainingArgs;
        int len$ = remainingArgs.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            String element = arr$[i$];
            traverse(new File(element), files);
        }

        if (files.isEmpty()) {
            System.out.println("Must specify files to process");
            usage();
        }

        return files;
    }

    private static AuditListener createListener(CommandLine line, OutputStream out, boolean closeOut) {
        String format = line.hasOption("f") ? line.getOptionValue("f") : "plain";
        Object listener = null;
        if ("xml".equals(format)) {
            listener = new XMLLogger(out, closeOut);
        } else if ("plain".equals(format)) {
            listener = new DefaultLogger(out, closeOut);
        } else {
            System.out.println("Invalid format: (" + format + "). Must be \'plain\' or \'xml\'.");
            usage();
        }

        return (AuditListener) listener;
    }

    private static Configuration loadConfig(CommandLine line, Properties props) {
        try {
            return ConfigurationLoader.loadConfiguration(line.getOptionValue("c"), new PropertiesExpander(props));
        } catch (CheckstyleException var3) {
            System.out.println("Error loading configuration file");
            var3.printStackTrace(System.out);
            System.exit(1);
            return null;
        }
    }

    private static void usage() {
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("java " + Main.class.getName() + " [options] -c <config.xml> file...", OPTS);
        System.exit(1);
    }

    private static void traverse(File node, List<File> files) {
        if (node.canRead()) {
            if (node.isDirectory()) {
                File[] nodes = node.listFiles();
                File[] arr$ = nodes;
                int len$ = nodes.length;

                for (int i$ = 0; i$ < len$; ++i$) {
                    File element = arr$[i$];
                    traverse(element, files);
                }
            } else if (node.isFile()) {
                files.add(node);
            }
        }

    }

    private static Properties loadProperties(File file) {
        Properties properties = new Properties();

        try {
            FileInputStream ex = new FileInputStream(file);
            Throwable var3 = null;

            try {
                properties.load(ex);
            } catch (Throwable var13) {
                var3 = var13;
                throw var13;
            } finally {
                if (ex != null) {
                    if (var3 != null) {
                        try {
                            ex.close();
                        } catch (Throwable var12) {
                            var3.addSuppressed(var12);
                        }
                    } else {
                        ex.close();
                    }
                }

            }
        } catch (IOException var15) {
            System.out.println("Unable to load properties from file: " + file.getAbsolutePath());
            var15.printStackTrace(System.out);
            System.exit(1);
        }

        return properties;
    }

    static {
        OPTS.addOption("c", true, "The check configuration file to use.");
        OPTS.addOption("o", true, "Sets the output file. Defaults to stdout");
        OPTS.addOption("p", true, "Loads the properties file");
        OPTS.addOption("f", true, "Sets the output format. (plain|xml). Defaults to plain");
        OPTS.addOption("v", false, "Print product version and exit");
    }
}
