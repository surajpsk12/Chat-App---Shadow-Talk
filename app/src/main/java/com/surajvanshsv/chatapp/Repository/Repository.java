package com.surajvanshsv.chatapp.Repository;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.surajvanshsv.chatapp.model.ChatGroup;
import com.surajvanshsv.chatapp.model.ChatMessage;
import com.surajvanshsv.chatapp.views.GroupsActvity;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    MutableLiveData<List<ChatGroup>> chatGroupMutableLiveData ;
    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference groupReference;

    MutableLiveData<List<ChatMessage>> messagesLiveData;

    public Repository(){
        this.chatGroupMutableLiveData = new MutableLiveData<>();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        messagesLiveData = new MutableLiveData<>();

    }



// authentication
    public void firebaseAnonymousAuth(Context context){
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent i = new Intent(context, GroupsActvity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            }
        });
    }
    public String getCurrentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    public void signOUT(){
        FirebaseAuth.getInstance().signOut();
    }

    // getting chatgroups available from firebase database


    public MutableLiveData<List<ChatGroup>> getChatGroupMutableLiveData() {
        List<ChatGroup> groupList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren() ){
                    ChatGroup group = new ChatGroup(dataSnapshot.getKey());
                    groupList.add(group);

                }
                chatGroupMutableLiveData.postValue(groupList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return chatGroupMutableLiveData;
    }

    // Creating a new group
    public void createNewChatGroup(String groupName){
        reference.child(groupName).setValue(groupName);

    }

    // Getting Messages Live Data
    public MutableLiveData<List<ChatMessage>> getMessagesLiveData(String groupName) {
        // child(groupName): used to specify a child node under the root reference
        groupReference = database.getReference().child(groupName);

        List<ChatMessage> messagesList = new ArrayList<>();

        groupReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesList.clear();


                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                    messagesList.add(message);
                }

                messagesLiveData.postValue(messagesList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return messagesLiveData;
    }

    public void sendMessage(String messageText, String chatGroup){

        DatabaseReference ref = database
                .getReference(chatGroup);


        if (!messageText.trim().equals("")){
            ChatMessage msg = new ChatMessage(
                    FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    messageText,
                    System.currentTimeMillis()
            );

            String randomKey = ref.push().getKey();

            ref.child(randomKey).setValue(msg);

        }
    }


}


