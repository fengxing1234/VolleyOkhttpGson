package cn.picc.com.volley.utils;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.zip.DeflaterOutputStream;

/**
 * User: jfy
 * Date: 11-11-4 2:30
 */
public class ClipherTool {
    public static String decompressData(String encdata) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InflaterOutputStreamJDK5 zos = new InflaterOutputStreamJDK5(bos);
            zos.write(Base64.decode(encdata, Base64.DEFAULT));
            zos.close();
            return new String(bos.toByteArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "UNZIP_ERR";
    }

    public static String compressData(String data) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DeflaterOutputStream zos = new DeflaterOutputStream(bos);
            zos.write(data.getBytes());
            zos.close();
            String s = Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);
            s = s.replaceAll("\r\n", "");
            return s;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "ZIP_ERR";
    }
}
