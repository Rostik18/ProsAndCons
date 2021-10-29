package com.example.prosandconsapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.prosandconsapp.repository.DatabaseAdapter;
import com.example.prosandconsapp.repository.entities.*;

public class GroupAdapter extends ArrayAdapter<GroupEntity> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<GroupEntity> groupList;

    private MainActivity parentActivity;

    private DatabaseAdapter dbAdapter;

    GroupAdapter(Context context, int resource, ArrayList<GroupEntity> groups) {
        super(context, resource, groups);
        this.groupList = groups;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);

        parentActivity = (MainActivity)context;
        dbAdapter = new DatabaseAdapter(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final GroupEntity group = groupList.get(position);

        viewHolder.titleTextView.setText(group.title);
        viewHolder.updateView(group.id);

        viewHolder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { viewHolder.onMinus(group.id); }
        });
        viewHolder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { viewHolder.onPlus(group.id); }
        });
        viewHolder.deleteGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { viewHolder.onDelete(group.id); }
        });

        return convertView;
    }

    private class ViewHolder {
        final Button plusButton, minusButton, deleteGroupButton;
        final TextView titleTextView, plusesTextView, minusesTextView, totalTextView;
        ViewHolder(View view){
            plusButton = view.findViewById(R.id.plusButton);
            minusButton = view.findViewById(R.id.minusButton);
            deleteGroupButton = view.findViewById(R.id.deleteGroupButton);
            titleTextView = view.findViewById(R.id.titleTextView);
            plusesTextView = view.findViewById(R.id.plusesTextView);
            minusesTextView = view.findViewById(R.id.minusesTextView);
            totalTextView = view.findViewById(R.id.totalTextView);
        }

        public void updateView(long groupId){

            dbAdapter.open();
            long total = dbAdapter.getRecordsCount(groupId);

            if(total == 0){
                plusesTextView.setText(" 0");
                minusesTextView.setText(" 0");
                totalTextView.setText(" 0");
                dbAdapter.close();
                return;
            }

            long positives = dbAdapter.getRecordsCount(groupId, true);
            long negatives = dbAdapter.getRecordsCount(groupId, false);
            dbAdapter.close();

            plusesTextView.setText(String.format(" %s (%.2f)", positives, (double) positives / total));
            minusesTextView.setText(String.format(" %s (%.2f)", negatives, (double) negatives / total));
            totalTextView.setText(String.format(" %s", total));
        }

        public void onPlus(long groupId){

            dbAdapter.open();
            dbAdapter.insertRecord(new RecordEntity(true, groupId));
            dbAdapter.close();

            updateView(groupId);
        }

        public void onMinus(long groupId){

            dbAdapter.open();
            dbAdapter.insertRecord(new RecordEntity(false, groupId));
            dbAdapter.close();

            updateView(groupId);
        }

        public void onDelete(long groupId){

            dbAdapter.open();
            dbAdapter.deleteGroup(groupId);
            dbAdapter.close();

            parentActivity.onResume();
        }
    }
}