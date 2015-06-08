package com.example.roses.myplayground;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import java.util.Arrays;

/**
 * Created by roses on 11/02/15.
 */
public class Connexion extends ActionBarActivity {

    // Create, automatically open (if applicable), save, and restore the
    // Active Session in a way that is similar to Android UI lifecycles.
    private UiLifecycleHelper uiHelper;
    private static final String TAG = "MainActivity";
    private Button auth;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connexion);
        // Set View that should be visible after log-in invisible initially
        //otherView = (View) findViewById(R.id.name);
        //otherView.setVisibility(View.VISIBLE);
        // To maintain FB Login session
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        sp = this.getSharedPreferences("nom_preference", Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    // Called when session changes
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    // When session is changed, this method is called from callback method
    private void onSessionStateChange(Session session, SessionState state,
                                      Exception exception) {
        final TextView name = (TextView) findViewById(R.id.name);
        // When Session is successfully opened (User logged-in)
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
            // make request to the /me API to get Graph user
            Request.newMeRequest(session, new Request.GraphUserCallback() {

                // callback after Graph API response with user
                // object
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        //Set visibility
                        name.setVisibility(View.VISIBLE);
                        // Set User name
                        name.setTextColor(Color.WHITE);
                        name.setText("Bienvenue  " + user.getName());

                        editor.putString("login", user.getName());
                        editor.commit();
                    }
                }
            }).executeAsync();
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uiHelper.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "OnActivityResult...");
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
}

/**private MainFragment mainFragment;

    public String ID_USER = "Invité";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            mainFragment = new MainFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, mainFragment)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            mainFragment = (MainFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.commit();
    }

}**/