package com.example.shlim.baseandroidproject.fcm;
import com.example.shlim.baseandroidproject.utils.LogUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class BaseFCMMsgService extends FirebaseMessagingService {
    private static final String TAG = BaseFCMMsgService.class.getSimpleName(); // 디버그 태그

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        LogUtil.i(TAG, "onMessageReceived() -> Start !!!");
        LogUtil.d(TAG, "onMessageReceived() -> getFrom : " + remoteMessage.getFrom());

        if(null != remoteMessage.getNotification()) {
            RemoteMessage.Notification noti = remoteMessage.getNotification();
            String msg = noti.getBody();
            LogUtil.d(TAG, "onMessageReceived() -> getBody :  " + msg);
        }

        if(remoteMessage.getData().size() > 0) {
            LogUtil.d(TAG, "onMessageReceived() -> getData :  " + remoteMessage.getData());

        }

        super.onMessageReceived(remoteMessage);
    }
}
