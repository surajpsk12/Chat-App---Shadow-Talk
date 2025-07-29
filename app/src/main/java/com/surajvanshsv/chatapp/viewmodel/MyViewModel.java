package com.surajvanshsv.chatapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.surajvanshsv.chatapp.Repository.Repository;
import com.surajvanshsv.chatapp.model.ChatGroup;
import com.surajvanshsv.chatapp.model.ChatMessage;

import java.util.List;

public class MyViewModel extends AndroidViewModel {

    Repository repository;

    public MyViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();

    }

    public void signUpAnonymousUser(){
        Application c = this.getApplication();
        repository.firebaseAnonymousAuth(c);
    }



    // getting chat groups
    public MutableLiveData<List<ChatGroup>> getGroupList (){
        return repository.getChatGroupMutableLiveData();
    }

    public void createNewGroup(String groupName){
        repository.createNewChatGroup(groupName);
    }

    //
    // Messages
    public MutableLiveData<List<ChatMessage>> getMessagesLiveData(String groupName){
        return repository.getMessagesLiveData(groupName);
    }

    public void sendMessage(String msg, String chatGroup){
        repository.sendMessage(msg,chatGroup);
    }

}
