package com.gy.fileserver;

import com.gy.fileserver.utils.AesUtil;
import com.gy.fileserver.utils.DigestUtil;
import com.gy.fileserver.utils.RsaUtil;
import org.junit.Test;

public class TestCode {

    public String publicKey ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCC3Hd9SPexdeMe8FSvU/3xFJ0INbmsRghXKIdGvmlk0r8mzBpVVPZkVPtrnVnuD9yZ9bDHgPqGJgqr1AGWraLnmX39ukQHpPsTzGBzCJizBMdiiUx7pyK0gq7ULhC+y9xSs9K2joczKQRiMEXGuzXrjuIZf0ITHJ5D9QAZNLQ7qwIDAQAB";
    public String privateKey ="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAILcd31I97F14x7wVK9T/fEUnQg1uaxGCFcoh0a+aWTSvybMGlVU9mRU+2udWe4P3Jn1sMeA+oYmCqvUAZatoueZff26RAek+xPMYHMImLMEx2KJTHunIrSCrtQuEL7L3FKz0raOhzMpBGIwRca7NeuO4hl/QhMcnkP1ABk0tDurAgMBAAECgYBJlA63afyjUkaG0di70D0bfVyiDhs86w+regU92LhH0VtbRC2WRzRWy5WmN44mFdMS2hswwaubG+6qGIVgJHdQcYCxJ+JY/fEAcTwJBgpFJhLJ2s4O1KYcsxe42TlsEX8hpgpcd7js3Tspk53E7oYzBOsEkYp7rMDhzyLR2pYEoQJBAN+Hsq0x8Cmxhl9BZaqT3eqHPqM2Iln0KDKyV9QDaqQLSGN7lnPF34fdzgopHBPIiufEwIc06qkDAK2/O4cBS3sCQQCV3rzCjsSwXxfgdOJLLQOefD+fB/3JgO7xAdeuU9k0Ctj3VrnTghhx0ZncbwsmSlwJ+yLDMHLa77P2g8g49wGRAkARNeuF9lBdhXOpJenkBlqR0jP6cRKYBnqJ3L1yb4jodI3wSvW8mEGrvmI82gl1ZH19duK0BlkDBl4pAo5cyLa9AkA8qXagDafBeAwzFzyNjz/aw5IA7fwktuKAfO1vK5NMRGPD4b+/w/XfBCcSGYRa0Fhm1dchSJgE1Gd0jazamehxAkA4u/W0eczFg+pqkP2ftt2m/s6oOvcJiolwBigHAEeuzmRoLAx7HC2Y77ZD3xqDP5j1wJxwTf7p4RriEsw7GXxG";
    @Test
    public void test() {
        try {
            //文件加密
            //String key = AesUtil.getKey();//aes秘钥
            String key = "etLcp5yisAX34Y1R";
            System.out.println("aes秘钥：" + key);
            String content = "respect you！哇咔咔";//加密内容
            System.out.println("加密内容：" + content);
            String encrypt = AesUtil.encrypt(content, key); //加密结果,存入文件
            System.out.println("内容加密结果：" + encrypt);

            //使用RSA公钥把aes秘钥加密
            //String publicKey = RsaUtil.getPublicKey();
            System.out.println("RSA公钥：" + publicKey);
            byte[] bytes1 = RsaUtil.encryptByPublicKey(key.getBytes(), publicKey);
            String s = DigestUtil.byte2hex(bytes1);//为了便于展示，字节转为十六进制查看
            System.out.println("aes秘钥加密：" + s);
            System.out.println("aes秘钥加密长度：" + s.length());

            //aes秘钥 使用RSA私钥解密
           // String privateKey = RsaUtil.getPrivateKey();
            System.out.println("RSA私钥：" + privateKey);
            byte[] bytes2 = RsaUtil.decryptByPrivateKey(DigestUtil.hex2byte(s.getBytes()),privateKey);
            System.out.println("aes秘钥解密：" + new String(bytes2));

            //文件解密
            //String str = "udbxXRYMuy06uu2XJgwyjQ==";
            String decrypt = AesUtil.decrypt(encrypt, new String(bytes2));
            System.out.println("解密内容：" + decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
