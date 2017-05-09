package com.sinoautodiagnoseos.openvcall.ui;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.readystatesoftware.viewbadger.BadgeView;
import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.Download.FileDownload;
import com.sinoautodiagnoseos.entity.Upload.Upload;
import com.sinoautodiagnoseos.entity.User.Skill;
import com.sinoautodiagnoseos.net.requestApi.HttpRequestApi;
import com.sinoautodiagnoseos.net.requestSubscribers.HttpSubscriber;
import com.sinoautodiagnoseos.net.requestSubscribers.SubscriberOnListener;
import com.sinoautodiagnoseos.openvcall.model.AGEventHandler;
import com.sinoautodiagnoseos.openvcall.model.CallHistoriesExpertsDtos;
import com.sinoautodiagnoseos.openvcall.model.ConstantApp;
import com.sinoautodiagnoseos.openvcall.model.ExpertsData;
import com.sinoautodiagnoseos.openvcall.model.ExpertsInfoList;
import com.sinoautodiagnoseos.openvcall.model.ListExpertsSearchDto;
import com.sinoautodiagnoseos.openvcall.model.ListExpertsSearchLists;
import com.sinoautodiagnoseos.openvcall.model.ListResult;
import com.sinoautodiagnoseos.openvcall.model.Message;
import com.sinoautodiagnoseos.openvcall.model.UploadDatas;
import com.sinoautodiagnoseos.openvcall.model.User;
import com.sinoautodiagnoseos.propeller.Constant;
import com.sinoautodiagnoseos.propeller.ExpertsScoreData;
import com.sinoautodiagnoseos.propeller.UserStatusData;
import com.sinoautodiagnoseos.propeller.VideoInfoData;
import com.sinoautodiagnoseos.propeller.ui.MDDialog;
import com.sinoautodiagnoseos.propeller.ui.RtlLinearLayoutManager;
import com.sinoautodiagnoseos.propeller.ui.expandButton.AllAngleExpandableButton;
import com.sinoautodiagnoseos.propeller.ui.expandButton.ButtonData;
import com.sinoautodiagnoseos.propeller.ui.expandButton.ButtonEventListener;
import com.sinoautodiagnoseos.propeller.ui.specialProgressBar.SpecialProgressBarView;
import com.sinoautodiagnoseos.utils.MyIntent;
import com.sinoautodiagnoseos.utils.ToastUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.sinoautodiagnoseos.R.drawable.file;


/**
 * Created by HQ_Demos on 2017/2/15.
 */
public class ChatActivity extends BaseActivity implements AGEventHandler {

    private final static Logger log = LoggerFactory.getLogger(ChatActivity.class);

    private GridVideoViewContainer mGridVideoViewContainer;

    private RelativeLayout mSmallVideoViewDock;

    // should only be modified under UI thread
    private final HashMap<Integer, SurfaceView> mUidsList = new HashMap<>(); // uid = 0 || uid == EngineConfig.mUid

    private volatile boolean mVideoMuted = false;

    private volatile boolean mAudioMuted = false;

    private volatile int mAudioRouting = -1; // Default

    private int which = 0;

    private AllAngleExpandableButton button ;

