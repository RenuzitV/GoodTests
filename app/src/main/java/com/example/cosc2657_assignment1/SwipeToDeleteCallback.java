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

abstract public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    Context mContext;
    private final Paint mClearPaint = new Paint();
    private final ColorDrawable mBackground = new ColorDrawable();
    private final int backgroundColor = Color.parseColor("#b80f0a");


    protected SwipeToDeleteCallback(Context context) {
        super(0, ItemTouchHelper.LEFT);
        mContext = context;
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();

        boolean isCancelled = dX == 0 && !isCurrentlyActive;

        if (isCancelled) {
            clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }

        mBackground.setColor(backgroundColor);
        mBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        mBackground.draw(c);


    }

    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
        c.drawRect(left, top, right, bottom, mClearPaint);

    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.7f;
    }

    public static class QuizContainerAdapter extends RecyclerView.Adapter<QuizContainerAdapter.ViewHolder> {

        private final List<Quiz> mData;
        private final LayoutInflater mInflater;
        private ItemClickListener mClickListener;

        // data is passed into the constructor
        public QuizContainerAdapter(Context context, List<Quiz> data) {
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
            String quizName = mData.get(position).getQuizName();
            holder.getTextView().setText(quizName);
        }

        // total number of rows
        @Override
        public int getItemCount() {
            return mData.size();
        }


        // convenience method for getting data at click position
        Quiz getItem(int id) {
            return mData.get(id);
        }

        public void removeItem(int position) {
            try {
                mData.remove(position);
                notifyItemRemoved(position);
            } catch (Exception ignored){

            }
        }

        public void restoreItem(Quiz item, int position) {
            try{
                mData.add(position, item);
                notifyItemInserted(position);
            } catch (Exception ignored){

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
}