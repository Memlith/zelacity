package com.example.zelacity.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.zelacity.R;
import com.example.zelacity.model.Report;
import com.example.zelacity.viewmodel.ReportViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class AddReportActivity extends AppCompatActivity {
    private TextInputEditText editTextTitle, editTextDescription, editTextAddress;
    private TextInputLayout textInputLayoutAddress;
    private TextView textViewLocationStatus;
    private ReportViewModel viewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude = 0.0, longitude = 0.0;
    private boolean hasLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextAddress = findViewById(R.id.editTextAddress);
        textInputLayoutAddress = findViewById(R.id.textInputLayoutAddress);
        textViewLocationStatus = findViewById(R.id.textViewLocationStatus);
        Button buttonSave = findViewById(R.id.buttonSave);

        viewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();

        buttonSave.setOnClickListener(v -> saveReport());
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            showAddressInput();
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                hasLocation = true;
                textViewLocationStatus.setText(getString(R.string.location_gps_success, latitude, longitude));
                textInputLayoutAddress.setVisibility(View.GONE);
            } else {
                textViewLocationStatus.setText(R.string.location_gps_failed);
                showAddressInput();
            }
        });
    }

    private void showAddressInput() {
        textInputLayoutAddress.setVisibility(View.VISIBLE);
        textViewLocationStatus.setText(R.string.location_manual);
    }

    private void saveReport() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, R.string.toast_fill_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!hasLocation && address.isEmpty()) {
            Toast.makeText(this, R.string.toast_missing_location, Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getUid();
        Report report = new Report(title, description, latitude, longitude, address, System.currentTimeMillis(), userId);
        viewModel.insert(report);
        
        Toast.makeText(this, R.string.toast_success, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                showAddressInput();
            }
        }
    }
}
