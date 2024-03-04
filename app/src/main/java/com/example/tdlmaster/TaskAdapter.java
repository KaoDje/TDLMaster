package com.example.tdlmaster;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {

    private int selectedItemPosition = -1; // Aucun élement sélectionné par défaut
    private Context context;
    private ArrayList<Task> tasks;

    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
        this.context = context;
        this.tasks = tasks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_layout, parent, false);
        }

        TextView taskTitle = convertView.findViewById(R.id.task_title);
        TextView taskDescription = convertView.findViewById(R.id.task_description);
        CheckBox completeButton = convertView.findViewById(R.id.complete_button);

        taskTitle.setText(task.getTitle());
        taskDescription.setText(task.getDescription());
        completeButton.setChecked(task.isCompleted());

        completeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    task.completeTask(getContext());
                } else {
                    task.undoCompletion(getContext());
                }
                notifyDataSetChanged();
            }
        });

        Log.d("Task", "Position : " + position);
        Log.d("Task", "Position : " + selectedItemPosition);

        if (position == selectedItemPosition) {
            Log.d("Task", "Selected");
            int color = ContextCompat.getColor(getContext(), R.color.selected_color);
            convertView.setBackgroundColor(color);
            //convertView.setBackgroundColor(getContext().getResources().getColor(R.color.selected_color));
        } else {
            Log.d("Task", "UnSelected");
            int color = ContextCompat.getColor(getContext(), R.color.default_color);
            convertView.setBackgroundColor(color);
            //convertView.setBackgroundColor(getContext().getResources().getColor(R.color.default_color));
        }

        return convertView;
    }

    public void toggleSelection(int position) {
        if (selectedItemPosition == position) {
            selectedItemPosition = -1;
        } else {
            selectedItemPosition = position;
        }
        Log.d("Task", "Position dans toggle : " + position);
        notifyDataSetChanged();
    }

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    public void resetSelectedItem() {
        selectedItemPosition = -1;
    }
}
