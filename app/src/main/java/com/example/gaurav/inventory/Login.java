package com.example.gaurav.inventory;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gaurav.inventory.Backend.Backendserver;
import com.example.gaurav.inventory.Backend.SessionManager;
import com.example.gaurav.inventory.Backend.Usersessionmanager;
import com.example.gaurav.inventory.startup.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.cookie.Cookie;

public class Login extends AppCompatActivity {


    EditText user, pass;
    Button login;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextInputLayout tilUser, tilPass;
    LinearLayout llUsername, llPassword;

    //  Boolean savelogin;
   //CheckBox savelogincheckbox;
 // Usersessionmanager session;

    Context context;
    private CookieStore httpCookieStore;
    private AsyncHttpClient client;
    SessionManager sessionManager;
    String csrfId, sessionId;
    public static File file;
    String TAG = "status";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.status));


        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.sign_in_button);
        tilUser = findViewById(R.id.til_user);
        tilPass = findViewById(R.id.til_password);
        llUsername = findViewById(R.id.llUsername);
        llPassword = findViewById(R.id.llPassword);
      //  session = new Usersessionmanager(getApplicationContext());


        httpCookieStore = new PersistentCookieStore(this);
        httpCookieStore.clear();
      //  client = new AsyncHttpClient();
        Backendserver backend = new Backendserver(getApplicationContext());

        client = backend.getHTTPClient();

        client.setCookieStore(httpCookieStore);

        isStoragePermissionGranted();
        sessionManager = new SessionManager(getApplicationContext());

           // sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE);
          //  savelogincheckbox = (CheckBox) findViewById(R.id.CheckBox);
         //  editor = sharedPreferences.edit();

        //   savelogin = sharedPreferences.getBoolean("savelogin", true);
       //  if (savelogin == true) {//    user.setText(sharedPreferences.getString("username", null));
      //      pass.setText(sharedPreferences.getString("password", null));
     //   }
    //  login.setOnClickListener(new View.OnClickListener() {
   //    @Override
  //    public void onClick(View v) {
 //       login();
//    }
//  });


        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                login();

                // Get username, password from EditText
             /*  String username = user.getText().toString();
                String password = pass.getText().toString();

                // Validate if username, password is filled
                if(username.trim().length() > 0 && password.trim().length() > 0){

                    // For testing puspose username, password is checked with static data
                    // username = admin
                    // password = admin

                    if(username.equals("admin") && password.equals("admin")){

                        // Creating user login session
                        // Statically storing name="Android Example"
                        // and email="androidexample84@gmail.com"


                        session.createUserLoginSession(" Admin ", "bamoyk@yahoo.com");

                        // Starting MainActivity
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        // Add new Flag to start new Activity
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                        finish();

                    }else{

                        // username / password doesn't match
                        Toast.makeText(getApplicationContext(), "Username/Password is incorrect", Toast.LENGTH_LONG).show();

                    }
                }else{

                    // user didn't entered username or password
                    Toast.makeText(getApplicationContext(), "Please enter username and password", Toast.LENGTH_LONG).show();

                }*/

            }
        });
        }

    public void register(View view) {
        Intent i = new Intent(getApplicationContext(), Registration.class);
        startActivity(i);
    }
    public void login() {
        Toast.makeText(this, Backendserver.url, Toast.LENGTH_LONG).show();
        String userName = user.getText().toString();
        String pass1 = pass.getText().toString();
        if (userName.isEmpty()){
            tilUser.setErrorEnabled(true);
            tilUser.setError("User name is required.");
            user.requestFocus();
        } else {
            tilUser.setErrorEnabled(false);
            if (pass1.isEmpty()){
                tilPass.setErrorEnabled(true);
                tilPass.setError("Password is required.");
                pass.requestFocus();
            } else {
                tilPass.setErrorEnabled(false);
                csrfId = sessionManager.getCsrfId();
                sessionId = sessionManager.getSessionId();
                if (csrfId.equals("") && sessionId.equals("")) {
                    RequestParams params = new RequestParams();
                    params.put("username", userName);
                    params.put("password", pass1);


                    client.post(Backendserver.url + "/login/?mode=api", params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject c) {
                             Log.e("LoginActivity", "  onSuccess");
                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                             super.onSuccess(statusCode, headers, c);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject c) {
                            super.onFailure(statusCode, headers, e, c);
                            if (statusCode == 401) {
                                Toast.makeText(Login.this, "Ops!Something went wrong", Toast.LENGTH_SHORT).show();
                                Log.e("LoginActivity", "  onFailure");
                            }
                        }

                        @Override
                        public void onFinish() {
                            List<Cookie> lst = httpCookieStore.getCookies();
                            if (lst.isEmpty()) {
                               // Toast.makeText(Login.this, String.format("Error , Empty login activity cookie store"), Toast.LENGTH_SHORT).show();
                                Log.e("LoginActivity", "Empty cookie store");
                            } else {
                                if (lst.size() < 2) {
                                    String msg = String.format("Error while logining, fetal error!");
                                    Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                                    Log.e("LoginActivity", ""+msg);
                                    return;
                                }

                                Cookie csrfCookie = lst.get(0);
                                Cookie sessionCookie = lst.get(1);

                                String csrf_token = csrfCookie.getValue();
                                String session_id = sessionCookie.getValue();
                                sessionManager.setCsrfId(csrf_token);
                                sessionManager.setSessionId(session_id);
                                sessionManager.setUsername(user.getText().toString());

                                startActivity(new Intent(Login.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();



                            /*   File dir = new File(Environment.getExternalStorageDirectory() + "/"+"inventory");
                                Log.e("MyAccountActivity", "" + Environment.getExternalStorageDirectory() + "/"+"inventory");
                                if (dir.exists())
                                    if (dir.isDirectory()) {
                                        String[] children = dir.list();
                                        for (int i = 0; i < children.length; i++) {
                                            new File(dir, children[i]).delete();
                                        }
                                        dir.delete();
                                    }
                                file = new File(Environment.getExternalStorageDirectory()+"/"+"inventory");
                                Log.e("directory",""+file.getAbsolutePath());
                                if (file.mkdir()) {
                                    sessionManager.setCsrfId(csrf_token);
                                    sessionManager.setSessionId(session_id);
                                    sessionManager.setUsername(user.getText().toString());
                                    Toast.makeText(Login.this, "Dir created", Toast.LENGTH_SHORT).show();
                                    String fileContents = "csrf_token " + sessionManager.getCsrfId() + " session_id " + sessionManager.getSessionId();
                                    FileOutputStream outputStream;
                                    try {
                                        String path = file.getAbsolutePath() + "/libre.txt";
                                        outputStream = new FileOutputStream(path);
                                        outputStream.write(fileContents.getBytes());
                                        outputStream.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Log.e("isExternalStorageWritab", "" + getApplication().getFilesDir().getAbsoluteFile().getPath());
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, "Dir not created", Toast.LENGTH_SHORT).show();
                                }*/
                            }
                            Log.e("LoginActivity", "  finished");
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent("com.cioc.libreerp.backendservice");
        sendBroadcast(intent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();
    }



    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(android.Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {
                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CALL_PHONE ,
                        android.Manifest.permission.READ_PHONE_STATE , android.Manifest.permission.PROCESS_OUTGOING_CALLS,
                        Manifest.permission.SEND_SMS}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 1; i < 6; i++) {
            if (requestCode == i){
                if (grantResults.length > 0
                        && grantResults[i-1] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[i-1] + "was " + grantResults[i-1]);
                    //resume tasks needing this permission
                }
                return;
            }
        }
    }
        }



