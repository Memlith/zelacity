package com.example.zelacity.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddReportActivity extends AppCompatActivity {
    private TextInputEditText editTextTitle, editTextDescription, editTextAddress;
    private TextInputLayout textInputLayoutAddress;
    private TextView textViewLocationStatus;
    private ReportViewModel viewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude = 0.0, longitude = 0.0;
    private String autoAddress = "";
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

        textViewLocationStatus.setText(R.string.location_obtaining);

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, new CancellationTokenSource().getToken())
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        hasLocation = true;

                        // Tenta obter o endereço automaticamente via Geocoder
                        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            if (addresses != null && !addresses.isEmpty()) {
                                autoAddress = addresses.get(0).getAddressLine(0);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        textViewLocationStatus.setText(getString(R.string.location_gps_success, latitude, longitude));
                        textInputLayoutAddress.setVisibility(View.GONE);
                    } else {
                        textViewLocationStatus.setText(R.string.location_gps_failed);
                        showAddressInput();
                    }
                })
                .addOnFailureListener(this, e -> {
                    textViewLocationStatus.setText(R.string.location_gps_failed);
                    showAddressInput();
                });
    }

    private void showAddressInput() {
        textInputLayoutAddress.setVisibility(View.VISIBLE);
        textViewLocationStatus.setText(R.string.location_manual);
    }

    private void saveReport() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String manualAddress = editTextAddress.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, R.string.toast_fill_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        // Se o endereço manual estiver vazio, usa o capturado automaticamente
        String finalAddress = manualAddress.isEmpty() ? autoAddress : manualAddress;

        if (!hasLocation && finalAddress.isEmpty()) {
            Toast.makeText(this, R.string.toast_missing_location, Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getUid();
        String defaultStatus = getString(R.string.status_waiting);
        Report report = new Report(title, description, latitude, longitude, finalAddress, System.currentTimeMillis(), userId, defaultStatus);
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
