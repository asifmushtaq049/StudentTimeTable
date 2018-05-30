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

import com.climesoft.studenttimetable.GroupChatActivity;
import com.climesoft.studenttimetable.R;
import com.climesoft.studenttimetable.SubjectAddActivity;
import com.climesoft.studenttimetable.meta.DBMeta;
import com.climesoft.studenttimetable.meta.KeyMeta;
import com.climesoft.studenttimetable.model.Group;
import com.climesoft.studenttimetable.model.Subject;
import com.climesoft.studenttimetable.util.ActivityUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class GroupChatAdapter extends FirestoreAdapter<GroupChatAdapter.ViewHolder>{

    private RecyclerView mRecyclerView;
    public GroupChatAdapter(Query query) {
        super(query);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == 0){
            return new OtherMessageHolder(inflater.inflate(R.layout.item_group_chat_other, parent, false));
        }
        if(viewType == 1){
            return new MessageHolder(inflater.inflate(R.layout.item_group_chat_me, parent, false));
        }
        else{
            return new NotifyHolder(inflater.inflate(R.layout.item_group_chat_notify, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        final DocumentSnapshot snapshot = getSnapshot(position);
        String member = snapshot.getString(DBMeta.DOCUMENT_MESSAGES_MEMBER);
        Boolean isNotify = snapshot.getBoolean(DBMeta.DOCUMENT_MESSAGES_IMPORTANT);
        if(isNotify != null){
            if(isNotify){
                return 2;
            }
        }
        if(member != null){
            if(member.equals(user.getUid())){
                return 1;
            }
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.bind(getSnapshot(position));
    }

    class NotifyHolder extends ViewHolder {
        private TextView notify_text;
        private TextView notify_title;
        private TextView notify_subject;
        private TextView notify_date_time;

        public View itemView;
        public NotifyHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            notify_text = itemView.findViewById(R.id.notify_text);
            notify_title = itemView.findViewById(R.id.notify_title);
            notify_subject = itemView.findViewById(R.id.notify_subject);
            notify_date_time = itemView.findViewById(R.id.notify_date_time);
        }
        @Override
        public void bind(final DocumentSnapshot snapshot) {
            notify_text.setText(snapshot.getString(DBMeta.DOCUMENT_MESSAGES_TEXT));
            notify_title.setText(snapshot.getString(DBMeta.DOCUMENT_MESSAGES_TITLE));
            notify_subject.setText(snapshot.getString(DBMeta.DOCUMENT_MESSAGES_SUBJECT).toUpperCase());
            notify_date_time.setText(snapshot.getString(DBMeta.DOCUMENT_MESSAGES_DATE_TIME).toUpperCase());
        }

    }

    class OtherMessageHolder extends ViewHolder{
        private TextView txtText;
        private TextView txtName;

        public View itemView;
        public OtherMessageHolder(final View itemView){
            super(itemView);
            this.itemView = itemView;
            txtText = itemView.findViewById(R.id.txtText);
            txtName = itemView.findViewById(R.id.txtName);
        }
        @Override
        public void bind(final DocumentSnapshot snapshot) {

            rootDb.collection(DBMeta.COLLECTION_USER)
                    .document(snapshot.getString(DBMeta.DOCUMENT_MESSAGES_MEMBER))
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            txtText.setText(snapshot.getString(DBMeta.DOCUMENT_MESSAGES_TEXT));
                            txtName.setText(documentSnapshot.getString(DBMeta.DOCUMENT_USER_NAME));
                        }
                    });
        }
    }

    class MessageHolder extends ViewHolder{
        private TextView txtText;

        public View itemView;
        public MessageHolder(final View itemView){
            super(itemView);
            this.itemView = itemView;
            txtText = itemView.findViewById(R.id.txtText);
        }
        @Override
        public void bind(final DocumentSnapshot snapshot) {
            txtText.setText(snapshot.getString(DBMeta.DOCUMENT_MESSAGES_TEXT));
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(final View itemView) {
            super(itemView);
        }
        public void bind(final DocumentSnapshot snapshot) {

        }
    }
}

