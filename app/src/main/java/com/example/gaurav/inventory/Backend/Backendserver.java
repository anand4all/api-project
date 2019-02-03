package com.example.gaurav.inventory.Backend;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;

public class Backendserver
{
   //   public static String url = "https://bruderer.cioc.in/";
   // public static String url = "https://sterlingselect.in/";
    public static String url = "http://192.168.0.17:9000/";
   // public static  String url = "https://demo.bnistore.in";
      public Context context;
      SessionManager sessionManager;
      AsyncHttpClient client;

    public Backendserver(Context context){
        this.context = context;
    }

    public AsyncHttpClient getHTTPClient(){
        sessionManager = new SessionManager(this.context);
        final String csrftoken = sessionManager.getCsrfId();
        final String sessionid = sessionManager.getSessionId();
        //commented

       // client = new AsyncHttpClient(true, 80,443);
        client  = new AsyncHttpClient();
        client.addHeader("Referer",url);

        if (sessionid.length()>csrftoken.length()) {
            client.addHeader("X-CSRFToken" , sessionid);
            client.addHeader("COOKIE", String.format("csrftoken=%s; sessionid=%s", sessionid, csrftoken));
        } else {
            client.addHeader("X-CSRFToken" , csrftoken);
            client.addHeader("COOKIE", String.format("csrftoken=%s; sessionid=%s", csrftoken, sessionid));
        }
        return client;
    }
}
