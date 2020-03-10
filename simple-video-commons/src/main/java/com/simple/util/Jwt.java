package com.simple.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: Jwt.java
 * @description: jwt token工具类
 * @author: fanyanghua
 * @create: 2019-10-25 16:15
 **/
@Slf4j
public class Jwt {
    /**
     * APP登录Token的生成和解析
     *
     */
    /** token秘钥，请勿泄露，请勿随便修改 backups:JKKLJOoasdlfj */
    public static final String SECRET = "JKKLJOoasdlfj";
    /** token 过期时间: 10天 */
    public static final int calendarField = Calendar.DATE;
    public static final int calendarInterval = 10;

    /**
     * JWT生成Token.<br/>
     *
     * JWT构成: header, payload, signature
     *
     * @param phoneNo 手机号
     * @param fromchannel 渠道
     * @param  deviceId 设备id
     */
    public static String createToken(String phoneNo,String fromchannel,String deviceId) throws Exception {
        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(calendarField, calendarInterval);
        Date expiresDate = nowTime.getTime();

        // header Map
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        // build token
        String token = JWT.create().withHeader(map) // header
                // payload
                .withClaim("fromChannel", fromchannel)
                .withClaim("phoneNo", StringUtils.isEmpty(phoneNo) ? null : phoneNo)
                .withClaim("deviceNo",deviceId)
                .withIssuedAt(iatDate) // sign time
                .withExpiresAt(expiresDate) // expire time
                .sign(Algorithm.HMAC256(SECRET)); // signature
        return token;
    }

    /**
     * 解密Token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String, Claim> verifyToken(String token) throws Exception {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            log.error("token 校验失败",e);
            // token 校验失败, 抛出Token验证非法异常
            //throw new BizException(QingLianErrorCode.QINGLIAN_ERROR_CODE_401);
        }
        if(jwt==null){
            log.error("token is null");
            //throw new BizException(QingLianErrorCode.QINGLIAN_ERROR_CODE_401);
        }
        return jwt.getClaims();
    }

    /**
     * 根据Token获取phoneNo
     *
     * @param token
     * @return user_id
     */
    public static String getPhoneNo(String token) {
        Map<String, Claim> claims = null;
        Claim phone_no_claim = null ;
        try {
            claims = verifyToken(token);
            phone_no_claim = claims.get("phoneNo");
            if (null == phone_no_claim || StringUtils.isEmpty(phone_no_claim.asString())) {
                log.error("token 校验phoneNo 为空");
                //TODO token 校验失败, 抛出Token验证非法异常
                //throw new BizException(QingLianErrorCode.QINGLIAN_ERROR_CODE_401);
            }
        } catch (Exception e) {
            log.error("token 校验失败",e);
            //throw new BizException(QingLianErrorCode.QINGLIAN_ERROR_CODE_401);
        }

        return phone_no_claim.asString();
    }


    public static void main(String[] args) throws Exception {
        System.out.println(createToken("13344445555","5","123432123fdsa"));
    }
}