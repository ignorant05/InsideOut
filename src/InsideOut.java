
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


import required.classes.*;

import org.apache.commons.cli.CommandLine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class InsideOut {

    private String inputFilePath;
    private String outputFilePath;

    public InsideOut(String inputFilePath, String outputFilePath) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
        
    }

    public File CheckAndModifyFileName(int i,File inputFile){
        
        if (!inputFile.exists()){
            return inputFile;
        }
        else {
            i++;
            String name = inputFile.getName();
            int index = name.lastIndexOf(".");
            String modifiedName;
            String ext = "";

        if (index == -1) {
            modifiedName = name;
        } else {
            modifiedName = name.substring(0, index);
            ext = name.substring(index);
        }
            String newfilename = String.format("%s(%d)%s", modifiedName,i,ext);
            File newfile= new File(inputFile.getParent(),newfilename);
            return CheckAndModifyFileName(i,newfile);
        }
        
        
    }
    
    public void compressToZip(String inputFilePath, String outputFilePath) throws Exception {

        Path inputPath = Paths.get(inputFilePath);
        Path outputPath = Paths.get(outputFilePath);

        byte[] inputFileData = Files.readAllBytes(inputPath); 
        byte[] compressedData = FileCompression.Zip(inputFileData);  

        if (compressedData.length == 0) {
            System.out.println("[-] Compression failed: no data was returned.");
            return;
        }    

        Files.createDirectories(outputPath.getParent());
        Files.write(outputPath, compressedData);
        
    }
    
    public void compressToZstd(String inputFilePath, String outputFilePath) throws Exception {

        Path inputPath = Paths.get(inputFilePath);
        Path outputPath = Paths.get(outputFilePath);

        byte[] inputFileData = Files.readAllBytes(inputPath); 
        byte[] compressedData = FileCompression.Zstd(inputFileData);  

        if (compressedData.length == 0) {
            System.out.println("[-] Compression failed: no data was returned.");
            return;
        }   

        Files.createDirectories(outputPath.getParent());
        Files.write(outputPath, compressedData);
        
    }
    
    public void compressToTar(String inputFilePath, String outputFilePath) throws Exception {
        
        Path inputPath = Paths.get(inputFilePath);
        Path outputPath = Paths.get(outputFilePath);

        byte[] inputFileData = Files.readAllBytes(inputPath); 
        byte[] compressedData = FileCompression.Tar(inputFileData); 
        
        if (compressedData.length == 0) {
            System.out.println("[-] Compression failed: no data was returned.");
            return;
        }  

        Files.createDirectories(outputPath.getParent());
        Files.write(outputPath, compressedData);
         
    }
    
    public void compressToXZ(String inputFilePath, String outputFilePath) throws Exception {
        
        Path inputPath = Paths.get(inputFilePath);
        Path outputPath = Paths.get(outputFilePath);

        byte[] inputFileData = Files.readAllBytes(inputPath); 
        byte[] compressedData = FileCompression.xz(inputFileData);  

        if (compressedData.length == 0) {
            System.out.println("[-] Compression failed: no data was returned.");
            return;
        }   

        Files.createDirectories(outputPath.getParent());
        Files.write(outputPath, compressedData);
        
    }
    
    public void compressToGzip(String inputFilePath, String outputFilePath) throws Exception {
        
        Path inputPath = Paths.get(inputFilePath);
        Path outputPath = Paths.get(outputFilePath);

        byte[] inputFileData = Files.readAllBytes(inputPath); 
        byte[] compressedData = FileCompression.GZIP(inputFileData);  

        if (compressedData.length == 0) {
            System.out.println("[-] Compression failed: no data was returned.");
            return;
        }   

        Files.createDirectories(outputPath.getParent());
        Files.write(outputPath, compressedData);
           
    }
    
    public void compressToBzip2(String inputFilePath, String outputFilePath) throws Exception {
        
        Path inputPath = Paths.get(inputFilePath);
        Path outputPath = Paths.get(outputFilePath);

        byte[] inputFileData = Files.readAllBytes(inputPath); 
        byte[] compressedData = FileCompression.bzip2(inputFileData);  

        if (compressedData.length == 0) {
            System.out.println("[-] Compression failed: no data was returned.");
            return;
        }   

        Files.createDirectories(outputPath.getParent());
        Files.write(outputPath, compressedData);
           
    }

    public void decompressFromZip(String inputFilePath, String outputDirPath) throws Exception {
        try {
            File inputFile = new File(inputFilePath);

            if (!inputFile.exists() || !inputFile.isFile()) {
                throw new IOException("Input file does not exist or is not a file: " + inputFilePath);
            }

            byte[] inputFileData = Files.readAllBytes(inputFile.toPath());

            Path tempDir = Files.createTempDirectory("decompression");
            FileDecompression.UnZip(inputFileData, tempDir.toString());

            Path outputDir = Paths.get(outputDirPath);
            Files.createDirectories(outputDir);

            Files.walk(tempDir).forEach(sourcePath -> {
                try {
                    Path targetPath = outputDir.resolve(tempDir.relativize(sourcePath));
                    Files.createDirectories(targetPath.getParent());
                    if (Files.isRegularFile(sourcePath)) {
                        Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    
    

    
    
    public void decompressFromGzip(String inputFilePath, String outputFilePath) throws Exception {

        try {
            Path inputPath = Paths.get(inputFilePath);
            byte[] inputFileData = Files.readAllBytes(inputPath);
            System.out.println("Input file size: " + inputFileData.length);
            File decompressedFile = FileDecompression.Gunzip(inputFileData, outputFilePath);

            if (!decompressedFile.toPath().normalize().equals(Paths.get(outputFilePath).normalize())) {
                Files.move(decompressedFile.toPath(), Paths.get(outputFilePath));
            }
        }catch(IOException e){
            e.printStackTrace();
            throw e ;
        }
    }
    
    public void decompressFromBzip2(String inputFilePath, String outputFilePath) throws Exception {
        
        Path inputPath = Paths.get(inputFilePath);
        byte[] inputFileData = Files.readAllBytes(inputPath); 
        File decompressedFile = FileDecompression.UnBzip2(inputFileData, outputFilePath); 

        if (!decompressedFile.toPath().equals(Paths.get(outputFilePath))) {
            Files.move(decompressedFile.toPath(), Paths.get(outputFilePath));
        }
    }
    
    public void decompressFromTar(String inputFilePath, String outputDirPath) throws Exception {
        try {
            File inputFile = new File(inputFilePath);

            if (!inputFile.exists() || !inputFile.isFile()) {
                throw new IOException("Input file does not exist or is not a file: " + inputFilePath);
            }

            byte[] inputFileData = Files.readAllBytes(inputFile.toPath());

            Path tempDir = Files.createTempDirectory("decompression");
            FileDecompression.ExtractTar(inputFileData, tempDir.toString());

            Path outputDir = Paths.get(outputDirPath);
            Files.createDirectories(outputDir);

            Files.walk(tempDir).forEach(sourcePath -> {
                try {
                    Path targetPath = outputDir.resolve(tempDir.relativize(sourcePath));
                    Files.createDirectories(targetPath.getParent());
                    if (Files.isRegularFile(sourcePath)) {
                        Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    public void decompressFromXZ(String inputFilePath, String outputFilePath) throws Exception {
        
        Path inputPath = Paths.get(inputFilePath);
        byte[] inputFileData = Files.readAllBytes(inputPath); 
        File decompressedFile = FileDecompression.UnXZ(inputFileData, outputFilePath); 

        if (!decompressedFile.toPath().equals(Paths.get(outputFilePath))) {
            Files.move(decompressedFile.toPath(), Paths.get(outputFilePath));
        }
    }
    
    public void decompressFromZstd(String inputFilePath, String outputFilePath) throws Exception {

        Path inputPath = Paths.get(inputFilePath);
        byte[] inputFileData = Files.readAllBytes(inputPath); 
        File decompressedFile = FileDecompression.UnZstd(inputFileData, outputFilePath);
        Path p = decompressedFile.toPath();

        if (!decompressedFile.toPath().equals(Paths.get(outputFilePath))) {
            Files.move(decompressedFile.toPath(), Paths.get(outputFilePath));
        }
    }
    
    public void decompressFromTarGz(String inputFilePath, String outputFilePath) throws Exception {

        Path inputPath = Paths.get(inputFilePath);
        byte[] inputFileData = Files.readAllBytes(inputPath); 
        File decompressedFile = FileDecompression.GunzipTar(inputFileData, outputFilePath); 

        if (!decompressedFile.toPath().equals(Paths.get(outputFilePath))) {
            Files.move(decompressedFile.toPath(), Paths.get(outputFilePath));
        }
    }

    
    public static void main(String[] args) throws Exception {

        
        CommandLine cmd = ParsingArgs.Parsing(args);

        String inputFilePath = cmd.getOptionValue("path");
        String outputFilePath = cmd.getOptionValue("output");
        if (outputFilePath == null || outputFilePath.isEmpty()) {
            outputFilePath = inputFilePath + ".zip";
        }
        
        boolean isCompression = cmd.hasOption("compress");
        boolean isDecompression = cmd.hasOption("decompress");

        String[] compressionTypes = cmd.getOptionValues("type");
        if (compressionTypes == null || compressionTypes.length == 0) {
            compressionTypes = new String[] { "zip" }; 
        }
    
        InsideOut insideOutInstance = new InsideOut(inputFilePath, outputFilePath);

        if (isCompression) {
            for (String type : compressionTypes) {
                String t = type.toLowerCase();

                switch (t){
                    case "zip" : {
                        try {
                            outputFilePath = inputFilePath+".zip";
                            insideOutInstance.compressToZip(inputFilePath, outputFilePath);
                            System.out.println(String.format("[+] %s has been successfully compressed to %s.", inputFilePath, outputFilePath));
                        } catch (Exception e) {
                            System.out.println("[-] Error during compression.");
                            e.printStackTrace();
                        }
                        break ;
                    }
                    
                    case "gz" : {
                        try {
                            outputFilePath = inputFilePath+".gz";
                            insideOutInstance.compressToGzip(inputFilePath, outputFilePath);
                            System.out.println(String.format("[+] %s has been successfully compressed to %s.", inputFilePath, outputFilePath));
                            
                        } catch (Exception e) {
                            System.out.println("[-] Error during compression.");
                            e.printStackTrace();
                        }
                        break ;
                    }
                    
                    case "bzip2" : {
                        try {
                            outputFilePath = inputFilePath+".bzip2";
                            insideOutInstance.compressToBzip2(inputFilePath, outputFilePath);
                            System.out.println(String.format("[+] %s has been successfully compressed to %s.", inputFilePath, outputFilePath));
                            
                        } catch (Exception e) {
                            System.out.println("[-] Error during compression.");
                            e.printStackTrace();
                        }
                        break ;
                    }
                    
                    case "tar" : {
                        try {
                            outputFilePath = inputFilePath+".tar";
                            insideOutInstance.compressToTar(inputFilePath, outputFilePath);
                            System.out.println(String.format("[+] %s has been successfully compressed to %s.", inputFilePath, outputFilePath));
                            
                        } catch (Exception e) {
                            System.out.println("[-] Error during compression.");
                            e.printStackTrace();
                        }
                        break ;
                    }
                    
                    case "xz" : {
                        try {
                            outputFilePath = inputFilePath+".xz";
                            insideOutInstance.compressToXZ(inputFilePath, outputFilePath);
                            System.out.println(String.format("[+] %s has been successfully compressed to %s.", inputFilePath, outputFilePath));
                            
                        } catch (Exception e) {
                            System.out.println("[-] Error during compression.");
                            e.printStackTrace();
                        }
                        break ;
                    }
                    
                    case "zstd" : {
                        try {
                            outputFilePath = inputFilePath+".zstd";
                            insideOutInstance.compressToZstd(inputFilePath, outputFilePath);
                            System.out.println(String.format("[+] %s has been successfully compressed to %s.", inputFilePath, outputFilePath));
                            
                        } catch (Exception e) {
                            System.out.println("[-] Error during compression.");
                            e.printStackTrace();
                        }
                        break ;
                    }
                    
                    
                    default : {
                        System.out.println(String.format("[-] Indefined compression type: %s", t));
                        continue;
                    }
                
                }
                
            }
        }
     
        
        else if (isDecompression) {
            String[] availableTypes = {"xz","zip","tar","gz","zstd","bzip2"};
            String typesString = inputFilePath.substring(inputFilePath.indexOf(".") + 1);
            if (typesString.endsWith(".tar.gz"))
            { try {
                int len = inputFilePath.length();
                outputFilePath = inputFilePath.substring(0,len-7 );
                insideOutInstance.decompressFromTarGz(inputFilePath, outputFilePath);
                System.out.println(String.format("[+] %s has been successfully decompressed into %s", inputFilePath,outputFilePath));
            } catch (Exception e) {
                System.out.println("[-] Error during decompression.");
                e.printStackTrace();
            }}
            else {

            List<String> validExtensions = new ArrayList<>();

            String[] extensions = typesString.split("\\.");
            for (String ext : extensions) {
                validExtensions.add(ext);
            }
            
            int length =0 ;
            Iterator<String> iterator = validExtensions.iterator();

            while (iterator.hasNext()) {
                String ta = iterator.next();
                boolean found = false;

                for (String av : availableTypes) {
                    if (av.equals(ta)) {
                        length++;
                        found = true;
                        break; 
                    }
                }

                if (!found) {
                    iterator.remove();
                }
            }
            Collections.reverse(validExtensions);

            for (int num = 0 ; num< length;num++)
                if (typesString.endsWith("zip")){
                    try {
                        outputFilePath = inputFilePath.replace(".zip", "");
                        insideOutInstance.decompressFromZip(inputFilePath, outputFilePath);
                        System.out.println(String.format("[+] %s has been successfully decompressed into %s", inputFilePath,outputFilePath));
                        
                        outputFilePath = inputFilePath.substring(0, inputFilePath.lastIndexOf('.'));
                        inputFilePath = outputFilePath;
                    } catch (Exception e) {
                        System.out.println("[-] Error during decompression.");
                        e.printStackTrace();
                }}
                else if (typesString.endsWith(".gz")){
                    try {
                        outputFilePath = inputFilePath.replace(".gz", "");
                        insideOutInstance.decompressFromGzip(inputFilePath, outputFilePath);
                        System.out.println(String.format("[+] %s has been successfully decompressed into %s", inputFilePath,outputFilePath));
                        
                        outputFilePath = inputFilePath.substring(0, inputFilePath.lastIndexOf('.'));
                        inputFilePath = outputFilePath;
                        } catch (Exception e) {
                        System.out.println("[-] Error during decompression.");
                        e.printStackTrace();
                }}
                else if ((typesString.endsWith(".zstd"))){
                    try {
                        outputFilePath = inputFilePath.replace(".zstd", "");
                        insideOutInstance.decompressFromZstd(inputFilePath, outputFilePath);
                        System.out.println(String.format("[+] %s has been successfully decompressed into %s", inputFilePath,outputFilePath));
                        inputFilePath = outputFilePath;
                        outputFilePath = inputFilePath.substring(0, inputFilePath.lastIndexOf('.'));
                    } catch (Exception e) {
                        System.out.println("[-] Error during decompression.");
                        e.printStackTrace();
                }}
                else if (typesString.endsWith(".bzip2")){
                    try {
                        outputFilePath = inputFilePath.replace(".bzip2", "");
                        insideOutInstance.decompressFromBzip2(inputFilePath, outputFilePath);
                        System.out.println(String.format("[+] %s has been successfully decompressed into %s", inputFilePath,outputFilePath));
                        inputFilePath = outputFilePath;
                        outputFilePath = inputFilePath.substring(0, inputFilePath.lastIndexOf('.'));
                    } catch (Exception e) {
                        System.out.println("[-] Error during decompression.");
                        e.printStackTrace();
                }}
                else if (typesString.endsWith(".tar")){
                    try {
                        outputFilePath = inputFilePath.replace(".tar", "");
                        insideOutInstance.decompressFromTar(inputFilePath, outputFilePath);
                        System.out.println(String.format("[+] %s has been successfully decompressed into %s", inputFilePath,outputFilePath));
                        inputFilePath = outputFilePath;
                        outputFilePath = inputFilePath.substring(0, inputFilePath.lastIndexOf('.'));
                    } catch (Exception e) {
                        System.out.println("[-] Error during decompression.");
                        e.printStackTrace();
                }}
                else if (typesString.endsWith(".xz")){
                    try {
                        outputFilePath = inputFilePath.replace(".xz", "");
                        insideOutInstance.decompressFromXZ(inputFilePath, outputFilePath);
                        System.out.println(String.format("[+] %s has been successfully decompressed into %s", inputFilePath,outputFilePath));
                        inputFilePath = outputFilePath;
                        outputFilePath = inputFilePath.substring(0, inputFilePath.lastIndexOf('.'));
                    } catch (Exception e) {
                        System.out.println("[-] Error during decompression.");
                        e.printStackTrace();
                }}
                else {
                    System.out.println("[-] Unsupported Type.");
                }
                
            
                }
            }
        }

    
}

