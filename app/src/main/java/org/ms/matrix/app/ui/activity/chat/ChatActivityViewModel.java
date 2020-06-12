package org.ms.matrix.app.ui.activity.chat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.ms.matrix.app.db.matrix.MessageList;

import java.util.List;

public class ChatActivityViewModel extends ViewModel {



    private MutableLiveData<List<MessageList>> messageListMutableLiveData=new MutableLiveData<>();

    public MutableLiveData<List<MessageList>> getMessageListMutableLiveData() {
        return messageListMutableLiveData;
    }
}
