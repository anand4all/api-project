package com.example.gaurav.inventory;

import android.app.AlertDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alespero.expandablecardview.ExpandableCardView;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.gaurav.inventory.Adapters.MaterialissueAdapter;
import com.example.gaurav.inventory.Adapters.materialadapter;
import com.example.gaurav.inventory.Backend.Backendserver;
import com.example.gaurav.inventory.entities.materialitem;
import com.example.gaurav.inventory.model.TitleCreator;
import com.example.gaurav.inventory.model.TitleParent;
import com.example.gaurav.inventory.model.Titlechild;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;

public class material extends AppCompatActivity {
    RecyclerView recyclerView;
  MaterialissueAdapter adapter;
  AsyncHttpClient client;
    String part_no;
    String qty;
    Toolbar toolbar;
  ArrayList<materialitem> matissuelist;
    List<Object> childList;
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((MaterialissueAdapter)recyclerView.getAdapter()).onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);
        toolbar = (Toolbar) findViewById(R.id.toolbarmi);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Material Issue");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.requestFocus();
        Backendserver backend = new Backendserver(getApplicationContext());

        client = backend.getHTTPClient();

        recyclerView = (RecyclerView)findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        matissuelist=new ArrayList();
        getmaterialissue();
        MaterialissueAdapter adapter = new MaterialissueAdapter(this,initData());
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);

        recyclerView.setAdapter(adapter);

    }

    private List<ParentObject> initData() {
        TitleCreator titleCreator = TitleCreator.get(this);
        List<TitleParent> titles = titleCreator.getAll();
        List<ParentObject> parentObject = new ArrayList<>();
        for(TitleParent title:titles)
        {
           childList = new ArrayList<>();

            childList.add(new Titlechild(part_no,qty));
            title.setChildObjectList(childList);
            parentObject.add(title);
        }
        return parentObject;

    }

    public void getmaterialissue(){


        //  final ProgressDialog progressDialog=new ProgressDialog(this);
        //   progressDialog.setMessage("Loading..");
        // progressDialog.show();

        final AlertDialog alertDialog= new SpotsDialog.Builder().setContext(this).build();
        alertDialog.show();
        // client.get( "https://api.myjson.com/bins/j5f6b",new JsonHttpResponseHandler() {
        client.get( Backendserver.url+ "/api/support/material/",new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                alertDialog.dismiss();

                for (int i = 0; i < response.length(); i++) {

                    JSONObject object=null;

                    try {
                        object = response.getJSONObject(i);
                        JSONObject project=object.getJSONObject("project");
                        String title = project.getString("title");
                        object.put("title",title);
                        JSONObject user=object.getJSONObject("user");
                        String user1 = user.getString("first_name");
                        object.put("user",user1);

                        JSONArray schedule_Array = object.getJSONArray("materialIssue");
                        for (int j=0;j<schedule_Array.length();j++)
                        {
                            JSONObject jOBJNEW = schedule_Array.getJSONObject(j);

                             qty=jOBJNEW.getString("qty");
                            String price=jOBJNEW.getString("price");

                            object.put("qty",qty);
                            object.put("price",price);

                            JSONObject product = jOBJNEW.getJSONObject("product");
                            part_no=product.getString("part_no");
                            object.put("part_no",part_no);


                          //Toast.makeText(getApplicationContext(),"res "+jOBJNEW.toString(),Toast.LENGTH_SHORT).show();

                        }
                        String dateofissue = project.getString("created");
                        object.put("date",dateofissue);
                        String comm_nr = project.getString("comm_nr");
                        object.put("comm_nr",comm_nr);

                        // Toast.makeText(getApplicationContext(),"res "+object.toString(),Toast.LENGTH_SHORT).show();
                        // object.put("name",name);
                        //object.put("mobile",mobile);
                        Log.e("errror"," "+object);
                        // Item item = new Item(object);

                     //   materiallist.add(new materialitem(object));
                        //       Toast.makeText(getApplicationContext(),"res "+productlists.toString()+" "+productlists.size(),Toast.LENGTH_SHORT).show();
                        //   Log.e("error"," "+Grnlists.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }




            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(),"data not fetching"+errorResponse,Toast.LENGTH_SHORT);
            }
        });

    }

}

