package com.example.sampleapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sampleapplication.R;
import com.example.sampleapplication.adapter.SearchListAdapter;
import com.example.sampleapplication.dao.ContactDAO;
import com.example.sampleapplication.database.DBHelper;
import com.example.sampleapplication.model.Contact;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private EditText mSearchText;
    private RecyclerView mRecyclerView;
    private Button mBtnSearch;

    private DBHelper helper;
    private ContactDAO dao;
    private SearchListAdapter listAdapter;
    private ArrayList<Contact> mContactModelArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        helper = new DBHelper(SearchActivity.this);
        dao = new ContactDAO(SearchActivity.this);
        mSearchText = (EditText) findViewById(R.id.editText);
        mRecyclerView = (RecyclerView) findViewById(R.id.view);
        mBtnSearch = (Button) findViewById(R.id.button2);

        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mSearchText.getText().toString();
//                dao.search(name);
//                listAdapter = new SearchListAdapter(mContactModelArrayList);
//                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//                mRecyclerView.setLayoutManager(mLayoutManager);
//                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//                mRecyclerView.setAdapter(listAdapter);
            }
        });




    }


    public ArrayList<String> getListCountry(){
        ArrayList<String> listcountry = new ArrayList<>();
        listcountry.add("Nigeria");
        listcountry.add("China");
        listcountry.add("USA");
        listcountry.add("Ghana");
        listcountry.add("Canada");
        listcountry.add("Finland");
        listcountry.add("Denmark");
        listcountry.add("Argentina");
        listcountry.add("Andorra");
        listcountry.add("Togo");
        return listcountry;
    }
}

