package com.rbc.yelp.ui.main;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.rbc.yelp.R;
import com.rbc.yelp.databinding.MainListCatItemBinding;
import com.rbc.yelp.databinding.MainListSubItemBinding;
import com.rbc.yelp.services.models.Business;
import com.rbc.yelp.services.models.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.RequiresApi;

public class MainListAdapter extends BaseExpandableListAdapter {
    // key: category alias
    // value: business
    private Map<String, List<Business>> catMap = new HashMap<>();
    // corresponding category alias list when adding it to the map
    private List<String> groupList = new ArrayList<>();

    @Override
    public int getGroupCount() {
        return catMap.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int getChildrenCount(int groupPosition) {
        return Objects.requireNonNull(catMap.getOrDefault(groupList.get(groupPosition), new ArrayList<>())).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return getChildAsBusiness(groupPosition,childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // the api return at most 1000 results
        return groupPosition * 1000L + childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        MainListCatItemBinding binding = MainListCatItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        binding.catItemTitle.setText(groupList.get(groupPosition));
        binding.catItemCount.setText(String.valueOf(getChildrenCount(groupPosition)));
        if (isExpanded) {
            binding.catItemIndicator.setImageResource(R.drawable.ic_expand_less_black_24dp);
        } else {
            binding.catItemIndicator.setImageResource(R.drawable.ic_expand_more_black_24dp);
        }
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        MainListSubItemBinding binding = MainListSubItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        binding.subItemText.setText(getChildAsBusiness(groupPosition,childPosition).getName());
        return binding.getRoot();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    protected void submit(List<Business> input) {
        for (Business business : input) {
            for (Category category : business.getCategories()) {
                // add business to each category list
                if (!catMap.containsKey(category.getAlias())) {
                    catMap.put(category.getAlias(), new ArrayList<>());
                    groupList.add(category.getAlias());
                }
                catMap.get(category.getAlias()).add(business);
            }
        }
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Business getChildAsBusiness(int groupPosition, int childPosition){
        return Objects.requireNonNull(catMap.getOrDefault(groupList.get(groupPosition), new ArrayList<>())).get(childPosition);
    }
}
