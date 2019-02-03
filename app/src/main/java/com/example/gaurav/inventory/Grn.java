package com.example.gaurav.inventory;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.gaurav.inventory.Adapters.Grn_adapter;
import com.example.gaurav.inventory.Backend.Backendserver;
import com.example.gaurav.inventory.entities.Grn_item;
import com.example.gaurav.inventory.entities.Item;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;

public class Grn extends AppCompatActivity {


  //  private static String URL_DATA = "http://192.168.0.111:8000/api/support/projects/?format=json";
    AsyncHttpClient client;
    public Context mContext;
    RecyclerView listshowgrn;
    Toolbar toolbar;
    ArrayList<Grn_item> Grnlists;
    Grn_adapter adapter;
    Toolbar toolbargrn;
    ImageView imageView,nodata;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grn);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.status));
        imageView=findViewById(R.id.noresult_img);
        nodata=findViewById(R.id.nodata);
        //search_partsadapter=new search_partsadapter(productlists);
        // client.addHeader("referer",url);
        imageView.setVisibility(View.GONE);
        toolbargrn = (Toolbar) findViewById(R.id.toolbargrn);
        setSupportActionBar(toolbargrn);
        getSupportActionBar().setTitle("Search Project");
        toolbargrn.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbargrn.requestFocus();

        listshowgrn = findViewById(R.id.listshowgrn);
        Grnlists = new ArrayList<>();
      //  client = new AsyncHttpClient();
        Backendserver backend = new Backendserver(getApplicationContext());
        //   getUserDetails();
        client = backend.getHTTPClient();

        nodata.setVisibility(View.GONE);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) myActionMenuItem.getActionView();
        changeSearchViewTextColor(searchView);
        ((EditText) searchView.findViewById(
                android.support.v7.appcompat.R.id.search_src_text)).
                setHintTextColor(getResources().getColor(R.color.gen_white));
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Grn_item> filtermodelist =filter(Grnlists,newText);
                //  if(filtermodelist.equals(""))
                // {
                //  imageView.setVisibility(View.VISIBLE);

                //   }
                //  else {
                //  imageView.setVisibility(View.GONE);
                adapter.setfilter(filtermodelist);
                ///   }
                return true;
            }
        });
        return true;
    }


    private ArrayList<Grn_item> filter(ArrayList<Grn_item> pl, String query) {
        query = query.toLowerCase();
        final ArrayList<Grn_item> filteredModeList = new ArrayList<>();
        for (Grn_item model : pl) {
            final String text = model.getTitle().toLowerCase();

            if (text.startsWith(query)) {
                filteredModeList.add(model);
              imageView.setVisibility(View.GONE);

            }

        }

        if (filteredModeList.size()==0)
        {
           imageView.setVisibility(View.VISIBLE);

        }

        return filteredModeList;
    }

    //for changing the text color of searchview
    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.WHITE);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }

    public void getprojects(){

   //   Toast.makeText(getApplicationContext(),""+URL_DATA,Toast.LENGTH_SHORT).show();

      //  final ProgressDialog progressDialog=new ProgressDialog(this);
     //   progressDialog.setMessage("Loading..");
       // progressDialog.show();

        final AlertDialog alertDialog= new SpotsDialog.Builder().setContext(this).build();
        alertDialog.show();
        // client.get( "https://api.myjson.com/bins/j5f6b",new JsonHttpResponseHandler() {
        client.get( Backendserver.url+ "/api/support/projects/?format=json",new JsonHttpResponseHandler() {
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
                //linearLayoutManager.setReverseLayout(true);
              //linearLayoutManager.setStackFromEnd(true);
                listshowgrn.setHasFixedSize(true);
                listshowgrn.setLayoutManager(linearLayoutManager);
                adapter = new Grn_adapter(Grnlists, Grn.this);
                listshowgrn.setAdapter(adapter);
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

