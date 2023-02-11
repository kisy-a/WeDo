package com.example.wedoapp;

import android.widget.Filter;

import java.util.ArrayList;

public class FilterTasks  extends Filter {

    private AdapterOrderBookingTransactions adapterOrderBookingTransactions;
    private ArrayList<Model_Order> filterList;

    public FilterTasks(AdapterOrderBookingTransactions adapterOrderBookingTransactions, ArrayList<Model_Order> filterList) {
        this.adapterOrderBookingTransactions = adapterOrderBookingTransactions;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        //check and valid data for search query
        if(constraint != null && constraint.length() > 0){
            //searching filed not empty, searching something, perform search

            //change to uppercase to make case insensitive
            constraint = constraint.toString().toUpperCase();

            //store filtered list
            ArrayList<Model_Order> filteredModel = new ArrayList<>();
            for (int i=0; i<filterList.size(); i++){
                //check, search by service name
                if (filterList.get(i).getOrderID().toUpperCase().contains(constraint)) {
                    filteredModel.add(filterList.get(i));
                }
            }
            results.count = filteredModel.size();
            results.values = filteredModel;
        }
        else {
            //searching filed empty, not searching, return original/all/complete list
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapterOrderBookingTransactions.OrderBookingTransactionsList = (ArrayList<Model_Order>) results.values;
        //refresh adapter
        adapterOrderBookingTransactions.notifyDataSetChanged();
    }
}
