package com.vladm.googleauthdemo;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws IOException, WriterException {
        //System.out.println(generateSecretKey());
        //System.out.println(getTOTPCode("2HREMAVIYVE4KVDAIDKW3ATZXW4MY463"));
        System.out.println(getGoogleAuthenticatorBarCode("2HREMAVIYVE4KVDAIDKW3ATZXW4MY463", "rjohn", "sastechstudio"));
        //createQRCode("otpauth://totp/sastechstudio%3Arjohn?secret=2HREMAVIYVE4KVDAIDKW3ATZXW4MY463&issuer=sastechstudio", "D:\\demo.png", 150, 150);

//        Scanner scanner = new Scanner(System.in);
//        String code = scanner.nextLine();
//        if (code.equals(getTOTPCode("2HREMAVIYVE4KVDAIDKW3ATZXW4MY463"))) {
//            System.out.println("Logged in successfully");
//        } else {
//            System.out.println("Invalid 2FA Code");
//        }
    }

    public static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    public static String generateSecretKeyWithTimer() {
        String secretKey = "QDWSM3OYBPGTEVSPB5FKVDM3CSNCWHVK";
        String lastCode = null;
        while (true) {
            String code = getTOTPCode(secretKey);
            if (!code.equals(lastCode)) {
                System.out.println(code);
            }
            lastCode = code;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            ;
        }
    }

    public static String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

    public static String getGoogleAuthenticatorBarCode(String secretKey, String account, String issuer) {
        try {
            return "otpauth://totp/" + URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20") + "?secret=" + URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20") + "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void createQRCode(String barCodeData, String filePath, int height, int width) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE, width, height);
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            MatrixToImageWriter.writeToStream(matrix, "png", out);
        }
    }

}
