package com.example.zelacity.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.zelacity.model.Report;
import com.example.zelacity.repository.ReportRepository;

import java.util.List;

public class ReportViewModel extends AndroidViewModel {
    private final ReportRepository repository;
    private final LiveData<List<Report>> allReports;

    public ReportViewModel(@NonNull Application application) {
        super(application);
        repository = new ReportRepository(application);
        allReports = repository.getAllReports();
    }

    public LiveData<List<Report>> getAllReports() {
        return allReports;
    }

    public void insert(Report report) {
        repository.insert(report);
    }
}
