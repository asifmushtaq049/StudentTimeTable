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
import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.model.Subject;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class SubjectsAdapter extends FirestoreAdapter<SubjectsAdapter.ViewHolder>{

    public SubjectsAdapter(Query query) {
        super(query);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_activity_subject, parent, false));
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
                            db.collection(DBMeta.COLLECTION_SUBJECT).document(snapshot.getId())
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
                            final Subject subject = snapshot.toObject(Subject.class);
                            if(subject != null){
                                subject.setId(snapshot.getId());
                                Bundle bundle = new Bundle();
                                bundle.putParcelable(KeyMeta.SUBJECT, subject);
                                ActivityUtil.moveToActivity(holder.itemView.getContext(), SubjectAddActivity.class, bundle);
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
        private TextView txtSubjectName;
        private TextView txtSubjectCode;
        public View itemView;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            txtSubjectName = itemView.findViewById(R.id.item_act_subject_name);
            txtSubjectCode = itemView.findViewById(R.id.item_act_subject_code);

        }

        public void bind(final DocumentSnapshot snapshot) {
            final Subject buss = snapshot.toObject(Subject.class);
            if(buss != null){
                txtSubjectName.setText(buss.getName().toUpperCase());
                txtSubjectCode.setText(buss.getCode().toUpperCase());
            }
        }
    }
}
