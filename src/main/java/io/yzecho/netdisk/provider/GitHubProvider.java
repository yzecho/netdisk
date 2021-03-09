package io.yzecho.netdisk.provider;

import com.alibaba.fastjson.JSON;
import io.yzecho.netdisk.model.dto.AccessTokenDTO;
import io.yzecho.netdisk.model.dto.GitHubUser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * @className: GitHubProvider
 * @description:
 * @author: liuzhe
 * @date: 2021/3/8
 **/
@Component
@Slf4j
public class GitHubProvider {

    public static final MediaType MEDIA_TYPE = MediaType.Companion.get("application/json; charset=utf-8");

    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        RequestBody body = RequestBody.Companion.create(JSON.toJSONString(accessTokenDTO), MEDIA_TYPE);
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();

            System.out.println(string);
            return string;
        } catch (IOException e) {
            log.error("getAccessToken error,{}", accessTokenDTO, e);
        }
        return null;
    }

    public GitHubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization", "token " + accessToken)
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            String string = Objects.requireNonNull(response.body()).string();
            return JSON.parseObject(string, GitHubUser.class);
        } catch (IOException e) {
            log.error("getUser error,{}", accessToken, e);
        }
        return null;
    }
}
