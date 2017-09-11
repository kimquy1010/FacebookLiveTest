package com.example.manhhung.facebooklivetest;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tvStatus = (TextView) findViewById(R.id.tv_status);
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                tvStatus.setText("login susses with token: " + loginResult.getAccessToken().getToken());

                String userId = AccessToken.getCurrentAccessToken().getUserId();
                Bundle bundle = new Bundle();

                bundle.putString("description","Test Upload");
                bundle.putString("title","Some Title");
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/"+userId+"/live_videos",
                        bundle,
                        HttpMethod.POST,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                        /* handle the result */
                                Log.e("amlan", "response" + response.toString());
                            }
                        }
                ).executeAsync();
            }

            @Override
            public void onCancel() {
                tvStatus.setText("login cancelled");

            }

            @Override
            public void onError(FacebookException error) {
                tvStatus.setText("login fail: " + error.getMessage());

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
