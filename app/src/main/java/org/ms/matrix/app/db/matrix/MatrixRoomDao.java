package org.ms.matrix.app.db.matrix;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MatrixRoomDao {


    @Insert
    void insert(MatrixRoom... room);

    @Update
    void update(MatrixRoom... room);

    @Delete
    void delete(MatrixRoom... room);


    @Query("SELECT * FROM _matrix_room")
    List<MatrixRoom> queryMatrixRooms();

    @Query("SELECT * FROM _matrix_room")
    LiveData<List<MatrixRoom>> liveDataMatrixRooms();

    @Query("SELECT * FROM _matrix_room WHERE _room_id=:roomId")
    MatrixRoom queryMatrixRoomByRoomId(String roomId);
}
