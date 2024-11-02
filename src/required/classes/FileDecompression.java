package required.classes;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.GZIPInputStream;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.github.luben.zstd.ZstdInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.z.ZCompressorInputStream;
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorInputStream;

public class FileDecompression {

    public static File UnZip(byte[] compressedData, String output) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
             ZipInputStream zis = new ZipInputStream(bais)) {
             
            File outputStruct = new File(output);
            if (!outputStruct.exists()) {
                outputStruct.mkdirs();
            }
            
            byte[] buffer = new byte[1024];
            ZipEntry zEntry;
            
            while ((zEntry = zis.getNextEntry()) != null) {
                File newFile = new File(outputStruct, zEntry.getName());
                
                if (!newFile.getCanonicalPath().startsWith(outputStruct.getCanonicalPath() + File.separator)) {
                    throw new IOException("Entry is outside of the target dir: " + zEntry.getName());
                }
                
                if (zEntry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    newFile.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
                zis.closeEntry();
            }
            
            return outputStruct;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static File Gunzip(byte[] compressedData, String output) throws IOException {

        File outputFile = new File(output);
        outputFile.getParentFile().mkdirs();
        try (ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
            FileOutputStream fos= new FileOutputStream(output);
            GzipCompressorInputStream gzis = new GzipCompressorInputStream(bais)) {

            byte[] buffer = new byte[1024];
            int length ; 
            
            while((length=gzis.read(buffer))>0){
                fos.write(buffer, 0, length);
            }
            
        }
        catch (IOException e){
            e.printStackTrace();
            throw e ;
        }
        return outputFile ;
    }

    public static File UnBzip2(byte[] compressedData, String output) throws IOException {

        File outputFile = new File(output);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
            FileOutputStream fos = new FileOutputStream(output);
            BZip2CompressorInputStream bz2is = new BZip2CompressorInputStream(bais)){
                
                    
            if (!outputFile.exists()){

                outputFile.getParentFile().mkdirs();

                byte[] buffer = new byte[1024];
                int length;

                while((length=bz2is.read(buffer))>0){
                    fos.write(buffer,0,length);
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
            throw e ;
        }
        return outputFile;
        
    }
   
    public static File UnXZ(byte[] compressedData, String output ) throws IOException {

        File outputFile = new File(output);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
            FileOutputStream fos= new FileOutputStream(output);
            XZCompressorInputStream xzis = new XZCompressorInputStream (bais)) {
                
                
            if (!outputFile.exists()){
                outputFile.getParentFile().mkdirs();

                byte[] buffer = new byte[1024];
                int length ; 

                while((length=xzis.read(buffer))>0){
                    fos.write(buffer, 0, length);
                }
            }  
        }
        catch (IOException e){
            e.printStackTrace();
            throw e ;
        }
        return outputFile ;
    }
    
    public static File UnZstd(byte[] compressedData, String output ) throws IOException {

        File outputFile = new File(output);
        outputFile.getParentFile().mkdirs();
        try (ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
            FileOutputStream fos= new FileOutputStream(output);
            ZstdCompressorInputStream zstdis = new ZstdCompressorInputStream (bais)) {
                
            byte[] buffer = new byte[1024];
            int length ; 

            while((length=zstdis.read(buffer))>0){
                fos.write(buffer, 0, length);
            }
        
        }
        catch (IOException e){
            e.printStackTrace();
            throw e ;
        }
        return outputFile ;
    }
    
    public static File ExtractTar(byte[] inputData, String output) throws IOException{

        try (ByteArrayInputStream bais = new ByteArrayInputStream(inputData);
            TarArchiveInputStream tais = new TarArchiveInputStream(bais)){
            
            File outputStruct = new File(output);
            if (!outputStruct.exists()) {
                outputStruct.mkdirs();
            }
            
            byte[] buffer = new byte[1024];
            TarArchiveEntry TarEntry = tais.getNextEntry();

            while (TarEntry!=null){
                File newfile = new File(outputStruct, TarEntry.getName());
                if (!newfile.getCanonicalPath().startsWith(outputStruct.getCanonicalPath() + File.separator)) {
                    throw new IOException("Entry is outside of the target dir: " + TarEntry.getName());
                }
                
                if (TarEntry.isDirectory()){
                    newfile.mkdir();
                }
                else {
                    newfile.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newfile)) {
                        int length;
                        while ((length = tais.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
                TarEntry = tais.getNextEntry(); 
            }
            return outputStruct;
        }
        catch (IOException e){
            e.printStackTrace();
            throw e ;
        }
    }

    public static File GunzipTar(byte[] inputData, String output) throws IOException{

        String taroutput = output+"/tmp.tar";
        File tarFile = Gunzip(inputData, taroutput);
        Path tarPath = tarFile.toPath();
        byte[] inputFileData = Files.readAllBytes(tarPath); 

        return ExtractTar(inputFileData, output);
    }
}



    

    

