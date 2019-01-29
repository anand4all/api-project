package com.example.gaurav.inventory.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaurav.inventory.Grn_project_details;
import com.example.gaurav.inventory.R;
import com.example.gaurav.inventory.entities.Grn_item;
import com.example.gaurav.inventory.entities.Grn_item_detail;

import java.util.ArrayList;

public class Grn_detail_adapter extends RecyclerView.Adapter<Grn_detail_adapter.Holderview> {

    private ArrayList<Grn_item_detail> Grnlist;
    private Context context;
    public Grn_detail_adapter(ArrayList<Grn_item_detail>Grnlist, Context context) {
        this.Grnlist = Grnlist;
        this.context = context;
    }
    @Override
    public Grn_detail_adapter.Holderview onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.customgrn_detail,parent,false);
        return new Grn_detail_adapter.Holderview(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull Grn_detail_adapter.Holderview holder, final int position) {
       // Grn_item_detail grn_item_detail=Grnlist.get(position);
        holder.part_no.setText(Grnlist.get(position).getPartno());
        holder.qty.setText(Grnlist.get(position).getQty());
        holder.barcode.setText(Grnlist.get(position).getBarcode());
        holder.description.setText(Grnlist.get(position).getDescription1());
        holder.price.setText(Grnlist.get(position).getPrice());
       // holder.v_name.setText("Project Name : "+grn_item_detail.getTitle()+"\n\n"+"Date : "+grn_item_detail.getMobile());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                Toast.makeText(context, "click on " + Grnlist.get(position).getPartno(),
                        Toast.LENGTH_SHORT).show();
              //  context.startActivity(new Intent(context, Grn_project_details.class));

            }
        });

    }


    @Override
    public int getItemCount() {
        return Grnlist.size();
    }

    class Holderview extends RecyclerView.ViewHolder
    {

        TextView part_no,qty,barcode,description,price;
        Holderview(View itemview)
        {
            super(itemview);

            barcode = (TextView) itemView.findViewById(R.id.grn_barcode);
            description = (TextView) itemView.findViewById(R.id.grn_description1);

            part_no = (TextView) itemView.findViewById(R.id.grn_parts_detail);
            qty = (TextView) itemView.findViewById(R.id.qty_grndetail);
            price = (TextView) itemView.findViewById(R.id.price_grn_detail);
        }
    }
}

