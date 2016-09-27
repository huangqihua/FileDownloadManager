package com.example.huangqihua.filedownloadapplication.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by huangqihua on 16/9/27.
 */
public class MiniMd5 {

    public static String md5String(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toString((messageDigest[i] & 0xff) + 0x100, 16).substring(1));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String Md5FileEncode(File file) {
        byte[] buf = new byte[8192];
        MessageDigest md;
        boolean fileIsNull = true;
        try {
            FileInputStream fis = new FileInputStream(file);
            int len = 0;
            md = MessageDigest.getInstance("MD5");
            len = fis.read(buf);
            if (len > 0) {
                fileIsNull = false;
                while (len > 0) {
                    md.update(buf, 0, len);
                    len = fis.read(buf);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        if (fileIsNull) {
            return "";
        } else {
            byte[] result = md.digest();
            return bufferToHex(result);
        }
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    private static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

}
