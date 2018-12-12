package com.example.gaurav.inventory;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class Add_parts extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView zXingScannerView;
    EditText txtResult;
    ImageView itemsQuantityAdd, itemsQuantityRemove;
    TextView itemquantity,additem;

    int count=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parts);

        itemquantity=(TextView)findViewById(R.id.item_quantity_addpart);
        itemsQuantityAdd=findViewById(R.id.items_quantity_add_addpart);
        itemsQuantityRemove=findViewById(R.id.items_quantity_remove_addpart);
        txtResult=findViewById(R.id.txtResult);
        additem=findViewById(R.id.add_addpart);

        askForPermission(Manifest.permission.CAMERA, CAMERA);
        zXingScannerView = new ZXingScannerView(this);

        zXingScannerView= (ZXingScannerView) findViewById(R.id.zxscan);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

        increasedecrese();
        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext()," added", Toast.LENGTH_SHORT).show();

            }
        });



    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Add_parts.this, permission) != PackageManager.PERMISSION_GRANTED) {

            //asking for permission prompt
            if (ActivityCompat.shouldShowRequestPermissionRationale(Add_parts.this, permission)) {
                //request for permissions and getting request code -- 5 for camera
                ActivityCompat.requestPermissions(Add_parts.this, new String[]{permission}, requestCode);

            } else {
                //requesting permission
                ActivityCompat.requestPermissions(Add_parts.this, new String[]{permission}, requestCode);
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
        //converting the data to json
      final String resultcode =result.toString();
     /*   JSONObject obj = null;
        try {
            obj = new JSONObject(result.getText());
            txtResult.setText(obj.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //setting values to textviews
*/
                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(200);
                    txtResult.setText(resultcode);

                    additem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext()," "+ resultcode.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });

       Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
        zXingScannerView.resumeCameraPreview(this);

    }


    public void increasedecrese()
    {
        itemsQuantityAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                itemquantity.setText(Integer.toString(count));
            }
        });
        itemsQuantityRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                if(count==0)
                {
                    itemquantity.setText(Integer.toString(0));

                }
                itemquantity.setText(Integer.toString(count));
            }
        });

    }




}



