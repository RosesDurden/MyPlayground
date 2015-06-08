package com.example.roses.myplayground;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * Created by roses on 20/05/15.
 */
public class SplashScreenActivity extends FragmentActivity{

    Intent connexion, mainActivity;
    SharedPreferences sp;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        final Connexion connexionUser = new Connexion();
        connexion= new Intent(this, Connexion.class);
        mainActivity = new Intent(this, MainActivity.class);
        //display the logo during 2 secondes,
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                sp = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                Log.d("sp : ", sp.toString());
                if(sp != null){
                    startActivity(mainActivity);
                }
                else {
                    //set the new Content of your activity
                    startActivity(connexion);
                }
            }
        }.start();
    }
}
