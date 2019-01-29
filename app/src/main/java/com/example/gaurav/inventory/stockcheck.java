package com.example.gaurav.inventory;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaurav.inventory.entities.remove_item;
import com.itextpdf.text.Document;

import com.example.gaurav.inventory.Adapters.Autocomplete_adapter;
import com.example.gaurav.inventory.Backend.Backendserver;
import com.example.gaurav.inventory.entities.Grn_item_detail;
import com.example.gaurav.inventory.entities.Item;
import com.example.gaurav.inventory.entities.stockitem;
import com.example.gaurav.inventory.startup.MainActivity;
import com.google.zxing.Result;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class stockcheck extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private Context context;
    Dialog dialog;
    Button downloadstocksheet;
    AutoCompleteTextView auto_stockcheck;
    private ZXingScannerView zXingScannerView;
    EditText txtResult;
    ImageView itemsQuantityAddstock, itemsQuantityRemovestock,scan,searchstock;
    TextView itemquantity,additem,Cancel,partno_dialogtxt;
    Button ok_dialog;
    FloatingActionButton fab;
    String countstock,result,stockpartno,stockproductpk,stockpk,barcode,stockitemqty;
    String resultcode,stockreportpk,qty,partno,productpk,stockcheckitempk;
    AsyncHttpClient client;
    ArrayList<Item> productlists1;
    ArrayList<stockitem> stockchecklist;
    Autocomplete_adapter adapter1;

    int stockqty;
    int count2=0;
    int count=1;

    DownloadManager downloadManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockcheck);


        Backendserver backendServer = new Backendserver(getApplicationContext());
        client = backendServer.getHTTPClient();
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.status));
        productlists1=new ArrayList<>();
        auto_stockcheck=findViewById(R.id.autocomplete_stockcheck);
        downloadstocksheet=findViewById(R.id.downloadbtn);

        searchstock=findViewById(R.id.searchstock);
        stockchecklist=new ArrayList<>();
        // fab=findViewById(R.id.flash);
        getproducts();
        auto_stockcheck.setThreshold(1);
        auto_stockcheck.setDropDownHeight(300);
        //edittext.setDropDownVerticalOffset(300);
        adapter1 = new Autocomplete_adapter(getApplicationContext(), R.layout.activity_stockcheck, R.id.lbl_name, productlists1);

        auto_stockcheck.setAdapter(adapter1);
        getstock();
        getcheckitemlist();


        // scan=findViewById(R.id.scan);

        //scan.setVisibility(View.GONE);

        askForPermission(Manifest.permission.CAMERA, CAMERA);
        zXingScannerView = new ZXingScannerView(this);
        zXingScannerView = (ZXingScannerView) findViewById(R.id.zscanstock_check);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

       /* increasedecrese();
        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext()," added", Toast.LENGTH_SHORT).show();

            }
        });*/




        searchstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auto_stockcheck.length() == 0) {
                    auto_stockcheck.setError("Enter Part Number");
                } else {



                    result = auto_stockcheck.getText().toString();

                    for(int i=0;i<productlists1.size();i++)
                    {
                        partno=productlists1.get(i).getPart_no();
                        productpk=productlists1.get(i).getPk();

                        Log.e("error",partno+" "+productpk);
                        // Toast.makeText(getApplicationContext()," "+partnobom,Toast.LENGTH_SHORT).show();

                        if(partno.equals(result))
                        {
                            Log.e("matched",partno+" "+productpk);
                            //  Toast.makeText(getApplicationContext(),"productpk "+productpk,Toast.LENGTH_SHORT).show();
                            break;
                        }

                    }

                    if(partno.equals(result)) {


                        for (int i = 0; i < stockchecklist.size(); i++) {
                            stockitemqty=stockchecklist.get(i).getStockitemqty();
                             stockcheckitempk=stockchecklist.get(i).getStockitempk();
                            stockpartno = stockchecklist.get(i).getPartnoumber();
                            stockproductpk = stockchecklist.get(i).getProductpk();
                            stockpk = stockchecklist.get(i).getStockpk();
                            Log.e("error", stockpartno + " " + stockproductpk + "" + stockpk);
                            // Toast.makeText(getApplicationContext()," "+partnobom,Toast.LENGTH_SHORT).show();

                            if (stockpartno.equals(result) && stockpk.equals(stockreportpk)) {
                                Log.e("matched", stockpartno + " " + stockproductpk + " " + stockpk);
                                //  Toast.makeText(getApplicationContext(),"productpk "+productpk,Toast.LENGTH_SHORT).show();
                                break;
                            }

                        }


                        if (stockpartno.equals(result) && stockpk.equals(stockreportpk)) {

                            auto_stockcheck.setText("");
                            stockDialogpatch();
                           // Toast.makeText(getApplicationContext(), "Product already exist for this stock ", Toast.LENGTH_LONG).show();
                        }
                       else  {
                            stockDialog();
                        }
                        // Toast.makeText(getApplicationContext(),"Part no is not matched ",Toast.LENGTH_SHORT).show();

                    }

                }
                zXingScannerView.resumeCameraPreview(stockcheck.this);

            }
        });

        downloadstocksheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text="gjoegohoeggprhoepghpghprghegprhpghrephprghiegphirepghprogogphoreghopeghopeprgho";

           //     createandDisplayPdf(text);

                downloadstock();

              //     MyCustomAlertDialog2();
              //   new LongOperation().execute();

          /*     try {
                    String extStorageDirectory = Environment.getExternalStorageDirectory()
                            .toString();
                    File folder = new File(extStorageDirectory, "pdf");
                    folder.mkdir();
                    File file = new File(folder, "sheetdownload.pdf");
                    try {
                        file.createNewFile();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    Downloader.DownloadFile(Backendserver.url+"api/support/stockSheet/?value=76&created=2019-01-28T04:25:56.079752Z", file);

                    showPdf();

                }
                catch (Exception e){} */

            }
        });

    }



    public void showPdf()
    {
        File file = new File(Environment.getExternalStorageDirectory()+"/pdf/sheetdownload.pdf");
        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        startActivity(intent);
    }


    @Override
    public void handleResult(Result result) {
         resultcode =result.toString();
        if (count2==0) {
            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(200);


            //  result = auto_stockcheck.getText().toString();

            for (int i = 0; i < productlists1.size(); i++) {
                partno = productlists1.get(i).getPart_no();
                productpk = productlists1.get(i).getPk();
                barcode = productlists1.get(i).getBarcode();

                Log.e("error", barcode + " " + productpk);
                // Toast.makeText(getApplicationContext()," "+partnobom,Toast.LENGTH_SHORT).show();

                if (barcode.equals(resultcode)) {
                    Log.e("matched", partno + " " + productpk);
                    //  Toast.makeText(getApplicationContext(),"productpk "+productpk,Toast.LENGTH_SHORT).show();
                    break;
                }

            }

            if (barcode.equals(resultcode)) {


                for (int i = 0; i < stockchecklist.size(); i++) {
                    stockcheckitempk=stockchecklist.get(i).getStockitempk();
                    stockpartno = stockchecklist.get(i).getPartnoumber();
                    stockproductpk = stockchecklist.get(i).getProductpk();
                    stockpk = stockchecklist.get(i).getStockpk();
                    stockitemqty=stockchecklist.get(i).getStockitemqty();
                    Log.e("error", stockpartno + " " + stockproductpk + "" + stockpk);
                    // Toast.makeText(getApplicationContext()," "+partnobom,Toast.LENGTH_SHORT).show();

                    if (stockpartno.equals(partno) && stockpk.equals(stockreportpk)) {
                        Log.e("matched", stockpartno + " " + stockproductpk + " " + stockpk);
                        //  Toast.makeText(getApplicationContext(),"productpk "+productpk,Toast.LENGTH_SHORT).show();
                        break;
                    }

                }


                if (stockpartno.equals(partno) && stockpk.equals(stockreportpk)) {
                    count2++;
                    stockDialogscannerpatch();
                   // Toast.makeText(getApplicationContext(), "Product already exist for this stock ", Toast.LENGTH_SHORT).show();
                } else {
                    count2++;
                    stockDialogscanner();
                }
                // Toast.makeText(getApplicationContext(),"Part no is not matched ",Toast.LENGTH_SHORT).show();

            }


        }

      //  Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
        zXingScannerView.resumeCameraPreview(this);

    }
    public void stockDialog(){
        dialog = new Dialog(stockcheck.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_stock_check);
        dialog.setTitle("My Custom Dialog");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        ok_dialog = (Button)dialog.findViewById(R.id.btn_Add_stock_ck);
        Cancel=dialog.findViewById(R.id.txt_cancel_dialog_stock_ck);
        partno_dialogtxt=dialog.findViewById(R.id.partno_dialog_stock_ck);

        itemquantity=dialog.findViewById(R.id.item_quantity_dialog_stock_ck);
        itemsQuantityAddstock=(ImageView)dialog.findViewById(R.id.items_quantity_add_dialog_stock_ck);
        itemsQuantityRemovestock=(ImageView) dialog.findViewById(R.id.items_quantity_rmv_stock_ck);

        // final String result2=resultcode;

        partno_dialogtxt.setText(result);


        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count2=0;
                auto_stockcheck.setText("");

                dialog.cancel();

                zXingScannerView = new ZXingScannerView(stockcheck.this);
                zXingScannerView = (ZXingScannerView) findViewById(R.id.zscanstock_check);
                zXingScannerView.setResultHandler(stockcheck.this);
                zXingScannerView.startCamera();

            }
        });
        ok_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auto_stockcheck.setText("");
                count2=0;
                dialog.cancel();

                zXingScannerView = new ZXingScannerView(stockcheck.this);
                zXingScannerView = (ZXingScannerView) findViewById(R.id.zscanstock_check);
                zXingScannerView.setResultHandler(stockcheck.this);
                zXingScannerView.startCamera();

                 qty = itemquantity.getText().toString();

                 poststockitem();
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




            }
        });

        itemsQuantityAddstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count<2)
                {


                }
                count++;
                itemquantity.setText(Integer.toString(count));
            }
        });
        itemsQuantityRemovestock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(count<2)
                {

                }
                else{
                    count--;
                }


                itemquantity.setText(Integer.toString(count));
            }
        });
        dialog.show();
        //   Window window = dialog.getWindow();
        //    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void stockDialogpatch(){
        dialog = new Dialog(stockcheck.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_stock_check);
        dialog.setTitle("My Custom Dialog");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        ok_dialog = (Button)dialog.findViewById(R.id.btn_Add_stock_ck);
        Cancel=dialog.findViewById(R.id.txt_cancel_dialog_stock_ck);
        partno_dialogtxt=dialog.findViewById(R.id.partno_dialog_stock_ck);

        itemquantity=dialog.findViewById(R.id.item_quantity_dialog_stock_ck);
        itemsQuantityAddstock=(ImageView)dialog.findViewById(R.id.items_quantity_add_dialog_stock_ck);
        itemsQuantityRemovestock=(ImageView) dialog.findViewById(R.id.items_quantity_rmv_stock_ck);

        // final String result2=resultcode;

        partno_dialogtxt.setText(result);

       itemquantity.setText(stockitemqty);

        stockqty=Integer.parseInt(stockitemqty);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count2=0;
                auto_stockcheck.setText("");

                dialog.cancel();

                zXingScannerView = new ZXingScannerView(stockcheck.this);
                zXingScannerView = (ZXingScannerView) findViewById(R.id.zscanstock_check);
                zXingScannerView.setResultHandler(stockcheck.this);
                zXingScannerView.startCamera();

            }
        });
        ok_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count2=0;
                auto_stockcheck.setText("");

                dialog.cancel();

                zXingScannerView = new ZXingScannerView(stockcheck.this);
                zXingScannerView = (ZXingScannerView) findViewById(R.id.zscanstock_check);
                zXingScannerView.setResultHandler(stockcheck.this);
                zXingScannerView.startCamera();



                qty = itemquantity.getText().toString();

                patchqty();
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




            }
        });

        itemsQuantityAddstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stockqty<2)
                {


                }
                stockqty++;
                itemquantity.setText(Integer.toString(stockqty));
            }
        });
        itemsQuantityRemovestock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(stockqty<2)
                {

                }
                else{
                    stockqty--;
                }


                itemquantity.setText(Integer.toString(stockqty));
            }
        });
        dialog.show();
        //   Window window = dialog.getWindow();
        //    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void stockDialogscanner(){
        dialog = new Dialog(stockcheck.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_stock_check);
        dialog.setTitle("My Custom Dialog");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        ok_dialog = (Button)dialog.findViewById(R.id.btn_Add_stock_ck);
        Cancel=dialog.findViewById(R.id.txt_cancel_dialog_stock_ck);
        partno_dialogtxt=dialog.findViewById(R.id.partno_dialog_stock_ck);

        itemquantity=dialog.findViewById(R.id.item_quantity_dialog_stock_ck);
        itemsQuantityAddstock=(ImageView)dialog.findViewById(R.id.items_quantity_add_dialog_stock_ck);
        itemsQuantityRemovestock=(ImageView) dialog.findViewById(R.id.items_quantity_rmv_stock_ck);

        // final String result2=resultcode;

        partno_dialogtxt.setText(partno);


        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count2=0;

                dialog.cancel();

                zXingScannerView = new ZXingScannerView(stockcheck.this);
                zXingScannerView = (ZXingScannerView) findViewById(R.id.zscanstock_check);
                zXingScannerView.setResultHandler(stockcheck.this);
                zXingScannerView.startCamera();

            }
        });
        ok_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auto_stockcheck.setText("");
                count2=0;
                dialog.cancel();

                zXingScannerView = new ZXingScannerView(stockcheck.this);
                zXingScannerView = (ZXingScannerView) findViewById(R.id.zscanstock_check);
                zXingScannerView.setResultHandler(stockcheck.this);
                zXingScannerView.startCamera();

                qty = itemquantity.getText().toString();

                poststockitem();
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




            }
        });

        itemsQuantityAddstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count<2)
                {


                }
                count++;
                itemquantity.setText(Integer.toString(count));
            }
        });
        itemsQuantityRemovestock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(count<2)
                {

                }
                else{
                    count--;
                }


                itemquantity.setText(Integer.toString(count));
            }
        });
        dialog.show();
        //   Window window = dialog.getWindow();
        //    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
    public void stockDialogscannerpatch(){
        dialog = new Dialog(stockcheck.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_stock_check);
        dialog.setTitle("My Custom Dialog");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        ok_dialog = (Button)dialog.findViewById(R.id.btn_Add_stock_ck);
        Cancel=dialog.findViewById(R.id.txt_cancel_dialog_stock_ck);
        partno_dialogtxt=dialog.findViewById(R.id.partno_dialog_stock_ck);

        itemquantity=dialog.findViewById(R.id.item_quantity_dialog_stock_ck);
        itemsQuantityAddstock=(ImageView)dialog.findViewById(R.id.items_quantity_add_dialog_stock_ck);
        itemsQuantityRemovestock=(ImageView) dialog.findViewById(R.id.items_quantity_rmv_stock_ck);

        // final String result2=resultcode;

        partno_dialogtxt.setText(partno);

        itemquantity.setText(stockitemqty);
        stockqty=Integer.parseInt(stockitemqty);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count2=0;

                dialog.cancel();

                zXingScannerView = new ZXingScannerView(stockcheck.this);
                zXingScannerView = (ZXingScannerView) findViewById(R.id.zscanstock_check);
                zXingScannerView.setResultHandler(stockcheck.this);
                zXingScannerView.startCamera();

            }
        });
        ok_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auto_stockcheck.setText("");
                count2=0;
                dialog.cancel();

                zXingScannerView = new ZXingScannerView(stockcheck.this);
                zXingScannerView = (ZXingScannerView) findViewById(R.id.zscanstock_check);
                zXingScannerView.setResultHandler(stockcheck.this);
                zXingScannerView.startCamera();

                qty = itemquantity.getText().toString();
                patchqty();
              //  poststockitem();
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




            }
        });

        itemsQuantityAddstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stockqty<2)
                {


                }
                stockqty++;
                itemquantity.setText(Integer.toString(stockqty));
            }
        });
        itemsQuantityRemovestock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(stockqty<2)
                {

                }
                else{
                    stockqty--;
                }


                itemquantity.setText(Integer.toString(stockqty));
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
    public void getstockcheck(){
        //Toast.makeText(getApplicationContext(),""+URL_DATA,Toast.LENGTH_SHORT).show();

        // client.get( "https://api.myjson.com/bins/j5f6b",new JsonHttpResponseHandler() {
        client.get( Backendserver.url+"/api/support/stockCheck/?format=json",new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                Log.e("error",""+response);
                //        Toast.makeText(getApplicationContext(),"res "+response.toString(),Toast.LENGTH_SHORT).show();
               /* for (int i = 0; i < response.length(); i++) {
                    JSONObject object=null;
                    try {
                        object = response.getJSONObject(i);
                        Log.e("error",""+object.toString());
                      //  productlists1.add(new Item(object));
                        //       Toast.makeText(getApplicationContext(),"res "+productlists.toString()+" "+productlists.size(),Toast.LENGTH_SHORT).show();
                        //   Log.e("errror"," "+productlists1.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } */
                // listshowrcy.setAdapter(new search_parts_adapter(productlists));

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(),"data not fetching"+errorResponse,Toast.LENGTH_SHORT);
            }
        });

    }
    public void getcheckitemlist(){
        //Toast.makeText(getApplicationContext(),""+URL_DATA,Toast.LENGTH_SHORT).show();

        // client.get( "https://api.myjson.com/bins/j5f6b",new JsonHttpResponseHandler() {
        client.get( Backendserver.url+"/api/support/stockCheckItem/?format=json",new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                Log.e("error",""+response);
                //        Toast.makeText(getApplicationContext(),"res "+response.toString(),Toast.LENGTH_SHORT).show();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject object=null;
                    try {
                        object = response.getJSONObject(i);


                        JSONObject data=object.getJSONObject("product");
                        String productpk= data.getString("pk");
                        String productpartno= data.getString("part_no");

                        object.put("part_nopro",productpartno);
                        object.put("productpk",productpk);


                        JSONObject stock=object.getJSONObject("stockReport");
                        String stockpk= stock.getString("pk");
                     //   String productpartno= data.getString("part_no");

                        object.put("stockpk",stockpk);
                       // object.put("productpk",productpk);

                        stockchecklist.add(new stockitem(object));
                        Log.e("error",""+productpartno.toString());
                        Log.e("error",""+productpk.toString());

                      //  productlists1.add(new Item(object));
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
    public void getstock(){
        //Toast.makeText(getApplicationContext(),""+URL_DATA,Toast.LENGTH_SHORT).show();

        // client.get( "https://api.myjson.com/bins/j5f6b",new JsonHttpResponseHandler() {
        client.get( Backendserver.url+"/api/support/stockCheck/?format=json",new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                Log.e("error",""+response);
                //        Toast.makeText(getApplicationContext(),"res "+response.toString(),Toast.LENGTH_SHORT).show();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject object=null;
                    try {
                        object = response.getJSONObject(i);
                        Log.e("error",""+object.toString());
                        countstock=object.getString("count");


                        JSONObject data=object.getJSONObject("data");
                        stockreportpk= data.getString("pk");

                      //  productlists1.add(new Item(object));
                        //       Toast.makeText(getApplicationContext(),"res "+productlists.toString()+" "+productlists.size(),Toast.LENGTH_SHORT).show();
                           Log.e("errror"," "+countstock.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                poststockreport();
                // listshowrcy.setAdapter(new search_parts_adapter(productlists));
            /*    if(countstock.equals("0"))
                {

                  /*  client.post(Backendserver.url + "/api/support/stockCheckReport/", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Toast.makeText(getApplicationContext(),"success item added",Toast.LENGTH_SHORT).show();


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(getApplicationContext(),"unsuccess ",Toast.LENGTH_SHORT).show();

                        }
                    });

                }
                else{

                    Log.e("error","stock already created today");


                } */
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(),"data not fetching"+errorResponse,Toast.LENGTH_SHORT);
            }
        });

    }
    public void poststockreport(){

        if(countstock.equals("0"))
        {

             client.post(Backendserver.url + "/api/support/stockCheckReport/", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Toast.makeText(getApplicationContext(),"Stock created",Toast.LENGTH_SHORT).show();

                               getstock();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(getApplicationContext(),"unsuccess ",Toast.LENGTH_SHORT).show();

                        }
                    });

        }
        else{
            Toast.makeText(getApplicationContext(),"Stock already created for today",Toast.LENGTH_SHORT).show();
            Log.e("error","stock already created today");


        }


    }
    public void poststockitem(){

           RequestParams params=new RequestParams();

           params.put("product",Integer.parseInt(productpk));
           params.put("qty",Integer.parseInt(qty));
           params.put("stockReport",Integer.parseInt(stockreportpk));

            client.post(Backendserver.url + "/api/support/stockCheckItem/", params,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Toast.makeText(getApplicationContext(),"Item added",Toast.LENGTH_SHORT).show();

                    stockchecklist.clear();
                    getcheckitemlist();


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getApplicationContext(),"unsuccess ",Toast.LENGTH_SHORT).show();

                }
            });




    }
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(stockcheck.this, permission) != PackageManager.PERMISSION_GRANTED) {

            //asking for permission prompt
            if (ActivityCompat.shouldShowRequestPermissionRationale(stockcheck.this, permission)) {
                //request for permissions and getting request code -- 5 for camera
                ActivityCompat.requestPermissions(stockcheck.this, new String[]{permission}, requestCode);

            } else  {
                //requesting permission
                ActivityCompat.requestPermissions(stockcheck.this, new String[]{permission}, requestCode);
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
            zXingScannerView = (ZXingScannerView) findViewById(R.id.zxscan);
            zXingScannerView.setResultHandler(this);
            zXingScannerView.startCamera();


        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }


    public void downloadstock(){

        File apkStorage = null;
        File outputFile = null;

        client.get( Backendserver.url+"/api/support/stockSheet/?value=76&created=2019-01-28T04:25:56.079752Z",new FileAsyncHttpResponseHandler(stockcheck.this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {

                Log.e("error",""+response.toString());



            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File errorResponse) {

                Toast.makeText(getApplicationContext(),"data not fetching"+errorResponse,Toast.LENGTH_SHORT).show();
            }
        });

    }



    public void patchqty(){

        RequestParams params = new RequestParams();
        params.put("qty", qty);
        client.patch(Backendserver.url + "/api/support/stockCheckItem/"+stockcheckitempk+"/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Toast.makeText(getApplicationContext(),"updated "+qty, Toast.LENGTH_SHORT).show();




            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "failure cart"+ stockcheckitempk, Toast.LENGTH_SHORT).show();
            }
        });


    }
    private class LongOperation extends AsyncTask<String, Void, String> {

        String Filepath;

        @Override
        protected String doInBackground(String... params)
        {
            try {
                String extStorageDirectory=Environment.getExternalStorageDirectory().toString();
                File folder = new File(extStorageDirectory, "IFIN-PDF");
                folder.mkdir();
                Filepath = "Quotationsheet-" + new Date().getDate()+new Date().getMonth()+
                        new Date().getYear()+".pdf";
                File file = new File(folder, Filepath);
                try {
                    file.createNewFile();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
                Downloader.DownloadFile
                        (Backendserver.url+"api/support/stockSheet/?value=76&created=2019-01-28T04:25:56.079752Z", file);//Paste your url here
            }
            catch (Exception e) {

            }
            return Filepath;
        }

        @Override
        protected void onPostExecute(String result) {

            //ProgressClass.progressClose();

            File file = new File(Environment.getExternalStorageDirectory()
                    + "/IFIN-PDF/" + result);

            PackageManager packageManager = getPackageManager();
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent .setType("application/pdf");
            List list = packageManager.queryIntentActivities(pdfIntent ,
                    PackageManager.MATCH_DEFAULT_ONLY);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            startActivity(intent);

        }

        @Override
        protected void onPreExecute() {

         //   ProgressClass.progressShow(stockcheck.this, "Connecting");
        }

    }

    public static class Downloader {
        private static final int  MEGABYTE = 1024 * 1024;

        public static void DownloadFile(String fileURL, File directory) {
            try {
                FileOutputStream file = new FileOutputStream(directory);
                URL url = new URL(fileURL);
                HttpURLConnection connection = (HttpURLConnection) url .openConnection();
                connection .setRequestMethod("GET");
                connection .setDoOutput(true);
                connection .connect();
                InputStream input = connection .getInputStream();
                byte[] buffer = new byte[MEGABYTE];
                int len = 0;
                while ((len = input .read(buffer)) > 0) {
                    file .write(buffer, 0, len );
                }
                file .close();
                } catch (FileNotFoundException e) {
                 e.printStackTrace();
                } catch (MalformedURLException e) {
                e.printStackTrace();
                } catch (IOException e) {
                e.printStackTrace();
                }
        }
    }

    public void createandDisplayPdf(String text) {

        Document doc = new Document();

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, "newFile.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            Paragraph p1 = new Paragraph(text);
            Font paraFont= new Font();
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(paraFont);

            //add paragraph to document
            doc.add(p1);

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally {
            doc.close();
        }

        viewPdf("newFile.pdf", "Dir");
    }

    // Method for opening a pdf file
    private void viewPdf(String file, String directory) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        Uri path = Uri.fromFile(pdfFile);

        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(stockcheck.this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }


}
