package com.czopi.administrador.lifeagenda;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.List;

public class MyTaskListAdapter extends ArrayAdapter<Task> {

    private HashMap<Integer, Boolean> mSelection = new HashMap<>();

    public MyTaskListAdapter(Context context, List<Task> items) {
        super(context, 0, items);
    }

    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }

    public void removeSelection(int position) {
        mSelection.remove(position);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelection = new HashMap<>();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_task_item, parent, false);
        }

        ImageView myImageView = (ImageView) convertView.findViewById(R.id.myImageView);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);

        int taskId = task.getId();
        String taskTitle = task.getTitle();
        LocalDate taskDueDate = task.getDueDate();
        int taskPriority = task.getPriority();
        String dueDateString = String.valueOf(taskDueDate.dayOfWeek().getAsShortText()) + " " +
                String.valueOf(taskDueDate.getDayOfMonth()) + "/" +
                String.valueOf(taskDueDate.getMonthOfYear()) + "/" +
                String.valueOf(taskDueDate.getYear());

        switch (taskPriority) {
            case 1:
                myImageView.setImageResource(R.drawable.number1);
                break;
            case 2:
                myImageView.setImageResource(R.drawable.number2);
                break;
            case 3:
                myImageView.setImageResource(R.drawable.number3);
                break;
            case 4:
                myImageView.setImageResource(R.drawable.number4);
                break;
            case 5:
                myImageView.setImageResource(R.drawable.number5);
                break;
            case 6:
                myImageView.setImageResource(R.drawable.number6);
                break;
            case 7:
                myImageView.setImageResource(R.drawable.number7);
                break;
            case 8:
                myImageView.setImageResource(R.drawable.number8);
                break;
            case 9:
                myImageView.setImageResource(R.drawable.number9);
                break;
            case 10:
                myImageView.setImageResource(R.drawable.number10);
                break;
        }

        if (mSelection.get(position) != null) {
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_gray));
        } else {
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
        }
        convertView.setTag(taskId);
        tvTitle.setText(taskTitle);
        tvDate.setText(dueDateString);
        if (!task.isDue()) {
            tvDate.setTextColor(ContextCompat.getColor(getContext(), R.color.transparent54));
        } else {
            tvDate.setTextColor(ContextCompat.getColor(getContext(), R.color.transparent54red));
        }

        return convertView;
    }

}