package com.example.gaurav.inventory.Backend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.gaurav.inventory.Login;

public class SessionManager {
    Context context;

    SharedPreferences sp;
    SharedPreferences.Editor spe;
    private String csrfId = "csrftoken";
    private String sessionId = "sessionid";
    private String userName = "username";
    private String loginVerify = "login";
    private String pass1 = "pass";

    public SessionManager(Context context) {
        this.context = context;
        sp = context.getSharedPreferences("registered_status", Context.MODE_PRIVATE);
        spe = sp.edit();
    }

    public String getCsrfId() {
        return sp.getString(csrfId, "");
    }

    public void setCsrfId(String csrf) {
        spe.putString(csrfId, csrf);
        spe.apply();
    }

    public String getSessionId() {
        return sp.getString(sessionId, "");
    }

    public void setSessionId(String session) {
        spe.putString(sessionId, session);
        spe.apply();
    }
    public Boolean getLoginVerify() {
        return sp.getBoolean(loginVerify, false);
    }

    public void setLoginVerify(Boolean session) {
        spe.putBoolean(loginVerify, session);
        spe.apply();
    }

    public String getUsername() {
        return sp.getString(userName, "");
    }

    public void setUsername(String username) {
        spe.putString(userName, username);
        spe.apply();
    }
    public String getPass1() {
        return sp.getString(pass1, "");
    }

    public void setPass1(String username) {
        spe.putString(pass1, username);
        spe.apply();
    }


    public void clearAll(){
        spe = sp.edit();
        spe.clear();
        spe.apply();
    }

    public void logoutUser(){

        // Clearing all user data from Shared Preferences
        spe.clear();
        spe.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(context, Login.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }

    public void checklogin() {
        // Check login status
        if (getUsername().equals("")) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, Login.class);

            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // Staring Login Activity
            context.startActivity(i);

        }

    }
}
