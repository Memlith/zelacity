package com.example.zelacity.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zelacity.R;
import com.example.zelacity.model.Report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {
    private List<Report> reports = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report currentReport = reports.get(position);
        Context context = holder.itemView.getContext();
        
        holder.textViewTitle.setText(currentReport.getTitle());
        holder.textViewDescription.setText(currentReport.getDescription());
        
        String locationInfo;
        if (currentReport.getAddress() != null && !currentReport.getAddress().isEmpty()) {
            locationInfo = currentReport.getAddress();
        } else if (currentReport.getLatitude() != 0.0 || currentReport.getLongitude() != 0.0) {
            locationInfo = context.getString(R.string.location_format, 
                    currentReport.getLatitude(), currentReport.getLongitude());
        } else {
            locationInfo = context.getString(R.string.location_gps_failed);
        }
        holder.textViewLocation.setText(locationInfo);
        
        if (currentReport.isSynced()) {
            holder.textViewStatus.setText(R.string.status_synced);
            holder.textViewStatus.setTextColor(ContextCompat.getColor(context, R.color.synced));
        } else {
            holder.textViewStatus.setText(R.string.status_pending);
            holder.textViewStatus.setTextColor(ContextCompat.getColor(context, R.color.pending));
        }

        String dateStr = dateFormat.format(new Date(currentReport.getTimestamp()));
        holder.textViewTimestamp.setText(dateStr);
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
        notifyDataSetChanged();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final TextView textViewDescription;
        private final TextView textViewLocation;
        private final TextView textViewStatus;
        private final TextView textViewTimestamp;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
        }
    }
}
