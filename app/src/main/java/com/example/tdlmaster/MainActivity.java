package com.example.tdlmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public static TaskList taskList;
    TaskAdapter taskAdapter;
    private boolean isSwipeOnSpinnerDetected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.tasks_list);

        /**
         * ListView Management
         */

        ImageButton deleteTaskButton = findViewById(R.id.deleteTaskButton);
        deleteTaskButton.setVisibility(View.INVISIBLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskAdapter adapter = (TaskAdapter) parent.getAdapter();
                adapter.toggleSelection(position);
                if(adapter.getSelectedItemPosition() != -1) {
                    deleteTaskButton.setVisibility(View.VISIBLE);
                } else {
                    deleteTaskButton.setVisibility(View.INVISIBLE);
                }
                deleteTaskButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = adapter.getSelectedItemPosition();
                        if (position != -1) {
                            Task taskToDelete = adapter.getItem(position);
                            taskList.deleteTask(taskToDelete.getId());
                            adapter.resetSelectedItem();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });

        /**
         * Task Management
         */

        taskList = new TaskList(MainActivity.this);

        // Initialisation de files pour tester l'app
        taskList.generateLogFilesForLastThreeWeeks();

        taskAdapter = new TaskAdapter(this, taskList.getTasks());

        listView.setAdapter(taskAdapter);

        ImageButton addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                AddTaskFragment addTaskFragment = new AddTaskFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, addTaskFragment)
                        .addToBackStack(null) // Permet de revenir en arrière à l'activité précédente avec le bouton retour
                        .commit();

            }
        });

        /**
         * Spinner Management
         */

        DatabaseHelper db = new DatabaseHelper(this);

        Spinner weekSpinner = findViewById(R.id.weekSpinner);

        /*
        List<TaskList> taskLists = db.getAllTaskLists();
        List<String> weeks = new ArrayList<>();
        for (TaskList taskList : taskLists) {
            String weekLabel = "Semaine " + taskList.getWeekNumber() + " - " + taskList.getYearNumber();
            weeks.add(weekLabel);
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_selected, weeks);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_items);
        weekSpinner.setAdapter(spinnerAdapter);
         */

        List<TaskList> taskLists = db.getAllTaskLists();
        ArrayAdapter<TaskList> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, taskLists);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekSpinner.setAdapter(spinnerAdapter);

        taskAdapter.notifyDataSetChanged();

        weekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {




            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TaskList selectedTaskList = taskLists.get(position);
                ArrayList<Task> newTasks;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                int actualWeekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
                Log.d("TaskList", "Actual Week : " + actualWeekNumber);
                if(selectedTaskList.getWeekNumber() == actualWeekNumber){
                    newTasks = db.getAllTasks();
                    Log.d("TaskList", "Week à afficher: " + selectedTaskList.getWeekNumber());
                    Log.d("TaskList", "Nombre tâches à afficher: " + newTasks.size());
                } else {
                    newTasks = selectedTaskList.getAllTasksFromFile(MainActivity.this);
                    Log.d("TaskList", "Tâches à afficher: " + selectedTaskList.getWeekNumber());
                    Log.d("TaskList", "Log File name : " + selectedTaskList.getLogFileName());
                    Log.d("TaskList", "Nombre tâches à afficher: " + newTasks.size());
                }
            }

            /*
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TaskList selectedTaskList = taskLists.get(position);
                if(selectedTaskList.isActive()){
                    taskAdapter = new TaskAdapter(MainActivity.this, selectedTaskList.getTasks());
                    Log.d("TaskList", "Tâches à afficher: " + selectedTaskList.getWeekNumber());
                } else {
                    selectedTaskList.getAllTasksFromFile();
                    taskAdapter = new TaskAdapter(MainActivity.this, selectedTaskList.getTasks());
                    Log.d("TaskList", "Tâches à afficher: " + selectedTaskList.getWeekNumber());
                }
            }*/

/*
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TaskList selectedTaskList = (TaskList) parent.getItemAtPosition(position);
                ListView newListView = (ListView) parent.findViewById(R.id.tasks_list);
                if(selectedTaskList.isActive()){
                    taskAdapter = new TaskAdapter(MainActivity.this, selectedTaskList.getTasks());
                } else {
                    selectedTaskList.getAllTasksFromFile(); // Assure-toi que cette méthode fonctionne correctement
                    taskAdapter = new TaskAdapter(MainActivity.this, selectedTaskList.getTasks());
                }
                newListView = findViewById(R.id.tasks_list);
                newListView.setAdapter(taskAdapter);
            }
*/

            /*
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TaskList selectedTaskList = taskLists.get(position);
                //Log.d("TaskList", getString(selectedTaskList.getWeekNumber()));

                ArrayList<Task> tasksToShow;
                if(selectedTaskList.isActive()){
                    tasksToShow = selectedTaskList.getTasks();
                    Log.d("TaskList", "Nombre de tâches à afficher: " + tasksToShow.size());
                } else {
                    selectedTaskList.getAllTasksFromFile(); // Cette ligne charge les tâches depuis le fichier dans 'tasks'
                    tasksToShow = selectedTaskList.getTasks(); // Maintenant 'tasks' contient les tâches du fichier
                    Log.d("TaskList", "Nombre de tâches à afficher: " + tasksToShow.size());
                }

                // Création d'un nouvel adaptateur avec les tâches à afficher
                taskAdapter = new TaskAdapter(MainActivity.this, tasksToShow);
                listView.setAdapter(taskAdapter); // Assure-toi d'avoir une référence à ta ListView ici
                taskAdapter.notifyDataSetChanged(); // Notifie l'adaptateur que les données ont changé
            }

             */


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * Tactile Touch Management
         */

        GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                int position = weekSpinner.getSelectedItemPosition();
                isSwipeOnSpinnerDetected = true;
                if (e1.getX() < e2.getX()) {
                    if (position > 0) {
                        weekSpinner.setSelection(position - 1);
                    }
                } else if (e1.getX() > e2.getX()) {
                    if (position < weekSpinner.getCount() - 1) {
                        weekSpinner.setSelection(position + 1);
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        weekSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (!isSwipeOnSpinnerDetected) {
                        v.performClick();
                    }
                    isSwipeOnSpinnerDetected = false;
                }
                return true;
            }
        });

        // AlarmManager

        scheduleWeeklyTaskListUpdate();
    }

    public void addNewTask(String title, String description) {
        Task newTask = new Task(title, description);
        taskList.addTask(newTask);
        taskAdapter.notifyDataSetChanged();
    }

    /**
     * Update TaskList
     */


    private void scheduleWeeklyTaskListUpdate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Paris")); // Assurez-vous d'utiliser le bon fuseau horaire
        calendar.add(Calendar.DAY_OF_YEAR, (Calendar.SATURDAY - calendar.get(Calendar.DAY_OF_WEEK) + 2) % 7); // Avance au prochain lundi
        calendar.set(Calendar.HOUR_OF_DAY, 0); // Mettez à minuit
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long delay = calendar.getTimeInMillis() - System.currentTimeMillis();
        if (delay < 0) {
            delay += AlarmManager.INTERVAL_DAY * 7;
        }

        PeriodicWorkRequest updateRequest =
                new PeriodicWorkRequest.Builder(TaskListUpdateWorker.class, 7, TimeUnit.DAYS)
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "taskListUpdate",
                ExistingPeriodicWorkPolicy.KEEP,
                updateRequest);
    }
}