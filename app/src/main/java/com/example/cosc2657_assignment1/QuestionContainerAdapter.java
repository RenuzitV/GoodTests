package com.example.cosc2657_assignment1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosc2657_assignment1.Question.Question;
import com.example.cosc2657_assignment1.Quiz.Quiz;

import java.util.List;

public class QuestionContainerAdapter extends RecyclerView.Adapter<QuestionContainerAdapter.ViewHolder> {

    private final List<String> mData;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public QuestionContainerAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.description);
            itemView.setOnClickListener(this);
        }

        public TextView getTextView() {
            return myTextView;
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_quizrow, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String quizName = mData.get(position);
        holder.getTextView().setText(quizName);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    public void removeItem(int position) {
        try {
            mData.remove(position);
            notifyItemRemoved(position);
        } catch (Exception ignored) {

        }
    }

    public void restoreItem(String item, int position) {
        try {
            mData.add(position, item);
            notifyItemInserted(position);
        } catch (Exception ignored) {

        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}