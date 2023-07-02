package com.example.karnekalist;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<RecyclerAdapterModel> arrayListTasks = new ArrayList<>();
    Intent iNext;
    RecyclerView recyclerTasks;
    FloatingActionButton floatingActionButton;
    TextView header;
    EditText newTask;
    Button doneButton;
    RecyclerAdapter adapter;
    private DBHandler myDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerTasks = findViewById(R.id.recyclerTasks);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        Dialog dialog = new Dialog(this);

        myDatabase = new DBHandler(MainActivity.this);

//        getSupportActionBar().hide();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.fab);
                header = dialog.findViewById(R.id.header);
                newTask = dialog.findViewById(R.id.newTask);
                doneButton = dialog.findViewById(R.id.doneButton);
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!newTask.getText().toString().equals("")){
                            arrayListTasks.add(new RecyclerAdapterModel(newTask.getText().toString()));
                            Toast.makeText(MainActivity.this, "NEW TASK ADDED SUCCESSFULLY!!", Toast.LENGTH_SHORT).show();
                            myDatabase.addNewTask(newTask.getText().toString());
                            dialog.dismiss();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "NO NEW TASK ADDED", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }


                    }
                });

                dialog.show();
            }
        });
        adapter = new RecyclerAdapter(this,arrayListTasks);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerTasks.setLayoutManager(mLayoutManager);
        recyclerTasks.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        arrayListTasks = myDatabase.readTasks();
        adapter = new RecyclerAdapter(MainActivity.this,arrayListTasks);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerTasks.setLayoutManager(mLayoutManager);
        recyclerTasks.setAdapter(adapter);
    }
}