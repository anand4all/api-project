package com.example.gaurav.inventory;


import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;


import com.example.gaurav.inventory.Adapters.Grn_adapter;
import com.example.gaurav.inventory.entities.Grn_item;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Grn extends AppCompatActivity {


    private static String URL_DATA = "http://192.168.0.111:8000/api/support/projects/?format=json";
    AsyncHttpClient client;
    public Context mContext;
    RecyclerView listshowgrn;
    Toolbar toolbar;
    ArrayList<Grn_item> Grnlists;
    Grn_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grn);
        listshowgrn = findViewById(R.id.listshowgrn);
        Grnlists = new ArrayList<>();
        client = new AsyncHttpClient();

        getprojects();

    /*    Grnlist.add(new Grn_item("TATA"));
        Grnlist.add(new Grn_item("Mahindra"));
        Grnlist.add(new Grn_item("TCS"));
        Grnlist.add(new Grn_item("company4"));
        Grnlist.add(new Grn_item("Company5"));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listshowgrn.setHasFixedSize(true);
        listshowgrn.setLayoutManager(linearLayoutManager);
        adapter = new Grn_adapter(Grnlist, Grn.this);
        listshowgrn.setAdapter(adapter); */
    }

    public void getprojects(){

      Toast.makeText(getApplicationContext(),""+URL_DATA,Toast.LENGTH_SHORT).show();

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();


        // client.get( "https://api.myjson.com/bins/j5f6b",new JsonHttpResponseHandler() {
        client.get( "http://192.168.0.111:8000/api/support/projects/?format=json",new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();

                //        Toast.makeText(getApplicationContext(),"res "+response.toString(),Toast.LENGTH_SHORT).show();
                for (int i = 0; i < response.length(); i++) {

                    JSONObject object=null;

                    try {
                        object = response.getJSONObject(i);

                        JSONObject service=object.getJSONObject("service");
                        String name = service.getString("name");
                        String mobile = service.getString("mobile");
                      /*  JSONArray schedule_Array = object.getJSONArray("service");
                        for (int j=0;j<schedule_Array.length();j++)
                        {
                            JSONObject jOBJNEW = schedule_Array.getJSONObject(j);
                            Toast.makeText(getApplicationContext(),"res "+jOBJNEW.toString(),Toast.LENGTH_SHORT).show();
                            Grnlists.add(new Grn_item(jOBJNEW));
                        }*/

                        // Toast.makeText(getApplicationContext(),"res "+object.toString(),Toast.LENGTH_SHORT).show();
                        object.put("name",name);
                        object.put("mobile",mobile);
                        Log.e("errror"," "+object);
                        // Item item = new Item(object);

                        Grnlists.add(new Grn_item(object));
                        //       Toast.makeText(getApplicationContext(),"res "+productlists.toString()+" "+productlists.size(),Toast.LENGTH_SHORT).show();
                        Log.e("error"," "+Grnlists.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
              //  linearLayoutManager.setReverseLayout(true);
              //  linearLayoutManager.setStackFromEnd(true);
                listshowgrn.setHasFixedSize(true);
                listshowgrn.setLayoutManager(linearLayoutManager);
                adapter = new Grn_adapter(Grnlists, Grn.this);
                listshowgrn.setAdapter(adapter);

                // listshowrcy.setAdapter(new search_parts_adapter(productlists));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(),"data not fetching"+errorResponse,Toast.LENGTH_SHORT);
            }
        });

    }

}

