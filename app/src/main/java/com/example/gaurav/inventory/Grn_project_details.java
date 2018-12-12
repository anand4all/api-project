package com.example.gaurav.inventory;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaurav.inventory.Adapters.Grn_adapter;
import com.example.gaurav.inventory.Adapters.Grn_detail_adapter;
import com.example.gaurav.inventory.Adapters.remove_selected_adapter;
import com.example.gaurav.inventory.Backend.Backendserver;
import com.example.gaurav.inventory.entities.Grn_item;
import com.example.gaurav.inventory.entities.Grn_item_detail;
import com.example.gaurav.inventory.entities.remove_selected_item;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import cz.msebera.android.httpclient.*;

import cz.msebera.android.httpclient.Header;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class Grn_project_details extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static Grn_project_details mActivity;

    private ZXingScannerView zXingScannerView;
    EditText txtgrn_result;
    ImageView itemsQuantityAddgrn, itemsQuantityRemovegrn;
    TextView itemquantitygrn, savegrn, partno_dialogtxt;
    RecyclerView listshowgrn_detail;
    AsyncHttpClient client;
    ArrayList<Grn_item_detail> Grnlists_detail;
    public Context mContext;
    Grn_detail_adapter adapter;

    Dialog dialog;

    Button ok_dialog, btn_remove_selected_ok;
    TextView Cancel, addparttxt;

    int count = 1;

    String resultcode, qtyscan;

    String projectpkbom;
    String productpk;
    String projectpk;

    searchh_result_parts searchh_result_parts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grn_project_details);
        listshowgrn_detail = findViewById(R.id.listgrn_pro_detail);
        txtgrn_result = findViewById(R.id.txt_project_serialno);
        savegrn = findViewById(R.id.save_grn);

        Backendserver backendServer = new Backendserver(mActivity);
        client = backendServer.getHTTPClient();

        addparttxt = findViewById(R.id.add_parttxt);
        Grnlists_detail = new ArrayList<>();

        searchh_result_parts=new searchh_result_parts();

        askForPermission(Manifest.permission.CAMERA, CAMERA);
        zXingScannerView = new ZXingScannerView(this);

        zXingScannerView = (ZXingScannerView) findViewById(R.id.zxscangrn);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
        //  savebtn();
        //   getprojects();
      //  getBOM();
        Addbom();
        //  Grnlists_detail.add(new Grn_item_detail("company4","0"));
        // Grnlists_detail.add(new Grn_item_detail("Company5","0"));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listshowgrn_detail.setHasFixedSize(true);
        listshowgrn_detail.setLayoutManager(linearLayoutManager);
        adapter = new Grn_detail_adapter(Grnlists_detail, Grn_project_details.this);
        //   listshowgrn_detail.setLayoutManager(new GridLayoutManager(this,3));
        listshowgrn_detail.setAdapter(adapter);

        savegrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Saved ", Toast.LENGTH_LONG).show();

            }
        });

        addparttxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtgrn_result.length() == 0) {
                    txtgrn_result.setError("Enter Part Number");
                } else {
                    MyCustomAlertDialog();
                }
        /*  if(txtgrn_result.length()==0)
            {

                txtgrn_result.setError("Enter Part Number");

            }
            else {

                String result = txtgrn_result.getText().toString();

                Grn_item_detail grn_item_detail = new Grn_item_detail(result);

                Grnlists_detail.add(grn_item_detail);
                adapter.notifyDataSetChanged();
            } */
            }
        });

        Bundle bundle = getIntent().getExtras();
        projectpk = bundle.getString("projectpk");
        Toast.makeText(getApplicationContext(),"projectpk "+projectpk,Toast.LENGTH_SHORT).show();


    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Grn_project_details.this, permission) != PackageManager.PERMISSION_GRANTED) {

            //asking for permission prompt
            if (ActivityCompat.shouldShowRequestPermissionRationale(Grn_project_details.this, permission)) {
                //request for permissions and getting request code -- 5 for camera
                ActivityCompat.requestPermissions(Grn_project_details.this, new String[]{permission}, requestCode);

            } else {
                //requesting permission
                ActivityCompat.requestPermissions(Grn_project_details.this, new String[]{permission}, requestCode);
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
            zXingScannerView = (ZXingScannerView) findViewById(R.id.zxscangrn);
            zXingScannerView.setResultHandler(this);
            zXingScannerView.startCamera();


        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void handleResult(Result result) {
        resultcode = result.getText();


        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        //  txtResult.setText(resultcode);
        MyCustomAlertDialog1();

        Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
        zXingScannerView.resumeCameraPreview(this);

    }

    public void MyCustomAlertDialog() {
        dialog = new Dialog(Grn_project_details.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogbox_grnproject_details);
        dialog.setTitle("My Custom Dialog");


        ok_dialog = (Button) dialog.findViewById(R.id.btn_Add_dialog);
        Cancel = dialog.findViewById(R.id.txt_cancel_dialog_project);
        partno_dialogtxt = dialog.findViewById(R.id.partno_dialog_project);

        zXingScannerView = new ZXingScannerView(this);

        itemquantitygrn = dialog.findViewById(R.id.item_quantity_dialog);
        itemsQuantityAddgrn = (ImageView) dialog.findViewById(R.id.items_quantity_add_dialog);
        itemsQuantityRemovegrn = (ImageView) dialog.findViewById(R.id.items_quantity_remove_dialog);

        String resultdialog = txtgrn_result.getText().toString();

        //    String result=resultcode;

        partno_dialogtxt.setText(resultdialog);
        //    partno_dialogtxt.setText(result);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();

            }
        });
        ok_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //zXingScannerView.startCamera();
                //Intent intent = new Intent(getApplicationContext(), Grn_project_details.class);
                //startActivity(intent);
                dialog.cancel();
                // if(resultcode==""||resultcode==null)
                //   {
                //      dialog.cancel();
                //  }
                //   else {

                //     String qty1 = itemquantitygrn.getText().toString();
                // Toast.makeText(getApplicationContext(), "Item Added", Toast.LENGTH_LONG).show();
                //    Grn_item_detail grn_item_detail = new Grn_item_detail(resultcode,qty1);
                //   Grnlists_detail.add(grn_item_detail);
                //   adapter.notifyDataSetChanged();
                //    }

                String result = txtgrn_result.getText().toString();
             //   String pk = txtgrn_result.getText().toString();

                String qty = itemquantitygrn.getText().toString();

              //  Grn_item_detail grn_item_detail1 = new Grn_item_detail(result, qty);

              //  Grnlists_detail.add(grn_item_detail1);
                adapter.notifyDataSetChanged();

            }
        });

        itemsQuantityAddgrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                itemquantitygrn.setText(Integer.toString(count));
            }
        });
        itemsQuantityRemovegrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                itemquantitygrn.setText(Integer.toString(count));
            }
        });
        dialog.show();
        //   Window window = dialog.getWindow();
        //    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

   /* public void save(){

        String textgrnresult_str=txtgrn_result.getText().toString().trim();

        RequestParams params = new RequestParams();

        //params.put("pk", );
        params.put("title", textgrnresult_str);

        // client.get( "https://api.myjson.com/bins/j5f6b",new JsonHttpResponseHandler() {
        client.post( "http://192.168.0.108:8000/api/support/bom/?format=json",params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                //        Toast.makeText(getApplicationContext(),"res "+response.toString(),Toast.LENGTH_SHORT).show();

                try {
                    String title = response.getString(Integer.parseInt("title"));

                   Toast.makeText(getApplicationContext(),"res "+title,Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(),"data not fetching"+errorResponse,Toast.LENGTH_SHORT);
            }
        });

    }

    public void savebtn()
    {
        savegrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Grn_project_details grn=new Grn_project_details();

               grn.save();
            }
        });
    }

*/

    public void getBOM()

    {
            client.get(Backendserver.url +"/api/support/bom/", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                   // Toast.makeText(getApplicationContext(), "res " + response.toString(), Toast.LENGTH_LONG).show();
                    Log.e("error", " " + response.toString());

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject object=null;

                        try {
                            object = response.getJSONObject(i);

                            JSONObject product=object.getJSONObject("products");
                            productpk=product.getString("pk");
                         //Toast.makeText(getApplicationContext(),"productpk "+productpk,Toast.LENGTH_SHORT).show();

                            JSONArray service=object.getJSONArray("project");
                            for (int j=0;j<service.length();j++)
                            {
                                    JSONObject jOBJNEW = service.getJSONObject(j);

                                    projectpkbom = jOBJNEW.getString("pk");

                                    if(projectpkbom.equals(projectpk))
                                    {
                                        Addbom();

                                        break;

                                    }

                             //    Toast.makeText(getApplicationContext(),"projectpk "+projectpkbom,Toast.LENGTH_SHORT).show();

                              //  Grnlists.add(new Grn_item(jOBJNEW));
                            }



                            //String pk = service.getString("pk");
                           //Toast.makeText(getApplicationContext(),"res "+pk.toString(),Toast.LENGTH_SHORT).show();
                          //  String mobile = service.getString("mobile");
                         /*JSONArray schedule_Array = object.getJSONArray("service");
                        for (int j=0;j<schedule_Array.length();j++)
                        {
                            JSONObject jOBJNEW = schedule_Array.getJSONObject(j);
                            Toast.makeText(getApplicationContext(),"res "+jOBJNEW.toString(),Toast.LENGTH_SHORT).show();
                            Grnlists.add(new Grn_item(jOBJNEW));
                        }*/

                            // Toast.makeText(getApplicationContext(),"res "+object.toString(),Toast.LENGTH_SHORT).show();
                            //object.put("pk",pk);
                           // object.put("mobile",mobile);
                            Log.e("errror"," "+object);
                            // Item item = new Item(object);

                            Grnlists_detail.add(new Grn_item_detail(object));
                                //  Toast.makeText(getApplicationContext(),"res "+object.toString(),Toast.LENGTH_SHORT).show();
                            Log.e("error"," "+Grnlists_detail.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                    //  linearLayoutManager.setReverseLayout(true);
                    //  linearLayoutManager.setStackFromEnd(true);
                    listshowgrn_detail.setHasFixedSize(true);
                    listshowgrn_detail.setLayoutManager(linearLayoutManager);
                    adapter = new Grn_detail_adapter(Grnlists_detail, Grn_project_details.this);
                    listshowgrn_detail.setAdapter(adapter);

                    // listshowrcy.setAdapter(new search_parts_adapter(productlists));
                    Toast.makeText(getApplicationContext(),"projectpkbom  "+projectpkbom,Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Toast.makeText(getApplicationContext(), "data not fetching" + errorResponse, Toast.LENGTH_SHORT);
                }
            });

}

public void Addbom() {

    RequestParams params = new RequestParams();
    params.put("products","5");
    params.put("quantity2", "2");
    params.put("project","1");
   // params.put("typ", "favourite");
    params.put("user", "1");

        client.post(Backendserver.url + "/api/support/bom/",params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getApplicationContext(),"success ",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


}

      public void MyCustomAlertDialog1(){
          dialog = new Dialog(Grn_project_details.this);
          dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
          dialog.setContentView(R.layout.dialogbox_grnproject_details);
          dialog.setTitle("My Custom Dialog");


          ok_dialog = (Button)dialog.findViewById(R.id.btn_Add_dialog);
          Cancel=dialog.findViewById(R.id.txt_cancel_dialog_project);
          partno_dialogtxt=dialog.findViewById(R.id.partno_dialog_project);

          itemquantitygrn=dialog.findViewById(R.id.item_quantity_dialog);
          itemsQuantityAddgrn=(ImageView)dialog.findViewById(R.id.items_quantity_add_dialog);
          itemsQuantityRemovegrn=(ImageView) dialog.findViewById(R.id.items_quantity_remove_dialog);

          String result=resultcode;

          partno_dialogtxt.setText(result);


          Cancel.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  dialog.cancel();

              }
          });
          ok_dialog.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {


                  dialog.cancel();

                 // String qty1 = itemquantitygrn.getText().toString();
                // Toast.makeText(getApplicationContext(), "Item Added", Toast.LENGTH_LONG).show();
               //  Grn_item_detail grn_item_detail = new Grn_item_detail(resultcode,qty1);
              //  Grnlists_detail.add(grn_item_detail);
                  adapter.notifyDataSetChanged();


              }
          });

          itemsQuantityAddgrn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  count++;
                  itemquantitygrn.setText(Integer.toString(count));
              }
          });
          itemsQuantityRemovegrn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  count--;
                  itemquantitygrn.setText(Integer.toString(count));
              }
          });
          dialog.show();
          //   Window window = dialog.getWindow();
          //    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
      }


}






