package com.example.movietask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder>{
    private ArrayList<String> suggestions;
    ItemClicked activity;

    public interface ItemClicked{
        void onItemClicked(int Index);
    }
    public SuggestionAdapter(Context context, ArrayList<String> list){
        suggestions = list;
        activity = (ItemClicked) context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvSuggestion;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvSuggestion = itemView.findViewById(R.id.tvSuggestion);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onItemClicked(suggestions.indexOf((String) v.getTag()));
                }
            });
        }
    }
    @NonNull
    @Override
    public SuggestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.suggestion_layout, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionAdapter.ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(suggestions.get(i));
        viewHolder.tvSuggestion.setText(suggestions.get(i));
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }
}
