package com.example.gaurav.inventory.startup;

import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaurav.inventory.Backend.Backendserver;
import com.example.gaurav.inventory.Backend.SessionManager;
import com.example.gaurav.inventory.Grn;
import com.example.gaurav.inventory.Login;
import com.example.gaurav.inventory.Materialissue;
import com.example.gaurav.inventory.R;
import com.example.gaurav.inventory.Remove_parts;

import com.example.gaurav.inventory.entities.remove_item;
import com.example.gaurav.inventory.material;
import com.example.gaurav.inventory.searchh_result_parts;
import com.example.gaurav.inventory.Add_parts;
import com.example.gaurav.inventory.stockcheck;
import com.google.zxing.Result;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
  // TextView Addprod,Search,grn,remove,scanbtn,username1 ;
   ImageView Addprod,Search,grn,remove;
   ImageView btn_logout,userimage;
   TextView username1,scanbtn,partno_dialogtxt,description1,price,weight,custom_no;
   Button btnok;
    private ZXingScannerView zXingScannerView;
 //   Usersessionmanager session;
   SessionManager sessionManager;

    AlertDialog.Builder builder;
    LinearLayout deliveryAction;
    Dialog dialog;

    int counts=0;

    private AsyncHttpClient client;
    public static String username = "";
    public static String userPK = "";
    String resultcode,prices="",weights="",customnos="",desc1="",partN0="";
    Context context;
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username1=findViewById(R.id.usernametxt);
        btn_logout=findViewById(R.id.btn_logout);
        userimage=findViewById(R.id.imageView3);
        sessionManager = new SessionManager(getApplicationContext());

        deliveryAction=(LinearLayout)findViewById(R.id.llUsername);
        Backendserver backend = new Backendserver(getApplicationContext());
        //   getUserDetails();
        client = backend.getHTTPClient();
       // dialog = new Dialog(MainActivity.this);
     //   if (Build.VERSION.SDK_INT < 27) {
         //   window=this.getWindow();
         //   window.setStatusBarColor(this.getResources().getColor(R.color.gen_white));
          //  window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

     //   }
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.status));

       /* if (!this.sessionManager.getLoginVerify()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(getApplicationContext(), Login.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            getApplicationContext().startActivity(i);
        }*/
        sessionManager.checklogin();
        Scanqr();


        Intent i = getIntent();
       // String text = i.getStringExtra ( "TextBox","" );
        getUserDetails();

        Log.e("error",""+userPK);
        username1.setText (i.getStringExtra(username));
        builder = new AlertDialog.Builder(this);
        btn_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //Uncomment the below code to Set the message and title from the strings.xml file
                builder.setMessage("jsdjvsd") .setTitle("sjbasjcb");

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to Log out ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                sessionManager.logoutUser();
                                finish();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Log out");
                alert.show();
            }



        });



    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {

        if (exit) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Press again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }

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

         /*       final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);

                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);

                Addprod.startAnimation(myAnim); */




                    Intent i = new Intent(getApplicationContext(), Materialissue.class);
                    startActivity(i);

            }
        });
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             /*   final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);

                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator); */


                    Intent i = new Intent(getApplicationContext(), stockcheck.class);
                    startActivity(i);


            }
        });
        grn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);

                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);  */


                    Intent i = new Intent(getApplicationContext(), Grn.class);
                    startActivity(i);


            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            /*    final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);

                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);

                remove.startAnimation(myAnim); */

                    Intent i = new Intent(getApplicationContext(), Remove_parts.class);
                    startActivity(i);


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
        zXingScannerView =new ZXingScannerView(this);
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

    }

    @Override
    public void handleResult(Result result) {

        resultcode=result.getText();
        Toast.makeText(getApplicationContext(),result.getText(),Toast.LENGTH_SHORT).show();
        if(counts==0){


            getproductbarcode();

        }
        counts++;

        zXingScannerView.resumeCameraPreview(this);

    }

  public void getUserDetails() {

            // btn_logout.setVisibility(View.GONE);
          //  deliveryAction.setVisibility(View.GONE);
          //  btn_logout.setVisibility(View.GONE);

            client.get(Backendserver.url + "/api/HR/users/?mode=mySelf&format=json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.e("MainActivity", "onSuccess");
                    super.onSuccess(statusCode, headers, response);
                   // btn_logout.setVisibility(View.VISIBLE);
                   // deliveryAction.setVisibility(View.VISIBLE);
                  //  username1.setVisibility(View.VISIBLE);

                    try {

                        JSONObject usrObj = response.getJSONObject(0);
                        userPK = usrObj.getString("pk");
                        Log.e("error",""+userPK);
                        username = usrObj.getString("username");
                        Log.e("error",""+username);

                        String firstName = usrObj.getString("first_name");
                        String lastName = usrObj.getString("last_name");
                        //    String email = usrObj.getString("email");
                        JSONObject profileObj = usrObj.getJSONObject("profile");

                        String dpLink = profileObj.getString("displayPicture");
                        if (dpLink.equals("null") || dpLink == null) {
                            dpLink = Backendserver.url + "static/images/userIcon.png";
                        }
                        String mobile = profileObj.getString("mobile");
                        username1.setText(firstName + " " + lastName);
                        Uri uri = Uri.parse(dpLink);
                        Picasso.with(getApplicationContext()).load(uri).resize(90,90).into(userimage);

                        //userImage.setImageURI(uri);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //getCartCount();
                    //Toast.makeText(getApplicationContext(),"cartSize",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.e("MainActivity", "onFailure");
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    Log.e("MainActivity", "onFinish");
                }
            });
        }


    class MyBounceInterpolator implements android.view.animation.Interpolator {
        private double mAmplitude = 1;
        private double mFrequency = 10;

        MyBounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }

    public void getproductbarcode(){

            // Toast.makeText(getApplicationContext(), "Status updated ", Toast.LENGTH_SHORT).show();
            client.get(Backendserver.url + "/api/support/products/?bar_code="+ resultcode, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    //        Toast.makeText(getApplicationContext(),"res "+response.toString(),Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject object1 = null;

                        try {
                            object1 = response.getJSONObject(i);
                            prices=object1.getString("price");
                            weights=object1.getString("weight");
                            customnos=object1.getString("customs_no");
                            desc1=object1.getString("description_1");
                            partN0=object1.getString("part_no");

                            Log.e("error",""+partN0);
                            Log.e("error",""+prices);
                            Log.e("error",""+weights);
                            Log.e("error",""+desc1);
                            Log.e("error",""+customnos);
                            MyCustomAlertDialog2();

                            //  Log.e("error"," "+prodlist.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Toast.makeText(getApplicationContext(), "data not fetching" + errorResponse, Toast.LENGTH_SHORT);
                }
            });

    }


    public void MyCustomAlertDialog2() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.scanbtndialog);
        dialog.setTitle("My Custom Dialog");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        btnok = (Button) dialog.findViewById(R.id.btn_Add_dialog);

        TextView back=dialog.findViewById(R.id.txtback);
        // Cancel = dialog.findViewById(R.id.txt_cancel_dialog);
        partno_dialogtxt = dialog.findViewById(R.id.partno_dialog_project);

        description1 = dialog.findViewById(R.id.description1scan);
        price = dialog.findViewById(R.id.pricescan);
        weight = dialog.findViewById(R.id.weightscan);
        custom_no = dialog.findViewById(R.id.customnoscan);


        partno_dialogtxt.setText(partN0);
        description1.setText(desc1);
        weight.setText("Weight : "+weights);
        custom_no.setText("Custom no : "+customnos);
        price.setText("Price : "+prices);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });
     btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                counts=0;
            dialog.cancel();

            }
        });

        //   Window window = dialog.getWindow();
        //    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
         }

    private boolean connectionChek(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
      }
   }



