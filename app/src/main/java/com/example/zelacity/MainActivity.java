package com.example.zelacity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zelacity.ui.AddReportActivity;
import com.example.zelacity.ui.ReportAdapter;
import com.example.zelacity.viewmodel.ReportViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private ReportViewModel viewModel;
    private ReportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewReports);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReportAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        viewModel.getAllReports().observe(this, reports -> {
            adapter.setReports(reports);
        });

        FloatingActionButton fab = findViewById(R.id.fabAddReport);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddReportActivity.class);
            startActivity(intent);
        });
    }
}
