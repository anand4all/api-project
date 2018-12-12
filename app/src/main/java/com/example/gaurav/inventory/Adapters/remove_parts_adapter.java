package com.example.gaurav.inventory.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaurav.inventory.R;
import com.example.gaurav.inventory.entities.remove_item;

import java.util.ArrayList;
import java.util.List;

public class remove_parts_adapter extends RecyclerView.Adapter<remove_parts_adapter.Holderview>{


    private List<remove_item> productlist_remove;
    private Context context;
    public remove_parts_adapter(List<remove_item> productlist_remove, Context context) {
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
        holder.v_name.setText(productlist_remove.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {

                Toast.makeText(context, "click on " + productlist_remove.get(position).getName(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override    public int getItemCount() {
        return productlist_remove.size();
    }
    public void setfilter(List<remove_item> listitem)
    {
        productlist_remove=new ArrayList<>();
        productlist_remove.addAll(listitem);
        notifyDataSetChanged();
    }
    class Holderview extends RecyclerView.ViewHolder
    {

        TextView v_name;
        Holderview(View itemview)
        {
            super(itemview);

            v_name = (TextView) itemView.findViewById(R.id.productrv_detail);
        }
    }

}
