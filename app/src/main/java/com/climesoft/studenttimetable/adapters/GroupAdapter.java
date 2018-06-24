package com.climesoft.studenttimetable.adapters;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.climesoft.studenttimetable.GroupAddActivity;
import com.climesoft.studenttimetable.GroupChatActivity;
import com.climesoft.studenttimetable.R;
import com.climesoft.studenttimetable.SubjectAddActivity;
import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.model.Group;
import com.climesoft.studenttimetable.model.Subject;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class GroupAdapter extends FirestoreAdapter<GroupAdapter.ViewHolder>{

    public GroupAdapter(Query query) {
        super(query);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_activity_group, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.bind(getSnapshot(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DocumentSnapshot snapshot = getSnapshot(holder.getAdapterPosition());
                final Group group = snapshot.toObject(Group.class);
                if(group != null){
                    group.setId(snapshot.getId());
                    final Bundle bundle = new Bundle();
                    bundle.putParcelable(KeyMeta.GROUP, group);
                    ActivityUtil.moveToActivity(holder.itemView.getContext(), GroupChatActivity.class, bundle);
                }
            }
        });
        if(user.getUid().equals(getSnapshot(position).getString(DBMeta.DOCUMENT_GROUP_ADMIN))){
            addLongListener(holder, position);
        }
    }

    private void addLongListener(final ViewHolder holder, int position){
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final CharSequence[] items = {"Delete"};

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());

                builder.setTitle("Select The Action");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(item == 0){
                            DocumentSnapshot snapshot = getSnapshot(holder.getAdapterPosition());
                            rootDb.collection(DBMeta.COLLECTION_GROUP).document(snapshot.getId())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            ActivityUtil.showMessage(holder.itemView.getContext(), "Deleted!");
                                            setQuery(mQuery);
                                        }
                                    });
                        }
//                        else if(item == 0){
//                            DocumentSnapshot snapshot = getSnapshot(holder.getAdapterPosition());
//                            final Group group = snapshot.toObject(Group.class);
//                            if(group != null){
//                                group.setId(snapshot.getId());
//                                Bundle bundle = new Bundle();
//                                bundle.putParcelable(KeyMeta.GROUP, group);
//                                ActivityUtil.moveToActivity(holder.itemView.getContext(), GroupAddActivity.class, bundle);
//                            }
//                        }
                    }
                });
                builder.show();
                return true;
            }
        });
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtGroupName;
        public View itemView;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            txtGroupName = itemView.findViewById(R.id.txtGroupName);
        }

        public void bind(final DocumentSnapshot snapshot) {
            final Group group = snapshot.toObject(Group.class);
            if(group != null){
                txtGroupName.setText(group.getName());
            }
        }
    }
}
