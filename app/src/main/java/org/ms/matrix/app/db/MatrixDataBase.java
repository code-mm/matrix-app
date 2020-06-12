package org.ms.matrix.app.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.ms.matrix.app.db.matrix.BuddyUser;
import org.ms.matrix.app.db.matrix.BuddyUserDao;
import org.ms.matrix.app.db.matrix.MatrixRoom;
import org.ms.matrix.app.db.matrix.MatrixRoomDao;
import org.ms.matrix.app.db.matrix.Member;
import org.ms.matrix.app.db.matrix.MemberDao;
import org.ms.matrix.app.db.matrix.MessageList;
import org.ms.matrix.app.db.matrix.MessageListDao;
import org.ms.matrix.app.db.user.User;
import org.ms.matrix.app.db.user.UserDao;
import org.ms.module.supper.client.Modules;

@Database(entities = {
        User.class,
        MessageList.class,
        MatrixRoom.class,
        BuddyUser.class,
        Member.class
}, version = 1, exportSchema = false)
public abstract class MatrixDataBase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract MessageListDao messageListDao();

    public abstract MatrixRoomDao matrixRoomDao();

    public abstract BuddyUserDao buddyUserDao();

    public abstract MemberDao memberDao();

    private static volatile MatrixDataBase INSTANCE;

    public static MatrixDataBase getInstance() {
        if (INSTANCE == null) {
            synchronized (MatrixDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(Modules.getDataModule().getAppData().getApplication(),
                            MatrixDataBase.class, "com-bdlbsc-matrix-user.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}