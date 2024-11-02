package required.classes;

import org.apache.commons.cli.*;

public class ParsingArgs {
    
    public static CommandLine Parsing(String[] args) {

        Options options = new Options();
        OptionGroup operation = new OptionGroup();

        Option compress= new Option("c","compress",false,"operation to proceed (Compression or Decompression)");
        Option decompress= new Option("d","decompress",false,"operation to proceed (Compression or Decompression)");
        operation.addOption(compress);
        operation.addOption(decompress);
        operation.setRequired(true);
        options.addOptionGroup(operation);

        Option type = new Option("t", "type", true, "select the compression type(s) you want from : zip, bzip2, gzip, zstd, tar and xz");
        type.setRequired(false); 
        options.addOption(type);
        Option inputPath = new Option("p","path", true, "input file path");
        inputPath.setRequired(true);
        inputPath.setArgs(1);
        options.addOption(inputPath);

        Option outputPath = new Option("o", "output", true, "output file path");
        outputPath.setRequired(false);
        outputPath.setArgs(1);
        options.addOption(outputPath);;

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("InsideOut", options);
            System.exit(1);
        }

        return cmd;
        
    }
}
