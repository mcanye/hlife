package hlife.utils.md5;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;


public class AES {
	private final String KEY_GENERATION_ALG = "PBKDF2WithHmacSHA1";

	private final int HASH_ITERATIONS = 10000;
	private final int KEY_LENGTH = 128;

	private char[] humanPassphrase = { 'P', '$', 'r', ' ', 'v', '9', 'l', 'K', 'j', 'm', '2', 'D', 'u', 'c', '@', 's',
			' ', 'L', 'a', 'b', 'F', 'n' };

	private byte[] salt = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF };

	private PBEKeySpec myKeyspec = new PBEKeySpec(humanPassphrase, salt, HASH_ITERATIONS, KEY_LENGTH);
	private final String CIPHERMODEPADDING = "AES/CBC/PKCS7Padding";

	private SecretKeyFactory keyfactory = null;
	private SecretKey sk = null;
	private SecretKeySpec skforAES = null;
	private byte[] iv = { 0xA, 1, 0xB, 5, 4, 0xF, 7, 9, 0x17, 3, 1, 6, 8, 0xC, 0xD, 91 };

	private IvParameterSpec ivParameterSpec;

	public AES() {

		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			keyfactory = SecretKeyFactory.getInstance(KEY_GENERATION_ALG);

			sk = keyfactory.generateSecret(myKeyspec);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		// This is our secret key. We could just save this to a file instead of
		// regenerating it
		// each time it is needed. But that file cannot be on the device (too
		// insecure). It could
		// be secure if we kept it on a server accessible through https.
		byte[] skAsByteArray = sk.getEncoded();
		skforAES = new SecretKeySpec(skAsByteArray, "AES");
		ivParameterSpec = new IvParameterSpec(iv);

	}

	private static AES singleton;

	public static AES getInstance() {
		if (singleton == null) {
			singleton = new AES();
		}
		return singleton;
	}

	/**
	 * 解密，并判断是否为有效请求 1:非法 2：合法，但时间戳不符合规定范围 3：通过
	 */
	public int isLegalReq(String apiKey, String url, String phone) {
		// 1:非法
		int flag = 1;
		// 解密
		String key = decrypt(apiKey);
		if (StringUtils.isEmpty(key) || key.length() <= 28 || !key.contains(",")) {
			return flag;
		}

		String[] secTPAV = key.split(",");
		if (secTPAV.length == 3 && secTPAV[0].length() == 25 && StringUtils.isNotEmpty(secTPAV[1])
				&& url.contains(secTPAV[1])) {
			String secStr = secTPAV[0].substring(0, 13);
			String phoneStr = secTPAV[0].substring(14, 25);
			// 校验phone是否一致
			if (!phone.equals(phoneStr)) {
				return flag;
			}
			if (StringUtils.isNumeric(secStr)) {
				long nowSecs = Calendar.getInstance().getTimeInMillis();
				if (Math.abs(nowSecs - Long.valueOf(secStr)) < 1000 * 60 * 5) {
					// 3：通过
					flag = 3;
				} else {
					// 2：合法，但时间戳不符合规定范围
					flag = 2;
				}
			}
		}
		return flag;
	}

	/**
	 * 解密，并判断是否为有效请求 1:非法 2：合法，但时间戳不符合规定范围 3：通过
	 */
	public int xyIsLegalReq(String apiKey, String url, String userId) {
		// 1:非法
		int flag = 1;
		// 解密
		String key = decrypt(apiKey);
		if (StringUtils.isEmpty(key) || key.length() <= 35 || !key.contains(",")) {
			return flag;
		}

		String[] secTPAV = key.split(",");
		if (secTPAV.length == 3 && secTPAV[0].length() == 32 && StringUtils.isNotEmpty(secTPAV[1])
				&& url.contains(secTPAV[1])) {
			String secStr = secTPAV[0].substring(0, 13);
			String phoneStr = secTPAV[0].substring(14, 32);
			// 校验phone是否一致
			if (!userId.equals(phoneStr)) {
				return flag;
			}
			if (StringUtils.isNumeric(secStr)) {
				long nowSecs = Calendar.getInstance().getTimeInMillis();
				if (Math.abs(nowSecs - Long.valueOf(secStr)) < 1000 * 60 * 5) {
					// 3：通过
					flag = 3;
				} else {
					// 2：合法，但时间戳不符合规定范围
					flag = 2;
				}
			}
		}
		return flag;
	}


	public String encrypt(byte[] plaintext) {

		byte[] ciphertext = encrypt(CIPHERMODEPADDING, skforAES, ivParameterSpec, plaintext);
		
		String base64_ciphertext = Base64Encoder.encode(ciphertext);
		return base64_ciphertext;
	}

	public String decrypt(String ciphertext_base64) {
		byte[] s = Base64Decoder.decodeToBytes(ciphertext_base64);
		s = decrypt(CIPHERMODEPADDING, skforAES, ivParameterSpec, s);
		return new String(s);
	}

	private byte[] encrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] msg) {
		byte[] resultByte = null;
		try {
			Cipher c = Cipher.getInstance(cmp);
			c.init(Cipher.ENCRYPT_MODE, sk, IV);
			resultByte = c.doFinal(msg);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return resultByte;
	}

	private byte[] decrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] ciphertext) {
		byte[] resultByte = null;
		try {
			Cipher c = Cipher.getInstance(cmp);
			c.init(Cipher.DECRYPT_MODE, sk, IV);
			return c.doFinal(ciphertext);
		} catch (Exception e) {
			return resultByte;
		}
	}
}
