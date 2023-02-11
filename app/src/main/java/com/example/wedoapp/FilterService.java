package com.example.wedoapp;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.Collections;

public class FilterService extends Filter {

    private AdapterServiceItem adapterServiceItem;
    private ArrayList<Model_Service> filterServiceList;

    public FilterService(AdapterServiceItem adapterServiceItem, ArrayList<Model_Service> filterServiceList) {
        this.adapterServiceItem = adapterServiceItem;
        this.filterServiceList = filterServiceList;
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
            ArrayList<Model_Service> filteredModel = new ArrayList<>();
            for (int i=0; i<filterServiceList.size(); i++){
                //check, search by service name
                if (filterServiceList.get(i).getSTitle().toUpperCase().contains(constraint)) {
                    filteredModel.add(filterServiceList.get(i));
                }
            }
            results.count = filteredModel.size();
            results.values = filteredModel;
        }
        else {
            //searching filed empty, not searching, return original/all/complete list
            results.count = filterServiceList.size();
            results.values = filterServiceList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapterServiceItem.ServiceItemList = (ArrayList<Model_Service>) results.values;
        //refresh adapter
        adapterServiceItem.notifyDataSetChanged();
    }
}
