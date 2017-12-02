package com.czopi.administrador.lifeagenda;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.joda.time.LocalDate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    TextView TVDueDate, TVCreationDate, TVPriority, TVDescription;
    List<Task> taskArrayList;
    Task task;
    int taskId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        Toolbar myToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TVDueDate = (TextView)findViewById(R.id.TVDueDate);
        TVCreationDate = (TextView)findViewById(R.id.TVCreationDate);
        TVPriority = (TextView)findViewById(R.id.TVPriority);
        TVDescription = (TextView)findViewById(R.id.TVDescription);

    }

    @Override
    protected void onResume() {
        super.onResume();
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson gson = builder.create();

        SharedPreferences prefs = getSharedPreferences(
                "com.czopi.administrador.lifeagenda", Context.MODE_PRIVATE);
        String json = prefs.getString("taskList", null);
        if (json == null) {
            taskArrayList = new ArrayList<>();
        } else {
            Type type = new TypeToken<ArrayList<Task>>(){}.getType();
            taskArrayList = gson.fromJson(json, type);
        }

        Intent i = getIntent();
        taskId = i.getIntExtra("taskSelected", 0);
        task = getTask(taskId);

        final String taskTitle = task.getTitle();
        final LocalDate taskDueDate = task.getDueDate();
        final LocalDate taskCreationDate = task.getCreationDate();
        final int taskPriority = task.getPriority();
        final String taskDescription = task.getDescription();

        final String dueDateString = String.valueOf(taskDueDate.dayOfWeek().getAsShortText()) + " " +
                String.valueOf(taskDueDate.getDayOfMonth()) + "/" +
                String.valueOf(taskDueDate.getMonthOfYear()) + "/" +
                String.valueOf(taskDueDate.getYear());
        final String creationDateString = String.valueOf(taskCreationDate.getDayOfMonth()) + "/" +
                String.valueOf(taskCreationDate.getMonthOfYear()) + "/" +
                String.valueOf(taskCreationDate.getYear());

        getSupportActionBar().setTitle(taskTitle);

        TVDueDate.setText(dueDateString);
        TVCreationDate.setText(creationDateString);
        TVPriority.setText(String.valueOf(taskPriority));
        TVDescription.setText(taskDescription);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Intent i1 = new Intent(this, EditTaskActivity.class);
                i1.putExtra("taskId", taskId);
                startActivity(i1);
                return true;
            case R.id.conclude:
                new AlertDialog.Builder(TaskActivity.this)
                        .setMessage("Are you sure you want to complete this task?")
                        .setPositiveButton("COMPLETE", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                        taskArrayList.remove(task);

                                        GsonBuilder builder = new GsonBuilder()
                                                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
                                        Gson gson = builder.create();

                                        SharedPreferences prefs = getSharedPreferences(
                                                "com.czopi.administrador.lifeagenda", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        String json = gson.toJson(taskArrayList);
                                        editor.putString("taskList", json);
                                        editor.apply();
                                        finish();
                                    }
                        })
                        .setNegativeButton("CANCEL", null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Task getTask(int id) {
        for(Task task : taskArrayList) {
            if(task.getId() == id) {
                return task;
            }
        }
        return null;
    }



}
