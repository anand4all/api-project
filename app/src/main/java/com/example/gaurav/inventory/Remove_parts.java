package com.example.gaurav.inventory;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaurav.inventory.Adapters.Auto_inventory_adapter;
import com.example.gaurav.inventory.Adapters.Autocomplete_adapter;
import com.example.gaurav.inventory.Adapters.Grn_detail_adapter;
import com.example.gaurav.inventory.Adapters.remove_parts_adapter;
import com.example.gaurav.inventory.Backend.Backendserver;
import com.example.gaurav.inventory.entities.Grn_item_detail;
import com.example.gaurav.inventory.entities.Item;
import com.example.gaurav.inventory.entities.getinventoryitem;
import com.example.gaurav.inventory.entities.remove_item;
import com.google.zxing.Result;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import cz.msebera.android.httpclient.Header;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class Remove_parts extends AppCompatActivity implements ZXingScannerView.ResultHandler {



    private ZXingScannerView zXingScannerView;
    ImageView itemsQuantityAddgrn, itemsQuantityRemovegrn,search;
    TextView itemquantitygrn, savegrn, partno_dialogtxt,Cancel;
    AutoCompleteTextView search_bar_remove;
    AsyncHttpClient client;
    int count = 1;
    int counts=0;
    Dialog dialog;
    RecyclerView inventorylistshow;
    remove_parts_adapter adapter;
    Button btn_remove_ok,remove;
    String resultcode,productpk,partno,barcode="null",description_1,Price,itempk,qty,description_2,weigh,custom_n="null";
    ArrayList<getinventoryitem> inventorylist;
    ArrayList<getinventoryitem> newinventorylist;
    ArrayList<remove_item>inventoryremovedlist;
    Auto_inventory_adapter adapter1;
    String searchedit;
    int totalqty,totalremqty;
    HashSet<getinventoryitem>listToSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_parts);
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


             //    if(status.equalsIgnoreCase("delivered"))
               //  {
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.status));


        Backendserver backendServer = new Backendserver(getApplicationContext());
        client = backendServer.getHTTPClient();
       inventorylistshow=findViewById(R.id.remove_partshow);

        search_bar_remove = findViewById(R.id.autocompleteremove);
        btn_remove_ok = findViewById(R.id.ok_btn_remove);
        search=findViewById(R.id.searchinventory);
        inventorylist=new ArrayList<>();
        inventoryremovedlist=new ArrayList<>();

        askForPermission(Manifest.permission.CAMERA, CAMERA);
        zXingScannerView = new ZXingScannerView(this);

        zXingScannerView= (ZXingScannerView) findViewById(R.id.zxscan_remove);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        inventorylistshow.setHasFixedSize(true);
        inventorylistshow.setLayoutManager(linearLayoutManager);

        adapter = new remove_parts_adapter(inventoryremovedlist, Remove_parts.this);

        inventorylistshow.setAdapter(adapter);


        search_bar_remove.setThreshold(2);
       // search_bar_remove.setDropDownHeight(300);
        //edittext.setDropDownVerticalOffset(300);
        adapter1 = new Auto_inventory_adapter(getApplicationContext(), R.layout.activity_remove_parts, R.id.lbl_name, inventorylist);

        search_bar_remove.setAdapter(adapter1);


     //   getinventory();
        getinventorydata();
        searchfunc();

        ok_btn();


    }
    private void ok_btn() {
        btn_remove_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i=inventoryremovedlist.size();

               if(i==0)
                {
                    Toast.makeText(getApplicationContext(),"Please add some product", Toast.LENGTH_SHORT).show();
                }
                else {

                    Intent intent = new Intent(getBaseContext(), remove_selected_parts.class);
                    intent.putParcelableArrayListExtra("mylist", inventoryremovedlist);
                    startActivity(intent);
                    //i.putParcelable("mylist", Parcels.wrap(myList));
                }
            }
        });
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Remove_parts.this, permission) != PackageManager.PERMISSION_GRANTED) {

            //asking for permission prompt

            if (ActivityCompat.shouldShowRequestPermissionRationale(Remove_parts.this, permission)) {
                //request for permissions and getting request code -- 5 for camera
                ActivityCompat.requestPermissions(Remove_parts.this, new String[]{permission}, requestCode);

            } else {
                //requesting permission
                ActivityCompat.requestPermissions(Remove_parts.this, new String[]{permission}, requestCode);
            }
        } else {
         //   Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
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

            Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();

            //try {
                zXingScannerView = new ZXingScannerView(this);
                zXingScannerView = (ZXingScannerView) findViewById(R.id.zxscan_remove);
                zXingScannerView.setResultHandler(this);
                zXingScannerView.startCamera();
         //   }
          //  catch (Exception e) {
            //    e.printStackTrace();
           // }


        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void handleResult(Result result) {
        resultcode=result.getText();





        //  txtResult.setText(resultcode);

      /*  for(int i=0;i<inventorylist.size();i++)
        {
            partno=inventorylist.get(i).getPart_no();
            itempk=inventorylist.get(i).getPk();
            productpk=inventorylist.get(i).getProductpk();
            barcode=inventorylist.get(i).getBrcode();
            description_1=inventorylist.get(i).getDescription_1();
            description_2=inventorylist.get(i).getDescription_2();
            Price=inventorylist.get(i).getPrice();
            weigh=inventorylist.get(i).getWeight();
            totalqty=Integer.parseInt(inventorylist.get(i).getQty());
            custom_n=inventorylist.get(i).getCustoms_no();


            //Toast.makeText(getApplicationContext()," "+partno,Toast.LENGTH_SHORT).show();
           // Toast.makeText(getApplicationContext()," "+itempk,Toast.LENGTH_SHORT).show();
           // Toast.makeText(getApplicationContext()," "+barcode,Toast.LENGTH_SHORT).show();

            if(barcode.equals(resultcode)||barcode == resultcode)
            {
                Log.e("matched",barcode+" "+itempk+" "+productpk+" "+description_1+" "+Price+" "+partno);
                Toast.makeText(getApplicationContext(),"itempk "+itempk,Toast.LENGTH_SHORT).show();
                break;
            }
        } */


    //    getbomfilter();

   //     MyCustomAlertDialog2();

        if(counts==0) {
            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(200);

            if (resultcode.length() == 0) {
            } else {

                counts++;
                Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
                // searchedit = search_bar_remove.getText().toString();
                for (int i = 0; i < inventorylist.size(); i++) {
                    partno = inventorylist.get(i).getPart_no();
                    totalqty = Integer.parseInt(inventorylist.get(i).getQty());
                    productpk = inventorylist.get(i).getProductpk();
                    barcode = inventorylist.get(i).getBrcode();
                    description_1 = inventorylist.get(i).getDescription_1();
                    description_2 = inventorylist.get(i).getDescription_2();
                    Price = inventorylist.get(i).getPrice();
                    weigh = inventorylist.get(i).getWeight();
                    //  custom_n=inventorylist.get(i).getCustoms_no();


                    //Toast.makeText(getApplicationContext()," "+partno,Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext()," "+itempk,Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext()," "+barcode,Toast.LENGTH_SHORT).show();

                    if (barcode.equals(resultcode)) {
                        Log.e("matched", barcode + " " + totalqty + " " + productpk + " " + description_1 + " " + Price + " " + partno + " " + description_2 + " " + weigh + " " + custom_n);
                        //  Toast.makeText(getApplicationContext(),"itempk "+itempk,Toast.LENGTH_SHORT).show();

                        break;
                    }

                }
                if (barcode.equals(resultcode)) {
                    Log.e("matched", barcode);


                    MyCustomAlertDialog2();

                } else {
                    Toast.makeText(Remove_parts.this, "Part no not matched", Toast.LENGTH_SHORT).show();
                    counts=0;

                }

            }

        }


        zXingScannerView.resumeCameraPreview(this);
    }

    public void MyCustomAlertDialog2() {
        dialog = new Dialog(Remove_parts.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_remove);
        dialog.setTitle("My Custom Dialog");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        remove = (Button) dialog.findViewById(R.id.btn_remove_dialog);
        Cancel = dialog.findViewById(R.id.txt_cancel_dialog);
        partno_dialogtxt = dialog.findViewById(R.id.partno_dialog_project);

        itemquantitygrn = dialog.findViewById(R.id.item_quantity_dialog_remove);
        itemsQuantityAddgrn = (ImageView) dialog.findViewById(R.id.items_quantity_add_dialog_remove);
        itemsQuantityRemovegrn = (ImageView) dialog.findViewById(R.id.items_quantity_remove_dialog_remove);

        partno_dialogtxt.setText(resultcode);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counts=0;
                dialog.cancel();
                zXingScannerView = new ZXingScannerView(Remove_parts.this);

                zXingScannerView= (ZXingScannerView) findViewById(R.id.zxscan_remove);
                zXingScannerView.setResultHandler(Remove_parts.this);
                zXingScannerView.startCamera();


            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counts=0;


                zXingScannerView = new ZXingScannerView(Remove_parts.this);

                zXingScannerView= (ZXingScannerView) findViewById(R.id.zxscan_remove);
                zXingScannerView.setResultHandler(Remove_parts.this);
                zXingScannerView.startCamera();


                search_bar_remove.setText("");

                count=1;
                qty = itemquantitygrn.getText().toString();



                if(Integer.parseInt(qty)>totalqty)
                {
                    Toast.makeText(getApplicationContext(), "Maximum quantity " + totalqty, Toast.LENGTH_SHORT).show();


                }
                else {

                    dialog.cancel();


                    if (barcode.equals(resultcode)) {
                        totalremqty =totalqty-Integer.parseInt(qty);

                        remove_item removeitem = new remove_item(partno, qty, itempk, barcode, description_1, productpk, Price, description_2, custom_n, weigh);
                        Log.e("matched", "" + weigh);

                        inventoryremovedlist.add(removeitem);
                        Toast.makeText(getApplicationContext(), " " + inventoryremovedlist, Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();



                   /* client.delete(Backendserver.url + "api/support/inventory/" + productpk + "/", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                            Toast.makeText(getApplicationContext(), "removed ", Toast.LENGTH_SHORT).show();
                            remove_item removeitem = new remove_item(partno,qty,itempk,barcode,description_1,productpk,Price,description_2,custom_n,weigh);
                            Log.e("matched", ""+weigh );

                            inventoryremovedlist.add(removeitem);
                            Toast.makeText(getApplicationContext(), " " + inventoryremovedlist, Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    }); */

                    }
                }
            }
        });

        itemsQuantityAddgrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (count< totalqty) {
                    //  itemsQuantityRemovegrn.setVisibility(View.VISIBLE);
                    count++;
                }
                else{
                    Toast.makeText(getApplicationContext(),"Maximum quantity "+totalqty,Toast.LENGTH_SHORT).show();
                }


                itemquantitygrn.setText(Integer.toString(count));
            }
        });
        itemsQuantityRemovegrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count>1) {
                    //  itemquantitygrn.setVisibility(View.GONE);

                    count--;
                }

                itemquantitygrn.setText(Integer.toString(count));
            }
        });
        //   Window window = dialog.getWindow();
        //    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
    }
        //   Window window = dialog.getWindow();



    public void MyCustomAlertDialog3() {
        dialog = new Dialog(Remove_parts.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_remove);
        dialog.setTitle("My Custom Dialog");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        remove = (Button) dialog.findViewById(R.id.btn_remove_dialog);
        Cancel = dialog.findViewById(R.id.txt_cancel_dialog);
        partno_dialogtxt = dialog.findViewById(R.id.partno_dialog_project);

        itemquantitygrn = dialog.findViewById(R.id.item_quantity_dialog_remove);
        itemsQuantityAddgrn = (ImageView) dialog.findViewById(R.id.items_quantity_add_dialog_remove);
        itemsQuantityRemovegrn = (ImageView) dialog.findViewById(R.id.items_quantity_remove_dialog_remove);

        //totalremqty=totalqty;

        partno_dialogtxt.setText(partno);


        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();

            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counts=0;
                search_bar_remove.setText("");

                count=1;
                qty = itemquantitygrn.getText().toString();



                if(Integer.parseInt(qty)>totalqty)
                {
                    Toast.makeText(getApplicationContext(), "Maximum quantity " + totalqty, Toast.LENGTH_SHORT).show();


                }
                else {

                    dialog.cancel();


                    if (partno.equals(searchedit)) {
                        totalremqty =totalqty-Integer.parseInt(qty);

                        remove_item removeitem = new remove_item(partno, qty, itempk, barcode, description_1, productpk, Price, description_2, custom_n, weigh);
                        Log.e("matched", "" + weigh);

                        inventoryremovedlist.add(removeitem);
                        Toast.makeText(getApplicationContext(), " " + inventoryremovedlist, Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();



                   /* client.delete(Backendserver.url + "api/support/inventory/" + productpk + "/", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                            Toast.makeText(getApplicationContext(), "removed ", Toast.LENGTH_SHORT).show();
                            remove_item removeitem = new remove_item(partno,qty,itempk,barcode,description_1,productpk,Price,description_2,custom_n,weigh);
                            Log.e("matched", ""+weigh );

                            inventoryremovedlist.add(removeitem);
                            Toast.makeText(getApplicationContext(), " " + inventoryremovedlist, Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    }); */

                    }
                }
            }
        });

        itemsQuantityAddgrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (count< totalqty) {
                  //  itemsQuantityRemovegrn.setVisibility(View.VISIBLE);
                    count++;
                }
                else{
                    Toast.makeText(getApplicationContext(),"Maximum quantity "+totalqty,Toast.LENGTH_SHORT).show();
                }


                itemquantitygrn.setText(Integer.toString(count));
            }
        });
        itemsQuantityRemovegrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count>1) {
                  //  itemquantitygrn.setVisibility(View.GONE);

                       count--;
                }

                itemquantitygrn.setText(Integer.toString(count));
            }
        });
        //   Window window = dialog.getWindow();
        //    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
    }
    //   Window window = dialog.getWindow();



    public void searchfunc(){

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (search_bar_remove.length()==0) {
                    search_bar_remove.setError("Enter any number");
                } else{
                    searchedit = search_bar_remove.getText().toString();
                    for (int i = 0; i < inventorylist.size(); i++) {
                        partno = inventorylist.get(i).getPart_no();
                        totalqty=Integer.parseInt(inventorylist.get(i).getQty());
                        productpk = inventorylist.get(i).getProductpk();
                        barcode = inventorylist.get(i).getBrcode();
                        description_1 = inventorylist.get(i).getDescription_1();
                        description_2=inventorylist.get(i).getDescription_2();
                        Price=inventorylist.get(i).getPrice();
                        weigh=inventorylist.get(i).getWeight();
                      //  custom_n=inventorylist.get(i).getCustoms_no();


                        //Toast.makeText(getApplicationContext()," "+partno,Toast.LENGTH_SHORT).show();
                        // Toast.makeText(getApplicationContext()," "+itempk,Toast.LENGTH_SHORT).show();
                        // Toast.makeText(getApplicationContext()," "+barcode,Toast.LENGTH_SHORT).show();

                        if (partno.equals(searchedit)) {
                            Log.e("matched", barcode + " " + totalqty + " " + productpk + " " + description_1 + " " + Price + " " + partno + " " + description_2 + " " +weigh + " " + custom_n);
                            //  Toast.makeText(getApplicationContext(),"itempk "+itempk,Toast.LENGTH_SHORT).show();

                            break;
                        }

                    }
                if (partno.equals(searchedit)) {
                    Log.e("matched", partno);
                    MyCustomAlertDialog3();
                } else {
                    Toast.makeText(Remove_parts.this, "Part no not matched", Toast.LENGTH_SHORT).show();
                }

             }
            }
        });


    }



    public void getinventory(){

        client.get(Backendserver.url +"api/support/inventory/",new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

               // Toast.makeText(getApplicationContext(),"data fetching",Toast.LENGTH_SHORT).show();

                for (int i = 0; i < response.length(); i++) {
                    JSONObject object=null;
                    try {
                        object = response.getJSONObject(i);

                        JSONObject service=object.getJSONObject("product");
                        String productpk = service.getString("pk");
                        String part_no = service.getString("part_no");
                        String barcode = service.getString("bar_code");
                        String price = service.getString("price");
                        String description_1 = service.getString("description_1");
                        String description_2 = service.getString("description_2");
                        String custom_no = service.getString("customs_no");
                        String weight = service.getString("weight");

                        // Toast.makeText(getApplicationContext(),"res "+object.toString(),Toast.LENGTH_SHORT).show();
                        object.put("productpk",productpk);
                        object.put("part_no",part_no);
                        object.put("bar_code",barcode);
                        object.put("price",price);
                        object.put("description_1",description_1);
                        object.put("description_2",description_2);
                        object.put("customs_no",custom_no);
                        object.put("weight",weight);
                      //  Log.e("errror"," "+weight);
                       Log.e("errror"," "+object);

                        inventorylist.add(new getinventoryitem(object));



                      //  Toast.makeText(getApplicationContext(),"res "+inventorylist.size(),Toast.LENGTH_SHORT).show();
                      //  listToSet=new HashSet<getinventoryitem>(inventorylist);
                     //   newinventorylist=new ArrayList<>(listToSet);

                       // Log.e("newlist",""+newinventorylist.size());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(),"data not fetching"+errorResponse,Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void getinventorydata(){
     //   Toast.makeText(getApplicationContext(),"data fetching",Toast.LENGTH_SHORT).show();
        client.get(Backendserver.url +"api/support/inventoryData/",new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                // Toast.makeText(getApplicationContext(),"data fetching"+response.toString(),Toast.LENGTH_SHORT).show();

                try {
                    JSONArray jsonArray=response.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject object=jsonArray.getJSONObject(i);
                     //   Log.e("errror"," "+jsonArray);

                        String productpk = object.getString("productPk");
                        String part_no = object.getString("productPartno");
                        String barcode = object.getString("productBarCode");
                        String totalqty = object.getString("totalqty");
                        String totalVal = object.getString("totalVal");
                        String price = object.getString("price");
                        String totalprice = object.getString("totalprice");

                        String description_1 = object.getString("productDesc");
                        String description_2 = object.getString("productDesc2");
                     //   String custom_no = object.getString("customs_no");
                        String weight = object.getString("weight");
                      //  Float total=Float.parseFloat(totalVal);

                        // Toast.makeText(getApplicationContext(),"res "+object.toString(),Toast.LENGTH_SHORT).show();

                        if(totalqty.equalsIgnoreCase("0"))
                        {
                            Log.e("errror"," "+totalqty);

                        }
                        else {
                            object.put("productpk", productpk);
                            object.put("part_no", part_no);
                             object.put("bar_code",barcode);
                            object.put("totalVal", totalVal);
                            object.put("totalqty", totalqty);
                            object.put("price", price);
                            object.put("totalprice", totalprice);
                            object.put("description_1", description_1);
                            object.put("description_2", description_2);
                            // object.put("customs_no",custom_no);
                            object.put("weight", weight);

                            inventorylist.add(new getinventoryitem(object));
                            Log.e("errror"," "+inventorylist.size());
                            Log.e("errror"," "+inventorylist);
                        }

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(),"data not fetching"+errorResponse,Toast.LENGTH_SHORT).show();
            }
        });


    }




}