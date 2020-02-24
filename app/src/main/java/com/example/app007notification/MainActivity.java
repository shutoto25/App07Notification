package com.example.app007notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

/**
 * notification create.
 * TODO URI不正で遷移失敗時にトースト出したいけど失敗の検知がわからない
 */
public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {
    /**
     * input notification title.
     */
    private EditText mTitle;
    /**
     * input notification text.
     */
    private EditText mText;
    /**
     * input Transition destination.
     */
    private EditText mOthersUri;
    /**
     * send button.
     */
    private Button mSendNotification;
    /**
     * Transition destination.
     */
    private RadioGroup mRadioGroup;
    /**
     * channel list.
     */
    private RadioGroup mChannelGroup;
    /**
     * channel id.
     */
    private static final String NOTIFICATION_CHANNEL_ID = "notification_channel";
    /**
     * channel id.
     */
    private static final String NOTIFICATION_CHANNEL_2_ID = "notification_channel2";
    /**
     * notificationManager.
     */
    private NotificationManager mManager;
    /**
     * notificationChannel.
     */
    private NotificationChannel mChannel;
    /**
     * notification id
     */
    private int mNotification_Id = 0;
    /**
     * notification1 id.
     */
    private int mNotification1_Id = 1;
    /**
     * notification2 id.
     */
    private int mNotification2_Id = 2;
    /**
     * default notification title.
     */
    private String mNotificationTitle = "default title";
    /**
     * default notification text.
     */
    private String mNotificationText = "default text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mTitle = findViewById(R.id.etTitle);
        mText = findViewById(R.id.etText);
        mOthersUri = findViewById(R.id.etOthersUri);
        mSendNotification = findViewById(R.id.btNotification);
        mSendNotification.setOnClickListener(this);
        mRadioGroup = findViewById(R.id.rgGroup);
        mChannelGroup = findViewById(R.id.rgChannel);

        createNotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "notification_Channel",
                "notification_description",
                NotificationManager.IMPORTANCE_DEFAULT);

        createNotificationChannel(NOTIFICATION_CHANNEL_2_ID,
                "のてぃふぃけーしょん",
                "せつめーーいぶーーーーーーーーーーーん",
                NotificationManager.IMPORTANCE_HIGH);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        deleteNotificationChannel();
    }

    /**
     * create NotificationChannel.
     */
    private void createNotificationChannel(String channelId, CharSequence name, String description, int importance) {

        // only on API 26+
        // because the NotificationChannel class is new and not in the support library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            mChannel = new NotificationChannel(
                    channelId, name, importance);
            mChannel.setDescription(description);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            // Register the channel with the system
            // can't change the importance or other notification behaviors after this.
            mManager = getSystemService(NotificationManager.class);
            mManager.createNotificationChannel(mChannel);
        }
    }

    /**
     * delete NotificationChannel.
     */
    private void deleteNotificationChannel() {

        if (mManager != null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mManager.deleteNotificationChannel(NOTIFICATION_CHANNEL_ID);
            mManager = null;
        }
    }

    /**
     * send Notification.
     */
    private void sendNotification(Intent intent, String channelId) {

        if (intent == null) {
            Toast.makeText(this, "遷移先を選択/入力してください", Toast.LENGTH_SHORT).show();
            return;
        }

        if (channelId == null) {
            Toast.makeText(this, "チャンネルを選択してください", Toast.LENGTH_SHORT).show();
            return;
        }

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, 0);

        Notification.Builder builder = new Notification.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(mNotificationTitle)
                .setContentText(mNotificationText)
                // Set the intent that will fire when the user taps the notification.
                .setContentIntent(pendingIntent)
                // Clear notification When top this.
                .setAutoCancel(true);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(mNotification_Id, builder.build());
    }

    /**
     * set notification text.
     * if text is empty, display default text.
     */
    private void setNotificationText() {

        if (!TextUtils.isEmpty(mTitle.getText().toString())) {
            mNotificationTitle = mTitle.getText().toString();
        }
        if (!TextUtils.isEmpty(mText.getText().toString())) {
            mNotificationText = mText.getText().toString();
        }
    }

    /**
     * get radioButton id.
     *
     * @return Transition destination.
     */
    private Intent getSelectedTransition() {

        Intent resultIntent = null;
        Uri selectItem;

        int id = mRadioGroup.getCheckedRadioButtonId();
        switch (id) {
            case R.id.rbNewActivity:
                resultIntent = new Intent(this, SubActivity.class);
                break;

            case R.id.rbGoogle:
                selectItem = Uri.parse("https://www.google.com/");
                resultIntent = new Intent(Intent.ACTION_VIEW, selectItem);
                break;

            case R.id.rbDevelopers:
                selectItem = Uri.parse("https://developer.android.com/");
                resultIntent = new Intent(Intent.ACTION_VIEW, selectItem);
                break;

            case R.id.rbOthers:
                String inputUri = mOthersUri.getText().toString();
                selectItem = Uri.parse(inputUri);
                resultIntent = new Intent(Intent.ACTION_VIEW, selectItem);

            default:
                break;
        }
        return resultIntent;
    }

    /**
     * get selected channel.
     *
     * @return selected channel.
     */
    private String getSelectedChannel() {

        String selectedChannel = null;
        int id = mChannelGroup.getCheckedRadioButtonId();
        switch (id) {
            case R.id.rbChannel1:
                selectedChannel = NOTIFICATION_CHANNEL_ID;
                this.mNotification_Id = mNotification1_Id;
                break;

            case R.id.rbChannel2:
                selectedChannel = NOTIFICATION_CHANNEL_2_ID;
                this.mNotification_Id = mNotification2_Id;
                break;

            default:
                break;
        }
        return selectedChannel;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btNotification:
                setNotificationText();
                sendNotification(getSelectedTransition(), getSelectedChannel());
                break;

            default:
                break;
        }
    }
}
