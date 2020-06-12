package org.ms.matrix.app.ui.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.matrix.androidsdk.call.IMXCall;
import org.matrix.androidsdk.call.IMXCallListener;
import org.matrix.androidsdk.call.IMXCallsManagerListener;
import org.matrix.androidsdk.call.MXCallsManager;
import org.matrix.androidsdk.call.VideoLayoutConfiguration;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.data.RoomMediaMessage;
import org.matrix.androidsdk.rest.client.CallRestClient;
import org.ms.matrix.app.MatrixClient;
import org.ms.matrix.app.R;
import org.ms.matrix.app.db.MatrixDbInjection;
import org.ms.matrix.app.db.matrix.MessageList;
import org.ms.matrix.app.ui.activity.videocall.VideoCallActivity;
import org.ms.module.base.view.BaseActivity;
import org.ms.module.base.view.BaseAppCompatActivity;
import org.ms.module.supper.client.Modules;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseAppCompatActivity<ChatActivityPresenter> implements IChatActivity {


    private static final String TAG = "ChatActivity";

    private EditText editText;
    private Button buttonSend;


    private RecyclerView recyclerViewMessage;

    private RecyclerViewMessageAdapter recyclerViewMessageAdapter;

    private ChatActivityViewModel chatActivityViewModel;

    private List<MessageBean> messageListList = new ArrayList<>();


    private RelativeLayout relativeLayoutRoot;

    private Button buttonVideoCall;


    @Override
    protected int getLayout() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        super.initView();

        editText = findViewById(R.id.editText);
        buttonSend = findViewById(R.id.buttonSend);
        recyclerViewMessage = findViewById(R.id.recyclerViewMessage);
        relativeLayoutRoot = findViewById(R.id.relativeLayoutRoot);
        buttonVideoCall = findViewById(R.id.buttonVideoCall);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatActivityViewModel = new ViewModelProvider(this).get(ChatActivityViewModel.class);


        String roomid = getIntent().getStringExtra("roomid");

        Modules.getUtilsModule().getToastUtils().show(roomid + "");


        chatActivityViewModel.getMessageListMutableLiveData().observe(this, new Observer<List<MessageList>>() {
            @Override
            public void onChanged(List<MessageList> messageLists) {


                if (messageLists != null) {
                    messageListList.clear();


                    for (MessageList it : messageLists) {

                        messageListList.add(MessageBean.builder()
                                .content(it.get_body())
                                .type(it.get_type())
                                .sender(it.get_sender())
                                .build());

                    }

                    if (recyclerViewMessageAdapter == null) {
                        recyclerViewMessageAdapter = new RecyclerViewMessageAdapter(ChatActivity.this, messageListList);

                        recyclerViewMessage.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                        recyclerViewMessage.setAdapter(recyclerViewMessageAdapter);
                        recyclerViewMessage.scrollToPosition(messageListList.size() - 1);

                    } else {
                        recyclerViewMessage.scrollToPosition(messageListList.size() - 1);
                        recyclerViewMessageAdapter.notifyDataSetChanged();
                    }


                }

            }
        });


        MatrixDbInjection.providerMessageListLocalDataSource().liveDataMessageListsByRoomId(roomid).observe(this, new Observer<List<MessageList>>() {
            @Override
            public void onChanged(List<MessageList> messageLists) {
                chatActivityViewModel.getMessageListMutableLiveData().postValue(messageLists);
            }
        });


        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();

                MatrixClient.getInstance().getMxFileStore().getRoom(roomid).sendTextMessage(s, "", "", new RoomMediaMessage.EventCreationListener() {
                    @Override
                    public void onEventCreated(RoomMediaMessage roomMediaMessage) {

                    }

                    @Override
                    public void onEventCreationFailed(RoomMediaMessage roomMediaMessage, String errorMessage) {

                    }

                    @Override
                    public void onEncryptionFailed(RoomMediaMessage roomMediaMessage) {

                    }
                });
            }
        });

        buttonVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(ChatActivity.this, VideoCallActivity.class);
                intent.putExtra("roomid", roomid);
                startActivity(intent);
            }
        });


    }
}
