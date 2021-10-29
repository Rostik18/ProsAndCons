package com.example.prosandconsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.prosandconsapp.repository.DatabaseAdapter;
import com.example.prosandconsapp.repository.entities.GroupEntity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView groupsList;
    private TextView newGroupTitleTextView;

    GroupAdapter groupAdapter;
    private DatabaseAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbAdapter = new DatabaseAdapter(this);
        groupsList = findViewById(R.id.groupsList);
        newGroupTitleTextView = findViewById(R.id.newGroupTitleTextView);
    }

    @Override
    public void onResume() {
        super.onResume();

        dbAdapter.open();
        groupAdapter = new GroupAdapter(this, R.layout.group_item, dbAdapter.getGroups());
        groupsList.setAdapter(groupAdapter);
        dbAdapter.close();
    }

    public void addGroup(View view){

        String groupName = newGroupTitleTextView.getText().toString();
        if(groupName.isEmpty())
            return;

        newGroupTitleTextView.setText("");

        dbAdapter.open();
        dbAdapter.insertGroup(new GroupEntity(0, groupName));
        groupAdapter = new GroupAdapter(this, R.layout.group_item, dbAdapter.getGroups());
        groupsList.setAdapter(groupAdapter); // do i ned it?
        dbAdapter.close();
    }
}