package com.example.zelacity.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.zelacity.data.local.AppDatabase;
import com.example.zelacity.data.local.ReportDao;
import com.example.zelacity.model.Report;
import com.example.zelacity.worker.SyncWorker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReportRepository {
    private final ReportDao reportDao;
    private final LiveData<List<Report>> allReports;
    private final ExecutorService executorService;
    private final Context context;

    public ReportRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        reportDao = db.reportDao();
        allReports = reportDao.getAllReports();
        executorService = Executors.newSingleThreadExecutor();
        context = application.getApplicationContext();
    }

    public LiveData<List<Report>> getAllReports() {
        return allReports;
    }

    public void insert(Report report) {
        executorService.execute(() -> {
            reportDao.insert(report);
            scheduleSync();
        });
    }

    private void scheduleSync() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest syncRequest = new OneTimeWorkRequest.Builder(SyncWorker.class)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(context).enqueue(syncRequest);
    }
}
