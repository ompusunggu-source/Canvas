package com.example.canvasbree;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.canvasbree.helper.PermissionHelper;

import static android.os.SystemClock.sleep;

//package helper;

public class SplashScreen extends AppCompatActivity {

    PermissionHelper permissionHelper;
    Intent intent;
    //private int waktu_splash = 4000;
    //private int waktu_loading = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        permissionHelper = new PermissionHelper(this);

 /*       setContentView(R.layout.splashscreen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //setelah loading maka akan langsung berpindah ke home activity
                Intent home=new Intent(SplashScreen.this, MainActivity.class);
                startActivity(home);
                finish();

            }
        },waktu_loading); **/

        checkAndRequestPermissions();

    }

    private boolean checkAndRequestPermissions() {
        permissionHelper.permissionListener(new PermissionHelper.PermissionListener() {
            @Override
            public void onPermissionCheckDone() {
                intent = new Intent(SplashScreen.this, Signature.class);
                startActivity(intent);
                finish();
            }
        });

        permissionHelper.checkAndRequestPermissions();

        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestCallBack(requestCode, permissions, grantResults);
        sleep(4000);    // taroin dibawah checkAndRequest
    }
}
