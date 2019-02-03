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

import com.example.gaurav.inventory.Add_parts;
import com.example.gaurav.inventory.Grn_project_details;
import com.example.gaurav.inventory.R;
import com.example.gaurav.inventory.entities.Grn_item;
import com.example.gaurav.inventory.entities.Item;

import java.util.ArrayList;

public class Grn_adapter extends RecyclerView.Adapter<Grn_adapter.Holderview>{
    private ArrayList<Grn_item> Grnlist;
    private Context context;
    public Grn_adapter(ArrayList<Grn_item> Grnlist, Context context) {
        this.Grnlist = Grnlist;
        this.context = context;
    }
    @Override
    public Grn_adapter.Holderview onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.customgrn,parent,false);
        return new Grn_adapter.Holderview(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull Holderview holder, final int position) {
        Grn_item grn_item=Grnlist.get(position);
        holder.v_name.setText(" "+grn_item.getTitle());
        holder.servicename.setText(" "+grn_item.getName());
        holder.mobile.setText(" "+grn_item.getMobile());
        holder.date.setText(" "+grn_item.getDate());
        holder.status.setText(" "+grn_item.getStatus());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {


             //   Toast.makeText(context, "click on " + Grnlist.get(position).getPk(),
                //        Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, Grn_project_details.class).putExtra("projectpk", Grnlist.get(position).getPk()));

            }
        });

    }

    public void setfilter(ArrayList<Grn_item>listitem)
    {

       /* if(listitem==null)
        {
            imageView.setVisibility(View.VISIBLE);
            notifyDataSetChanged();

        }*/
        Grnlist = new ArrayList<>();
        Grnlist.addAll(listitem);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return Grnlist.size();
    }

    class Holderview extends RecyclerView.ViewHolder
    {

        TextView v_name,servicename,mobile,date,status;
        Holderview(View itemview)
        {
            super(itemview);

            v_name = (TextView) itemView.findViewById(R.id.companyname_grn);
            servicename=itemview.findViewById(R.id.servicename);
            mobile=itemview.findViewById(R.id.mobile);
            status=itemview.findViewById(R.id.status);
            date=itemview.findViewById(R.id.date);

        }
    }
}

