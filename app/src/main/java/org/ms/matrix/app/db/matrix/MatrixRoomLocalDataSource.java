package org.ms.matrix.app.db.matrix;

import androidx.lifecycle.LiveData;

import org.ms.module.supper.client.Modules;

import java.util.List;

public class MatrixRoomLocalDataSource implements MatrixRoomDataSource {


    private MatrixRoomDao dao;


    public MatrixRoomLocalDataSource(MatrixRoomDao dao) {
        this.dao = dao;
    }

    @Override
    public void insert(MatrixRoom... rooms) {

        Modules.getUtilsModule().getThreadPoolUtils().runSubThread(new Runnable() {
            @Override
            public void run() {

                for (MatrixRoom it : rooms) {
                    if (it.get_room_id() != null) {

                        MatrixRoom matrixRoom = queryMatrixRoomByRoomId(it.get_room_id());

                        if (matrixRoom == null) {
                            dao.insert(it);
                        } else {
                            dao.update(it);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void update(MatrixRoom... room) {
        Modules.getUtilsModule().getThreadPoolUtils().runSubThread(new Runnable() {
            @Override
            public void run() {
                dao.update(room);
            }
        });
    }

    @Override
    public void delete(MatrixRoom... room) {
        Modules.getUtilsModule().getThreadPoolUtils().runSubThread(new Runnable() {
            @Override
            public void run() {
                dao.delete(room);
            }
        });
    }

    @Override
    public List<MatrixRoom> queryMatrixRooms() {
        return dao.queryMatrixRooms();
    }

    @Override
    public LiveData<List<MatrixRoom>> liveDataMatrixRooms() {
        return dao.liveDataMatrixRooms();
    }

    @Override
    public MatrixRoom queryMatrixRoomByRoomId(String roomId) {
        return dao.queryMatrixRoomByRoomId(roomId);
    }
}
