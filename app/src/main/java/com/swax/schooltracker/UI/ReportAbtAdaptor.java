package com.swax.schooltracker.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swax.schooltracker.R;

import java.time.format.DateTimeFormatter;
import java.util.List;

import Entities.Assessment;

public class ReportAbtAdaptor extends RecyclerView.Adapter<ReportAbtAdaptor.ReportAbtViewHolder>{

    List<Assessment> mAssessments;
    private final Context mContext;
    private final LayoutInflater mInflater;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public ReportAbtAdaptor(Context context){
        mInflater=LayoutInflater.from(context);
        this.mContext = context;
    }

    class ReportAbtViewHolder extends RecyclerView.ViewHolder {
        private final TextView reportAbtId;
        private final TextView reportAbtName;
        private final TextView reportAbtType;
        private final TextView reportAbtStart;
        private final TextView reportAbtEnd;

        private ReportAbtViewHolder(View itemView){
            super(itemView);
            reportAbtId = itemView.findViewById(R.id.reportAbtId);
            reportAbtName = itemView.findViewById(R.id.reportAbtName);
            reportAbtType = itemView.findViewById(R.id.reportAbtType);
            reportAbtStart = itemView.findViewById(R.id.reportAbtStart);
            reportAbtEnd = itemView.findViewById(R.id.reportAbtEnd);
        }
    }

    @NonNull
    @Override
    public ReportAbtAdaptor.ReportAbtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View reportAbtView = mInflater.inflate(R.layout.listitem_reportabt, parent, false);
        return new ReportAbtAdaptor.ReportAbtViewHolder(reportAbtView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAbtAdaptor.ReportAbtViewHolder holder, int position) {
        if(mAssessments != null) {
            Assessment current = mAssessments.get(position);
            holder.reportAbtId.setText(current.getAssessmentId().toString());
            holder.reportAbtName.setText(current.getAssessmentName());
            holder.reportAbtType.setText(current.getAssessmentType());
            holder.reportAbtStart.setText(current.getAssessmentStart().format(formatter));
            holder.reportAbtEnd.setText(current.getAssessmentEnd().format(formatter));
        }
    }

    @Override
    public int getItemCount() {
        if(mAssessments != null){
            return mAssessments.size();
        } else {
            return 0;
        }
    }

    public void setAssessments(List<Assessment> assessments){
        mAssessments = assessments;
        notifyDataSetChanged();
    }
}
