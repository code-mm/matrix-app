package org.ms.matrix.app.db.matrix;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MessageListDao {

    @Insert
    void insert(MessageList... messageLists);

    @Delete
    void delete(MessageList... messageLists);

    @Update
    void update(MessageList... messageLists);

    @Query("SELECT * FROM _matrix_message_list")
    List<MessageList> quertAllMessageList();

    @Query("SELECT * FROM _matrix_message_list ORDER BY _timestamp DESC LIMIT 0,1")
    LiveData<MessageList> liveDataLatestMessageList();

    @Query("SELECT * FROM _matrix_message_list")
    LiveData<List<MessageList>> liveDataMessageList();


    @Query("SELECT * FROM _matrix_message_list WHERE _event_id=:eventId")
    MessageList queryMessageListByEventId(String eventId);


    @Query("SELECT * FROM _matrix_message_list WHERE _room_id=:roomId")
    LiveData<List<MessageList>> liveDataMessageListsByRoomId(String roomId);

}
