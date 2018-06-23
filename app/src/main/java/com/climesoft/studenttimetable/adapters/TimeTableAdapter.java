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

import com.climesoft.studenttimetable.R;
import com.climesoft.studenttimetable.SubjectAddActivity;
import com.climesoft.studenttimetable.TimeTableAddActivity;
import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.model.Subject;
import com.climesoft.studenttimetable.model.TimeTable;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.sql.Time;

public class TimeTableAdapter extends FirestoreAdapter<TimeTableAdapter.ViewHolder>{

    public TimeTableAdapter(Query query) {
        super(query);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_activity_timetable, parent, false));
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
                            db.collection(DBMeta.COLLECTION_TIMETABLE).document(snapshot.getId())
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
                            final TimeTable timeTable = snapshot.toObject(TimeTable.class);
                            if(timeTable != null){
                                timeTable.setId(snapshot.getId());
                                Bundle bundle = new Bundle();
                                bundle.putString(KeyMeta.DAY, snapshot.getString(DBMeta.DOCUMENT_TIMETABLE_DAY));
                                bundle.putParcelable(KeyMeta.TIMETABLE, timeTable);
                                ActivityUtil.moveToActivity(holder.itemView.getContext(), TimeTableAddActivity.class, bundle);
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
        private TextView txtSubject;
        private TextView txtTime;
        public View itemView;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            txtSubject = itemView.findViewById(R.id.txtSubject);
            txtTime = itemView.findViewById(R.id.txtTime);

        }

        public void bind(final DocumentSnapshot snapshot) {
            final TimeTable timeTable = snapshot.toObject(TimeTable.class);
            if(timeTable != null){
                timeTable.setSubjectId(snapshot.getDocumentReference(DBMeta.DOCUMENT_TIMETABLE_SUBJECT).getId());
                db.collection(DBMeta.COLLECTION_SUBJECT)
                    .document(timeTable.getSubjectId())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            txtSubject.setText(documentSnapshot.getString(DBMeta.DOCUMENT_SUBJECT_NAME).toUpperCase());
                            txtTime.setText(timeTable.getTime().toUpperCase());
                        }
                    });
            }
        }
    }
}
