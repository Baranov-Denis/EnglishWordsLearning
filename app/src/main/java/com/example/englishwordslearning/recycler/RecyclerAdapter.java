package com.example.englishwordslearning.recycler;

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


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private String[] englishWord;
    private String[] russianWord;
    private String[] statistic;
    private Listener listener;
    private SparseBooleanArray selectedItems;


    private int selectedPos = RecyclerView.NO_POSITION;

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    public interface Listener{
        void onClick(int position);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public RecyclerAdapter(String[] englishWord, String[] russianWord, String[] statistic) {
        this.englishWord = englishWord;
        this.russianWord = russianWord;
        this.statistic = statistic;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_for_recycler,parent,false);
     //   CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_for_recycler_highlighted,parent,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.itemView.setSelected(selectedPos == position);


        CardView cardView = holder.cardView;
        TextView englishTextView = (TextView) cardView.findViewById(R.id.english_word_recycler);
        TextView russianTextView = (TextView) cardView.findViewById(R.id.russian_word_recycler);
        TextView stat = cardView.findViewById(R.id.statistic_word_recycler);
        englishTextView.setText(englishWord[position]);
        russianTextView.setText(russianWord[position]);
        stat.setText(statistic[position]);

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
      return englishWord.length;
    }







    public  class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
        }

    }
}
