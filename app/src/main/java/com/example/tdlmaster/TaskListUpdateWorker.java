package com.example.tdlmaster;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class TaskListUpdateWorker extends Worker {

    public TaskListUpdateWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        TaskList l = new TaskList(getApplicationContext());
        l.closeAndPrepareForNextWeek();

        showNotification(getApplicationContext());

        return Result.success();
    }

    private void showNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "TASK_LIST_CHANNEL";
        String channelName = "Weekly Task List Update";
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setContentTitle("Task List Update")
                .setContentText("Your task list has been updated for the new week.")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build();

        notificationManager.notify(1, notification);
    }
}
