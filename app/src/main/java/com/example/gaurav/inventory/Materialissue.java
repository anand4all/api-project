package com.example.gaurav.inventory;

import android.app.AlertDialog;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.gaurav.inventory.Adapters.Grn_adapter;
import com.example.gaurav.inventory.Adapters.MaterialissueAdapter;
import com.example.gaurav.inventory.Adapters.materialadapter;
import com.example.gaurav.inventory.Backend.Backendserver;
import com.example.gaurav.inventory.entities.Grn_item;
import com.example.gaurav.inventory.entities.arraymatitem;
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

public class Materialissue extends AppCompatActivity {

    RecyclerView recyclerView;
    Toolbar toolbar;
    AsyncHttpClient client;
    materialadapter adapter;
    SearchView searchView;
    ImageView imageView;
    ArrayList<materialitem>materiallist;
    ArrayList<arraymatitem>matlarraylist;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((MaterialissueAdapter)recyclerView.getAdapter()).onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ((MaterialissueAdapter)recyclerView.getAdapter()).onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materialissue);

        toolbar = (Toolbar) findViewById(R.id.toolbarmi);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Material Issue");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.requestFocus();
        Backendserver backend = new Backendserver(getApplicationContext());

        client = backend.getHTTPClient();

        materiallist=new ArrayList<>();
        matlarraylist=new ArrayList<>();

        recyclerView=(RecyclerView)findViewById(R.id.materialissuelist) ;
       // recyclerView.setLayoutManager(new LinearLayoutManager(this));

       // MaterialissueAdapter adapter=new MaterialissueAdapter(this,initData());
       // adapter.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
      //  adapter.setParentClickableViewAnimationDefaultDuration();
      //  adapter.setParentAndIconExpandOnClick(true);
      //  recyclerView.setAdapter(adapter);
        imageView=findViewById(R.id.noresult_img);
        getmaterialissue();
    }

   /* @Override
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
                ArrayList<materialitem> filtermodelist =filter(materiallist,newText);
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


    private ArrayList<materialitem> filter(ArrayList<materialitem> pl, String query) {
        query = query.toLowerCase();
        final ArrayList<materialitem> filteredModeList = new ArrayList<>();
        for (materialitem model : pl) {
            final String text = model.getPartno().toLowerCase();

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
    } */



   /* private ArrayList<ParentObject> initData(){

        TitleCreator titleCreator=TitleCreator.get(this);
        ArrayList<TitleParent>titles=titleCreator.getAll();
        ArrayList <ParentObject>parentObjects=new ArrayList<>();

        for(TitleParent titleParent:titles)
        {
          ArrayList<Object>childlist=new ArrayList<>();
          childlist.add(new Titlechild("sdds","dsdsdds"));

          Log.e("error","dsfsf");
          titleParent.setChildObjectList(childlist);
          parentObjects.add(titleParent);
        }
       return parentObjects;
    } */


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

                            String qty=jOBJNEW.getString("qty");
                            String price=jOBJNEW.getString("price");

                            object.put("qty",qty);
                         //   jOBJNEW.put("qty",qty);
                            object.put("price",price);

                            JSONObject product = jOBJNEW.getJSONObject("product");
                            String part_no=product.getString("part_no");
                            object.put("part_no",part_no);
                          //  jOBJNEW.put("part_no",part_no);
                          //   matlarraylist.add(new arraymatitem(jOBJNEW));

                          //  Log.e("errror"," "+jOBJNEW);

                          // Toast.makeText(getApplicationContext(),"res "+jOBJNEW.toString(),Toast.LENGTH_SHORT).show();

                        }
                        String dateofissue = project.getString("created");
                        object.put("date",dateofissue);
                        String comm_nr = project.getString("comm_nr");
                        object.put("comm_nr",comm_nr);


                        //Toast.makeText(getApplicationContext(),"res"+object.toString(),Toast.LENGTH_SHORT).show();

                        //object.put("name",name);

                        //object.put("mobile",mobile);

                        //Log.e("errror"," "+object);

                        //tem item = new Item(object);

                        materiallist.add(new materialitem(object));
                        //       Toast.makeText(getApplicationContext(),"res "+productlists.toString()+" "+productlists.size(),Toast.LENGTH_SHORT).show();
                     //   Log.e("error"," "+object.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                //linearLayoutManager.setReverseLayout(true);
                //linearLayoutManager.setStackFromEnd(true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter = new materialadapter(materiallist, Materialissue.this);
                recyclerView.setAdapter(adapter);
                //listshowrcy.setAdapter(new search_parts_adapter(productlists));*/

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(),"data not fetching"+errorResponse,Toast.LENGTH_SHORT);
            }
        });

    }


}
