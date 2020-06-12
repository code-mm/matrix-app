package org.ms.matrix.app.db.matrix;

import androidx.lifecycle.LiveData;

import org.ms.module.supper.client.Modules;

import java.util.List;

public class BuddyUserLocalDataSource implements BuddyUserDataSource {

    BuddyUserDao dao;

    public BuddyUserLocalDataSource(BuddyUserDao dao) {
        this.dao = dao;
    }

    @Override
    public void insert(BuddyUser... buddyUsers) {
        Modules.getUtilsModule().getThreadPoolUtils().runSubThread(new Runnable() {
            @Override
            public void run() {

                for (BuddyUser buddyUser : buddyUsers) {
                    BuddyUser buddyUser1 = queryBuddyUserByUserId(buddyUser.getUser_id());
                    if (buddyUser1 == null) {
                        dao.insert(buddyUser);
                    } else {
                        dao.update(buddyUser);
                    }
                }
            }
        });
    }

    @Override
    public void delete(BuddyUser... buddyUsers) {

        Modules.getUtilsModule().getThreadPoolUtils().runSubThread(new Runnable() {
            @Override
            public void run() {
                dao.delete(buddyUsers);
            }
        });

    }

    @Override
    public void update(BuddyUser... buddyUsers) {
        Modules.getUtilsModule().getThreadPoolUtils().runSubThread(new Runnable() {
            @Override
            public void run() {
                dao.update(buddyUsers);
            }
        });
    }

    @Override
    public LiveData<List<BuddyUser>> liveDataBuddyUsers() {
        return dao.liveDataBuddyUsers();
    }

    @Override
    public List<BuddyUser> queryBuddyUsers() {
        return dao.queryBuddyUsers();
    }

    @Override
    public BuddyUser queryBuddyUserByUserId(String userId) {
        return dao.queryBuddyUserByUserId(userId);
    }
}