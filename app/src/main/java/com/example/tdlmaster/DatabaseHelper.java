package com.example.tdlmaster;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "taskManager.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_TASKS = "tasks";
    private static final String KEY_TASK_ID = "id";
    private static final String KEY_TASK_TITLE = "title";
    private static final String KEY_TASK_DESCRIPTION = "description";
    private static final String KEY_TASK_IS_COMPLETED = "isCompleted";
    private static final String KEY_TASK_CREATED_AT = "createdAt";

    private static final String TABLE_TASKLISTS = "taskLists";
    private static final String KEY_LIST_ID = "id";
    private static final String KEY_WEEK_NUMBER = "weekNumber";
    private static final String KEY_YEAR_NUMBER = "yearNumber";
    private static final String KEY_LOG_FILE_NAME = "logFileName";

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_TASKS = "CREATE TABLE " + TABLE_TASKS + "("
                + KEY_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TASK_TITLE + " TEXT,"
                + KEY_TASK_DESCRIPTION + " TEXT,"
                + KEY_TASK_IS_COMPLETED + " INTEGER,"
                + KEY_TASK_CREATED_AT + " INTEGER" + ")";

        String CREATE_TABLE_TASKLISTS = "CREATE TABLE " + TABLE_TASKLISTS + "("
                + KEY_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_WEEK_NUMBER + " INTEGER,"
                + KEY_YEAR_NUMBER + " INTEGER,"
                + KEY_LOG_FILE_NAME + " TEXT" + ")";

        db.execSQL(CREATE_TABLE_TASKS);
        db.execSQL(CREATE_TABLE_TASKLISTS);
        Log.d("DatabaseHelper", "Database created with tables");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void addTaskList(int weekNumber, int yearNumber, String logFileName) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Vérifier si la TaskList existe déjà
        Cursor cursor = db.query(TABLE_TASKLISTS, new String[] {KEY_LIST_ID},
                KEY_WEEK_NUMBER + "=? AND " + KEY_YEAR_NUMBER + "=?",
                new String[] {String.valueOf(weekNumber), String.valueOf(yearNumber)},
                null, null, null);

        if (!cursor.moveToFirst()) { // Si la TaskList n'existe pas, ajouter une nouvelle entrée
            ContentValues values = new ContentValues();
            values.put(KEY_WEEK_NUMBER, weekNumber);
            values.put(KEY_YEAR_NUMBER, yearNumber);
            values.put(KEY_LOG_FILE_NAME, logFileName);
            db.insert(TABLE_TASKLISTS, null, values);
            Log.d("DatabaseHelper", "TaskList insérée");
        }

        cursor.close();
        db.close();
    }

    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_TITLE, task.getTitle());
        values.put(KEY_TASK_DESCRIPTION, task.getDescription());
        values.put(KEY_TASK_IS_COMPLETED, task.isCompleted() ? 1 : 0);
        values.put(KEY_TASK_CREATED_AT, task.getCreatedAt().getTime());

        long taskId = db.insert(TABLE_TASKS, null, values);
        db.close();

        if (taskId == -1) {
            Log.e("DatabaseHelper", "Échec de l'insertion de la tâche");
        } else {
            Log.d("DatabaseHelper", "Tâche insérée avec succès avec l'ID: " + taskId);
        }

        return taskId;
    }

    @SuppressLint("Range")
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(KEY_TASK_ID)));
                task.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TASK_TITLE)));
                task.setDescription(cursor.getString(cursor.getColumnIndex(KEY_TASK_DESCRIPTION)));
                task.setIsCompleted(cursor.getInt(cursor.getColumnIndex(KEY_TASK_IS_COMPLETED)) == 1);
                task.setCreatedAt(new Date(cursor.getLong(cursor.getColumnIndex(KEY_TASK_CREATED_AT))));
                tasks.add(task);
                Log.d("DatabaseHelper", String.valueOf(task.isCompleted()));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return tasks;
    }

    public void deleteCompletedTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_TASK_IS_COMPLETED + "=?", new String[]{"1"});
        db.close();
    }

    public void updateTaskList() {
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int yearNumber = calendar.get(Calendar.YEAR);
        int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
        String logFileName = "TDLMaster-" + yearNumber + "-" + weekNumber + ".json";

        ContentValues values = new ContentValues();
        values.put(KEY_WEEK_NUMBER, weekNumber);
        values.put(KEY_YEAR_NUMBER, yearNumber);
        values.put(KEY_LOG_FILE_NAME, logFileName);

        String selectQuery = "SELECT * FROM " + TABLE_TASKLISTS + " WHERE "
                + KEY_WEEK_NUMBER + "=" + weekNumber + " AND "
                + KEY_YEAR_NUMBER + "=" + yearNumber;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            db.update(TABLE_TASKLISTS, values, KEY_WEEK_NUMBER + "=? AND " + KEY_YEAR_NUMBER + "=?",
                    new String[]{String.valueOf(weekNumber), String.valueOf(yearNumber)});
        } else {
            db.insert(TABLE_TASKLISTS, null, values);
        }

        cursor.close();
        db.close();
    }

    @SuppressLint("Range")
    public ArrayList<TaskList> getAllTaskLists() {
        ArrayList<TaskList> taskLists = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TASKLISTS;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int weekNumber = cursor.getInt(cursor.getColumnIndex(KEY_WEEK_NUMBER));
                int yearNumber = cursor.getInt(cursor.getColumnIndex(KEY_YEAR_NUMBER));
                String logFileName = cursor.getString(cursor.getColumnIndex(KEY_LOG_FILE_NAME));

                TaskList taskList = new TaskList(weekNumber, yearNumber, logFileName);
                taskLists.add(taskList);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return taskLists;
    }

    public void completeTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_IS_COMPLETED, 1);

        int rowsAffected = db.update(TABLE_TASKS, values, KEY_TASK_ID + " = ?", new String[] { String.valueOf(taskId) });

        if (rowsAffected > 0) {
            Log.d("DatabaseHelper", "Tâche complétée avec succès avec l'ID: " + taskId);
        } else {
            Log.e("DatabaseHelper", "Échec de la mise à jour de la tâche avec l'ID: " + taskId);
        }

        db.close();
    }

    public void undoCompletion(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_IS_COMPLETED, 0);

        int rowsAffected = db.update(TABLE_TASKS, values, KEY_TASK_ID + " = ?", new String[] { String.valueOf(taskId) });

        if (rowsAffected > 0) {
            Log.d("DatabaseHelper", "Tâche undo avec succès");
        } else {
            Log.e("DatabaseHelper", "Échec de la mise à jour de la tâche avec l'ID: " + taskId);
        }

        db.close();
    }

    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_TASK_ID + " = ?", new String[] {String.valueOf(taskId)});
        db.close();
    }
}