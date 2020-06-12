package org.ms.matrix.app.ui.fragment.message;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.matrix.androidsdk.data.Room;
import org.ms.matrix.app.MatrixClient;
import org.ms.matrix.app.R;
import org.ms.matrix.app.db.MatrixDbInjection;
import org.ms.matrix.app.db.matrix.MatrixRoom;
import org.ms.matrix.app.db.matrix.MessageList;
import org.ms.module.base.inter.IView;
import org.ms.module.base.view.BaseFragment;
import org.ms.module.supper.client.Modules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MessageFragment extends BaseFragment<MessageFragmentPresenter> implements IView {

    private static final String TAG = "MessageFragment";

    private RecyclerView recyclerViewMessageList;

    private RecyclerViewMessageListAdapter recyclerViewMessageListAdapter;


    private List<RecyclerViewMessageListBean> recyclerViewMessageListBeans = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.fragment_message;
    }


    @Override
    protected void initView() {
        super.initView();

        recyclerViewMessageList = findView(R.id.recyclerViewMessageList);


    }

    public static MessageFragment newInstance() {

        Bundle args = new Bundle();

        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected MessageFragmentPresenter initPresenter() {
        return new MessageFragmentPresenter(this);
    }


    /**
     * 去除重复id房间
     *
     * @param aDrivers
     * @return
     */
    public static List<MessageList> removeRepeatMessageList(List<MessageList> aDrivers) {
        Set<MessageList> set = new TreeSet<MessageList>(new Comparator<MessageList>() {
            @Override
            public int compare(MessageList o1, MessageList o2) {
                return o1._room_id.compareTo(o2._room_id);
            }
        });
        set.addAll(aDrivers);
        return new ArrayList<MessageList>(set);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        MatrixDbInjection.providerMessageListLocalDataSource().liveDataMessageList().observe(this, new Observer<List<MessageList>>() {
            @Override
            public void onChanged(List<MessageList> messageLists) {
                if (messageLists != null) {
                    List<MessageList> messageLists1 = removeRepeatMessageList(messageLists);
                    recyclerViewMessageListBeans.clear();

                    for (MessageList messageList : messageLists1) {


                        Modules.getUtilsModule().getThreadPoolUtils().runSubThread(new Runnable() {
                            @Override
                            public void run() {

                                MatrixRoom matrixRoom = MatrixDbInjection.providerMatrixRoomLocalDataSource().queryMatrixRoomByRoomId(messageList.get_room_id());


                                if (matrixRoom != null) {
                                    recyclerViewMessageListBeans.add(RecyclerViewMessageListBean
                                            .builder()
                                            .roomName(matrixRoom.get_room_displayname())
                                            .roomAvatar(matrixRoom.get_room_avatar())
                                            .roomId(matrixRoom.get_room_id())
                                            .build()
                                    );
                                }

                            }
                        });
                    }

                    if (recyclerViewMessageListAdapter == null) {
                        recyclerViewMessageListAdapter = new RecyclerViewMessageListAdapter(getContext(), recyclerViewMessageListBeans);


                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                       // linearLayoutManager.setStackFromEnd(true);
                        recyclerViewMessageList.setLayoutManager(linearLayoutManager);

                        recyclerViewMessageList.setAdapter(recyclerViewMessageListAdapter);
                      //  recyclerViewMessageList.scrollToPosition(recyclerViewMessageListBeans.size() - 1);

                    } else {
                       /// recyclerViewMessageList.scrollToPosition(recyclerViewMessageListBeans.size() - 1);
                        recyclerViewMessageListAdapter.notifyDataSetChanged();
                    }
                }
                Log.e(TAG, "onChanged: " + messageLists.size());
            }
        });

        MatrixDbInjection.providerMatrixRoomLocalDataSource().liveDataMatrixRooms().observe(this
                , new Observer<List<MatrixRoom>>() {
                    @Override
                    public void onChanged(List<MatrixRoom> matrixRooms) {
                        Log.e(TAG, "onChanged: " + matrixRooms.toString());
                        for (MatrixRoom room : matrixRooms) {
                            for (RecyclerViewMessageListBean it : recyclerViewMessageListBeans) {
                                if (room.get_room_id().equals(it.getRoomId())) {
                                    it.setRoomName(room.get_room_displayname());
                                }
                            }
                        }
                        if (recyclerViewMessageListAdapter != null) {
                            recyclerViewMessageListAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}

