package com.gromore.demo.utils;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {

    private static InputStream is = null;

    public static class ResponseBody{
        public String code;
        public String msg;
        public User user;
    }
    public static class User{
        public int uid;
        public String uname;
        public String password;
        public String oaid;
        public float points;
    }

    /**
     * 阻塞post
     */
    public static ResponseBody sendPost(String url, String param) {
        final CountDownLatch lt = new CountDownLatch(1);
        OkHttpClient okHttp = new OkHttpClient().newBuilder().proxy(Proxy.NO_PROXY).build();
        FormBody.Builder form = new FormBody.Builder();
        ResponseBody result = new ResponseBody();
        User user = new User();
        if (!param.equals("")) {
            String[] bodyArr = param.split("&");
            for (String item : bodyArr) {
                String[] itemArr = item.split("=");
                if (itemArr.length == 1) {
                    form.add(itemArr[0], "");
                } else {
                    form.add(itemArr[0], itemArr[1]);
                }
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(form.build())
                .build();

        okHttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) {
                try {
                    String jsonStr = Objects.requireNonNull(response.body()).string();
                    Log.e("mylog", "onResponse: "+jsonStr );
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    result.code = jsonObject.getString("code");
                    result.msg = jsonObject.getString("msg");
                    if(result.code.equals("0")){
                        user.uid = jsonObject.getJSONObject("data").getInt("uid");
                        user.uname = jsonObject.getJSONObject("data").getString("uname");
                        user.password = jsonObject.getJSONObject("data").getString("password");
                        user.oaid = jsonObject.getJSONObject("data").getString("oaid");
                        user.points = Float.parseFloat(jsonObject.getJSONObject("data").getString("points"));
                        result.user = user;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                lt.countDown();
            }

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {

            }
        });

        try {
            lt.await();
        } catch (InterruptedException ignored) {
        }

        return result;
    }

    /**
     * 阻塞get
     */
    public static InputStream syncGet(String url) {
        final CountDownLatch lt = new CountDownLatch(1);
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().get()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                is = Objects.requireNonNull(response.body()).byteStream();
                lt.countDown();
            }

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
        try {
            lt.await();
        } catch (InterruptedException ignored) {
        }
        return is;
    }

}