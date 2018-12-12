package com.example.gaurav.inventory.startup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaurav.inventory.Backend.Usersessionmanager;
import com.example.gaurav.inventory.Grn;
import com.example.gaurav.inventory.Login;
import com.example.gaurav.inventory.R;
import com.example.gaurav.inventory.Remove_parts;
import com.example.gaurav.inventory.searchh_result_parts;
import com.example.gaurav.inventory.Add_parts;
import com.google.zxing.Result;

import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
   TextView Addprod,Search,grn,remove,scanbtn,username ;
   ImageView btn_logout;
    private ZXingScannerView zXingScannerView;
    Usersessionmanager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=findViewById(R.id.usernametxt);
        btn_logout=findViewById(R.id.btn_logout);
        session = new Usersessionmanager(getApplicationContext());

        Scanqr();

        Intent i = getIntent();
       // String text = i.getStringExtra ( "TextBox","" );
        username.setText (i.getStringExtra("TextBox"));



        if(session.checkLogin())
            finish();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // get name
        String name = user.get(Usersessionmanager.KEY_NAME);

        // get email
        String email = user.get(Usersessionmanager.KEY_EMAIL);

        // Show user data on activity
        username.setText(Html.fromHtml("  <b>" + name + "</b>"));
       // lblEmail.setText(Html.fromHtml("Email: <b>" + email + "</b>"));


        btn_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Clear the User session data
                // and redirect user to LoginActivity
                session.logoutUser();
            }
        });
    }




    private void Scanqr(){

        Addprod=findViewById(R.id.Add);
        Search=findViewById(R.id.Search);
        grn=findViewById(R.id.Grn);
        remove=findViewById(R.id.Remove);
        scanbtn=findViewById(R.id.ScanQr_btn);

        Addprod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.equals(""))
                {
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(getApplicationContext(), Add_parts.class);
                    startActivity(i);
                }
            }
        });
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.equals(""))
                {
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(getApplicationContext(), searchh_result_parts.class);
                    startActivity(i);
                }

            }
        });
        grn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.equals(""))
                {
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(getApplicationContext(), Grn.class);
                    startActivity(i);
                }

            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.equals(""))
                {
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(getApplicationContext(), Remove_parts.class);
                    startActivity(i);
                }

            }
        });




    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            //asking for permission prompt

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
                //request for permissions and getting request code -- 5 for camera
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {
                //requesting permission
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }
    //Response for RunTime Permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                //Camera
                case 5:
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 12);
                    }
                    // askForPermission(Manifest.permission.CAMERA,CAMERA);
                    break;

            }

            Toast.makeText(this, "Permission granted",Toast.LENGTH_SHORT).show();
            zXingScannerView =new ZXingScannerView(getApplicationContext());
            setContentView(zXingScannerView);
            zXingScannerView.setResultHandler(this);
            zXingScannerView.startCamera();


        }
        else
        {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }




    public void scan(View view){
        askForPermission(Manifest.permission.CAMERA,CAMERA);
        zXingScannerView =new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

    }

   
    @Override
    public void handleResult(Result result) {
        Toast.makeText(getApplicationContext(),result.getText(),Toast.LENGTH_SHORT).show();
        zXingScannerView.resumeCameraPreview(this);

    }






}
