package com.example.tdlmaster;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

public class TaskList {
    private Context context;
    private int weekNumber;
    private int yearNumber;
    private String logFileName;
    ArrayList<Task> tasks;
    DatabaseHelper db;
    private boolean isActive;

    public TaskList(Context context){
        this.context = context;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        this.yearNumber = calendar.get(Calendar.YEAR);
        this.weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
        this.logFileName = "";
        this.isActive = true;
        db = new DatabaseHelper(context);
        tasks = db.getAllTasks();
        db.addTaskList(this.weekNumber, this.yearNumber, this.logFileName);
    }

    public TaskList(int weekNumber, int yearNumber, String logFileName) {
        this.weekNumber = weekNumber;
        this.yearNumber = yearNumber;
        this.logFileName = logFileName;
    }

    public TaskList(int weekNumber, int yearNumber, String logFileName, ArrayList<Task> tasks) {
        this.weekNumber = weekNumber;
        this.yearNumber = yearNumber;
        this.logFileName = logFileName;
        this.tasks = tasks;
    }

    public boolean isActive() { return this.isActive; }

    public int getWeekNumber() {
        return weekNumber;
    }

    public int getYearNumber() {
        return yearNumber;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void addTask(Task newTask) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(newTask);
        db.addTask(newTask);
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void closeAndPrepareForNextWeek() {
        this.logFileName = "TDLMaster-" + this.yearNumber + '-' + this.weekNumber + ".json";
        generateWeeklyLogFile(this);
        db.deleteCompletedTasks();
        db.updateTaskList();
    }

    private void generateWeeklyLogFile(TaskList taskList) {
        taskList.isActive = false;
        Gson gson = new Gson();
        String json = gson.toJson(taskList.getTasks());
        try {
            FileOutputStream fos = context.openFileOutput(taskList.getLogFileName(), Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            outputStreamWriter.write(json);
            outputStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Task> getAllTasksFromFile(Context appContext) {
        if(context == null) {
            context = appContext;
        }
        Gson gson = new GsonBuilder()
                .setDateFormat("MMM dd, yyyy hh:mm:ss a") // Définis le format de date utilisé dans ton fichier JSON
                .create();
            ArrayList<Task> tasks = new ArrayList<>();

            String fileName = this.logFileName;
            Log.d("TaskList", "Get All Tasks From file : " + fileName);

            try {
                FileInputStream fis = context.openFileInput(fileName);
                InputStreamReader isr = new InputStreamReader(fis);

                Type taskListType = new TypeToken<ArrayList<Task>>(){}.getType();
                tasks = gson.fromJson(isr, taskListType);

                isr.close();
            } catch (Exception e) {
                Log.d("TaskList", "Error when reading file");
                e.printStackTrace();
            }

        Log.d("TaskList", "Get All Tasks From file, tasks size : " + tasks.size());

            this.tasks = tasks;
            return tasks;
    }

    public void deleteTask(int taskId) {
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (task.getId() == taskId) {
                iterator.remove();
                break;
            }
        }
        db = new DatabaseHelper(context);
        db.deleteTask(taskId);
    }

    /**
     * Initialisation de fichiers pour les tests de l'app
     */

    private boolean logFileExists(String fileName) {
        File file = context.getFileStreamPath(fileName);
        return file != null && file.exists();
    }

    private ArrayList<Task> generateSampleTasksForWeek() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Task 1", "Description 1"));
        tasks.add(new Task("Task 2", "Description 2"));
        return tasks;
    }

    public void generateLogFilesForLastThreeWeeks() {
        Calendar calendar = Calendar.getInstance();

        for (int i = 1; i <= 3; i++) {
            calendar.add(Calendar.WEEK_OF_YEAR, -i);
            int year = calendar.get(Calendar.YEAR);
            int week = calendar.get(Calendar.WEEK_OF_YEAR);


            String logFileName = "TDLMaster-" + year + "-" + week + ".json";

            if (!logFileExists(logFileName)) {
                TaskList taskList = new TaskList(week, year, logFileName, generateSampleTasksForWeek());
                generateWeeklyLogFile(taskList);
            }
            calendar.setTime(new Date());

            db.addTaskList(week, year, logFileName);
        }
    }

    @Override
    public String toString() {
        return "Semaine " + this.weekNumber + " - " + this.yearNumber;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
}
