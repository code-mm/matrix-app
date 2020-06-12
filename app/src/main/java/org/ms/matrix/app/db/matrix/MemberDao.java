package org.ms.matrix.app.db.matrix;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MemberDao {

    @Insert
    void insert(Member... members);

    @Delete
    void delete(Member... members);

    @Update
    void update(Member... members);

    @Query("SELECT * FROM _member WHERE _room_id=:roomId AND _member_id=:memberId")
    Member queryMember(String roomId, String memberId);

    @Query("SELECT * FROM _member")
    LiveData<List<Member>> liveDataMembers();

}
