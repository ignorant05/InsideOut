package required.classes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.IOException;

import java.util.zip.GZIPOutputStream;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

import org.apache.commons.compress.compressors.zstandard.ZstdCompressorOutputStream;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZOutputStream;

public class FileCompression {

    public static byte[] Zip(byte[] inputData) throws IOException {
        
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {
    
            ZipEntry zipEntry = new ZipEntry("compressedData");
            zos.putNextEntry(zipEntry);
            zos.write(inputData);
            zos.closeEntry();
    
            return baos.toByteArray();
        }
        catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    public static byte[] GZIP(byte[] inputData)throws IOException{
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gos = new GZIPOutputStream(baos)){

                gos.write(inputData);
                gos.finish();
        
                return baos.toByteArray();
            }
            catch (IOException e) {
                e.printStackTrace();
                throw e;   
            }
        }

    public static byte[] bzip2(byte[] inputData)throws IOException{

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BZip2CompressorOutputStream bzos = new BZip2CompressorOutputStream(baos)){

                bzos.write(inputData);
                bzos.finish();
                
                return baos.toByteArray();
            }
            catch (IOException e){
                e.printStackTrace();
                throw e;
            }

}
    
    public static byte[] xz(byte[] inputData) throws IOException{

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ByteArrayInputStream bais = new ByteArrayInputStream(inputData);
            XZOutputStream xzOut = new XZOutputStream(baos, new LZMA2Options())){
                
            byte[] buffer = new byte[1024];
            int length;
            while ((length = bais.read(buffer)) != -1) {
                xzOut.write(buffer, 0, length);
            }
            xzOut.finish();
                return baos.toByteArray();
            }
            catch (IOException e){
                e.printStackTrace();
                throw e;
            }
    }

    public static byte[] Tar(byte[] inputData) throws IOException {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();

            TarArchiveOutputStream taros = new TarArchiveOutputStream(baos)){
                
                TarArchiveEntry TarEntry = new TarArchiveEntry("compressedData");
                
                TarEntry.setSize(inputData.length);

                taros.putArchiveEntry(TarEntry);

                taros.write(inputData);

                taros.closeArchiveEntry();

                taros.finish();

                return baos.toByteArray();
        }
        catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static byte[] Zstd(byte[] inputData) throws IOException{
        
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZstdCompressorOutputStream zstdos = new ZstdCompressorOutputStream(baos)){

                zstdos.write(inputData);
                zstdos.flush();
                zstdos.close();

                return baos.toByteArray();
            }
            catch (IOException e){
                e.printStackTrace();
                throw e ;
            }
    }

}



