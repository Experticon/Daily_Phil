package com.example.dailyphill;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class under_note_adapter extends RecyclerView.Adapter<under_note_adapter.MyViewHolder> implements Filterable {

    public ArrayList<Under_note> notes_list;
    public ArrayList<Under_note> notes_list_filter;

    private static OnItemClickListener listener;

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0) {
                    filterResults.values = notes_list_filter;
                    filterResults.count = notes_list_filter.size();
                }
                else {
                    String searchStr = constraint.toString().toLowerCase();
                    ArrayList<Under_note> userModels = new ArrayList<>();
                    for (Under_note under_note: notes_list_filter) {
                        if(under_note.getDay().toLowerCase().contains(searchStr) ||
                                under_note.getText().toLowerCase().contains(searchStr))
                            userModels.add(under_note);
                    }
                    filterResults.values = userModels;
                    filterResults.count = userModels.size();
                }


                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notes_list = (ArrayList<Under_note>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener (OnItemClickListener clickListener){
        listener = clickListener;
    }
    public under_note_adapter(ArrayList<Under_note> notes_list) {
        this.notes_list = notes_list;
        this.notes_list_filter = notes_list;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView day_name;
        private TextView date;
        private ImageButton lookButton;

        public MyViewHolder(final View view, OnItemClickListener listener) {
            super(view);
            day_name = view.findViewById(R.id.day_name);
            date = view.findViewById(R.id.date);
            lookButton = view.findViewById(R.id.look_button);
            lookButton.setImageResource(R.drawable.edit_pen);

            lookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public under_note_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.under_note_list_item, parent, false);
        return new MyViewHolder(listItem, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String Text = notes_list.get(position).getText();
        String date = notes_list.get(position).getDay();
        holder.day_name.setText(Text);
        holder.date.setText(date);
    }

    @Override
    public int getItemCount() {
        return notes_list.size();
    }

}

