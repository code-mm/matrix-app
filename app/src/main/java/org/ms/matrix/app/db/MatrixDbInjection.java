package org.ms.matrix.app.db;


import org.ms.matrix.app.db.matrix.BuddyUserDao;
import org.ms.matrix.app.db.matrix.BuddyUserLocalDataSource;
import org.ms.matrix.app.db.matrix.MatrixRoomDao;
import org.ms.matrix.app.db.matrix.MatrixRoomLocalDataSource;
import org.ms.matrix.app.db.matrix.MemberDao;
import org.ms.matrix.app.db.matrix.MemberLocalDataSource;
import org.ms.matrix.app.db.matrix.MessageListDao;
import org.ms.matrix.app.db.matrix.MessageListLocalDataSource;
import org.ms.matrix.app.db.user.LocalUserDataSource;
import org.ms.matrix.app.db.user.UserDataSource;

// 用户数据库
public class MatrixDbInjection {

    public static UserDataSource providerUserDataSource() {
        MatrixDataBase database = MatrixDataBase.getInstance();
        return new LocalUserDataSource(database.userDao());
    }
    

    public static MessageListDao providerMessageListLocalDataSource() {
        MatrixDataBase database = MatrixDataBase.getInstance();
        return new MessageListLocalDataSource(database.messageListDao());
    }

    public static MatrixRoomDao providerMatrixRoomLocalDataSource() {
        MatrixDataBase database = MatrixDataBase.getInstance();
        return new MatrixRoomLocalDataSource(database.matrixRoomDao());
    }

    public static BuddyUserDao providerBuddyUserLocalDataSource() {
        MatrixDataBase database = MatrixDataBase.getInstance();
        return new BuddyUserLocalDataSource(database.buddyUserDao());
    }

    public static MemberDao providerMemberLocalDataSource() {
        MatrixDataBase database = MatrixDataBase.getInstance();
        return new MemberLocalDataSource(database.memberDao());
    }
}