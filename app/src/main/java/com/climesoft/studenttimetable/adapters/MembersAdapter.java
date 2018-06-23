package com.climesoft.studenttimetable.adapters;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.climesoft.studenttimetable.GroupAddActivity;
import com.climesoft.studenttimetable.GroupChatActivity;
import com.climesoft.studenttimetable.MembersActivity;
import com.climesoft.studenttimetable.R;
import com.climesoft.studenttimetable.SubjectAddActivity;
import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.model.Group;
import com.climesoft.studenttimetable.model.Subject;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder>{

    private ArrayList<String> members;
    protected FirebaseFirestore rootdb;
    private Group group;
    protected FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public MembersAdapter(ArrayList<String> members, FirebaseFirestore rootdb, Group group) {
        this.members = members;
        this.rootdb = rootdb;
        this.group = group;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_group_member, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.bind(position);
        if(user.getUid().equals(group.getAdmin())){
            addLongListener(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    private void addLongListener(final ViewHolder holder, final int position){
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                final CharSequence[] items = {"Remove"};

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());

                builder.setTitle("Select The Action");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(item == 0){

                            final Map<String, Object> data = new HashMap<>();
                            data.put(DBMeta.DOCUMENT_GROUP_MEMBERS+"."+members.get(position), FieldValue.delete());
                            rootdb.collection(DBMeta.COLLECTION_GROUP).document(group.getId())
                                    .update(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            ActivityUtil.showMessage(v.getContext(), "Member Removed!");
                                            members.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position,members.size());
                                        }
                                    });
                        }
                    }
                });
                builder.show();
                return true;
            }
        });
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtMemberName;
        private TextView txtMemberEmail;
        public View itemView;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            txtMemberName = itemView.findViewById(R.id.txtMemberName);
            txtMemberEmail = itemView.findViewById(R.id.txtMemberEmail);
        }

        public void bind(final int position) {
            String memberId = members.get(position);
            rootdb.collection(DBMeta.COLLECTION_USER)
                    .document(memberId)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            txtMemberName.setText(documentSnapshot.getString(DBMeta.DOCUMENT_USER_NAME));
                            txtMemberEmail.setText(documentSnapshot.getString(DBMeta.DOCUMENT_USER_EMAIL));
                        }
                    });

        }
    }
}
