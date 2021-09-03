package com.example.aiagri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    static int SPLASH_SCREEN=5000;

    Animation topAnim , botAnim;
    ImageView imageView;
    TextView logo,slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AIAgri);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        topAnim = AnimationUtils.loadAnimation(this ,R.anim.top_animation);
        botAnim = AnimationUtils.loadAnimation(this ,R.anim.buttom_animation);

        imageView =findViewById(R.id.imageView2);
        logo =findViewById(R.id.textView);
        slogan =findViewById(R.id.textView2);

        imageView.setAnimation(topAnim);
        logo.setAnimation(botAnim);
        slogan.setAnimation(botAnim);

        new Handler().postDelayed((Runnable) () -> {
            Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        },SPLASH_SCREEN);

    }
}