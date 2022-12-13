package com.swax.schooltracker.UI;

import android.content.Context;
import android.content.Intent;
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

public class AssessmentAdaptor extends RecyclerView.Adapter<AssessmentAdaptor.AssessmentViewHolder>{

    private List<Assessment> mAssessmentList;
    private final Context mContext;
    private final LayoutInflater mInflater;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

    public AssessmentAdaptor(Context context){
        mInflater=LayoutInflater.from(context);
        this.mContext = context;
    }

    class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView assessmentIdView;
        private final TextView assessmentNameView;
        private final TextView assessmentStartView;
        private final TextView assessmentEndView;

        private AssessmentViewHolder(View itemView){
            super(itemView);
            assessmentIdView = itemView.findViewById(R.id.assessmentId);
            assessmentNameView = itemView.findViewById(R.id.assessmentName);
            assessmentStartView = itemView.findViewById(R.id.assessmentStart);
            assessmentEndView = itemView.findViewById(R.id.assessmentEnd);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mContext instanceof CourseActivity){
                        return;
                    } else {
                        int position = getAdapterPosition();
                        final Assessment current = mAssessmentList.get(position);
                        Intent intent = new Intent(mContext, CourseActivity.class);
                        intent.putExtra("id", current.getAssessmentId());
                        mContext.startActivity(intent);
                    }

                }
            });
        }
    }

    @NonNull
    @Override
    public AssessmentAdaptor.AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View assessmentView = mInflater.inflate(R.layout.listitem_assessment, parent, false);
        return new AssessmentAdaptor.AssessmentViewHolder(assessmentView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdaptor.AssessmentViewHolder holder, int position) {
        if(mAssessmentList != null) {
            Assessment current = mAssessmentList.get(position);
            holder.assessmentIdView.setText(current.getAssessmentId().toString());
            holder.assessmentNameView.setText(current.getAssessmentName());
            holder.assessmentStartView.setText(current.getAssessmentStart().format(formatter));
            holder.assessmentEndView.setText(current.getAssessmentEnd().format(formatter));
        }
    }

    @Override
    public int getItemCount() {
        if(mAssessmentList != null){
            return mAssessmentList.size();
        } else {
            return 0;
        }
    }

    public void setAssessments(List<Assessment> assessments){
        mAssessmentList = assessments;
        notifyDataSetChanged();
    }

}
