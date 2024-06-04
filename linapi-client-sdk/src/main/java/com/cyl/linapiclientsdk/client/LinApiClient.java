package com.cyl.linapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.cyl.common.entity.User;

import java.util.HashMap;
import java.util.Map;

import static com.cyl.linapiclientsdk.utils.SignUtils.genSign;


/**
 * 调用第三方接口的客户端
 *
 * @author <a href="https://github.com/cmaiyatang">YoungLin</a>
 */
public class LinApiClient {

    private static final String GATEWAY_HOST = "http://localhost:8200";

    private static final String MYHost = "http://localhost:8102";

    private String accessKey;

    private String secretKey;

    public LinApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.get(GATEWAY_HOST + "/api/name/", paramMap);
        System.out.println(result);
        return result;
    }

    public String getUsernameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/name/json")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        System.out.println(httpResponse.getStatus());
        String result = httpResponse.body();
        System.out.println(result);
        return result;
    }

    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        // 一定不能直接发送
//        hashMap.put("secretKey", secretKey);
        //添加随机数 和 时间戳防止重放
        hashMap.put("once", RandomUtil.randomNumbers(4));
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("body", body);
        hashMap.put("sign", genSign(body, secretKey));
        return hashMap;
    }
}
