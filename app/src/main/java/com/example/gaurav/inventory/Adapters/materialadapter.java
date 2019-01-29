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
import com.example.gaurav.inventory.entities.materialitem;

import java.util.ArrayList;

public class materialadapter extends RecyclerView.Adapter<materialadapter.Holderview>{
    private ArrayList<materialitem> materiallist;
    private Context context;
    public materialadapter(ArrayList<materialitem> list, Context context) {
        this.materiallist = list;
        this.context = context;
    }
    @Override
    public materialadapter.Holderview onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.materialitem,parent,false);
        return new materialadapter.Holderview(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull materialadapter.Holderview holder, final int position) {
        materialitem materialitem=materiallist.get(position);
        holder.partno.setText("Part no : "+materialitem.getPartno());
        holder.title.setText(" "+materialitem.getTitlemat());
        holder.qty.setText("Quantity : "+materialitem.getQtymat());
        holder.price.setText("Price : "+materialitem.getPricemat());
        holder.user.setText("Engineer : "+materialitem.getUsernam());
        holder.comm_nr.setText("Comm nr : "+materialitem.getComm_nr());
        holder.dateofisuue.setText(materialitem.getDateofisuue());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {


                Toast.makeText(context, "click on " + materiallist.get(position).getPartno(),
                        Toast.LENGTH_SHORT).show();
              //  context.startActivity(new Intent(context, Grn_project_details.class).putExtra("projectpk", materiallist.get(position).getPartno()));

            }
        });

    }

    public void setfilter(ArrayList<materialitem>listitem)
    {

       /* if(listitem==null)
        {
            imageView.setVisibility(View.VISIBLE);
            notifyDataSetChanged();

        }*/
        materiallist = new ArrayList<>();
        materiallist.addAll(listitem);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return materiallist.size();
    }

    class Holderview extends RecyclerView.ViewHolder
    {

        TextView title,price,partno,qty,dateofisuue,user,comm_nr;
        Holderview(View itemview)
        {
            super(itemview);

            title = (TextView) itemView.findViewById(R.id.titlemat);
            price=itemview.findViewById(R.id.pricemat);
            qty=itemview.findViewById(R.id.qty);
            partno=itemview.findViewById(R.id.partno);
            dateofisuue=itemview.findViewById(R.id.dateofissue);
            user=itemview.findViewById(R.id.usermat);
            comm_nr=itemview.findViewById(R.id.comm_nr);

        }
    }
}

