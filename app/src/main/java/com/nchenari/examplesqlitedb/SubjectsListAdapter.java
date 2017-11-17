package com.nchenari.examplesqlitedb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.nchenari.examplesqlitedb.model.Subject;

import java.util.ArrayList;

/**
 * Created by nimachenari on 4/3/15.
 */

public class SubjectsListAdapter extends ArrayAdapter {

    private Context context;
    private int resourceId;
    private ArrayList items;

    public SubjectsListAdapter(Context context, int resourceId, ArrayList items) {
        super(context, resourceId, items);
        this.context = context;
        this.resourceId = resourceId;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Subject subjectToView = (Subject) getItem(position);

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(resourceId, null);
        }

        TextView listItemTextView = (TextView) convertView.findViewById(R.id.listItemTextView);
        listItemTextView.setText(items.get(position).toString());

        Button listItemViewButton = (Button) convertView.findViewById(R.id.listItemViewButton);
        listItemViewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SubjectInfoActivity.class);
                intent.putExtra("SubjectToView", subjectToView);
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

}
