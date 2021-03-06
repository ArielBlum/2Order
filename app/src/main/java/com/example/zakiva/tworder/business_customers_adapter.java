package com.example.zakiva.tworder;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zakiva on 11/27/15.
 */

public class business_customers_adapter extends BaseExpandableListAdapter {

    private static final String TAG = ">>>>debug";
    private LayoutInflater inflater;
    private ArrayList<business_customer_group> mParent;
    //private RatingBar urgentBar;
    private Context context;

    public business_customers_adapter(Context context, ArrayList<business_customer_group> parent){
        mParent = parent;
        inflater = LayoutInflater.from(context);
        this.context = context;

    }


    @Override
    //counts the number of group/parent items so the list knows how many times calls getGroupView() method
    public int getGroupCount() {
        return mParent.size();
    }

    @Override
    //counts the number of children items so the list knows how many times calls getChildView() method
    public int getChildrenCount(int i) {
        return mParent.get(i).getArrayChildren().size();
    }

    @Override
    //gets the title of each parent/group
    public Object getGroup(int i) {
        return mParent.get(i).getTitle();
    }

    @Override
    //gets the name of each item
    public Object getChild(int i, int i1) {
        return mParent.get(i).getArrayChildren().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    //in this method you must set the text to see the parent/group on the list
    public View getGroupView(final int groupPosition, boolean b, View view, ViewGroup viewGroup) {

        ViewHolder holder = new ViewHolder();
        holder.groupPosition = groupPosition;

        if (view == null) {
            view = inflater.inflate(R.layout.business_customer_group, viewGroup,false);
        }
        TextView textView = (TextView) view.findViewById(R.id.list_item_text_view);
        textView.setText(getGroup(groupPosition).toString());
        view.setTag(holder);

        //return the entire view
        return view;
    }

    @Override
    //in this method you must set the text to see the children on the list
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {

        ViewHolder holder = new ViewHolder();
        holder.childPosition = childPosition;
        holder.groupPosition = groupPosition;

        view = inflater.inflate(R.layout.business_list_item, viewGroup,false);

        TextView textView = (TextView) view.findViewById(R.id.list_item_text_child);

        textView.setText(mParent.get(groupPosition).getArrayChildren().get(childPosition));
        TextView key = (TextView) view.findViewById(R.id.key);
        key.setText(mParent.get(groupPosition).getItemKey());
        Button changeStatusButton = (Button) view.findViewById(R.id.statusButton);
        ((ViewGroup) changeStatusButton.getParent()).removeView(changeStatusButton);
        Button information_button = (Button) view.findViewById(R.id.information_button);

        information_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String phone = mParent.get(groupPosition).getArrayChildren().get(0).substring(7);
                String name = mParent.get(groupPosition).getTitle().substring(10);
                String counter = mParent.get(groupPosition).getArrayChildren().get(1).substring(14);

                Log.i(TAG, "phone = " + phone);
                Log.i(TAG, "name = " + name);
                Log.i(TAG, "counter = " + counter);

                Intent intent = new Intent(context, single_customer_information.class);
                intent.putExtra("parse", "false");
                intent.putExtra("name", name);
                intent.putExtra("phone", phone);
                intent.putExtra("date", mParent.get(groupPosition).getItemKey());
                intent.putExtra("counter", counter);
                context.startActivity(intent);
            }
        });

        if(childPosition!=2) {
            ((ViewGroup) information_button.getParent()).removeView(information_button);
        }
        else
            information_button.setBackgroundResource(R.drawable.contact_info);

        view.setTag(holder);

        //return the entire view
        return view;
    }



    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        /* used to make the notifyDataSetChanged() method work */
        super.registerDataSetObserver(observer);
    }

    protected class ViewHolder {
        protected int childPosition;
        protected int groupPosition;
    }

}


