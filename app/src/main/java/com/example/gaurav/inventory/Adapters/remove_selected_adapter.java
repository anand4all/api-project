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

import com.example.gaurav.inventory.R;
import com.example.gaurav.inventory.entities.remove_selected_item;

import java.util.List;

public class remove_selected_adapter extends RecyclerView.Adapter<remove_selected_adapter.Holderview>{


    private List<remove_selected_item> rvslist;
    private Context context;
    public remove_selected_adapter(List<remove_selected_item> Grnlist, Context context) {
        this.rvslist = Grnlist;
        this.context = context;
    }
    @Override
    public remove_selected_adapter.Holderview onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.customremove_selected,parent,false);
        return new remove_selected_adapter.Holderview(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull remove_selected_adapter.Holderview holder, final int position) {
        holder.v_name.setText(rvslist.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {

                Toast.makeText(context, "click on " + rvslist.get(position).getName(),

                        Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return rvslist.size();
    }

    class Holderview extends RecyclerView.ViewHolder
    {

        TextView v_name;

        Holderview(View itemview)
        {
            super(itemview);

            v_name = (TextView) itemView.findViewById(R.id.productrvs_id);


        }
    }
}

