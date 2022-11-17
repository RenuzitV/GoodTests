package com.example.cosc2657_assignment1;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cosc2657_assignment1.Quiz.Quiz;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeToDeleteCallback.QuizContainerAdapter.ItemClickListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference cRef = db.collection(Quiz.CollectionName);

    ConstraintLayout layout;
    FloatingActionButton addNewQuizButton;
    RecyclerView recyclerView;
    SwipeToDeleteCallback.QuizContainerAdapter adapter;

    ArrayList<Quiz> quizzes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.parentid);
        addNewQuizButton = findViewById(R.id.addNewQuizButton);
        recyclerView = findViewById(R.id.quizContainer);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(() -> {
            refreshData(); // your code
            pullToRefresh.setRefreshing(false);
        });

        addNewQuizButton.setOnClickListener(view -> {
            Quiz quiz = new Quiz();
            cRef.add(quiz)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SwipeToDeleteCallback.QuizContainerAdapter(this, quizzes);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        enableSwipeToDeleteAndUndo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        cRef.addSnapshotListener(this, (value, error) -> {
            if (error != null || value == null){
                System.out.println("Error loading quizzes, " + error);
            }
            else {
                fetchQuizNames(value.getDocuments());
                adapter.notifyItemRangeChanged(0, quizzes.size());
                System.out.println("Found change in quizzes, count is " + quizzes.size());
            }
        });
        System.out.println("successfully added snapshot listener");
    }

    protected void fetchQuizNames(List<DocumentSnapshot> documentSnapshots){
        while (documentSnapshots.size() < quizzes.size()) {
            quizzes.remove(quizzes.size()-1);
            adapter.notifyItemRemoved(quizzes.size());
        }

        for (int i = 0; i < documentSnapshots.size(); ++i) {
            DocumentSnapshot dc = documentSnapshots.get(i);
            Quiz temp = dc.toObject(Quiz.class);
            if (temp != null) {
                temp.setQid(dc.getId());
                if (quizzes.size() > i && !quizzes.get(i).equals(temp)) {
                    System.out.println("update on old quiz " + quizzes.get(i).getQuizName() + " to " + temp.getQuizName());
                    quizzes.set(i, temp);
                    adapter.notifyItemChanged(i);
                }
                else if (quizzes.size() <= i){
                    System.out.println("new quiz " + temp.getQuizName() + " received.");
                    quizzes.add(temp);
                    adapter.notifyItemChanged(i);
                }
            }
        }
    }

    void refreshData(){
        cRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                fetchQuizNames(task.getResult().getDocuments());
                System.out.println("successfully fetched quizzes, got " + quizzes.size() + " quizzes.");
            }
            else {
                System.out.println("Error fetching quiz names, " + task.getException());
            }
        });
    }


    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final Quiz item = adapter.getItem(position);

                adapter.removeItem(position);
                db.collection(Quiz.CollectionName).document(item.qid).delete().addOnCompleteListener(deleteHandler(item, position));
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    OnCompleteListener<Void> deleteHandler(Quiz item, int position){
        return task -> {
            if (task.isSuccessful()){
                Snackbar snackbar = Snackbar
                        .make(layout, "Quiz " + item.getQuizName() + " was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", view -> {
                    adapter.restoreItem(item, position);
                    cRef.document(item.getQid()).set(item)
                            .addOnSuccessListener(documentReference -> Log.d(TAG, "readded quiz " + item.getQid()))
                            .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
            else {
                Snackbar snackbar = Snackbar
                        .make(layout, "Could not remove item from the list", Snackbar.LENGTH_LONG);
                snackbar.setAction("RETRY", view -> db.collection(Quiz.CollectionName).document(item.qid).delete().addOnCompleteListener(deleteHandler(item, position)));
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(MainActivity.this, QuizOverviewActivity.class);
        intent.putExtra("quiz", adapter.getItem(position));
        startActivity(intent);
//        Toast.makeText(this, "You clicked " + adapter.getItem(position).getQuizName() + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

}