    private Chronometer channel_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        channel_time= (Chronometer) findViewById(R.id.channel_time);
        initTime();
        initExplandButton();
    }
    List<FileDownload> filelists = new ArrayList<FileDownload>();
    @Override
    protected void onResume() {
        super.onResume();
        if (com.sinoautodiagnoseos.utils.Constant.is_push
//                && com.sinoautodiagnoseos.utils.Constant.USERROLE.equals("技师")
                ){
//            filelists=com.sinoautodiagnoseos.utils.Constant.file_list.get(com.sinoautodiagnoseos.utils.Constant.ROOMID);
            badge = new BadgeView(this, button);
            badge.setText("1");
            badge.show();
            initExplandButton();
        }
        che_list= com.sinoautodiagnoseos.utils.Constant.save_experts.get(com.sinoautodiagnoseos.utils.Constant.ROOMID);
        filelists = com.sinoautodiagnoseos.utils.Constant.file_list;
        Log.e("is_close--------", "----"+com.sinoautodiagnoseos.utils.Constant.is_close+"-----");
        if (com.sinoautodiagnoseos.utils.Constant.is_close==true){
            System.out.println("-----执行了------");
        }
    }

    private void initTime() {
        channel_time.setBase(SystemClock.elapsedRealtime());//计时器清零
        int hour = (int) ((SystemClock.elapsedRealtime() - channel_time.getBase()) / 1000 / 60);
        channel_time.setFormat("0"+ String.valueOf(hour)+":%s");
        channel_time.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        doRenderRemoteUi(uid);
    }

    private void doRenderRemoteUi(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                if (mUidsList.containsKey(uid)) {
                    return;
                }

                SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
                mUidsList.put(uid, surfaceV);

                boolean useDefaultLayout = mLayoutType == LAYOUT_TYPE_DEFAULT && mUidsList.size() != 2;

                surfaceV.setZOrderOnTop(!useDefaultLayout);
                surfaceV.setZOrderMediaOverlay(!useDefaultLayout);

                rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));

                if (useDefaultLayout) {
                    log.debug("doRenderRemoteUi LAYOUT_TYPE_DEFAULT " + (uid & 0xFFFFFFFFL));
                    switchToDefaultVideoView();
                } else {
                    int bigBgUid = mSmallVideoViewAdapter == null ? uid : mSmallVideoViewAdapter.getExceptedUid();
                    log.debug("doRenderRemoteUi LAYOUT_TYPE_SMALL " + (uid & 0xFFFFFFFFL) + " " + (bigBgUid & 0xFFFFFFFFL));
                    switchToSmallVideoView(bigBgUid);
                }
            }
        });
    }

    @Override
    public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
        log.debug("onJoinChannelSuccess " + channel + " " + (uid & 0xFFFFFFFFL) + " " + elapsed);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                SurfaceView local = mUidsList.remove(0);

                if (local == null) {
                    return;
                }

                mUidsList.put(uid, local);
            }
        });
    }

    @Override
    public void onUserOffline(int uid, int reason) {
        doRemoveRemoteUi(uid);
    }

    @Override
    public void onExtraCallback(final int type, final Object... data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                doHandleExtraCallback(type, data);
            }
        });
    }

    private void doHandleExtraCallback(int type, Object... data) {
        int peerUid;
        boolean muted;

        switch (type) {
            case AGEventHandler.EVENT_TYPE_ON_USER_AUDIO_MUTED:
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];

                if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                    HashMap<Integer, Integer> status = new HashMap<>();
                    status.put(peerUid, muted ? UserStatusData.AUDIO_MUTED : UserStatusData.DEFAULT_STATUS);
                    mGridVideoViewContainer.notifyUiChanged(mUidsList, config().mUid, status, null);
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_MUTED:
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];

                doHideTargetView(peerUid, muted);

                break;

            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_STATS:
                IRtcEngineEventHandler.RemoteVideoStats stats = (IRtcEngineEventHandler.RemoteVideoStats) data[0];

                if (Constant.SHOW_VIDEO_INFO) {
                    if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                        mGridVideoViewContainer.addVideoInfo(stats.uid, new VideoInfoData(stats.width, stats.height, stats.delay, stats.receivedFrameRate, stats.receivedBitrate));
                        int uid = config().mUid;
                        int profileIndex = getVideoProfileIndex();
                        String resolution = getResources().getStringArray(R.array.string_array_resolutions)[profileIndex];
                        String fps = getResources().getStringArray(R.array.string_array_frame_rate)[profileIndex];
                        String bitrate = getResources().getStringArray(R.array.string_array_bit_rate)[profileIndex];

                        String[] rwh = resolution.split("x");
                        int width = Integer.valueOf(rwh[0]);
                        int height = Integer.valueOf(rwh[1]);

                        mGridVideoViewContainer.addVideoInfo(uid, new VideoInfoData(width > height ? width : height,
                                width > height ? height : width,
                                0, Integer.valueOf(fps), Integer.valueOf(bitrate)));
                    }
                } else {
                    mGridVideoViewContainer.cleanVideoInfo();
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_SPEAKER_STATS:
                IRtcEngineEventHandler.AudioVolumeInfo[] infos = (IRtcEngineEventHandler.AudioVolumeInfo[]) data[0];

                if (infos.length == 1 && infos[0].uid == 0) { // local guy, ignore it
                    break;
                }

                if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                    HashMap<Integer, Integer> volume = new HashMap<>();

                    for (IRtcEngineEventHandler.AudioVolumeInfo each : infos) {
                        peerUid = each.uid;
                        int peerVolume = each.volume;

                        if (peerUid == 0) {
                            continue;
                        }
                        volume.put(peerUid, peerVolume);
                    }
                    mGridVideoViewContainer.notifyUiChanged(mUidsList, config().mUid, null, volume);
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_APP_ERROR:
                int subType = (int) data[0];

                if (subType == ConstantApp.AppError.NO_NETWORK_CONNECTION) {
                    showLongToast(getString(R.string.msg_no_network_connection));
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_DATA_CHANNEL_MSG:

                peerUid = (Integer) data[0];
                final byte[] content = (byte[]) data[1];
                notifyMessageChanged(new Message(new User(peerUid, String.valueOf(peerUid)), new String(content)));

                break;

            case AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR: {
                int error = (int) data[0];
                String description = (String) data[1];

                notifyMessageChanged(new Message(new User(0, null), error + " " + description));

                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED:
                notifyHeadsetPlugged((int) data[0]);

                break;

        }
    }


    private void requestRemoteStreamType(final int currentHostCount) {
        log.debug("requestRemoteStreamType " + currentHostCount);
    }

    private void doRemoveRemoteUi(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                Object target = mUidsList.remove(uid);
                if (target == null) {
                    return;
                }

                int bigBgUid = -1;
                if (mSmallVideoViewAdapter != null) {
                    bigBgUid = mSmallVideoViewAdapter.getExceptedUid();
                }

                log.debug("doRemoveRemoteUi " + (uid & 0xFFFFFFFFL) + " " + (bigBgUid & 0xFFFFFFFFL) + " " + mLayoutType);

                if (mLayoutType == LAYOUT_TYPE_DEFAULT || uid == bigBgUid) {
                    switchToDefaultVideoView();
                } else {
                    switchToSmallVideoView(bigBgUid);
                }
            }
        });
    }

    private SmallVideoViewAdapter mSmallVideoViewAdapter;

    private void switchToDefaultVideoView() {
        if (mSmallVideoViewDock != null) {
            mSmallVideoViewDock.setVisibility(View.GONE);
        }
        mGridVideoViewContainer.initViewContainer(getApplicationContext(), config().mUid, mUidsList);

        mLayoutType = LAYOUT_TYPE_DEFAULT;
    }

    private void switchToSmallVideoView(int bigBgUid) {
        HashMap<Integer, SurfaceView> slice = new HashMap<>(1);
        slice.put(bigBgUid, mUidsList.get(bigBgUid));
        mGridVideoViewContainer.initViewContainer(getApplicationContext(), bigBgUid, slice);

        bindToSmallVideoView(bigBgUid);

        mLayoutType = LAYOUT_TYPE_SMALL;

        requestRemoteStreamType(mUidsList.size());
    }

    public int mLayoutType = LAYOUT_TYPE_DEFAULT;

    public static final int LAYOUT_TYPE_DEFAULT = 0;

    public static final int LAYOUT_TYPE_SMALL = 1;

    private void bindToSmallVideoView(int exceptUid) {
        if (mSmallVideoViewDock == null) {
            ViewStub stub = (ViewStub) findViewById(R.id.small_video_view_dock);
            mSmallVideoViewDock = (RelativeLayout) stub.inflate();
        }

        boolean twoWayVideoCall = mUidsList.size() == 2;

        RecyclerView recycler = (RecyclerView) findViewById(R.id.small_video_view_container);

        boolean create = false;

        if (mSmallVideoViewAdapter == null) {
            create = true;
            mSmallVideoViewAdapter = new SmallVideoViewAdapter(this, config().mUid, exceptUid, mUidsList, new VideoViewEventListener() {
                @Override
                public void onItemDoubleClick(View v, Object item) {
                    switchToDefaultVideoView();
                }
            });
            mSmallVideoViewAdapter.setHasStableIds(true);
        }
        recycler.setHasFixedSize(true);

        log.debug("bindToSmallVideoView " + twoWayVideoCall + " " + (exceptUid & 0xFFFFFFFFL));

        if (twoWayVideoCall) {
            recycler.setLayoutManager(new RtlLinearLayoutManager(this, RtlLinearLayoutManager.HORIZONTAL, false));
        } else {
            recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }
        recycler.addItemDecoration(new SmallVideoViewDecoration());
        recycler.setAdapter(mSmallVideoViewAdapter);

        recycler.setDrawingCacheEnabled(true);
        recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        if (!create) {
            mSmallVideoViewAdapter.setLocalUid(config().mUid);
            mSmallVideoViewAdapter.notifyUiChanged(mUidsList, exceptUid, null, null);
        }
        recycler.setVisibility(View.VISIBLE);
        mSmallVideoViewDock.setVisibility(View.VISIBLE);
    }

    public void notifyHeadsetPlugged(final int routing) {
        log.info("notifyHeadsetPlugged " + routing + " " + mVideoMuted);

        mAudioRouting = routing;

        if (!mVideoMuted) {
            return;
        }

        ImageView iv = (ImageView) findViewById(R.id.customized_function_id);
        if (mAudioRouting == 3) { // Speakerphone
            iv.setColorFilter(getResources().getColor(R.color.agora_blue), PorterDuff.Mode.MULTIPLY);
        } else {
            iv.clearColorFilter();
        }
    }

    @Override
    protected void initUIandEvent() {
        event().addEventHandler(this);

        Intent i = getIntent();

        String channelName = i.getStringExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME);

        final String encryptionKey = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ENCRYPTION_KEY);

        final String encryptionMode = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ENCRYPTION_MODE);

        doConfigEngine(encryptionKey, encryptionMode);

        mGridVideoViewContainer = (GridVideoViewContainer) findViewById(R.id.grid_video_view_container);
        mGridVideoViewContainer.setItemEventHandler(new VideoViewEventListener() {
            @Override
            public void onItemDoubleClick(View v, Object item) {
                log.debug("onItemDoubleClick " + v + " " + item + " " + mLayoutType);

                if (mUidsList.size() < 2) {
                    return;
                }

                UserStatusData user = (UserStatusData) item;
                int uid = (user.mUid == 0) ? config().mUid : user.mUid;

                if (mLayoutType == LAYOUT_TYPE_DEFAULT && mUidsList.size() != 1) {
                    switchToSmallVideoView(uid);
                } else {
                    switchToDefaultVideoView();
                }
            }
        });

        SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
        rtcEngine().setupLocalVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, 0));
        surfaceV.setZOrderOnTop(false);
        surfaceV.setZOrderMediaOverlay(false);

        mUidsList.put(0, surfaceV); // get first surface view

        mGridVideoViewContainer.initViewContainer(getApplicationContext(), 0, mUidsList); // first is now full view
        worker().preview(true, surfaceV, 0);

        worker().joinChannel(channelName, config().mUid);

        TextView textChannelName = (TextView) findViewById(R.id.channel_name);
        textChannelName.setText(channelName);

        optional();

        LinearLayout bottomContainer = (LinearLayout) findViewById(R.id.bottom_container);
        FrameLayout.MarginLayoutParams fmp = (FrameLayout.MarginLayoutParams) bottomContainer.getLayoutParams();
        fmp.bottomMargin = virtualKeyHeight() + 16;

        initMessageList();
    }

    public void onClickHideIME(View view) {
        log.debug("onClickHideIME " + view);

        closeIME(findViewById(R.id.msg_content));

        findViewById(R.id.msg_input_container).setVisibility(View.GONE);
        findViewById(R.id.bottom_action_end_call).setVisibility(View.VISIBLE);
        findViewById(R.id.bottom_action_container).setVisibility(View.VISIBLE);
    }

    private InChannelMessageListAdapter mMsgAdapter;

    private ArrayList<Message> mMsgList;

    private void initMessageList() {
        mMsgList = new ArrayList<>();
        RecyclerView msgListView = (RecyclerView) findViewById(R.id.msg_list);

        mMsgAdapter = new InChannelMessageListAdapter(this, mMsgList);
        mMsgAdapter.setHasStableIds(true);
        msgListView.setAdapter(mMsgAdapter);
        msgListView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        msgListView.addItemDecoration(new MessageListDecoration());
    }

    private void notifyMessageChanged(Message msg) {
        mMsgList.add(msg);

        int MAX_MESSAGE_COUNT = 16;

        if (mMsgList.size() > MAX_MESSAGE_COUNT) {
            int toRemove = mMsgList.size() - MAX_MESSAGE_COUNT;
            for (int i = 0; i < toRemove; i++) {
                mMsgList.remove(i);
            }
        }

        mMsgAdapter.notifyDataSetChanged();
    }

    private int mDataStreamId;

    private void sendChannelMsg(String msgStr) {
        RtcEngine rtcEngine = rtcEngine();
        if (mDataStreamId <= 0) {
            mDataStreamId = rtcEngine.createDataStream(true, true); // boolean reliable, boolean ordered
        }

        if (mDataStreamId < 0) {
            String errorMsg = "Create data stream error happened " + mDataStreamId;
            log.warn(errorMsg);
            showLongToast(errorMsg);
            return;
        }

        byte[] encodedMsg;
        try {
            encodedMsg = msgStr.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            encodedMsg = msgStr.getBytes();
        }

        rtcEngine.sendStreamMessage(mDataStreamId, encodedMsg);
    }

    private void optional() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
    }

    private void optionalDestroy() {
    }

    private int getVideoProfileIndex() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int profileIndex = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_PROFILE_IDX, ConstantApp.DEFAULT_PROFILE_IDX);
        if (profileIndex > ConstantApp.VIDEO_PROFILES.length - 1) {
            profileIndex = ConstantApp.DEFAULT_PROFILE_IDX;

            // save the new value
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(ConstantApp.PrefManager.PREF_PROPERTY_PROFILE_IDX, profileIndex);
            editor.apply();
        }
        return profileIndex;
    }

    private void doConfigEngine(String encryptionKey, String encryptionMode) {
        int vProfile = ConstantApp.VIDEO_PROFILES[getVideoProfileIndex()];

        worker().configEngine(vProfile, encryptionKey, encryptionMode);
    }

    public void onBtn0Clicked(View view) {
        log.info("onBtn0Clicked " + view + " " + mVideoMuted + " " + mAudioMuted);
        showMessageEditContainer();
    }

    private void showMessageEditContainer() {
        findViewById(R.id.bottom_action_container).setVisibility(View.GONE);
        findViewById(R.id.bottom_action_end_call).setVisibility(View.GONE);
        findViewById(R.id.msg_input_container).setVisibility(View.VISIBLE);

        EditText edit = (EditText) findViewById(R.id.msg_content);

        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String msgStr = v.getText().toString();
                    if (TextUtils.isEmpty(msgStr)) {
                        return false;
                    }
                    sendChannelMsg(msgStr);

                    v.setText("");

                    Message msg = new Message(Message.MSG_TYPE_TEXT,
                            new User(config().mUid, String.valueOf(config().mUid)), msgStr);
                    notifyMessageChanged(msg);

                    return true;
                }
                return false;
            }
        });

        openIME(edit);
    }

    public void onCustomizedFunctionClicked(View view) {
        log.info("onCustomizedFunctionClicked " + view + " " + mVideoMuted + " " + mAudioMuted + " " + mAudioRouting);
        if (mVideoMuted) {
            onSwitchSpeakerClicked();
        } else {
            onSwitchCameraClicked();
        }
    }

    private void onSwitchCameraClicked() {
        RtcEngine rtcEngine = rtcEngine();
        rtcEngine.switchCamera();
    }

    private void onSwitchSpeakerClicked() {
        RtcEngine rtcEngine = rtcEngine();
        rtcEngine.setEnableSpeakerphone(mAudioRouting != 3);
    }

    @Override
    protected void deInitUIandEvent() {
        optionalDestroy();
        doLeaveChannel();
        event().removeEventHandler(this);
        mUidsList.clear();
    }

    private void doLeaveChannel() {
        worker().leaveChannel(config().mChannel);
        worker().preview(false, null, 0);
    }

    public void onEndCallClicked(View view) {
        log.info("onEndCallClicked " + view);
        channel_time.stop();
        String isFrom= com.sinoautodiagnoseos.utils.Constant.ISFROM;
        if (isFrom=="0"){
            if (che_list.size()==0){
                setUserStatus();
                finish();
                com.sinoautodiagnoseos.utils.Constant.is_push=false;
            }else {
                showMyDialog();
            }

        }else if (isFrom=="1"){
            if (che_list.size()==0){
                setUserStatus();
                finish();
                com.sinoautodiagnoseos.utils.Constant.is_push=false;
            }else {
                showMyDialog();
            }
        }else {
            setUserStatus();
            finish();
            com.sinoautodiagnoseos.utils.Constant.is_push=false;
        }
    }

    public void setUserStatus(){
        /**
         * 用户状态为闲
         */
        HttpRequestApi.getInstance().onLine(new HttpSubscriber<Skill>(new SubscriberOnListener<Skill>() {
            @Override
            public void onSucceed(Skill data) {
            }

            @Override
            public void onError(int code, String msg) {
            }
        }, ChatActivity.this));
    }

    public void onRequestClicked(View view){
        log.info("onRequestClicked"+view);
        findexpertslist();
    }
    private ListExpertsSearchDto les=new ListExpertsSearchDto();
    private void expertsRequests(){
        com.sinoautodiagnoseos.utils.Constant.TOKEN= com.sinoautodiagnoseos.utils.SharedPreferences.getInstance().getString("checktoken","");
        com.sinoautodiagnoseos.utils.Constant.REGISTRATION= com.sinoautodiagnoseos.utils.SharedPreferences.getInstance().getString("RegistrationId","");
        HttpRequestApi.getInstance().expertsRequestExperts(com.sinoautodiagnoseos.utils.Constant.ROOMID,
                new HttpSubscriber<ListExpertsSearchLists>(new SubscriberOnListener<ListExpertsSearchLists>()
                {
                    @Override
                    public void onSucceed(ListExpertsSearchLists data) {
                        com.sinoautodiagnoseos.utils.Constant.Experts=data.getListExpertsSearchDto();
                        Log.e("-----onSucceed------",data.getListExpertsSearchDto().getFaults()+"----"+data.getListExpertsSearchDto().getCarBrandId());
                    }

                    @Override
                    public void onError(int code, String msg) {
                    }
                },ChatActivity.this));
    }

    /**
     *
     * 视频中邀请专家列表接口方法
     */
    private static final int REQUEST_EXPERTS = 1000;
    private void findexpertslist() {
//        les=expertsRequests();
        com.sinoautodiagnoseos.utils.Constant.TOKEN= com.sinoautodiagnoseos.utils.SharedPreferences.getInstance().getString("checktoken","");
        com.sinoautodiagnoseos.utils.Constant.REGISTRATION= com.sinoautodiagnoseos.utils.SharedPreferences.getInstance().getString("RegistrationId","");
        expertsRequests();
        Gson gson=new Gson();
//        System.out.println("ChatActivity_Constant.Experts="+ com.sinoautodiagnoseos.utils.Constant.Experts.getCarBrandId());
        String json = gson.toJson(com.sinoautodiagnoseos.utils.Constant.Experts);
//        String json = gson.toJson(les);
        Log.e("JSON",json);

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),json);
        HttpRequestApi.getInstance().findexpertslist(requestBody,
                new HttpSubscriber<ExpertsInfoList>(new SubscriberOnListener<ExpertsInfoList>()
                {
                    @Override
                    public void onSucceed(ExpertsInfoList data) {
                        System.out.println("--------onSucceed-------");
                        System.out.println("listSize="+data.getData().size()+"\n"+"counts="+data.getTotalCount());
                        List<ExpertsData> expertsDatas = data.getData();
//                        int counts=data.getTotalCount();
                        if (expertsDatas.size()==0||expertsDatas==null){
                            showToast("暂无空闲在线的专家");
                        }else {
                            System.out.println("----执行了-----");
                            Intent intent = new Intent();
                            intent.setClass(ChatActivity.this,RequestOthersActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("extersDatas", (Serializable) expertsDatas);
                            intent.putExtras(bundle);
                            startActivityForResult(intent,REQUEST_EXPERTS);
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {
                        System.out.println("--------onError-------");
                    }
                },ChatActivity.this));
    }


    List<ExpertsScoreData> info=new ArrayList<>();
    private MyDialog builder;
    private String is_satisfied = "";
    private float ratingCount =0;
    private ListViewAdapter listAdapter;
    private void showMyDialog() {
        View view = getLayoutInflater().inflate(R.layout.mydialog_layout,null);
        final TextView dialog_title= (TextView) view.findViewById(R.id.dialog_title);
        final TextView solve= (TextView) view.findViewById(R.id.solve);
        final TextView unsolve= (TextView) view.findViewById(R.id.unsolve);
        final ImageView close_btn= (ImageView) view.findViewById(R.id.close_btn);
        final ImageView satisfied_img = (ImageView) view.findViewById(R.id.satisfied_img);
        final ImageView unsatisfied_img = (ImageView) view.findViewById(R.id.unsatisfied_img);
        final Button submit = (Button) view.findViewById(R.id.submit_btn);
        final ListView listView= (ListView) view.findViewById(R.id.show_score);


        Log.e("List",che_list.size()+"");
        listAdapter=new ListViewAdapter(che_list,this);
        listView.setAdapter(listAdapter);

        builder = new MyDialog(ChatActivity.this,0,0,view, R.style.DialogTheme);
        builder.setCancelable(false);
        builder.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    updatecallhistoriesresult();
                    builder.dismiss();
                    finish();
                    che_list.clear();//退出清除集合
                    che_lists.clear();
                com.sinoautodiagnoseos.utils.Constant.TOKEN= com.sinoautodiagnoseos.utils.SharedPreferences.getInstance().getString("checktoken","");
                com.sinoautodiagnoseos.utils.Constant.REGISTRATION= com.sinoautodiagnoseos.utils.SharedPreferences.getInstance().getString("RegistrationId","");
                    HttpRequestApi.getInstance().onLine( new HttpSubscriber<Skill>(new SubscriberOnListener<Skill>() {
                    @Override
                    public void onSucceed(Skill data) {
                    }

                    @Override
                    public void onError(int code, String msg) {

                    }
                }, ChatActivity.this));

////                //呼叫者主动挂断电话
//                HttpRequestApi.getInstance().getCallStatus( com.sinoautodiagnoseos.utils.Constant.ROOMID,new HttpSubscriber<Skill>(new SubscriberOnListener<Skill>() {
//                    @Override
//                    public void onSucceed(Skill data) {
//                        com.sinoautodiagnoseos.utils.Constant.ROOMID=null;//清除房间号
//                    }
//
//                    @Override
//                    public void onError(int code, String msg) {
//
//                    }
//                }, ChatActivity.this));
            }
        });

        //关闭dialog
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            builder.dismiss();
            }
        });

        satisfied_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                which=1;is_satisfied="2";
                satisfied_img.setImageResource(R.drawable.satisfied_choise);
                unsatisfied_img.setImageResource(R.drawable.unsatisfied_unchoise);
                solve.setTextColor(getResources().getColor(R.color.orange));
                unsolve.setTextColor(getResources().getColor(R.color.gray));
            }
        });

        unsatisfied_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                which=0;is_satisfied="1";
                satisfied_img.setImageResource(R.drawable.satisfied_unchoise);
                unsatisfied_img.setImageResource(R.drawable.unsatisfied_choise);
                solve.setTextColor(getResources().getColor(R.color.gray));
                unsolve.setTextColor(getResources().getColor(R.color.orange));
            }
        });
    }

    /**
     * 挂断视频，提交评分接口
     * 1-未解决  2-已解决
     */
    ListResult lr;
    CallHistoriesExpertsDtos che ;
    List<CallHistoriesExpertsDtos> che_list = new ArrayList<CallHistoriesExpertsDtos>();
    List<CallHistoriesExpertsDtos> che_lists = new ArrayList<CallHistoriesExpertsDtos>();
    private void updatecallhistoriesresult() {
        com.sinoautodiagnoseos.utils.Constant.TOKEN= com.sinoautodiagnoseos.utils.SharedPreferences.getInstance().getString("checktoken","");
        com.sinoautodiagnoseos.utils.Constant.REGISTRATION= com.sinoautodiagnoseos.utils.SharedPreferences.getInstance().getString("RegistrationId","");
        Log.e("result",che_list.size()+"");
        lr=new ListResult();
        for (int i=0;i<che_list.size();i++){
            che=new CallHistoriesExpertsDtos();
//            che.setExpertId(che_list.get(i).getExpertId());
//            che.setMemberId(che_list.get(i).getMemberId());
            che.setExpertNum(che_list.get(i).getExpertNum());
            che.setStarRating(che_list.get(i).getStarRating());
            che.setCallhistoryId(com.sinoautodiagnoseos.utils.Constant.ROOMID);
//            che.setCallhistoryId(che_list.get(i).getCallhistoryId());
            che_lists.add(che);
        }
        lr.setResult(is_satisfied);
//        lr.setCallHistoryId(expertsData.getId());
        lr.setCallHistoryId(com.sinoautodiagnoseos.utils.Constant.ROOMID);
        lr.setCallHistoriesExpertsDtos(che_lists);

        Gson gson=new Gson();
        String json = gson.toJson(lr);
        Log.e("JSON",json);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        HttpRequestApi.getInstance().updatecallhistoriesresult(requestBody,
                new HttpSubscriber<ListResult>(new SubscriberOnListener<ListResult>()
        {
            @Override
            public void onSucceed(ListResult data) {
                endCall();
                ToastUtils.showShort(ChatActivity.this,getString(R.string.score_str));
            }

            @Override
            public void onError(int code, String msg) {

            }
        },ChatActivity.this)
        );
    }

    //呼叫主动挂断电话
    private void endCall(){
        System.out.println("RoomId------"+ com.sinoautodiagnoseos.utils.Constant.ROOMID);
        com.sinoautodiagnoseos.utils.Constant.TOKEN= com.sinoautodiagnoseos.utils.SharedPreferences.getInstance().getString("checktoken","");
        com.sinoautodiagnoseos.utils.Constant.REGISTRATION= com.sinoautodiagnoseos.utils.SharedPreferences.getInstance().getString("RegistrationId","");
        HttpRequestApi.getInstance().getCallStatus(com.sinoautodiagnoseos.utils.Constant.ROOMID,
                new HttpSubscriber<Skill>(new SubscriberOnListener<Skill>() {
                    @Override
                    public void onSucceed(Skill data) {
                        Log.e("onSucceed","onSucceed");
                    }

                    @Override
                    public void onError(int code, String msg) {
                        Log.e("onError","onError");
                    }
                },ChatActivity.this));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sp = getSharedPreferences("roomInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor clear_roomInfo = sp.edit();
        clear_roomInfo.clear();
        clear_roomInfo.commit();
        if (che_list!=null){
            che_list.clear();//退出清除集合
        }
       if (che_lists!=null){
           che_lists.clear();
       }
//        com.sinoautodiagnoseos.utils.Constant.ROOMID=null;
        com.sinoautodiagnoseos.utils.Constant.Experts=null;
        channel_time.stop();
//        download_img=null;
//        download_filename=null;
        this.finish();
    }



    BadgeView badge;
    private void initExplandButton() {
        button = (AllAngleExpandableButton) findViewById(R.id.button_expandable_90_90);
        List<ButtonData> buttonDatas = new ArrayList<>();
        int[] drawable = {R.drawable.menu, file};
        for (int i = 0; i < 2; i++) {
            ButtonData buttonData;
            if (i == 0) {
                buttonData = ButtonData.buildIconButton(this, drawable[i], 15);
            } else {
                buttonData = ButtonData.buildIconButton(this, drawable[i], 0);
            }
            buttonDatas.add(buttonData);
        }
        button.setButtonDatas(buttonDatas);
        setListener(button);
    }


    private void setListener(AllAngleExpandableButton button) {
        button.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int index) {
                if (index==1){
                    if (com.sinoautodiagnoseos.utils.Constant.USERROLE.equals("技师")){
                        if (com.sinoautodiagnoseos.utils.Constant.DOWNLOADURL!=null){
                            showDownLoadUI();//下载文件
                        }else {
                            showToast("暂无可下载内容");
                        }

                    }else {
                        if (com.sinoautodiagnoseos.utils.Constant.DOWNLOADURL!=null){
                            showDownLoadUI();//下载文件
                        }else {
                            showMDDialog();//上传文件
                        }
                    }
//                    if (com.sinoautodiagnoseos.utils.Constant.USERROLE.equals("坐席专家")
//                            ||com.sinoautodiagnoseos.utils.Constant.USERROLE.equals("外部专家")
//                            ||com.sinoautodiagnoseos.utils.Constant.USERROLE.equals("高级专家")
//                            &&com.sinoautodiagnoseos.utils.Constant.DOWNLOADURL==null){
//                        showMDDialog();//点击了上传文件夹
//                    }else(com.sinoautodiagnoseos.utils.Constant.USERROLE.equals("坐席专家")
//                            ||com.sinoautodiagnoseos.utils.Constant.USERROLE.equals("外部专家")
//                            ||com.sinoautodiagnoseos.utils.Constant.USERROLE.equals("高级专家")
//                            &&com.sinoautodiagnoseos.utils.Constant.DOWNLOADURL!=null){
//                        showDownLoadUI();
//                    }
//                    else
//                    {
//                        if (com.sinoautodiagnoseos.utils.Constant.DOWNLOADURL==null){
//                            showToast("暂无可下载内容");
//                        }else {
//                            showDownLoadUI();//下载文件
//                        }
//                    }
                }
            }
            @Override
            public void onExpand() {
//                showToast("onExpand");
            }
            @Override
            public void onCollapse() {
//                showToast("onCollapse");
            }
        });
    }
    private TextView download_filename;
    SpecialProgressBarView d_sp;
    private CircleImageView download_img;
    private String fileName,fileUrl;
    private GridView gridView;
    private FileAdapter fileAdapter;
    private void showDownLoadUI() {
        dialog= new MDDialog.Builder(ChatActivity.this);
        dialog .setContentView(R.layout.download_dialog)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                        System.out.println("filelists="+filelists.size());
                        gridView= (GridView) contentView.findViewById(R.id.grid_view);
                        fileAdapter=new FileAdapter(ChatActivity.this,filelists);
                        gridView.setAdapter(fileAdapter);
//                        download_filename= (TextView) contentView.findViewById(R.id.download_filename);
//                        download_img= (CircleImageView) contentView.findViewById(R.id.download_pic);
                        d_sp= (SpecialProgressBarView) contentView.findViewById(R.id.d_ls);
                        initDownloadProgressBar();
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                fileUrl=filelists.get(position).getFileUrl();
                                fileName=filelists.get(position).getFileName();
                                System.out.println("------fileUrl="+fileUrl+"\n"+"------fileName="+fileName);
                                String sdcardDir = Environment.getExternalStorageDirectory().getPath()+"/SinoAuto/";
                                String fileAllPath=sdcardDir+fileName;
                                getFilePath(sdcardDir,fileName);
                                if (filesExists(fileAllPath)){
                                    if (fileName.contains(".jpg")||fileName.contains(".png")||fileName.contains(".bmp")||
                                            fileName.contains(".gif")){
                                        startActivity( MyIntent.getImageFileIntent(fileAllPath));
                                    } else if(fileName.contains(".html")){
                                        startActivity(MyIntent.getHtmlFileIntent(fileAllPath));
                                    }
                                    else if (fileName.contains(".txt")){
                                        startActivity(MyIntent.getTextFileIntent(fileAllPath,true));
                                    }
                                    else if (fileName.contains(".pdf")){
                                        startActivity(MyIntent.getPdfFileIntent(fileAllPath));
                                    }
                                    else if (fileName.contains(".mp3")||fileName.contains(".wav"))
                                    {
                                        startActivity(MyIntent.getAudioFileIntent(fileAllPath));
                                    }
                                    else if (fileName.contains(".mp4")||fileName.contains(".avi")||
                                            fileName.contains(".rmvb")||fileName.contains(".3gp"))
                                    {
                                        startActivity(MyIntent.getVideoFileIntent(fileAllPath));
                                    }
                                    else if (fileName.contains(".doc")||fileName.contains(".docx")){
                                        startActivity(MyIntent.getWordFileIntent(fileAllPath));
                                    }
                                    else if (fileName.contains(".xls")||fileName.contains(".xlsx")){
                                        startActivity(MyIntent.getExcelFileIntent(fileAllPath));
                                    }
                                    else if (fileName.contains(".ppt")){
                                        startActivity( MyIntent.getPptFileIntent(fileAllPath));
                                    }
                                }else{
                                    d_sp.setVisibility(View.VISIBLE);
                                    num = 0;
                                    d_sp.beginStarting();//启动开始开始动画
                                    downloadAsyn(fileUrl,sdcardDir,fileName);
                                    if (fileName.contains(".jpg")||fileName.contains(".png")||fileName.contains(".bmp")||
                                            fileName.contains(".gif")){
                                        startActivity( MyIntent.getImageFileIntent(fileAllPath));
                                    } else if(fileName.contains(".html")){
                                        startActivity(MyIntent.getHtmlFileIntent(fileAllPath));
                                    }
                                    else if (fileName.contains(".txt")){
                                        startActivity(MyIntent.getTextFileIntent(fileAllPath,true));
                                    }
                                    else if (fileName.contains(".pdf")){
                                        startActivity(MyIntent.getPdfFileIntent(fileAllPath));
                                    }
                                    else if (fileName.contains(".mp3")||fileName.contains(".wav"))
                                    {
                                        startActivity(MyIntent.getAudioFileIntent(fileAllPath));
                                    }
                                    else if (fileName.contains(".mp4")||fileName.contains(".avi")||
                                            fileName.contains(".rmvb")||fileName.contains(".3gp"))
                                    {
                                        startActivity(MyIntent.getVideoFileIntent(fileAllPath));
                                    }
                                    else if (fileName.contains(".doc")||fileName.contains(".docx")){
                                        startActivity(MyIntent.getWordFileIntent(fileAllPath));
                                    }
                                    else if (fileName.contains(".xls")||fileName.contains(".xlsx")){
                                        startActivity(MyIntent.getExcelFileIntent(fileAllPath));
                                    }
                                    else if (fileName.contains(".ppt")){
                                        startActivity( MyIntent.getPptFileIntent(fileAllPath));
                                    }
                                }
                            }
                        });
                        fileName= com.sinoautodiagnoseos.utils.Constant.FILENAME;
                        fileUrl= com.sinoautodiagnoseos.utils.Constant.DOWNLOADURL;
                    }
                })
                .setTitle("下载文件列表")
                .setNegativeButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButtonMultiListener(new MDDialog.OnMultiClickListener() {
                    @Override
                    public void onClick(View clickedView, View contentView) {
//                        Toast.makeText(getApplicationContext(), "edittext 0 : " + et.getText(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnItemClickListener(new MDDialog.OnItemClickListener() {
                    @Override
                    public void onItemClicked(int index) {
                    }
                })
                .setWidthMaxDp(600)
                .setShowButtons(true)
                .create()
                .show();
    }

    public boolean filesExists( String fileAllPath){
        try{
            File file = new File(fileAllPath);
            if(!file.exists()){
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public static File getFilePath(String filePath,
                                   String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file;
    }

    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }

    public void downloadAsyn(final String url, final String destDir, final String fileNames){
        final OkHttpClient client= new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.isSuccessful());
                InputStream is=null;
                byte[]buf=new byte[2048];
//                sendMsg(1);
                int numread=0;
                FileOutputStream fos=null;
                try {
                    is=response.body().byteStream();
                    final File file = new File(destDir + fileNames);
                    fos=new FileOutputStream(file);
                    while ((numread=is.read(buf))!=-1){
                        fos.write(buf,0,numread);
//                        sendMsg(1);// 更新进度条
                    }
//                    sendMsg(2);// 通知下载完成
                    android.os.Message msg = msg_handler.obtainMessage();
                    msg.what=5;
                    msg_handler.sendMessage(msg);
                    fos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    try {
                        if (is!=null)is.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try {
                        if (fos!=null)fos.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private EditText et;
    private ImageView select_path;
    int num = 0;
    SpecialProgressBarView ls;
    MDDialog.Builder dialog;
    private void showMDDialog() {
       dialog= new MDDialog.Builder(ChatActivity.this);
               dialog .setContentView(R.layout.content_dialog)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                        et = (EditText)contentView.findViewById(R.id.edit0);
                        et.setInputType(InputType.TYPE_NULL);//禁止软键盘弹出 也就是不可编辑
                        select_path= (ImageView) contentView.findViewById(R.id.select_path);
                        select_path.setOnClickListener(selectListener);

                        ls = (SpecialProgressBarView) contentView.findViewById(R.id.ls);
                        initProgressBar();

                    }
                })
                .setTitle("添加文件路径")
                .setNegativeButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadFile1();
                    }
                })
                .setPositiveButtonMultiListener(new MDDialog.OnMultiClickListener() {
                    @Override
                    public void onClick(View clickedView, View contentView) {
                        EditText et = (EditText)contentView.findViewById(R.id.edit0);
//                        Toast.makeText(getApplicationContext(), "edittext 0 : " + et.getText(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnItemClickListener(new MDDialog.OnItemClickListener() {
                    @Override
                    public void onItemClicked(int index) {
                        if (index == 0) {
                        } else if (index == 1) {
                        } else if (index ==2 ){
                        }
                    }
                })
                .setWidthMaxDp(600)
                .setShowButtons(true)
                .create()
                .show();
    }

    private void initDownloadProgressBar() {
        d_sp.setEndSuccessBackgroundColor(Color.parseColor("#66A269"))//设置进度完成时背景颜色
                .setEndSuccessDrawable(R.drawable.ic_done_white_36dp,null)//设置进度完成时背景图片
                .setCanEndSuccessClickable(false)//设置进度完成后是否可以再次点击开始
                .setProgressBarColor(Color.WHITE)//进度条颜色
                .setCanDragChangeProgress(false)//是否进度条是否可以拖拽
                .setCanReBack(true)//是否在进度成功后返回初始状态
                .setProgressBarBgColor(getResources().getColor(R.color.agora_blue))//进度条背景颜色
                .setProgressBarHeight(d_sp.dip2px(ChatActivity.this,4f))//进度条宽度
                .setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()))//设置字体大小
                .setStartDrawable(R.drawable.ic_get_app_white_36dp,null)//设置开始时背景图片
                .setTextColorSuccess(Color.parseColor("#66A269"))//设置成功时字体颜色
                .setTextColorNormal(Color.parseColor("#491C14"))//设置默认字体颜色
                .setTextColorError(Color.parseColor("#BC5246"));//设置错误时字体颜色

        d_sp.setOnAnimationEndListener(new SpecialProgressBarView.AnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                d_sp.setMax(187);
//                ls.setProgress(num);//在动画结束时设置进度
                android.os.Message msg = msg_handler.obtainMessage();
                msg.what=4;
                msg_handler.sendMessage(msg);
            }
        });

        d_sp.setOntextChangeListener(new SpecialProgressBarView.OntextChangeListener() {
            @Override
            public String onProgressTextChange(SpecialProgressBarView specialProgressBarView, int max, int progress) {
                return progress * 100 / max + "%";
            }

            @Override
            public String onErrorTextChange(SpecialProgressBarView specialProgressBarView, int max, int progress) {
                return "error";
            }

            @Override
            public String onSuccessTextChange(SpecialProgressBarView specialProgressBarView, int max, int progress) {
                return "done";
            }
        });
    }

    private void initProgressBar() {
        ls.setEndSuccessBackgroundColor(Color.parseColor("#66A269"))//设置进度完成时背景颜色
                .setEndSuccessDrawable(R.drawable.ic_done_white_36dp,null)//设置进度完成时背景图片
                .setCanEndSuccessClickable(false)//设置进度完成后是否可以再次点击开始
                .setProgressBarColor(Color.WHITE)//进度条颜色
                .setCanDragChangeProgress(false)//是否进度条是否可以拖拽
                .setCanReBack(true)//是否在进度成功后返回初始状态
                .setProgressBarBgColor(getResources().getColor(R.color.agora_blue))//进度条背景颜色
                .setProgressBarHeight(ls.dip2px(ChatActivity.this,4))//进度条宽度
                .setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()))//设置字体大小
                .setStartDrawable(R.drawable.ic_file_upload_white_36dp,null)//设置开始时背景图片
                .setTextColorSuccess(Color.parseColor("#66A269"))//设置成功时字体颜色
                .setTextColorNormal(Color.parseColor("#491C14"))//设置默认字体颜色
                .setTextColorError(Color.parseColor("#BC5246"));//设置错误时字体颜色

        ls.setOnAnimationEndListener(new SpecialProgressBarView.AnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                ls.setMax(187);
//                ls.setProgress(num);//在动画结束时设置进度
                android.os.Message msg = msg_handler.obtainMessage();
                msg.what=2;
                msg_handler.sendMessage(msg);
            }
        });

        ls.setOntextChangeListener(new SpecialProgressBarView.OntextChangeListener() {
            @Override
            public String onProgressTextChange(SpecialProgressBarView specialProgressBarView, int max, int progress) {
                return progress * 100 / max + "%";
            }

            @Override
            public String onErrorTextChange(SpecialProgressBarView specialProgressBarView, int max, int progress) {
                return "error";
            }

            @Override
            public String onSuccessTextChange(SpecialProgressBarView specialProgressBarView, int max, int progress) {
                return "done";
            }
        });
    }

    private static final int FILE_SELECT_CODE = 0;
    View.OnClickListener selectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");//过滤文件类型（所有）
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            try {
                startActivityForResult(Intent.createChooser(intent, "请选择文件！"), FILE_SELECT_CODE);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(ChatActivity.this, "未安装文件管理器！", Toast.LENGTH_SHORT).show();
            }
        }
    };
    Handler msg_handler = new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case 1:
                    String path = (String) msg.obj;
                    et.setText(path);
                    break;
                case 2:
                    num++;
                    if (num<=ls.getMax()){
                        ls.setProgress(num);
                        msg_handler.sendEmptyMessageDelayed(2, 50);
                    }
                    break;
                case 3:
                    String msg_str= (String) msg.obj;
                    showToast(msg_str);
                    break;
                case 5:
                    String msg_success= (String) msg.obj;
