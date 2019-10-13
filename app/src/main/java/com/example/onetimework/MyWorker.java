package com.example.onetimework;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        String desc = data.getString(Constants.KEY_TASK_DESC);
        displayNotification("Hey I am your work", desc);

        Data outputData = new Data.Builder()
                .putString(Constants.OUTPUT_DATA, "Task finished by gamir")
                .build();
        return Result.success(outputData);
    }

    private void displayNotification(String task, String desc) {
        Log.e(Constants.TAG, "displaying Notificaton: ");
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Simplified coding", "fff", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            Log.e(Constants.TAG, "displayNotification: displaying notification oreo");
        }

        Log.e(Constants.TAG, "displayNotification: displaying notification");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Simplified coding")
                .setContentTitle(task)
                .setContentText(desc)
                .setSmallIcon(R.mipmap.ic_launcher);
        manager.notify(1, builder.build());
    }
}
