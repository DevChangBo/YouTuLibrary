package com.example.youtulibrary.youtu;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * ================================================================
 * 创建时间：2018/6/11 10:14
 * 创 建 人：Mr.常
 * 文件描述：
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
public class HMACSHA1 {
    private static final String HMAC_SHA1 = "HmacSHA1";

    public static byte[] getSignature(String data, String key) throws Exception {
        Mac mac = Mac.getInstance(HMAC_SHA1);
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(),
                mac.getAlgorithm());
        mac.init(signingKey);

        return mac.doFinal(data.getBytes());
    }
}
