package org.ms.matrix.app.db.matrix;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BuddyUserDao {

    @Insert
    void insert(BuddyUser... buddyUsers);

    @Delete
    void delete(BuddyUser... buddyUsers);

    @Update
    void update(BuddyUser... buddyUsers);

    @Query("SELECT * FROM _buddy_user")
    LiveData<List<BuddyUser>> liveDataBuddyUsers();

    @Query("SELECT * FROM _buddy_user")
    List<BuddyUser> queryBuddyUsers();


    @Query("SELECT * FROM _buddy_user WHERE _user_id =:userId")
    BuddyUser queryBuddyUserByUserId(String userId);


}
