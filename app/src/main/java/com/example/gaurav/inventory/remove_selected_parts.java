package com.example.gaurav.inventory;

import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaurav.inventory.Adapters.remove_selected_adapter;
import com.example.gaurav.inventory.entities.remove_selected_item;

import java.util.ArrayList;
import java.util.List;

public class remove_selected_parts extends AppCompatActivity {
    RecyclerView rvs_partsshow;
    Toolbar toolbar;
    List<remove_selected_item> Grnlist = new ArrayList<>();
    remove_selected_adapter adapter;

    Dialog dialog;
    Button ok_dialog,btn_remove_selected_ok;
    TextView Cancel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_selected_parts);
        rvs_partsshow=findViewById(R.id.rvs_partsshow);
        btn_remove_selected_ok=findViewById(R.id.ok_btn_remove_selected);

        Grnlist.add(new remove_selected_item("TATA"));
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

        setToogleEvent(rvs_partsshow);
        ok_btn();

    }

    private void setToogleEvent(RecyclerView rvs_partsshow) {
        for(int i=0;i<rvs_partsshow.getChildCount();i++)

        {
           final CardView cardView=(CardView) rvs_partsshow.getChildAt(i);
           cardView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if(cardView.getCardBackgroundColor().getDefaultColor()== -1)
                   {
                      cardView.setCardBackgroundColor(Color.parseColor("#FF6F00"));
                   }
                   else
                   {
                      cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                   }
               }
           });

        }

    }


    private void ok_btn() {
        btn_remove_selected_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCustomAlertDialog();

            }
        });
    }




    public void MyCustomAlertDialog(){
        dialog = new Dialog(remove_selected_parts.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_remove);
        dialog.setTitle("My Custom Dialog");

        ok_dialog = (Button)dialog.findViewById(R.id.btn_remove_dialog);
        Cancel=dialog.findViewById(R.id.txt_cancel_dialog);

        ok_dialog.setEnabled(true);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });
        ok_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Item Removed", Toast.LENGTH_LONG).show();
            }
        });

        dialog.show();
       Window window = dialog.getWindow();
       window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
    //for changing the text color of searchview
}