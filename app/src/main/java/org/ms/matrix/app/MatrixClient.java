package org.ms.matrix.app;

import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.JsonElement;

import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.call.IMXCall;
import org.matrix.androidsdk.call.IMXCallsManagerListener;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.data.MyUser;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.store.MXFileStore;
import org.matrix.androidsdk.listeners.MXEventListener;
import org.matrix.androidsdk.rest.client.LoginRestClient;
import org.matrix.androidsdk.rest.client.RoomsRestClient;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.login.Credentials;
import org.matrix.androidsdk.rest.model.message.Message;
import org.matrix.androidsdk.rest.model.pid.ThirdPartyIdentifier;
import org.matrix.androidsdk.rest.model.sync.AccountDataElement;
import org.ms.matrix.app.db.MatrixDbInjection;
import org.ms.matrix.app.db.matrix.BuddyUser;
import org.ms.matrix.app.db.matrix.MatrixRoom;
import org.ms.matrix.app.db.matrix.Member;
import org.ms.matrix.app.db.matrix.MessageList;
import org.ms.module.supper.client.Modules;

import java.util.Collection;
import java.util.List;

public class MatrixClient {


    private static final String TAG = "MatrixClient";
    private static final MatrixClient instance = new MatrixClient();
    private HomeServerConnectionConfig homeServerConnectionConfig;
    private MXSession mxSession;
    private LoginRestClient loginRestClient;
    private RoomsRestClient roomsRestClient;
    private MXFileStore mxFileStore;
    private MXDataHandler mxDataHandler;


    private String userId;

    public String getUserId() {
        return userId;
    }

    public static MatrixClient getInstance() {
        return instance;
    }


    public void init() {
        homeServerConnectionConfig = new HomeServerConnectionConfig.Builder()
                .withHomeServerUri(Uri.parse("https://matrix.mhw828.com"))
                .build();
    }


