package net.deerhunter.ars.protocol;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

import net.deerhunter.ars.protocol.packets.MainPacket;

/**
 * Created with IntelliJ IDEA.
 * User: DeerHunter
 * Date: 19.10.12
 * Time: 13:01
 * To change this template use File | Settings | File Templates.
 */
public class Uploader {
    public static boolean sendPacket(MainPacket packet) throws IOException {

        BufferedInputStream fis = new BufferedInputStream(new ByteArrayInputStream(packet.getPacket()));

        HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL("http://192.168.1.4/upload.php?filename=" + System.currentTimeMillis()).openConnection();
        httpUrlConnection.setDoOutput(true);
        httpUrlConnection.setRequestMethod("POST");
        OutputStream os = httpUrlConnection.getOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(os);

        int totalByte = packet.getPacketLength();
        int byteTransferred = 0;
        byte[] fileBuffer = new byte[totalByte];
        do {
            int readDataLength = fis.read(fileBuffer, byteTransferred, totalByte - byteTransferred);
            bos.write(fileBuffer, byteTransferred, readDataLength);

            byteTransferred += readDataLength;
        } while (totalByte != byteTransferred);

        bos.flush();
        bos.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));

        boolean result = false;
        String s = in.readLine();
        if (s != null && s.equals("Done"))
            result = true;

        Log.e("result", s);
        
        in.close();
        fis.close();

        return result;
    }
}