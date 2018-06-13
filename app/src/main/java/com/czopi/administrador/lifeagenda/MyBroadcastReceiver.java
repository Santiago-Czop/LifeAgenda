package com.czopi.administrador.lifeagenda;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.joda.time.LocalDate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MyBroadcastReceiver extends BroadcastReceiver {

    List<Task> taskArrayList, taskArrayListDue, taskArrayListOverDue;

    @Override
    public void onReceive(Context context, Intent intent) {
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson gson = builder.create();

        SharedPreferences prefs = context.getSharedPreferences(
                "com.czopi.administrador.lifeagenda", Context.MODE_PRIVATE);


        boolean notification = prefs.getBoolean("ntf_boolean", true);
        if (!notification) {
            return;
        }
        int minPriority = prefs.getInt("minPriority", 6);
        boolean vibration = prefs.getBoolean("vbr_boolean", true);
        String jsonA = prefs.getString("taskList", null);
        if (jsonA == null) {
            taskArrayList = new ArrayList<>();
        } else {
            Type type = new TypeToken<ArrayList<Task>>() {
            }.getType();
            taskArrayList = gson.fromJson(jsonA, type);
        }

        taskArrayListDue = new ArrayList<>();
        LocalDate today = new LocalDate();
        for (Task task : taskArrayList) {
            if (task.getDueDate().isEqual(today) && task.getPriority() >= minPriority) {
                taskArrayListDue.add(task);
            }
        }
        int taskDCounter = taskArrayListDue.size();

        if (taskDCounter != 0) {
            String notificationInfo0 = "";
            Intent i0 = new Intent(context, MainActivity.class);
            Task dueTask = taskArrayListDue.get(0);
            if (taskDCounter > 1) {
                String tempString;
                if (taskDCounter > 2) {
                    tempString = String.valueOf(taskDCounter - 1) + " others";
                } else {
                    tempString = "another";
                }
                notificationInfo0 = dueTask.getTitle() + " and " + tempString + "are due";
            } else if (taskDCounter == 1) {
                i0 = new Intent (context, TaskActivity.class);
                i0.putExtra("taskSelected", dueTask.getId());
                notificationInfo0 = dueTask.getTitle() + " is due";
            }
            PendingIntent resultPendingIntent0 =
                    PendingIntent.getActivity(
                            context,
                            0,
                            i0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            NotificationCompat.Builder mBuilder0 = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Life Agenda - Expired Tasks")
                    .setContentInfo(notificationInfo0)
                    .setContentIntent(resultPendingIntent0)
                    .setAutoCancel(true);
            if (vibration) {
                mBuilder0.setDefaults(Notification.DEFAULT_VIBRATE);
            }
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, mBuilder0.build());
        }

        taskArrayListOverDue = new ArrayList<>();
        for (Task task : taskArrayList) {
            if (task.isDue()) {
                taskArrayListOverDue.add(task);
            }
        }
        int taskODCounter = taskArrayListOverDue.size();
        if (taskODCounter != 0) {
            String notificationInfo1 = "";
            Intent i1 = new Intent (context, MainActivity.class);
            Task overDueTask = taskArrayListOverDue.get(0);
            if (taskODCounter > 1) {
                String tempString;
                if (taskODCounter > 2) {
                    tempString = String.valueOf(taskODCounter - 1) + " others";
                } else {
                    tempString = "another";
                }
                notificationInfo1 = overDueTask.getTitle() + " and " + tempString + "are overdue";
            } else if (taskODCounter == 1) {
                i1 = new Intent (context, TaskActivity.class);
                i1.putExtra("taskSelected", overDueTask.getId());
                notificationInfo1 = overDueTask.getTitle() + " is overdue";
            }
            PendingIntent resultPendingIntent1 =
                    PendingIntent.getActivity(
                            context,
                            0,
                            i1,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            NotificationCompat.Builder mBuilder1 = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Life Agenda - Expired Tasks")
                    .setContentInfo(notificationInfo1)
                    .setContentIntent(resultPendingIntent1)
                    .setAutoCancel(true);
            if (vibration) {
                mBuilder1.setDefaults(Notification.DEFAULT_VIBRATE);
            }
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, mBuilder1.build());
        }

    }
}
