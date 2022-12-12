package com.swax.schooltracker.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swax.schooltracker.R;

import java.util.List;

import Database.Repository;
import Entities.Term;

public class TermListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        populateRecyclerview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateRecyclerview();
    }

    public void populateRecyclerview(){
        RecyclerView termRecyclerView = findViewById(R.id.termListRecyclerView);
        Repository repo = new Repository(getApplication());
        List<Term> terms = repo.getAllTerms();
        final TermAdaptor adapter = new TermAdaptor(this);
        termRecyclerView.setAdapter(adapter);
        termRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setTerms(terms);
    }

    public void termListFABClick(View view){
        Intent intent = new Intent(this, TermActivity.class);
        startActivity(intent);
    }

}
