package com.gromore.demo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.github.gzuliyujiang.oaid.DeviceIdentifier;
import com.gromore.demo.App;
import com.gromore.demo.R;
import com.gromore.demo.utils.HttpUtils;


public class LoginActivity extends Activity {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        String OAID = DeviceIdentifier.getOAID(this);

        EditText etUname = findViewById(R.id.et_uname);
        EditText etPassword = findViewById(R.id.et_password);
        etUname.setText(sp.getString("uname",""));
        etPassword.setText(sp.getString("password",""));

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(view -> {
            String uname = etUname.getText().toString();
            String password = etPassword.getText().toString();
            editor = sp.edit();
            editor.putString("uname", uname);
            editor.putString("password", password);
            editor.commit();
            //登录验证
            HttpUtils.ResponseBody respBody = login(uname,password,OAID);
            if(respBody.code.equals("0"))
                startActivity(new Intent(this,MainActivity.class));
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("登录失败");
                builder.setMessage(respBody.msg);
                builder.setPositiveButton("确定", (dialog, which) -> {
                    // 执行确认操作
                });
                builder.show();
            }

        });
    }

    private HttpUtils.ResponseBody login(String uname, String password, String oaid) {
        String params = String.format("uname=%s&password=%s&oaid=%s", uname, password, oaid);
        return HttpUtils.sendPost("http://111.231.12.122:8081/user/login", params);
    }


}
