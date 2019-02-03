package com.example.gaurav.inventory.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    private int count=0;


    public materialadapter(ArrayList<materialitem> list, Context context) {
        this.materiallist = list;
        this.context = context;
        this.setHasStableIds(true);
    }


    @Override
    public materialadapter.Holderview onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.materialitem,parent,false);
        return new materialadapter.Holderview(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull final materialadapter.Holderview holder, int position) {


        if(count<materiallist.size()) {
            count++;

            materialitem materialitem = materiallist.get(position);

            //    holder.partno.setText("Part no : "+materialitem.getPartno());
            //    holder.qty.setText("Quantity : "+materialitem.getQtymat());
            //   holder.price.setText("Price : "+materialitem.getPricemat());

            holder.title.setText(" " + materialitem.getTitlemat());
            holder.user.setText("Engineer : " + materialitem.getUsernam());
            holder.comm_nr.setText("Comm nr : " + materialitem.getComm_nr());
            holder.dateofisuue.setText(materialitem.getDateofisuue());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Toast.makeText(context, "click on " + materiallist.get(position).getPartno(),
                    //      Toast.LENGTH_SHORT).show();
                    if (holder.lin_bmo_details.getVisibility() == View.VISIBLE) {
                        holder.lin_bmo_details.setVisibility(View.GONE);
                    } else {
                        holder.lin_bmo_details.setVisibility(View.VISIBLE);
                    }

                    //  context.startActivity(new Intent(context, Grn_project_details.class).putExtra("projectpk", materiallist.get(position).getPartno()));
                }

            });

            //    holder.lin_bmo_details.setVisibility(View.VISIBLE);
            Log.e("error", "" + materiallist.size());

            for (int j = 0; j < materiallist.get(position).getMaterialissueitems().size(); j++) {
                Log.e("error", "" + materiallist.get(position).getMaterialissueitems().size());

                //  Log.e("error","adapterloop");
                LinearLayout llh = new LinearLayout(context);
                llh.setOrientation(LinearLayout.HORIZONTAL);
                llh.setWeightSum(3);
                llh.setPadding(5, 5, 5, 5);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                p.weight = 1;

                LinearLayout.LayoutParams p_title = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                p_title.weight = 1;

                TextView sub_item_name = new TextView(context);
                TextView sub_order_qty = new TextView(context);
                TextView sub_order_price = new TextView(context);

                //TextView  sub_order_amount= new TextView(context);

                sub_item_name.setText(materiallist.get(position).getMaterialissueitems().get(j).getPartno());
                sub_item_name.setPadding(0, 0, 0, 0);
                sub_order_qty.setText(materiallist.get(position).getMaterialissueitems().get(j).getQuantity());
                sub_order_qty.setGravity(Gravity.CENTER);
                sub_order_price.setText(materiallist.get(position).getMaterialissueitems().get(j).getPrice());
                sub_order_price.setGravity(Gravity.CENTER);
                //sub_order_amount.setText(resultList.get(position).getCatProducts().get(j).getDel_item_total_price());
                // sub_order_amount.setGravity(Gravity.RIGHT);
                // sub_order_amount.setPadding(0,0,0,5);

                sub_item_name.setLayoutParams(p_title);
                sub_order_qty.setLayoutParams(p);
                sub_order_price.setLayoutParams(p);
                //sub_order_amount.setLayoutParams(p);

                sub_item_name.setGravity(Gravity.LEFT);
                sub_order_qty.setGravity(Gravity.CENTER);
                sub_order_price.setGravity(Gravity.CENTER);
                //sub_order_amount.setGravity(Gravity.RIGHT);


                llh.addView(sub_item_name);
                llh.addView(sub_order_qty);
                llh.addView(sub_order_price);
                //llh.addView(sub_order_amount);
                holder.lin_order_details.addView(llh);


            }

            //  materiallist.clear();

            //  holder.itemView.setTag(materiallist.get(position));

        }
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
      //  notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return materiallist.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class Holderview extends RecyclerView.ViewHolder
    {

        TextView title,price,partno,qty,dateofisuue,user,comm_nr;
        LinearLayout lin_order_details,lin_bmo_details;
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
            lin_order_details=(LinearLayout)itemview.findViewById(R.id.lin_order_details);
            lin_bmo_details=(LinearLayout)itemview.findViewById(R.id.lin_bmo_details);

        }
    }
}

