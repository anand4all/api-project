package com.example.gaurav.inventory.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.gaurav.inventory.R;
import com.example.gaurav.inventory.entities.Item;
import com.example.gaurav.inventory.entities.getinventoryitem;

import java.util.ArrayList;

public class Auto_inventory_adapter extends ArrayAdapter<getinventoryitem> {

    Context context;
    int resource, textViewResourceId;
    ArrayList<getinventoryitem> items, tempItems, suggestions;

    public Auto_inventory_adapter(Context context, int resource, int textViewResourceId,ArrayList<getinventoryitem> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;// this makes the difference.
        suggestions = new ArrayList<getinventoryitem>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_people, parent, false);
        }
        getinventoryitem item = items.get(position);
        if (item != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(item.getPart_no());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((getinventoryitem) resultValue).getPart_no();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (getinventoryitem item : items) {
                    if (item.getPart_no().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(item);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<getinventoryitem> filterList = (ArrayList<getinventoryitem>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (getinventoryitem item : filterList) {
                    add(item);
                    notifyDataSetChanged();
                }
            }
        }
    };
}

