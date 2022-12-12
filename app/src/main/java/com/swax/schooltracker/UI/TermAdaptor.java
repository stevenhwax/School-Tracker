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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import Entities.Term;

public class TermAdaptor extends RecyclerView.Adapter<TermAdaptor.TermViewHolder> {

    private List<Term> mTermList;
    private final Context mContext;
    private final LayoutInflater mInflater;
    private String myFormat = "MM/dd/yyyy";
    private SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public TermAdaptor(Context context){
        mInflater=LayoutInflater.from(context);
        this.mContext = context;
    }

    class TermViewHolder extends RecyclerView.ViewHolder{
        private final TextView termIdView;
        private final TextView termNameView;
        private final TextView termStartView;
        private final TextView termEndView;

        private TermViewHolder(View itemView){
            super(itemView);
            termIdView = itemView.findViewById(R.id.termId);
            termNameView = itemView.findViewById(R.id.termName);
            termStartView = itemView.findViewById(R.id.termStart);
            termEndView = itemView.findViewById(R.id.termEnd);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Term current = mTermList.get(position);
                    Intent intent = new Intent(mContext, TermActivity.class);
                    intent.putExtra("id", current.getTermId());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public TermAdaptor.TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View termView=mInflater.inflate(R.layout.listitem_term, parent, false);
        return new TermViewHolder(termView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdaptor.TermViewHolder holder, int position) {
        if(mTermList != null) {
            Term current = mTermList.get(position);
            holder.termIdView.setText(current.getTermId().toString());
            holder.termNameView.setText(current.getTermName());
            holder.termStartView.setText(current.getTermStart().format(formatter));
            holder.termEndView.setText(current.getTermEnd().format(formatter));
        }
    }

    @Override
    public int getItemCount() {
        if(mTermList != null){
            return mTermList.size();
        } else {
            return 0;
        }
    }

    public void setTerms(List<Term> terms){
        mTermList = terms;
        notifyDataSetChanged();
    }
}
