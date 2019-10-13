package com.example.onetimework;

import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        workManagerFunctionality();
    }

    private void workManagerFunctionality() {

        Data data = new Data.Builder()
                .putString(Constants.KEY_TASK_DESC, "Sending the work data")
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
/*            Constraints constraints = new Constraints.Builder()
                    .setRequiresDeviceIdle(true).build();*/


            OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(MyWorker.class)
                    .setInputData(data)
/*                  .setConstraints(constraints)*/
                    .build();
            WorkManager.getInstance().enqueue(request);
            WorkManager.getInstance().getWorkInfoById(request.getId());
            WorkManager.getInstance().getWorkInfoByIdLiveData(request.getId()).observe(this, new Observer<WorkInfo>() {
                @Override
                public void onChanged(WorkInfo workInfo) {
                    if (workInfo != null) {
                        if (workInfo.getState().isFinished()) {
                            Data observedData = workInfo.getOutputData();
                            String output = observedData.getString(Constants.OUTPUT_DATA);
                            mTextMessage.append(output + "\n");
                            Log.e(Constants.TAG, output);
                        }
                        Log.e(Constants.TAG, "live data info");
                        String status = workInfo.getState().name();
                        mTextMessage.append(status);
                    }
                }
            });
        }
    }

}