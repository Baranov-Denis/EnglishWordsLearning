package com.example.englishwordslearning.recycler_for_table_names;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishwordslearning.R;
import com.example.englishwordslearning.recycler.RecyclerAdapter;

public class RecyclerAdapterForTableNames extends RecyclerView.Adapter<RecyclerAdapterForTableNames.ViewHolder> {


    private String[] tableNames;

    private com.example.englishwordslearning.recycler.RecyclerAdapter.Listener listener;
    private SparseBooleanArray selectedItems;


    private int selectedPos = RecyclerView.NO_POSITION;

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    public interface Listener{
        void onClick(int position);
    }

    public void setListener(com.example.englishwordslearning.recycler.RecyclerAdapter.Listener listener) {
        this.listener = listener;
    }

    public RecyclerAdapterForTableNames(String[] tableNames) {
        this.tableNames = tableNames;
    }

    @NonNull
    @Override
    public RecyclerAdapterForTableNames.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_for_table_names,parent,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.itemView.setSelected(selectedPos == position);


        CardView cardView = holder.cardView;
        TextView tableName = (TextView) cardView.findViewById(R.id.table_name_place_for_recycler);

        tableName.setText(tableNames[position]);


        cardView.setOnClickListener(view ->  {
            notifyItemChanged(selectedPos);
            selectedPos = holder.getAdapterPosition();
            notifyItemChanged(selectedPos);
            if (listener != null){
                listener.onClick(holder.getAdapterPosition());
            }

        });
    }

    @Override
    public int getItemCount() {
        return tableNames.length;
    }







    public  class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
        }

    }
}
