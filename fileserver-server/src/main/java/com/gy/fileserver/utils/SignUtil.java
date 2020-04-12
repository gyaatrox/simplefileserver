package com.gy.fileserver.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class SignUtil {

    /**
     * RSA私钥加签
     * @param priKeyText 经过base64处理后的私钥
     * @param plainText 明文内容
     * @return 十六进制的签名字符串
     * @throws Exception
     */
    public static String sign(byte[] priKeyText, String plainText) throws Exception {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(priKeyText));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey prikey = keyf.generatePrivate(priPKCS8);

            // 用私钥对信息生成数字签名
            java.security.Signature signet = java.security.Signature.getInstance("SHA256withRSA");
            signet.initSign(prikey);
            signet.update(plainText.getBytes("UTF-8"));
            return DigestUtil.byte2hex(signet.sign());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 公钥验签
     * @param pubKeyText 经过base64处理后的公钥
     * @param plainText 明文内容
     * @param signText 十六进制的签名字符串
     * @return 验签结果 true验证一致 false验证不一致
     */
    public static boolean verify(byte[] pubKeyText, String plainText, String signText) {
        try {
            // 解密由base64编码的公钥,并构造X509EncodedKeySpec对象
            java.security.spec.X509EncodedKeySpec bobPubKeySpec = new java.security.spec.X509EncodedKeySpec(
                    Base64.decodeBase64(pubKeyText));
            // RSA算法
            java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");
            // 取公钥匙对象
            java.security.PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
            // 十六进制数字签名转为字节
            byte[] signed = DigestUtil.hex2byte(signText.getBytes("UTF-8"));
            java.security.Signature signatureChecker = java.security.Signature.getInstance("SHA256withRSA");
            signatureChecker.initVerify(pubKey);
            signatureChecker.update(plainText.getBytes("UTF-8"));
            // 验证签名是否正常
            return signatureChecker.verify(signed);
        } catch (Throwable e) {
            return false;
        }
    }
}