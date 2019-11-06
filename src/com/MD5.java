package com;

import java.security.MessageDigest;

public class MD5 {

	public static String md5(String text) {
		try {
			if (text == null)
				return null;

			MessageDigest md = MessageDigest.getInstance("MD5");

			md.update(text.getBytes("UTF-8"));

			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			return buf.toString();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}
}
