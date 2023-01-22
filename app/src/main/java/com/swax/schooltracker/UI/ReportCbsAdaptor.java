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

import Entities.Course;

public class ReportCbsAdaptor extends RecyclerView.Adapter<ReportCbsAdaptor.ReportCbsViewHolder> {

    private List<Course> mCourseList;
    private final Context mContext;
    private final LayoutInflater mInflater;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public ReportCbsAdaptor(Context context){
        mInflater=LayoutInflater.from(context);
        this.mContext = context;
    }

    class ReportCbsViewHolder extends RecyclerView.ViewHolder {
        private final TextView reportCbsId;
        private final TextView reportCbsName;
        private final TextView reportCbsStatus;
        private final TextView reportCbsStart;
        private final TextView reportCbsEnd;

        private ReportCbsViewHolder(View itemView){
            super(itemView);
            reportCbsId = itemView.findViewById(R.id.reportCbsId);
            reportCbsName = itemView.findViewById(R.id.reportCbsName);
            reportCbsStatus = itemView.findViewById(R.id.reportCbsStatus);
            reportCbsStart = itemView.findViewById(R.id.reportCbsStart);
            reportCbsEnd = itemView.findViewById(R.id.reportCbsEnd);
        }
    }

    @NonNull
    @Override
    public ReportCbsAdaptor.ReportCbsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View reportCbsView = mInflater.inflate(R.layout.listitem_reportcbs, parent, false);
        return new ReportCbsAdaptor.ReportCbsViewHolder(reportCbsView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportCbsAdaptor.ReportCbsViewHolder holder, int position) {
        if(mCourseList != null) {
            Course current = mCourseList.get(position);
            holder.reportCbsId.setText(current.getCourseId().toString());
            holder.reportCbsName.setText(current.getCourseName());
            holder.reportCbsStatus.setText(current.getCourseStatus());
            holder.reportCbsStart.setText(current.getCourseStart().format(formatter));
            holder.reportCbsEnd.setText(current.getCourseEnd().format(formatter));
        }
    }

    @Override
    public int getItemCount() {
        if(mCourseList != null){
            return mCourseList.size();
        } else {
            return 0;
        }
    }

    public void setCourses(List<Course> courses){
        mCourseList = courses;
        notifyDataSetChanged();
    }
}
