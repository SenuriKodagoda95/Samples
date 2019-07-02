package com.example.sampleapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleapplication.R;
import com.example.sampleapplication.model.Contact;

import java.util.ArrayList;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ContactViewHolder> {

    private ArrayList<Contact> list = new ArrayList<>();

    public static class ContactViewHolder extends RecyclerView.ViewHolder{
        public TextView name,number;

        public ContactViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            number = (TextView) view.findViewById(R.id.number);
        }


    }

    public SearchListAdapter(ArrayList<Contact> list){
        this.list = list;
    }

    @NonNull
    @Override
    public SearchListAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_view_layout,parent,false);

        return new SearchListAdapter.ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact model = list.get(position);
        holder.name.setText(model.getName());
        holder.number.setText(model.getNumber());

    }



    @Override
    public int getItemCount() {
        return list.size();
    }


}
