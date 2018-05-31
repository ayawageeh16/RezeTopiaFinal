package io.krito.com.rezetopia.helper;

import android.os.Debug;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class EncodeBase64 {

    public static final long SAFETY_MEMORY_BUFFER = 10;//MB

    private String encodeFileToBase64Binary(String fileName) throws IOException {

        File file = new File(fileName);
        byte[] bytes = loadFile(file);
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);

//        byte[] encoded = Base64.encodeBase64(bytes);
//        String encodedString = new String(encoded);

        return encodedString;
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        is.close();
        return bytes;
    }

    public static double availableMemoryMB(){
        double max = Runtime.getRuntime().maxMemory()/1024;
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        return (max - memoryInfo.getTotalPss())/1024;
    }
}
