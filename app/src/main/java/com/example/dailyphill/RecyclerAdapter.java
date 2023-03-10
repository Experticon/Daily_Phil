package com.example.dailyphill;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<Day_Name> days_list;

    private static OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onClick(int position);
    }

    public void setOnItemClickListener (OnItemClickListener clickListener){
        listener = clickListener;
    }

    public RecyclerAdapter(ArrayList<Day_Name> days_list) {
        this.days_list = days_list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView day_name;
        private TextView date;
        private ImageButton deleteButton;
        private ImageButton updateButton;

        public MyViewHolder(final View view, OnItemClickListener listener) {
            super(view);
            day_name = view.findViewById(R.id.day_name);
            date = view.findViewById(R.id.date);
            deleteButton = view.findViewById(R.id.delete_button);
            updateButton = view.findViewById(R.id.update_button);
            deleteButton.setImageResource(R.drawable.trash_can);
            updateButton.setImageResource(R.drawable.edit_pen);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(listItem, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String showText = days_list.get(position).getShowText();
        String date = days_list.get(position).getDay();
        holder.day_name.setText(showText);
        holder.date.setText(date);
        /*holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //days_list.remove(holder.getAdapterPosition());
                    //notifyDataSetChanged();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return days_list.size();
    }

}
