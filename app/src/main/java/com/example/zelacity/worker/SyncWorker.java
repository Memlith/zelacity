package com.example.zelacity.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.zelacity.data.local.AppDatabase;
import com.example.zelacity.data.local.ReportDao;
import com.example.zelacity.data.remote.FirebaseHelper;
import com.example.zelacity.model.Report;
import com.google.android.gms.tasks.Tasks;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SyncWorker extends Worker {
    private final ReportDao reportDao;
    private final FirebaseHelper firebaseHelper;

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        reportDao = AppDatabase.getInstance(context).reportDao();
        firebaseHelper = new FirebaseHelper();
    }

    @NonNull
    @Override
    public Result doWork() {
        List<Report> unsyncedReports = reportDao.getUnsyncedReports();
        
        for (Report report : unsyncedReports) {
            try {
                Tasks.await(firebaseHelper.uploadReport(report));
                report.setSynced(true);
                reportDao.update(report);
            } catch (ExecutionException | InterruptedException e) {
                return Result.retry();
            }
        }
        
        return Result.success();
    }
}