//                    badge.setVisibility(View.INVISIBLE);
                    break;
                case 4:
                    num++;
                    if (num<=d_sp.getMax()){
                        d_sp.setProgress(num);
                        msg_handler.sendEmptyMessageDelayed(4, 50);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void showProgress() {
        ls.setVisibility(View.VISIBLE);
        num = 0;
        ls.beginStarting();//启动开始开始动画
    }
    private ProgressBar progressBar;
    String download_url,download_fileName,download_id,extensio;
    UploadDatas uploadDatas;
    private void uploadFile1() {
        com.sinoautodiagnoseos.utils.Constant.TOKEN= com.sinoautodiagnoseos.utils.SharedPreferences.getInstance().getString("checktoken","");
        com.sinoautodiagnoseos.utils.Constant.REGISTRATION= com.sinoautodiagnoseos.utils.SharedPreferences.getInstance().getString("RegistrationId","");
        String path = et.getText().toString().trim();
        final String fileName = path.substring(path.lastIndexOf("/")+1);
        if (path.equals("")&&path==null){
            showToast("请选择你要上传的文件");
        }else {
            HttpRequestApi.getInstance().uploadFile(upImage(path,fileName), new HttpSubscriber<Upload>(new SubscriberOnListener<Upload>() {
                @Override
                public void onSucceed(Upload upload) {
                    download_url=upload.getData().getDownloadUrl();//下载文件地址
                    download_fileName=upload.getData().getFilename();//文件名
                    download_id=upload.getData().getId();
                    extensio=upload.getData().getExtension();

                    uploadDatas=new UploadDatas();
                    uploadDatas.setCallHistoryId(com.sinoautodiagnoseos.utils.Constant.ROOMID);
                    uploadDatas.setFileId(download_id);
                    uploadDatas.setFileName(download_fileName);
                    uploadDatas.setFileUrl(download_url);
                    uploadDatas.setFileSize(0);
                    uploadDatas.setContentType(extensio);
                    Gson gson = new Gson();
                    String json = gson.toJson(uploadDatas);

                    Log.e("JSON", json);
                    com.sinoautodiagnoseos.utils.Constant.TOKEN= com.sinoautodiagnoseos.utils.SharedPreferences.getInstance().getString("checktoken","");
                    com.sinoautodiagnoseos.utils.Constant.REGISTRATION= com.sinoautodiagnoseos.utils.SharedPreferences.getInstance().getString("RegistrationId","");
                    RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
                    HttpRequestApi.getInstance().uploadFiles(requestBody, new HttpSubscriber<String>(new SubscriberOnListener<String>() {
                        @Override
                        public void onSucceed(String data) {
//                            PushUtil.DownloadPush(ChatActivity.this, com.sinoautodiagnoseos.utils.Constant.SACCOUNT,download_url,download_fileName);
                        }

                        @Override
                        public void onError(int code, String msg) {

                        }
                    },ChatActivity.this));
                }

                @Override
                public void onError(int code, String msg) {
                }
            }, ChatActivity.this));
        }
    }


    /**
     * 上传图片的请求封装
     *
     *
     * @param path
     * @param fileName
     * @return
     */
    public MultipartBody upImage(String path, String fileName) {
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("file", fileName, requestFile);
        builder.addFormDataPart("title", fileName);
        builder.addFormDataPart("alt", "");
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }




    ExpertsData expertsData;
    CallHistoriesExpertsDtos callHistoriesExpertsDtos ;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case FILE_SELECT_CODE:
                if (resultCode==RESULT_OK){
                    Uri uri = data.getData();
//                    String path = FileUtil.getPath(this,uri);//得到文件路径
                    String path = FileUtil.getMobilePath(this,uri);
                    android.os.Message msg = msg_handler.obtainMessage();
                    msg.what=1;
                    msg.obj = path;
                    msg_handler.sendMessage(msg);
                    if (path!=null){
                        //处理上传的动画
                        showProgress();
                    }
                }
                break;
            case REQUEST_EXPERTS:
                if (resultCode==1000){
                    expertsData = (ExpertsData) data.getExtras().get("expertsInfo");
                    callHistoriesExpertsDtos = new CallHistoriesExpertsDtos();
//                    callHistoriesExpertsDtos.setExpertId(expertsData.getId());
//                    callHistoriesExpertsDtos.setMemberId(expertsData.getMemberId());
//                    callHistoriesExpertsDtos.setCallhistoryId(expertsData.getCallHistoryId());
                    callHistoriesExpertsDtos.setName(expertsData.getName());
                    callHistoriesExpertsDtos.setAvatar(expertsData.getAvatorUrl());
                    che_list.add(callHistoriesExpertsDtos);
                    Log.e("che_list",che_list.size()+"");
//                    lr.setCallHistoriesExpertsDtos(che_list);
                }
        }
    }

    static class FileUtil {
        public static String getPath(Context context, Uri uri) {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                String[] projection = { "_data" };
                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver().query(uri, projection, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow("_data");
                    if (cursor.moveToFirst()) {
                        return cursor.getString(column_index);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else if ("file".equalsIgnoreCase(uri.getScheme())){
                return uri.getPath();
            }
                return null;
        }
        //专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
        @SuppressLint("NewApi")
        public static String getMobilePath(final Context context, final Uri uri) {
            final boolean isKitKat = Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT;
            if (isKitKat&& DocumentsContract.isDocumentUri(context,uri)){
                if (isExternalStorageDocument(uri)){
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                } else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
            return null;
        }

        public static String getDataColumn(Context context, Uri uri, String selection,
                                           String[] selectionArgs) {

            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = {column};

            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(column_index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }

        public static boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }


        public static boolean isExternalStorageDocument(Uri uri){
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        public static boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri.getAuthority());
        }
    }
    private void showToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

//    private String is_show = "";
    public void onVoiceChatClicked(View view) {
        log.info("onVoiceChatClicked " + view + " " + mUidsList.size() + " video_status: " + mVideoMuted + " audio_status: " + mAudioMuted);
        showToast("录制功能暂未开放!");

//            is_show="1";
//        if (is_show=="1"){
//            mSmallVideoViewDock.setVisibility(View.INVISIBLE);
//            is_show="0";
//        }else if (is_show=="0") {
//            mSmallVideoViewDock.setVisibility(View.VISIBLE);
//        }

//        if (mUidsList.size() == 0) {
//            return;
//        }
//
//        SurfaceView surfaceV = getLocalView();
//        ViewParent parent;
//        if (surfaceV == null || (parent = surfaceV.getParent()) == null) {
//            log.warn("onVoiceChatClicked " + view + " " + surfaceV);
//            return;
//        }
//
//        RtcEngine rtcEngine = rtcEngine();
//        mVideoMuted = !mVideoMuted;
//
//        if (mVideoMuted) {
//            rtcEngine.disableVideo();
//        } else {
//            rtcEngine.enableVideo();
//        }
//
//        ImageView iv = (ImageView) view;
//
//        iv.setImageResource(mVideoMuted ? R.drawable.btn_video : R.drawable.btn_voice);
//
//        hideLocalView(mVideoMuted);
//
//        if (mVideoMuted) {
//            resetToVideoDisabledUI();
//        } else {
//            resetToVideoEnabledUI();
//        }
    }

    private SurfaceView getLocalView() {
        for (HashMap.Entry<Integer, SurfaceView> entry : mUidsList.entrySet()) {
            if (entry.getKey() == 0 || entry.getKey() == config().mUid) {
                return entry.getValue();
            }
        }

        return null;
    }

    private void hideLocalView(boolean hide) {
        int uid = config().mUid;
        doHideTargetView(uid, hide);
    }

    private void doHideTargetView(int targetUid, boolean hide) {
        HashMap<Integer, Integer> status = new HashMap<>();
        status.put(targetUid, hide ? UserStatusData.VIDEO_MUTED : UserStatusData.DEFAULT_STATUS);
        if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
            mGridVideoViewContainer.notifyUiChanged(mUidsList, targetUid, status, null);
        } else if (mLayoutType == LAYOUT_TYPE_SMALL) {
            UserStatusData bigBgUser = mGridVideoViewContainer.getItem(0);
            if (bigBgUser.mUid == targetUid) { // big background is target view
                mGridVideoViewContainer.notifyUiChanged(mUidsList, targetUid, status, null);
            } else { // find target view in small video view list
                log.warn("SmallVideoViewAdapter call notifyUiChanged " + mUidsList + " " + (bigBgUser.mUid & 0xFFFFFFFFL) + " target: " + (targetUid & 0xFFFFFFFFL) + "==" + targetUid + " " + status);
                mSmallVideoViewAdapter.notifyUiChanged(mUidsList, bigBgUser.mUid, status, null);
            }
        }
    }

    private void resetToVideoEnabledUI() {
        ImageView iv = (ImageView) findViewById(R.id.customized_function_id);
        iv.setImageResource(R.drawable.btn_switch_camera);
        iv.clearColorFilter();

        notifyHeadsetPlugged(mAudioRouting);
    }

    private void resetToVideoDisabledUI() {
        ImageView iv = (ImageView) findViewById(R.id.customized_function_id);
        iv.setImageResource(R.drawable.btn_speaker);
        iv.clearColorFilter();

        notifyHeadsetPlugged(mAudioRouting);
    }

    public void onVoiceMuteClicked(View view) {
        log.info("onVoiceMuteClicked " + view + " " + mUidsList.size() + " video_status: " + mVideoMuted + " audio_status: " + mAudioMuted);
        if (mUidsList.size() == 0) {
            return;
        }

        RtcEngine rtcEngine = rtcEngine();
        rtcEngine.muteLocalAudioStream(mAudioMuted = !mAudioMuted);

        ImageView iv = (ImageView) view;

        if (mAudioMuted) {
            iv.setColorFilter(getResources().getColor(R.color.agora_blue), PorterDuff.Mode.MULTIPLY);
        } else {
            iv.clearColorFilter();
        }
    }


}
