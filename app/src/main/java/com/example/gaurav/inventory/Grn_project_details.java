package com.example.gaurav.inventory;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaurav.inventory.Adapters.Autocomplete_adapter;
import com.example.gaurav.inventory.Adapters.Grn_adapter;
import com.example.gaurav.inventory.Adapters.Grn_detail_adapter;
import com.example.gaurav.inventory.Adapters.remove_selected_adapter;
import com.example.gaurav.inventory.Adapters.search_parts_adapter;
import com.example.gaurav.inventory.Backend.Backendserver;
import com.example.gaurav.inventory.entities.Grn_item;
import com.example.gaurav.inventory.entities.Grn_item_detail;
import com.example.gaurav.inventory.entities.Item;
import com.example.gaurav.inventory.entities.getbomitem;
import com.example.gaurav.inventory.entities.remove_selected_item;
import com.example.gaurav.inventory.startup.MainActivity;
import com.google.android.gms.vision.barcode.Barcode;
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
   // EditText txtgrn_result;
    ImageView itemsQuantityAddgrn, itemsQuantityRemovegrn;
    TextView itemquantitygrn, savegrn, partno_dialogtxt;
    RecyclerView listshowgrn_detail;
    AsyncHttpClient client;
    ArrayList<Grn_item_detail> Grnlists_detail;
    ArrayList<getbomitem> getbomArrayList;
    public Context mContext;
    Grn_detail_adapter adapter;
    AlertDialog.Builder builder;

    Dialog dialog;
    AutoCompleteTextView edittext;
    Button ok_dialog, btn_remove_selected_ok;
    TextView Cancel, addparttxt;

    int count = 1;
    int count2=0;

    ArrayList<Item> productlists1;
    Autocomplete_adapter adapter1;

    String resultcode, qtyscan;

    String projectpkbom;
    String productpk;
    String productpkbom;
    String projectpk;
    String result,qty;
 //   String result2;
    String bompk;
    String barcode,barcodebom;
    String partno,partnobom;
    String description1,Price;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grn_project_details);



        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.status));


        listshowgrn_detail = findViewById(R.id.listgrn_pro_detail);
       // txtgrn_result = findViewById(R.id.txt_project_serialno);
     //   savegrn = findViewById(R.id.save_grn);
        edittext=findViewById(R.id.autocompleteedit);
        builder = new AlertDialog.Builder(this);

        Backendserver backendServer = new Backendserver(getApplicationContext());
        client = backendServer.getHTTPClient();

        addparttxt = findViewById(R.id.add_parttxt);
        Grnlists_detail = new ArrayList<>();
        getbomArrayList=new ArrayList<>() ;

        askForPermission(Manifest.permission.CAMERA, CAMERA);
        zXingScannerView = new ZXingScannerView(this);

        zXingScannerView = (ZXingScannerView) findViewById(R.id.zxscangrn);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
        productlists1=new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        projectpk = bundle.getString("projectpk");
     //   Toast.makeText(getApplicationContext(),"projectpk "+projectpk,Toast.LENGTH_SHORT).show();
        getproducts();

       // getBOM();
        //getBOMproductpk();

        Toast.makeText(getApplicationContext(), "bom" + Grnlists_detail.size(), Toast.LENGTH_SHORT);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listshowgrn_detail.setHasFixedSize(true);
        listshowgrn_detail.setLayoutManager(linearLayoutManager);
        adapter = new Grn_detail_adapter(Grnlists_detail, Grn_project_details.this);
        //   listshowgrn_detail.setLayoutManager(new GridLayoutManager(this,3));
        listshowgrn_detail.setAdapter(adapter);

        edittext.setThreshold(1);
        edittext.setDropDownHeight(300);
        //edittext.setDropDownVerticalOffset(300);
        adapter1 = new Autocomplete_adapter(getApplicationContext(), R.layout.activity_grn_project_details, R.id.lbl_name, productlists1);

        edittext.setAdapter(adapter1);


        addparttxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edittext.length() == 0) {
                    edittext.setError("Enter Part Number");
                } else {

                    result = edittext.getText().toString();

                    for(int i=0;i<productlists1.size();i++)
                    {
                        partno=productlists1.get(i).getPart_no();
                        productpk=productlists1.get(i).getPk();
                        barcode=productlists1.get(i).getBarcode();
                        description1=productlists1.get(i).getDescription_1();
                        Price=productlists1.get(i).getPrice();
                        // Toast.makeText(getApplicationContext()," "+partnobom,Toast.LENGTH_SHORT).show();

                        if(partno.equals(result))
                        {
                            Log.e("matched",partno+" "+productpk);
                            Toast.makeText(getApplicationContext(),"productpk "+productpk,Toast.LENGTH_SHORT).show();
                            break;
                        }

                    }
                    getbomfilter();

                    if(partno.equals(result)) {
                        MyCustomAlertDialog();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Part no is not matched ",Toast.LENGTH_SHORT).show();
                    }


                }
                zXingScannerView.resumeCameraPreview(Grn_project_details.this);

            }
        });



      /* savegrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   save();
                //   Toast.makeText(getApplicationContext(), "Saved ", Toast.LENGTH_LONG).show();
            }
        }); */
    }

  /*  @Override
    public void onBackPressed() {
      //  builder.setMessage("jsdjvsd") .setTitle("sjbasjcb");

        //Setting message manually and performing action on button click
        builder.setMessage(" Are u sure ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        Intent i = new Intent(getApplication(), MainActivity.class);
                      //  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                     //   getApplicationContext().finish()


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

        alert.show();

    } */

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
          //  Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
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
    public void onResume() {
        super.onResume();  // Always call the superclass method first
         zXingScannerView.setResultHandler(this);
        // Get the Camera instance as the activity achieves full user focus
            zXingScannerView.startCamera(); // Local method to handle camera init
        }
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        // Get the Camera instance as the activity achieves full user focus
        zXingScannerView.startCamera(); // Local method to handle camera init
    }




    @Override
    public void handleResult(Result result) {
        resultcode = result.getText();

        zXingScannerView.resumeCameraPreview(this);
        //  txtResult.setText(resultcode);

        if (count2==0) {

            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(200);
            for (int i = 0; i < productlists1.size(); i++) {
                partno = productlists1.get(i).getPart_no();
                productpk = productlists1.get(i).getPk();
                barcode = productlists1.get(i).getBarcode();
                description1 = productlists1.get(i).getDescription_1();
                Price = productlists1.get(i).getPrice();

                Log.e("barcode", barcode + " " + productpk);

                // Toast.makeText(getApplicationContext()," "+partnobom,Toast.LENGTH_SHORT).show();

                if (barcode.equals(resultcode) || barcode == resultcode) {
                    Log.e("matched", barcode + " " + productpk);
                    Toast.makeText(getApplicationContext(), "productpk " + productpk, Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            zXingScannerView.resumeCameraPreview(this);
            getbomfilter();

            MyCustomAlertDialog1();

            count2++;
            Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
        }

        zXingScannerView.resumeCameraPreview(this);
    }

    public void MyCustomAlertDialog() {
        dialog = new Dialog(Grn_project_details.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogbox_grnproject_details);
        dialog.setTitle("My Custom Dialog");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        ok_dialog = (Button) dialog.findViewById(R.id.btn_Add_dialog);
        Cancel = dialog.findViewById(R.id.txt_cancel_dialog_project);
        partno_dialogtxt = dialog.findViewById(R.id.partno_dialog_project);

        zXingScannerView = new ZXingScannerView(this);

        itemquantitygrn = dialog.findViewById(R.id.item_quantity_dialog);
        itemsQuantityAddgrn = (ImageView) dialog.findViewById(R.id.items_quantity_add_dialog);
        itemsQuantityRemovegrn = (ImageView) dialog.findViewById(R.id.items_quantity_remove_dialog);

        String resultdialog = edittext.getText().toString();

        partno_dialogtxt.setText(resultdialog);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              count2=0;
                dialog.cancel();

            }
        });
        ok_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            count2=0;

                //zXingScannerView.startCamera();
                //Intent intent = new Intent(getApplicationContext(), Grn_project_details.class);
                //startActivity(intent);

              //  String pk = txtgrn_result.getText().toString();
                qty = itemquantitygrn.getText().toString();

               //Toast.makeText(getApplicationContext()," "+productlists1.size(),Toast.LENGTH_SHORT).show();
               // Toast.makeText(getApplicationContext()," "+Grnlists_detail.size(),Toast.LENGTH_SHORT).show();
               // Toast.makeText(getApplicationContext()," "+getbomArrayList.size(),Toast.LENGTH_SHORT).show();

                if(partno.equals(result)&&partno.equals(partnobom)){
                 //   Toast.makeText(getApplicationContext(),"patch ", Toast.LENGTH_SHORT).show();

                       RequestParams params = new RequestParams();
                        params.put("quantity2", qty);
                        client.patch(Backendserver.url + "/api/support/bom/"+bompk+"/", params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                Toast.makeText(getApplicationContext(),"updated "+qty, Toast.LENGTH_SHORT).show();
                                Grn_item_detail grn_item_detail1 = new Grn_item_detail("Part no : "+partno, qty,productpk,"Barcode no : "+barcode,description1,Price);

                                Grnlists_detail.add(grn_item_detail1);
                                adapter.notifyDataSetChanged();
                                productlists1.clear();
                                getproducts();



                                //mContext.startActivity(new Intent(mContext, CartListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));//comment
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Toast.makeText(getApplicationContext(), "failure cart"+ projectpk, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                else if (partno.equals(result)){

                    Toast.makeText(getApplicationContext(),"Product is not added in PO",Toast.LENGTH_SHORT).show();
                }
               /*      else if (partno.equals(result)){

                    RequestParams params = new RequestParams();
                    params.put("products",productpk);
                    params.put("quantity2", qty);
                    params.put("project",projectpk);
                    //params.put("typ", "favourite");
                    params.put("user", MainActivity.userPK);

                    client.post(Backendserver.url + "/api/support/bom/",params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Toast.makeText(getApplicationContext(),"success item added",Toast.LENGTH_SHORT).show();
                            Grn_item_detail grn_item_detail1 = new Grn_item_detail("Part no : "+partno, qty,productpk,"Barcode no : "+barcode,description1,Price);
                            Grnlists_detail.add(grn_item_detail1);
                            adapter.notifyDataSetChanged();
                            productlists1.clear();
                            getproducts();

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(getApplicationContext(),"unsuccess ",Toast.LENGTH_SHORT).show();

                        }
                    });

                } */
                else{

                    Toast.makeText(getApplicationContext(),"Enter valid part no",Toast.LENGTH_SHORT).show();
                }

                count=1;
                dialog.cancel();

                edittext.setText("");

            }
        });

        itemsQuantityAddgrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // count=1;
                count++;
                itemquantitygrn.setText(Integer.toString(count));
            }
        });
        itemsQuantityRemovegrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count<2)
                {

                }
                else{
                  //  count=1;
                    count--;
                }
                itemquantitygrn.setText(Integer.toString(count));
            }
        });
        dialog.show();

        //   Window window = dialog.getWindow();
        //   window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

      public void MyCustomAlertDialog1(){
          dialog = new Dialog(Grn_project_details.this);
          dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
          dialog.setContentView(R.layout.dialogbox_grnproject_details);
          dialog.setTitle("My Custom Dialog");
          dialog.setCanceledOnTouchOutside(false);
          dialog.setCancelable(false);

          ok_dialog = (Button)dialog.findViewById(R.id.btn_Add_dialog);
          Cancel=dialog.findViewById(R.id.txt_cancel_dialog_project);
          partno_dialogtxt=dialog.findViewById(R.id.partno_dialog_project);

          itemquantitygrn=dialog.findViewById(R.id.item_quantity_dialog);
          itemsQuantityAddgrn=(ImageView)dialog.findViewById(R.id.items_quantity_add_dialog);
          itemsQuantityRemovegrn=(ImageView) dialog.findViewById(R.id.items_quantity_remove_dialog);

          // final String result2=resultcode;

          partno_dialogtxt.setText(resultcode);


          Cancel.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  count2=0;
                  dialog.cancel();

                  zXingScannerView = new ZXingScannerView(Grn_project_details.this);
                  zXingScannerView = (ZXingScannerView) findViewById(R.id.zxscangrn);
                  zXingScannerView.setResultHandler(Grn_project_details.this);
                  zXingScannerView.startCamera();

              }
          });
          ok_dialog.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  edittext.setText("");
                  count2=0;
                  dialog.cancel();

                  zXingScannerView = new ZXingScannerView(Grn_project_details.this);
                  zXingScannerView = (ZXingScannerView) findViewById(R.id.zxscangrn);
                  zXingScannerView.setResultHandler(Grn_project_details.this);
                  zXingScannerView.startCamera();

                   qty = itemquantitygrn.getText().toString();


               /*   for(int i=0;i<productlists1.size();i++)
                  {
                      partno=productlists1.get(i).getPart_no();
                      productpk=productlists1.get(i).getPk();
                      // Toast.makeText(getApplicationContext()," "+partnobom,Toast.LENGTH_SHORT).show();

                      if(partno.equals(result2))
                      {
                          Log.e("matched",partno+" "+productpk);
                          Toast.makeText(getApplicationContext(),"productpk "+productpk,Toast.LENGTH_SHORT).show();
                          break;
                      }

                  } */


                  if(barcode.equals(resultcode)&&barcode.equals(barcodebom)){
                      //   Toast.makeText(getApplicationContext(),"patch ", Toast.LENGTH_SHORT).show();
                      RequestParams params = new RequestParams();
                      params.put("quantity2", qty);
                      client.patch(Backendserver.url + "/api/support/bom/"+bompk+"/", params, new AsyncHttpResponseHandler() {
                          @Override
                          public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                              Toast.makeText(getApplicationContext(),"updated "+qty, Toast.LENGTH_SHORT).show();
                              Grn_item_detail grn_item_detail1 = new Grn_item_detail("Part no : "+partno,qty,productpk,"Barcode no : "+resultcode,description1,Price);

                              Grnlists_detail.add(grn_item_detail1);
                              adapter.notifyDataSetChanged();

                              //mContext.startActivity(new Intent(mContext, CartListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));//comment
                          }
                          @Override
                          public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                              Toast.makeText(getApplicationContext(), "failure cart"+ projectpk, Toast.LENGTH_SHORT).show();
                          }
                      });

                  }
                  else if (barcode.equals(result)){

                      Toast.makeText(getApplicationContext(),"Product is not added in PO",Toast.LENGTH_SHORT).show();
                  }
               /*   else if (barcode.equals(resultcode)){

                      RequestParams params = new RequestParams();
                      params.put("products",productpk);
                      params.put("quantity2", qty);
                      params.put("project",projectpk);
                      //params.put("typ", "favourite");
                      params.put("user", MainActivity.userPK);

                      client.post(Backendserver.url + "/api/support/bom/",params, new AsyncHttpResponseHandler() {
                          @Override
                          public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                              Toast.makeText(getApplicationContext(),"success item added",Toast.LENGTH_SHORT).show();
                              Grn_item_detail grn_item_detail2 = new Grn_item_detail("Part no : "+partno, qty,productpk,"Barcode no : "+resultcode,description1,Price);

                              Grnlists_detail.add(grn_item_detail2);
                              adapter.notifyDataSetChanged();
                              zXingScannerView.resumeCameraPreview(Grn_project_details.this);
                              zXingScannerView.startCamera();
                          }

                          @Override
                          public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                              Toast.makeText(getApplicationContext(),"unsuccess ",Toast.LENGTH_SHORT).show();

                          }
                      });

                  } */
                  else{

                      Toast.makeText(getApplicationContext(),"Enter valid part no",Toast.LENGTH_SHORT).show();
                  }

              }
          });

          itemsQuantityAddgrn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(count<2)
                  {


                  }
                  count++;
                  itemquantitygrn.setText(Integer.toString(count));
              }
          });
          itemsQuantityRemovegrn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  if(count<2)
                  {

                  }
                  else{
                      count--;
                  }


                  itemquantitygrn.setText(Integer.toString(count));
              }
          });
          dialog.show();
          //   Window window = dialog.getWindow();
          //    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
      }

    public void getproducts(){
        //Toast.makeText(getApplicationContext(),""+URL_DATA,Toast.LENGTH_SHORT).show();

        // client.get( "https://api.myjson.com/bins/j5f6b",new JsonHttpResponseHandler() {
        client.get( Backendserver.url+"/api/support/products/?format=json",new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                //        Toast.makeText(getApplicationContext(),"res "+response.toString(),Toast.LENGTH_SHORT).show();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject object=null;
                    try {
                        object = response.getJSONObject(i);

                        productlists1.add(new Item(object));
                        //       Toast.makeText(getApplicationContext(),"res "+productlists.toString()+" "+productlists.size(),Toast.LENGTH_SHORT).show();
                    //   Log.e("errror"," "+productlists1.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // listshowrcy.setAdapter(new search_parts_adapter(productlists));

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(),"data not fetching"+errorResponse,Toast.LENGTH_SHORT);
            }
        });

    }

    public void getbomfilter()


    {
        Log.e("error", " kjhh" );
        client.get(Backendserver.url +"api/support/bom/?&products="+productpk+"&user="+MainActivity.userPK+"&project="+projectpk+"/", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                // Toast.makeText(getApplicationContext(), "res " + response.toString(), Toast.LENGTH_LONG).show();
              //  Log.e("error", " kjhh"+response.toString() );
                for (int i = 0; i < response.length(); i++) {

                    JSONObject object=null;

                    try {
                        object = response.getJSONObject(i);
                        bompk = object.getString("pk");
                        JSONObject service=object.getJSONObject("products");
                        productpkbom = service.getString("pk");
                        partnobom = service.getString("part_no");
                        barcodebom = service.getString("bar_code");

                        Toast.makeText(getApplicationContext(), "bom" + bompk, Toast.LENGTH_SHORT);
                        Log.e("error", " " + bompk);
                        Log.e("error", " " + partnobom);
                        Log.e("error", " " + barcodebom);

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



        public void save() {
            for (int i = 0; i < Grnlists_detail.size(); i++)

            {
                int pk = Integer.parseInt(Grnlists_detail.get(i).getPk());
                int qty2 = Integer.parseInt(Grnlists_detail.get(i).getQty());
                String price=Grnlists_detail.get(i).getPrice();
              // String qty2=Grnlists_detail.get(i).getQty();

                RequestParams params = new RequestParams();
                params.put("project", Integer.parseInt(projectpk));
                params.put("addedqty", qty2);
                params.put("product", pk);
                params.put("qty", qty2);
                params.put("rate", price);
                Toast.makeText(getApplicationContext(), "pk" + pk + "qty"+qty2 , Toast.LENGTH_SHORT).show();

                client.post(Backendserver.url + "api/support/inventory/", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(getApplicationContext(), "success item added", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Grn_project_details.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getApplicationContext(), "unsuccess " + statusCode + " " + error, Toast.LENGTH_SHORT).show();
                        Log.e("error", statusCode + " " + error);

                    }
                });

            }
        }


}







