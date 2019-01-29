package com.example.gaurav.inventory.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaurav.inventory.Grn_project_details;
import com.example.gaurav.inventory.R;
import com.example.gaurav.inventory.entities.remove_item;
import com.example.gaurav.inventory.remove_selected_parts;

import java.util.ArrayList;
import java.util.List;

public class remove_parts_adapter extends RecyclerView.Adapter<remove_parts_adapter.Holderview>{


    private ArrayList<remove_item> productlist_remove;
    private Context context;
    public remove_parts_adapter(ArrayList<remove_item> productlist_remove, Context context) {
        this.productlist_remove= productlist_remove;
        this.context = context;
    }
    @Override    public remove_parts_adapter.Holderview onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.customremove_item,parent,false);
        return new Holderview(layout);
    }
    @Override
    public void onBindViewHolder(remove_parts_adapter.Holderview holder, final int position) {
        holder.description.setText(productlist_remove.get(position).getDescription1());
        holder.part_no.setText(productlist_remove.get(position).getPartno());
        holder.weight.setText(productlist_remove.get(position).getWeight());
        holder.qty.setText(productlist_remove.get(position).getQty());
        holder.price.setText(productlist_remove.get(position).getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {

                Toast.makeText(context, "click on " + productlist_remove.get(position).getPartno(),
                        Toast.LENGTH_LONG).show();
             //   context.startActivity(new Intent(context,remove_selected_parts.class)
                  //       .putExtra("productpk", productlist_remove.get(position).getProductpk())
                   //      .putExtra("qty", productlist_remove.get(position).getQty()));
            }
        });
    }
    @Override
    public int getItemCount() {
        return productlist_remove.size();
    }

    class Holderview extends RecyclerView.ViewHolder
    {


        TextView part_no,qty,barcode,description,price,weight;
        Holderview(View itemview)
        {
            super(itemview);

            description = (TextView) itemView.findViewById(R.id.inventory_rv);
            part_no = (TextView) itemView.findViewById(R.id.inventory_partno_rv);
            weight = (TextView) itemView.findViewById(R.id.inventory_barcode_rv);
            qty = (TextView) itemView.findViewById(R.id.inventory_qty_rv);
            price = (TextView) itemView.findViewById(R.id.inventory_price_rv);
        }
    }

}
