package com.example.gaurav.inventory.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaurav.inventory.Interface.Itemclicklistner;
import com.example.gaurav.inventory.R;
import com.example.gaurav.inventory.common.common;
import com.example.gaurav.inventory.entities.Grn_item;
import com.example.gaurav.inventory.entities.remove_selected_item;

import java.util.ArrayList;
import java.util.List;

public class remove_selected_adapter extends RecyclerView.Adapter<Holderview> {


    private ArrayList<Grn_item> rvslist;
    private Context context;
    int rowindex=-1;
    public int projectpk;


    public remove_selected_adapter(ArrayList<Grn_item> Grnlist, Context context) {
        this.rvslist = Grnlist;
        this.context = context;
    }
    @Override
    public Holderview onCreateViewHolder(ViewGroup parent, int viewType) {
       LayoutInflater inflater=LayoutInflater.from(context);
       View layout=inflater.inflate(R.layout.customremove_selected,parent,false);
        return new Holderview(layout);
    }

    @Override
    public void onBindViewHolder(Holderview holder, int position) {
        holder.v_name.setText(rvslist.get(position).getTitle());
        holder.setItemclicklistner(new Itemclicklistner() {
            @Override
            public void onClick(View view, int position) {
                rowindex=position;
                common.currentitem=rvslist.get(position);
                notifyDataSetChanged();
                projectpk=Integer.parseInt(rvslist.get(position).getPk());

             //   Toast.makeText(context, "click on " + rvslist.get(position).getPk(),

                   //     Toast.LENGTH_SHORT).show();
            }
        });

       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rowindex=position;

                Toast.makeText(context, "click on " + rvslist.get(position).getName(),

                        Toast.LENGTH_SHORT).show();
            }
        });*/


        if(rowindex==position)
        {

            holder.itemView.setBackgroundColor(Color.parseColor("#82CAFA"));

        }
        else{
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }



    @Override
    public int getItemCount() {
        return rvslist.size();
    }


    public int projectpk() {

        return projectpk;
    }
}
class Holderview extends RecyclerView.ViewHolder implements View.OnClickListener
{
    Itemclicklistner itemclicklistner;

    TextView v_name;


    Holderview(View itemview)
    {
        super(itemview);

        v_name = (TextView) itemView.findViewById(R.id.productrvs_id);

        itemview.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        itemclicklistner.onClick(v,getAdapterPosition());
    }

    public void setItemclicklistner(Itemclicklistner itemclicklistner) {
        this.itemclicklistner = itemclicklistner;
    }
}

