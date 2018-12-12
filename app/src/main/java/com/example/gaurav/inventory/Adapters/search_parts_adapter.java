package com.example.gaurav.inventory.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaurav.inventory.R;
import com.example.gaurav.inventory.entities.Item;

import java.util.ArrayList;


public class search_parts_adapter extends RecyclerView.Adapter<search_parts_adapter.Holderview> {
    private Context context;
    ArrayList<Item> product;


    public search_parts_adapter(ArrayList<Item> productlist,Context context) {
        this.product = productlist;
        this.context = context;
    }


    @NonNull
    @Override
    public search_parts_adapter.Holderview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.customitem,parent,false);

        return new search_parts_adapter.Holderview(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull search_parts_adapter.Holderview holder, final int position) {
        Item item=product.get(position);
        holder.v_name.setText(item.getPart_no());
        holder.product_detail.setText("Description : "+item.getDescription_1()+" "+ item.getDescription_2());
        holder.price.setText("Price : "+item.getPrice());
        holder.weight.setText("Weight : "+item.getWeight());
        holder.custom_no.setText("Custom No : "+item.getCustoms_no());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {

                Toast.makeText(context, "click on " + product.get(position).getPart_no(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }
    public void setfilter(ArrayList<Item>listitem)
    {

       /* if(listitem==null)
        {
            imageView.setVisibility(View.VISIBLE);
            notifyDataSetChanged();

        }*/
            product = new ArrayList<>();
            product.addAll(listitem);
            notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return product.size();
    }
    class Holderview extends RecyclerView.ViewHolder
    {
       // ImageView imageView;

        TextView v_name,product_detail,price,weight,custom_no;
        Holderview(View itemview)
        {
            super(itemview);

            v_name = (TextView) itemView.findViewById(R.id.product_id);
            product_detail = (TextView) itemView.findViewById(R.id.product_detail);
            price = (TextView) itemView.findViewById(R.id.price);
            weight = (TextView) itemView.findViewById(R.id.weight);
            custom_no = (TextView) itemView.findViewById(R.id.custom_no);
            //imageView=itemView.findViewById(R.id.noresult_img);
        }
    }
}


