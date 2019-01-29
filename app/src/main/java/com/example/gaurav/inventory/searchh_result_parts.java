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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.support.v7.widget.SearchView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaurav.inventory.Adapters.search_parts_adapter;
import com.example.gaurav.inventory.Backend.Backendserver;
import com.example.gaurav.inventory.entities.Item;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;

public class searchh_result_parts extends AppCompatActivity {

    public Context mContext;
    RecyclerView listshowrcy;
    Toolbar toolbar;
    ArrayList<Item> productlists;
    search_parts_adapter adapter;
    SearchView searchView;
    AsyncHttpClient client;
    ImageView imageView;
  //  search_partsadapter search_partsadapter;
  private static String URL_DATA = "http://192.168.1.111:8000/api/support/projects/?format=json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchh_result_parts);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.status));


        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Part Number");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.requestFocus();
       // client = new AsyncHttpClient();.
        Backendserver backend = new Backendserver(getApplicationContext());
        //   getUserDetails();
        client = backend.getHTTPClient();
        listshowrcy = findViewById(R.id.listshow);
        productlists = new ArrayList<>();

        imageView=findViewById(R.id.noresult_img);
        //search_partsadapter=new search_partsadapter(productlists);
       // client.addHeader("referer",url);
        imageView.setVisibility(View.GONE);
      getproducts();



    /*    productlists.add(new Item("Harley Davidson Street 750 2016 Std"));
        productlists.add(new Item("Triumph Street Scramble 2017 Std"));
        productlists.add(new Item("Suzuki GSX R1000 2017 STD"));
        productlists.add(new Item("Suzuki GSX R1000 2017 R"));
        productlists.add(new Item("Suzuki Gixxer 2017 SP"));
        productlists.add(new Item("Suzuki Gixxer 2017 SF 2017 Fuel injected ABS"));
        productlists.add(new Item("BMW R 1200 R 2017"));
        productlists.add(new Item("BMW R 1200 RS 2017"));
        productlists.add(new Item("BMW R 1200 GSA 2017"));
        productlists.add(new Item("Royal Enfield Classic 350 2017 Gunmental Grey"));
        productlists.add(new Item("Honda MSX125 Grom 2018 STD"));
        productlists.add(new Item("UM Motorcycles Renegade 2017 classic"));
        productlists.add(new Item("Ducati Scrambler 2017 Mach 2.0"));
        productlists.add(new Item("yamaha Fazer 2017 25"));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listshowrcy.setHasFixedSize(true);
        listshowrcy.setLayoutManager(linearLayoutManager);
        adapter = new search_parts_adapter(productlists, searchh_result_parts.this);
        listshowrcy.setAdapter(adapter);  */


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
                ArrayList<Item> filtermodelist =filter(productlists,newText);
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


    private ArrayList<Item> filter(ArrayList<Item> pl, String query) {
        query = query.toLowerCase();
        final ArrayList<Item> filteredModeList = new ArrayList<>();
        for (Item model : pl) {
            final String text = model.getPart_no().toLowerCase();

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


    public void getproducts(){
        //Toast.makeText(getApplicationContext(),""+URL_DATA,Toast.LENGTH_SHORT).show();
        final AlertDialog alertDialog= new SpotsDialog.Builder().setContext(this).build();
        alertDialog.show();
  //  final ProgressDialog progressDialog=new ProgressDialog(this);
  //  progressDialog.setMessage("Loading..");
  //  progressDialog.show();


      // client.get( "https://api.myjson.com/bins/j5f6b",new JsonHttpResponseHandler() {
     client.get( Backendserver.url+ "/api/support/products/?format=json",new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                alertDialog.dismiss();
       //        Toast.makeText(getApplicationContext(),"res "+response.toString(),Toast.LENGTH_SHORT).show();
              for (int i = 0; i < response.length(); i++) {
                    JSONObject object=null;
                    try {
                        object = response.getJSONObject(i);
                          /*object.getString( "part_no");
                            object.getString("pk");
                            object.getString("description_1");
                            object.getString( "description_2");
                            object.getString("weight");
                            object.getString( "price");
                            object.getString( "custom_number");*/
                      // Toast.makeText(getApplicationContext(),"res "+object.toString(),Toast.LENGTH_SHORT).show();
                        Log.e("error"," "+object);
                     // Item item = new Item(object);
                        productlists.add(new Item(object));
                 //       Toast.makeText(getApplicationContext(),"res "+productlists.toString()+" "+productlists.size(),Toast.LENGTH_SHORT).show();
                        Log.e("error"," "+productlists.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

              }

                   LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                   listshowrcy.setHasFixedSize(true);
                   listshowrcy.setLayoutManager(linearLayoutManager);
                   adapter = new search_parts_adapter(productlists, searchh_result_parts.this);
                   listshowrcy.setAdapter(adapter);
                  // listshowrcy.setAdapter(new search_parts_adapter(productlists));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(),"data not fetching"+errorResponse,Toast.LENGTH_SHORT).show();
            }
        });
    }

 /*  public class search_partsadapter extends RecyclerView.Adapter<Holderview> {
        ArrayList<Item> product;
        public search_partsadapter(ArrayList<Item> productlist) {
            this.product = productlist;
        }

        @NonNull
        @Override
        public Holderview onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customitem, viewGroup, false);
            return new Holderview(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holderview holder, final int position) {
            Item item=product.get(position);
            holder.v_name.setText("Part No : "+item.getPart_no()+"\n\n"+"Description-1 : "+item.getDescription_1()+"\n\n"+"Description-2 : "+ item.getDescription_2()+"\n\n"+"Price : "+item.getPrice()+"\n\n"+"Weight : "+item.getWeight()+"\n\n"+"Customs No : "+item.getCustoms_no());

           holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override            public void onClick(View view) {

                    Toast.makeText(getApplicationContext(), "click on " + product.get(position).getPart_no(),
                            Toast.LENGTH_LONG).show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return product.size();
        }

       public void setfilter(ArrayList<Item>listitem)
        {
            product=new ArrayList<>();
            product.addAll(listitem);
            notifyDataSetChanged();
        }

    }
    private class Holderview extends RecyclerView.ViewHolder {

        TextView v_name;

      public Holderview (@NonNull View itemview) {
            super(itemview);

            v_name = (TextView) itemView.findViewById(R.id.product_id);
        }
    }*/
}












