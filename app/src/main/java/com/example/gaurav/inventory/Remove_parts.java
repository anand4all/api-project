package com.example.gaurav.inventory;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class Remove_parts extends AppCompatActivity implements ZXingScannerView.ResultHandler {



    private ZXingScannerView zXingScannerView;
    EditText search_bar_remove;
    Button btn_remove_ok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_parts);


        search_bar_remove = findViewById(R.id.search_bar_remove);
        btn_remove_ok = findViewById(R.id.ok_btn_remove);

        askForPermission(Manifest.permission.CAMERA, CAMERA);
        zXingScannerView = new ZXingScannerView(this);

        zXingScannerView= (ZXingScannerView) findViewById(R.id.zxscan_remove);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

        ok_btn();


    }


    private void ok_btn() {
        btn_remove_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), remove_selected_parts.class);
                startActivity(i);

            }
        });
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Remove_parts.this, permission) != PackageManager.PERMISSION_GRANTED) {

            //asking for permission prompt

            if (ActivityCompat.shouldShowRequestPermissionRationale(Remove_parts.this, permission)) {
                //request for permissions and getting request code -- 5 for camera
                ActivityCompat.requestPermissions(Remove_parts.this, new String[]{permission}, requestCode);

            } else {
                //requesting permission
                ActivityCompat.requestPermissions(Remove_parts.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    //Response for RunTime Permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
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

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            zXingScannerView = new ZXingScannerView(this);
            zXingScannerView = (ZXingScannerView) findViewById(R.id.zxscan);
            zXingScannerView.setResultHandler(this);
            zXingScannerView.startCamera();


        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void handleResult(Result result) {
        //   String resultcode=result.getText();


        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        //  txtResult.setText(resultcode);


        Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
        zXingScannerView.resumeCameraPreview(this);

    }


}