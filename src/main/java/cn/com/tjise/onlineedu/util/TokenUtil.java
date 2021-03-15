package cn.com.tjise.onlineedu.util;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.util.Date;
import java.util.Objects;

/**
 * @author admin
 * token 工具类
 */
public class TokenUtil
{
    /**
     * 创建秘钥
     */
    private static final byte[] SECRET = "6MNSobBRCHGIO0fS6MNSobBRCHGIO0fS".getBytes();
    
    /**
     * 过期时间2小时
     */
    private static final long EXPIRE_TIME = 7_200_000;
    // 测试时间
//    private static final long EXPIRE_TIME = 10_000; 
    
    /**
     * 生成token
     */
    public static String buildToken(String userId)
    {
        try
        {
            /**
             * 1.创建一个32-byte的密匙
             */
            MACSigner macSigner = new MACSigner(SECRET);
            /**
             * 2.建立payload载体
             */
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("doi")
                .issuer("http://www.doiduoyi.com")
                .expirationTime(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .claim("ACCOUNT", userId)
                .build();
            /**
             * 3. 建立签名
             */
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(macSigner);
            /**
             * 4. 生成token
             */
            String token = signedJWT.serialize();
            return token;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 验证token
     *
     * @param token
     * @return
     */
    public static boolean validToken(String token)
    {
        try
        {
            SignedJWT jwt = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET);
            //校验是否有效
            if (!jwt.verify(verifier))
            {
                return false;
            }
            
            //校验超时
            Date expirationTime = jwt.getJWTClaimsSet().getExpirationTime();
            if (new Date().after(expirationTime))
            {
                return false;
            }
            
            //获取载体中的数据
            Object account = jwt.getJWTClaimsSet().getClaim("ACCOUNT");
            //是否有openUid
            if (Objects.isNull(account))
            {
                return false;
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