    /**
     * 登录
     *
     * @param username
     * @param password
     * @param callBack
     */
    public void login(String username, String password, ApiCallback<Credentials> callBack) {
        loginRestClient = new LoginRestClient(homeServerConnectionConfig);

        loginRestClient.loginWithUser(username, password, new ApiCallback<Credentials>() {
            @Override
            public void onNetworkError(Exception e) {

                if (callBack != null) {
                    callBack.onNetworkError(e);
                }
            }

            @Override
            public void onMatrixError(MatrixError e) {
                if (callBack != null) {
                    callBack.onMatrixError(e);
                }
            }

            @Override
            public void onUnexpectedError(Exception e) {
                if (callBack != null) {
                    callBack.onUnexpectedError(e);
                }
            }

            @Override
            public void onSuccess(Credentials info) {

                userId = info.getUserId();

                homeServerConnectionConfig.setCredentials(info);

                mxFileStore = new MXFileStore(
                        homeServerConnectionConfig,
                        false,
                        Modules.getDataModule().getAppData().getApplication());


                // open
                mxFileStore.open();

                mxDataHandler = new MXDataHandler(mxFileStore, info);

                mxSession = new MXSession.Builder(homeServerConnectionConfig,
                        mxDataHandler,
                        Modules.getDataModule().getAppData().getApplication())
                        .build();

                mxSession.getDataHandler().addListener(new MXEventListener() {

                    @Override
                    public void onLiveEvent(Event event, RoomState roomState) {
                        super.onLiveEvent(event, roomState);
                        Log.e(TAG, "onLiveEvent: " + event.toString());

                        String type = event.getType();

                        Log.e(TAG, "onLiveEvent type : " + type);

                        // 消息
                        if (Event.EVENT_TYPE_MESSAGE.equals(type)) {
                            JsonElement content = event.getContent();
                            Message message = Modules.getUtilsModule().getGsonUtils().fromJson(content.toString(), Message.class);
                            if (message != null) {
                                String msgtype = message.msgtype;
                                Log.e(TAG, "onLiveEvent: " + msgtype);

                                // 文字消息
                                if (Message.MSGTYPE_TEXT.equals(msgtype)) {
                                    String body = message.body;
                                    Log.e(TAG, "onLiveEvent: " + body);

                                    MessageList messageList = MessageList.builder()._room_id(event.getRoomId())
                                            .event_id(event.getEventId())

                                            ._body(body)
                                            ._read(false)
                                            ._sender(event.sender)
                                            ._timestamp(SystemClock.currentThreadTimeMillis())
                                            ._originserverts(event.getOriginServerTs())
                                            ._type(msgtype)
                                            .build();

                                    MatrixDbInjection.providerMessageListLocalDataSource().insert(messageList);

                                }
                            }
                        }
                    }

                    @Override
                    public void onInitialSyncComplete(String toToken) {
                        super.onInitialSyncComplete(toToken);
                        Log.e(TAG, "onInitialSyncComplete: 初始化同步完成");

                        List<ThirdPartyIdentifier> thirdPartyIdentifiersEmails = mxSession.getMyUser().getlinkedEmails();

                        List<ThirdPartyIdentifier> thirdPartyIdentifiersPhoneNumbers = mxSession.getMyUser().getlinkedPhoneNumbers();


                        // 获取用户列表
                        Collection<User> users = mxSession.getDataHandler().getStore().getUsers();

                        for (User user : users) {
                            Log.e(TAG, "onInitialSyncComplete: " + user.user_id);

                            BuddyUser buddyUser = BuddyUser.builder().user_id(user.user_id)
                                    .avatar_url(user.avatar_url)
                                    .displayname(user.displayname)
                                    .currently_active(user.currently_active)
                                    .presence(user.presence)
                                    .statusMsg(user.statusMsg)
                                    .lastActiveAgo(user.lastActiveAgo)
                                    .build();

                            MatrixDbInjection.providerBuddyUserLocalDataSource().insert(buddyUser);
                        }

                        Collection<Room> rooms = mxFileStore.getRooms();

                        for (Room room : rooms) {


                            // 获取房间成员
                            room.getMembersAsync(new ApiCallback<List<RoomMember>>() {
                                @Override
                                public void onNetworkError(Exception e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onMatrixError(MatrixError e) {
                                    Log.e(TAG, "onMatrixError: " + e.getMessage());
                                }

                                @Override
                                public void onUnexpectedError(Exception e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onSuccess(List<RoomMember> info) {
                                    Log.e(TAG, "onSuccess: " + info.toString());
                                    // 将房间成员存入数据库
                                    for (RoomMember it : info) {
                                        MatrixDbInjection.providerMemberLocalDataSource().insert(
                                                Member.builder()
                                                        ._member_id(it.getUserId())
                                                        ._room_id(room.getRoomId())
                                                        ._avatar_url(it.avatarUrl)
                                                        ._name(it.getName())
                                                        ._displayname(it.displayname)
                                                        .build()
                                        );
                                    }
                                }
                            });


                            // 将房间信息存入数据库
                            if (room.getRoomId() != null) {
                                MatrixDbInjection.providerMatrixRoomLocalDataSource().insert(
                                        MatrixRoom.builder()
                                                ._room_displayname(room.getRoomDisplayName(Modules.getDataModule().getAppData().getApplication()))
                                                ._room_id(room.getRoomId())
                                                ._room_avatar(room.getAvatarUrl())
                                                ._room_aliases(room.getAliases().toString())
                                                .build());
                            }


                            // 监听 call
                            mxSession.mCallsManager.addListener(new IMXCallsManagerListener() {
                                @Override
                                public void onIncomingCall(IMXCall call, MXUsersDevicesMap<MXDeviceInfo> unknownDevices) {
                                    Log.e(TAG, "onIncomingCall: ");


                                    call.createCallView();


                                }

                                @Override
                                public void onOutgoingCall(IMXCall call) {

                                    Log.e(TAG, "onOutgoingCall: ");
                                    call.createCallView();

                                }

                                @Override
                                public void onCallHangUp(IMXCall call) {


                                    Log.e(TAG, "onCallHangUp: ");

                                }

                                @Override
                                public void onVoipConferenceStarted(String roomId) {


                                    Log.e(TAG, "onVoipConferenceStarted: " + roomId);

                                }

                                @Override
                                public void onVoipConferenceFinished(String roomId) {


                                    Log.e(TAG, "onVoipConferenceFinished: " + roomId);

                                }
                            });
                        }
                    }

                    @Override
                    public void onAccountDataUpdated(AccountDataElement accountDataElement) {
                        super.onAccountDataUpdated(accountDataElement);
                        Log.e(TAG, "onAccountDataUpdated: " + accountDataElement.toString());
                    }

                    @Override
                    public void onAccountInfoUpdate(MyUser myUser) {
                        super.onAccountInfoUpdate(myUser);
                        Log.e(TAG, "onAccountInfoUpdate: " + myUser.toString());
                    }
                });

                // 开始同步
                startEventStream();

                if (callBack != null) {
                    callBack.onSuccess(info);
                }

            }
        });
    }

    public MXSession getMxSession() {
        return mxSession;
    }

    public MXFileStore getMxFileStore() {
        return mxFileStore;
    }


    public void startEventStream() {
        Log.e(TAG, "startEventStream: ");
        mxSession.startEventStream(null);
    }
}
