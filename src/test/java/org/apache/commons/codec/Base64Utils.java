package org.apache.commons.codec;
import org.apache.commons.codec.binary.Base64;

public class Base64Utils {

	public synchronized static String encode(String src) {
		Base64 base64 = new Base64();
		byte[] enbytes = null;
		String encodeStr = null;
		enbytes = base64.encode(src.getBytes());
		encodeStr = new String(enbytes);
		return encodeStr;
	}
	public synchronized static String decode(String src) {
		Base64 base64 = new Base64();
		byte[] enbytes = null;
		String encodeStr = null;
		enbytes = base64.decode(src.getBytes());
		encodeStr = new String(enbytes);
		return encodeStr;
	}
}
