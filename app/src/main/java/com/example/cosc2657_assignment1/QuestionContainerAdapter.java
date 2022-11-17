package com.example.cosc2657_assignment1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosc2657_assignment1.Answer.Answer;

import java.util.List;

public class QuestionContainerAdapter extends RecyclerView.Adapter<QuestionContainerAdapter.ViewHolder> {

    private List<Answer> mData;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public QuestionContainerAdapter(Context context, List<Answer> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final EditText myTextView;
        private final CheckBox mCheckBox;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.answerDescription);
            mCheckBox = itemView.findViewById(R.id.checkButton);
            itemView.setOnClickListener(this);
        }

        public EditText getTextView() {
            return myTextView;
        }

        public CheckBox getCheckBox() {
            return mCheckBox;
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
        View view = mInflater.inflate(R.layout.answer_row_edit, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String answerName = mData.get(position).getAnswerName();
        holder.getTextView().setText(answerName);
        holder.getTextView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mData.get(holder.getAdapterPosition()).setAnswerName(s.toString());
            }
        });

        holder.getCheckBox().setActivated(mData.get(position).isCorrect());
        holder.getCheckBox().setOnClickListener(v -> mData.get(holder.getAdapterPosition()).setCorrect(holder.getCheckBox().isChecked()));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // convenience method for getting data at click position
    Answer getItem(int id) {
        return mData.get(id);
    }

    public void addItem(int position){
        try {
            mData.add(position, new Answer());
            notifyItemInserted(position);
        } catch (Exception ignored) {

        }
    }

    public void removeItem(int position) {
        try {
            mData.remove(position);
            notifyItemRemoved(position);
        } catch (Exception ignored) {

        }
    }

    public void restoreItem(Answer item, int position) {
        try {
            mData.add(position, item);
            notifyItemInserted(position);
        } catch (Exception ignored) {

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Answer> data) {
        try {
            mData = data;
            notifyDataSetChanged();
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