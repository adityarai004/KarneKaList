package com.example.karnekalist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    Context context;
    DBHandler myDatabase = new DBHandler(context);
    ContentValues contentValues = new ContentValues();
    ArrayList<RecyclerAdapterModel> arrayListRecyclerAdapterModel;
    RecyclerAdapter(Context context, ArrayList<RecyclerAdapterModel> arrayListRecyclerAdapterModel){
        this.context = context;
        this.arrayListRecyclerAdapterModel = arrayListRecyclerAdapterModel;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_task,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.task.setText(arrayListRecyclerAdapterModel.get(position).getTask());

        holder.LLRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.fab);
                TextView header = dialog.findViewById(R.id.header);
                EditText task = dialog.findViewById(R.id.newTask);
                Button doneButton  = dialog.findViewById(R.id.doneButton);
                header.setText("Update Task");
                doneButton.setText("Update");

                task.setText(arrayListRecyclerAdapterModel.get(position).getTask());


                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = "";
                        int id;
                        if(!task.getText().toString().equals("")) {
                            name = task.getText().toString();
                            id = arrayListRecyclerAdapterModel.get(position).getId();
                            DBHandler mdb = new DBHandler(context);
                            mdb.updateTask(name, id);
//                            RecyclerAdapterModel model = new RecyclerAdapterModel();

//                            ArrayList<RecyclerAdapterModel> arrTasks = new ArrayList<>();
//                            arrTasks =  myDatabase.readTasks();

//                            for(int i = 0;i<arrTasks.size();i++){
//
//                            }
//                            model.id = arrTasks.indexOf(model.task);
////                            DBHandler myDatabase = new DBHandler(context);
//                            myDatabase.updateTask(model);
                            arrayListRecyclerAdapterModel.set(position,new RecyclerAdapterModel(name,position));

                            notifyItemChanged(position);
                        }
                        else{
                            Toast.makeText(context, "Invalid Input, Contant Not Added.", Toast.LENGTH_SHORT).show();
                            return;
                        }




                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });
        holder.LLRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Contact");
                builder.setMessage("Are You Sure Want to delete this Task?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        DBHandler dbHelper = new DBHandler(context);
//                        dbHelper.deleteTask(arrayListRecyclerAdapterModel.get(position).getTask());
//                        name = task.getText().toString();
                        int id = arrayListRecyclerAdapterModel.get(position).getId();
                        DBHandler mdb = new DBHandler(context);
                        mdb.deleteTask(id);


                        arrayListRecyclerAdapterModel.remove(position);
                        notifyItemRemoved(position);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListRecyclerAdapterModel.size();
    }

//    private void deletePlace(String taskName){
//        DBHandler dbHelper = new DBHandler(context);
//        dbHelper.deleteTask(arrayListRecyclerAdapterModel.get(position).getTask());
//        arrayListRecyclerAdapterModel.remove(position);
//    }
//    private void update(int position,String name){
//        DBHandler dbHelper = new DBHandler(context);
//        dbHelper.updateTask(new RecyclerAdapterModel(name,position));
//        arrayListRecyclerAdapterModel.set(position,new RecyclerAdapterModel(name));
//    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView task;
        LinearLayout LLRow;
        public ViewHolder(View itemView){
            super(itemView);

            task = itemView.findViewById(R.id.task);
            LLRow = itemView.findViewById(R.id.LLRow);
        }
    }
}
