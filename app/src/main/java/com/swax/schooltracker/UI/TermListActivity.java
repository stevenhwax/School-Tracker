package com.swax.schooltracker.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swax.schooltracker.R;

import java.util.ArrayList;
import java.util.List;

import Database.Repository;
import Entities.Term;

public class TermListActivity extends AppCompatActivity {

    private List<Term> terms;
    private Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(repo == null){
            repo = new Repository(getApplication());
        }
        terms = repo.getAllTerms();
        populateRecyclerview(terms);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(repo == null){
            repo = new Repository(getApplication());
        }
        terms = repo.getAllTerms();
        populateRecyclerview(terms);
    }

    public void termListSearchButton(View view){
        terms = repo.getAllTerms();
        List<Term> filtered = new ArrayList<>();
        EditText termListSearchEditText = findViewById(R.id.termListSearchEditText);
        for(Term t : terms){
            if(t.getTermName().contains(termListSearchEditText.getText())){
                filtered.add(t);
            }
        }
        populateRecyclerview(filtered);
    }

    public void populateRecyclerview(List<Term> terms){
        RecyclerView termRecyclerView = findViewById(R.id.termListRecyclerView);
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
