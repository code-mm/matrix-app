package org.ms.matrix.app.ui.activity.videocall;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import org.matrix.androidsdk.call.IMXCall;
import org.matrix.androidsdk.call.IMXCallListener;
import org.matrix.androidsdk.call.IMXCallsManagerListener;
import org.matrix.androidsdk.call.VideoLayoutConfiguration;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.ms.matrix.app.MatrixClient;
import org.ms.matrix.app.R;
import org.ms.module.base.view.BaseAppCompatActivity;

public class VideoCallActivity extends BaseAppCompatActivity {


    private static final String TAG = "VideoCallActivity";

    private RelativeLayout relativeLayoutRoot;

    // video display size
    private VideoLayoutConfiguration mLocalVideoLayoutConfig;

    private String roomid;


    private Button buttonHangUp;


    private IMXCall imxCall;

    @Override
    protected int getLayout() {
        return R.layout.activity_video_call;
    }


    @Override
    protected void initView() {
        super.initView();
        relativeLayoutRoot = findViewById(R.id.relativeLayoutRoot);
        buttonHangUp = findViewById(R.id.buttonHangUp);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        roomid = getIntent().getStringExtra("roomid");

        mLocalVideoLayoutConfig = new VideoLayoutConfiguration(0, 0, 100, 100);


        buttonHangUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (imxCall != null) {
                    imxCall.hangup("");
                    finish();
                }

            }
        });




        MatrixClient.getInstance()
                .getMxSession()
                .mCallsManager
                .createCallInRoom(roomid, true, new ApiCallback<IMXCall>() {
                    @Override
                    public void onNetworkError(Exception e) {
                        Log.e(TAG, "onNetworkError: ");
                    }

                    @Override
                    public void onMatrixError(MatrixError e) {

                        Log.e(TAG, "onMatrixError: ");
                    }

                    @Override
                    public void onUnexpectedError(Exception e) {

                        Log.e(TAG, "onUnexpectedError: ");
                    }

                    @Override
                    public void onSuccess(IMXCall info) {

                        imxCall = info;
                        Log.e(TAG, "onSuccess: " + info);

                        info.addListener(new IMXCallListener() {
                            @Override
                            public void onStateDidChange(String state) {
                                Log.e(TAG, "onStateDidChange: ");

                            }

                            @Override
                            public void onCallError(String error) {


                                Log.e(TAG, "onCallError: ");

                            }

                            @Override
                            public void onCallViewCreated(View callView) {
                                Log.e(TAG, "onCallViewCreated: ");

                                if (callView != null) {
                                    relativeLayoutRoot.removeView(callView);
                                    relativeLayoutRoot.addView(callView);
                                }

                            }

                            @Override
                            public void onReady() {

                                Log.e(TAG, "onReady: ");

                                info.placeCall(mLocalVideoLayoutConfig);

                            }

                            @Override
                            public void onCallAnsweredElsewhere() {


                                Log.e(TAG, "onCallAnsweredElsewhere: ");

                            }

                            @Override
                            public void onCallEnd(int aReasonId) {


                                Log.e(TAG, "onCallEnd: ");

                            }

                            @Override
                            public void onPreviewSizeChanged(int width, int height) {

                                Log.e(TAG, "onPreviewSizeChanged: ");

                            }
                        });
                    }
                });
    }
}

