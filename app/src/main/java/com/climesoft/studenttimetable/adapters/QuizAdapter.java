package com.climesoft.studenttimetable.adapters;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.climesoft.studenttimetable.AssignmentAddActivity;
import com.climesoft.studenttimetable.QuizAddActivity;
import com.climesoft.studenttimetable.R;
import com.climesoft.studenttimetable.SubjectAddActivity;
import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.model.Assignment;
import com.climesoft.studenttimetable.model.Quiz;
import com.climesoft.studenttimetable.model.Subject;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class QuizAdapter extends FirestoreAdapter<QuizAdapter.ViewHolder>{

    public QuizAdapter(Query query) {
        super(query);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_assign_quiz, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.bind(getSnapshot(position));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final CharSequence[] items = {"Edit","Delete"};

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());

                builder.setTitle("Select The Action");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(item == 1){
                            DocumentSnapshot snapshot = getSnapshot(holder.getAdapterPosition());
                            db.collection(DBMeta.COLLECTION_QUIZ).document(snapshot.getId())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            ActivityUtil.showMessage(holder.itemView.getContext(), "Deleted!");
                                            setQuery(mQuery);
                                        }
                                    });
                        }
                        else if(item == 0){
                            DocumentSnapshot snapshot = getSnapshot(holder.getAdapterPosition());
                            final Quiz quiz = snapshot.toObject(Quiz.class);
                            if(quiz != null){
                                quiz.setId(snapshot.getId());
                                quiz.setSubjectId(snapshot.getDocumentReference(DBMeta.DOCUMENT_QUIZ_SUBJECT).getId());
                                Bundle bundle = new Bundle();
                                bundle.putParcelable(KeyMeta.QUIZ, quiz);
                                ActivityUtil.moveToActivity(holder.itemView.getContext(), QuizAddActivity.class, bundle);
                            }
                        }
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTopic;
        private TextView txtSubject;
        private TextView txtDeadline;
        public View itemView;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            txtTopic = itemView.findViewById(R.id.act_quiz_ass_topic);
            txtSubject = itemView.findViewById(R.id.act_quiz_ass_subject);
            txtDeadline = itemView.findViewById(R.id.act_quiz_ass_deadline);
        }

        public void bind(final DocumentSnapshot snapshot) {
            final Quiz quiz = snapshot.toObject(Quiz.class);
            if(quiz != null){
                if(snapshot.getDocumentReference(DBMeta.DOCUMENT_QUIZ_SUBJECT)!=null){
                    quiz.setSubjectId(snapshot.getDocumentReference(DBMeta.DOCUMENT_QUIZ_SUBJECT).getId());
                    db.collection(DBMeta.COLLECTION_SUBJECT).document(quiz.getSubjectId()).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    txtTopic.setText(quiz.getTopic());
                                    txtSubject.setText(documentSnapshot.getString(DBMeta.DOCUMENT_SUBJECT_NAME).toUpperCase());
                                    txtDeadline.setText(quiz.getDate() + " " + quiz.getTime());
                                }
                            });
                }
            }
        }
    }
}