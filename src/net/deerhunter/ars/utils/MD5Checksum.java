package net.deerhunter.ars.utils;

import java.io.*;
import java.security.MessageDigest;

/**
 * This class is used to generate an MD5 checksum.
 * 
 * @author DeerHunter
 */
public class MD5Checksum {
	private MD5Checksum() {
	}

	/**
	 * Method generates an MD5 checksum byte array for the given <code>String</code> parameter.
	 * @param text Text the MD5 checksum will be generated for
	 * @return Byte array that represents the MD5 checksum
	 * @throws Exception if some problems with IO stream is happened
	 */
	public static byte[] createChecksum(String text) throws Exception{
		InputStream fis = new FileInputStream(text);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return complete.digest();
	}

	/**
	 * Method generates an MD5 checksum string for the given <code>String</code> parameter.
	 * @param text Text the MD5 checksum will be generated for
	 * @return String that represents the MD5 checksum
	 * @throws Exception if some problems with IO stream is happened
	 */
	public static String getMD5Checksum(String text) throws Exception {
		byte[] b = createChecksum(text);
		String result = "";

		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
}
