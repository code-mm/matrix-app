package org.ms.matrix.app.db.matrix;

import androidx.lifecycle.LiveData;

import org.ms.module.supper.client.Modules;

import java.util.List;

public class MemberLocalDataSource implements MemberDataSource {


    MemberDao dao;

    public MemberLocalDataSource(MemberDao dao) {
        this.dao = dao;
    }

    @Override
    public void insert(Member... members) {


        Modules.getUtilsModule().getThreadPoolUtils().runSubThread(new Runnable() {
            @Override
            public void run() {

                for (Member it : members) {
                    if (dao.queryMember(it.get_room_id(), it.get_member_id()) == null) {

                        dao.insert(it);
                    } else {
                        dao.update(it);
                    }
                }
            }
        });

    }

    @Override
    public void delete(Member... members) {


        Modules.getUtilsModule().getThreadPoolUtils().runSubThread(new Runnable() {
            @Override
            public void run() {
                dao.delete(members);
            }
        });

    }

    @Override
    public void update(Member... members) {


        Modules.getUtilsModule().getThreadPoolUtils().runSubThread(new Runnable() {
            @Override
            public void run() {

                dao.update(members);

            }
        });

    }


    @Override
    public Member queryMember(String roomId, String memberId) {
        return dao.queryMember(
                roomId, memberId
        );
    }

    @Override
    public LiveData<List<Member>> liveDataMembers() {
        return dao.liveDataMembers();
    }
}
