package org.ms.matrix.app.db.matrix;

import androidx.lifecycle.LiveData;

import org.ms.module.supper.client.Modules;

import java.util.List;

public class MessageListLocalDataSource implements MessageListDataSource {

    private MessageListDao dao;


    public MessageListLocalDataSource(MessageListDao dao) {
        this.dao = dao;
    }

    @Override
    public void insert(MessageList... messageLists) {
        Modules.getUtilsModule().getThreadPoolUtils().runSubThread(new Runnable() {
            @Override
            public void run() {
                for (MessageList messageList : messageLists) {
                    MessageList messageListT = queryMessageListByEventId(messageList.getEvent_id());
                    if (messageListT == null) {
                        dao.insert(messageList);
                    } else {
                        dao.update(messageList);
                    }
                }
            }
        });
    }

    @Override
    public void delete(MessageList... messageLists) {
        Modules.getUtilsModule().getThreadPoolUtils().runSubThread(new Runnable() {
            @Override
            public void run() {
                dao.delete(messageLists);
            }
        });
    }

    @Override
    public void update(MessageList... messageLists) {
        Modules.getUtilsModule().getThreadPoolUtils().runSubThread(new Runnable() {
            @Override
            public void run() {
                dao.update(messageLists);
            }
        });
    }

    @Override
    public List<MessageList> quertAllMessageList() {
        return dao.quertAllMessageList();
    }

    @Override
    public LiveData<MessageList> liveDataLatestMessageList() {
        return dao.liveDataLatestMessageList();
    }

    @Override
    public LiveData<List<MessageList>> liveDataMessageList() {
        return dao.liveDataMessageList();
    }

    @Override
    public MessageList queryMessageListByEventId(String eventId) {
        return dao.queryMessageListByEventId(eventId);
    }

    @Override
    public LiveData<List<MessageList>> liveDataMessageListsByRoomId(String roomId) {
        return dao.liveDataMessageListsByRoomId(roomId);
    }
}
