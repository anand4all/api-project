package com.example.gaurav.inventory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaurav.inventory.Adapters.remove_selected_adapter;
import com.example.gaurav.inventory.Backend.Backendserver;
import com.example.gaurav.inventory.common.common;
import com.example.gaurav.inventory.entities.Grn_item;
import com.example.gaurav.inventory.entities.remove_item;
import com.example.gaurav.inventory.startup.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;

public class remove_selected_parts extends AppCompatActivity{
    RecyclerView rvs_partsshow;
    Toolbar toolbar;
    public Context mContext;
    ArrayList<Grn_item> Grnlists;

    ArrayList<remove_item> myList;

    remove_selected_adapter adapter;

    ArrayList array1;

    Dialog dialog;
    Button ok_dialog,btn_remove_selected_ok;
    TextView Cancel;
    AsyncHttpClient client;
    Remove_parts remove_parts;
    String productpk,qty;
    public int projectpk;

    float weight1=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_selected_parts);
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.status));

        rvs_partsshow=findViewById(R.id.rvs_partsshow);
        btn_remove_selected_ok=findViewById(R.id.ok_btn_remove_selected);
        Backendserver backendServer = new Backendserver(getApplicationContext());
        client = backendServer.getHTTPClient();
        remove_parts=new Remove_parts();
        myList=new ArrayList<>();

        Grnlists = new ArrayList<>();
        array1 = new ArrayList();

       // adapter=new remove_selected_adapter(Grnlists,this);

        myList = getIntent().getParcelableArrayListExtra("mylist");
        Log.e("errror"," "+myList);
        getprojects();



     /* Grnlist.add(new remove_selected_item("TATA"));
        Grnlist.add(new remove_selected_item("Mahindra"));
        Grnlist.add(new remove_selected_item("TCS"));
        Grnlist.add(new remove_selected_item("company4"));
        Grnlist.add(new remove_selected_item("Company5"));

      //  LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvs_partsshow.setHasFixedSize(true);
     //   rvs_partsshow.setLayoutManager(linearLayoutManager);
        adapter = new remove_selected_adapter(Grnlist, remove_selected_parts.this);
        rvs_partsshow.setLayoutManager(new GridLayoutManager(this,3));
        rvs_partsshow.setAdapter(adapter);

        setToogleEvent(rvs_partsshow); */
        ok_btn();

    }


    private void ok_btn() {
        btn_remove_selected_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                projectpk = Integer.parseInt(common.currentitem.getPk());
                Toast.makeText(getApplicationContext(), "pk" + projectpk, Toast.LENGTH_SHORT).show();

                //  MyCustomAlertDialog();
                if (projectpk == 0) {
                    Toast.makeText(getApplicationContext(), "please select project", Toast.LENGTH_SHORT).show();
                }


            /*    try {
                JSONObject product = new JSONObject();


                    product.put("bar_code", null);
                    product.put("pk", 1);
                    product.put("customs_no", "84669430");
                    product.put("description_1", "SUPPORT");
                    product.put("description_2", null);
                    product.put("parent", null);
                    product.put("part_no", "20735.00.0.00");
                    product.put("price", 976.5);
                    product.put("prodQty", 1);
                    product.put("sheet", null);
                    product.put("weight", 30.5);

                    array.add(product);
                    Log.e("error", " " + array);

                } catch (JSONException e) {
                    e.printStackTrace();
                } */
                //product.put("created", "2018-12-05T11:17:29.146967Z");


                 JSONArray array=new JSONArray();
                  JSONObject product=null;
                try {
                   for (int i =0 ; i < myList.size(); i++) {
                      //  remove_item cart = myList.get(i);
                     //    weight1=Float.parseFloat(myList.get(i).getWeight());
                        product = new JSONObject();
                        product.put("pk", Integer.parseInt(myList.get(i).getProductpk()));
                        product.put("bar_code", myList.get(i).getBarcode());
                        product.put("customs_no", myList.get(i).getCustom_no());
                        product.put("description_1", myList.get(i).getDescription1());
                        product.put("description_2", myList.get(i).getDescription1());
                        product.put("parent", null);
                        product.put("part_no", myList.get(i).getPartno());
                        product.put("price",Float.parseFloat( myList.get(i).getPrice()));
                        product.put("prodQty", Integer.parseInt(myList.get(i).getQty()));
                        product.put("sheet", null);
                         if(myList.get(i).getWeight().equals("null")) {
                             product.put("weight",0.0);
                        }
                        else{
                             product.put("weight",Double.parseDouble(myList.get(i).getWeight()));
                        }

                        array.put(product);
                   }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            /*    try {
                  //  for (int i = 0; i < 1; i++) {
                        JSONObject product = new JSONObject();

                        //product.put("part_no", Integer.parseInt(myList.get(i).getProductpk()));
                        //product.put("description_1", Integer.parseInt(myList.get(i).getProductpk()));
                        //product.put("prodSku", cart.getProdSku());
                        //product.put("created", "2018-12-05T11:17:29.146967Z");
                        product.put("pk", "1");
                      //  product.put("bar_code", null);
                        product.put("customs_no", "84669430");
                        product.put("description_1", "SUPPORT");
                     //   product.put("description_2", null);
                      //  product.put("parent", null);
                        product.put("part_no", "20735.00.0.00");
                        product.put("price", "976.5");
                        product.put("prodQty", "1");
                  //      product.put("sheet", null);
                        product.put("weight", "30.5");

                        array1.add(product);
                   // }
                } catch (JSONException e) {
                    e.printStackTrace();
                } */
                //Log.e("error", " " + array1);
               // Log.e("error", " " + projectpk);
               // Log.e("error", " " + Integer.parseInt(MainActivity.userPK));



                RequestParams params = new RequestParams();

                params.put("products", array);
                params.put("project", projectpk);
                params.put("user", Integer.parseInt(MainActivity.userPK));
                //params.put("prodListQty",3);
                Log.e("error", " " + params);
             client.post(Backendserver.url + "api/support/order/", params, new AsyncHttpResponseHandler() {
                   @Override
                      public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                       try {
                           String str = new String(responseBody, "UTF-8");
                           Log.e("sucess","sucees"+" "+str);
                           Toast.makeText(getApplicationContext(),"order successful ",Toast.LENGTH_SHORT).show();
                           projectpk=0;
                           Intent i = new Intent(getApplicationContext(), MainActivity.class);
                           i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                           // Add new Flag to start new Activity
                           i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

                           startActivity(i);
                           finish();

                       } catch (UnsupportedEncodingException e) {
                           e.printStackTrace();
                       }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.e("error", " " + error+statusCode+responseBody);

                    }

                });

               /* client.post(Backendserver.url + "api/support/order/", params,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject c) {
                        super.onSuccess(statusCode, headers, c);
                        Log.e("LoginActivity", "  onSuccess"+c);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject c) {
                        super.onFailure(statusCode, headers, e, c);
                        if (statusCode == 401) {
                            Toast.makeText(remove_selected_parts.this, "un success", Toast.LENGTH_SHORT).show();
                            Log.e("LoginActivity", "  onFailure");
                        }
                    }

                }); */

                }


        });

    }


    public void getprojects(){

     //   final ProgressDialog progressDialog=new ProgressDialog(this);
      //  progressDialog.setMessage("Loading..");
     //   progressDialog.show();
        final AlertDialog alertDialog= new SpotsDialog.Builder().setContext(this).build();
        alertDialog.show();

        client.get( Backendserver.url+"/api/support/projects/?format=json",new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                alertDialog.dismiss();
                //        Toast.makeText(getApplicationContext(),"res "+response.toString(),Toast.LENGTH_SHORT).show();
                for (int i = 0; i < response.length(); i++) {

                    JSONObject object=null;
                    try {
                        object = response.getJSONObject(i);
                        JSONObject service=object.getJSONObject("service");
                        String name = service.getString("name");
                        String mobile = service.getString("mobile");

                        object.put("name",name);
                        object.put("mobile",mobile);
                        Log.e("errror"," "+object);
                        // Item item = new Item(object);

                        Grnlists.add(new Grn_item(object));
                        Log.e("error"," "+Grnlists.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //  LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                //  linearLayoutManager.setReverseLayout(true);
                //  linearLayoutManager.setStackFromEnd(true);
                adapter = new remove_selected_adapter(Grnlists, remove_selected_parts.this);
                rvs_partsshow.setHasFixedSize(true);
                rvs_partsshow.setLayoutManager(new GridLayoutManager(mContext,3));
                rvs_partsshow.setAdapter(adapter);
                //listshowrcy.setAdapter(new search_parts_adapter(productlists));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(),"data not fetching"+errorResponse,Toast.LENGTH_SHORT);
            }
        });

    }
}