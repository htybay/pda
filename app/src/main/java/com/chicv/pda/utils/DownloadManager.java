package com.chicv.pda.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.chicv.pda.R;
import com.chicv.pda.base.BaseApplication;
import com.chicv.pda.widget.UpdateDialog;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;


/**
 * Created by Liheyu on 2017/9/25.
 * Email:liheyu999@163.com
 * 下载APP,并通知安装
 */

public class DownloadManager {

    public static final String TAG = "DownloadManager";
    public static final String TEST_UPDATE_ADDRESS = "http://47.112.137.28:8090/";
    private static final int NOTIFY_ID = 9;

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotifyBuilder;
    private UpdateDialog progressDialog;
    private Activity mActivity;


    public DownloadManager(Activity context) {
        this.mActivity = context;
        FileDownloader.setup(context);
    }

    public void updateAPP(final String url) {
        showDialog();
        String directoryPath = Environment.getExternalStorageDirectory().getPath() + "/" + "pda/";
        File file = new File(directoryPath);
        if (!file.exists()) file.mkdirs();
        FileDownloader.getImpl().create(url)
                .setPath(directoryPath + "pda.apk")
                .setForceReDownload(true)
                .setCallbackProgressTimes(100)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d(TAG, "url:" + task.getUrl() + "\n" + "fileName:" + task.getFilename());
                        Log.d(TAG, "pending: " + "大小：" + FileUtils.getFormatSize(totalBytes));
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        Log.d(TAG, "connected: " + totalBytes);
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d(TAG, "progress: " + soFarBytes + "总大小：" + totalBytes);
//                        if (mNotificationManager != null && mNotifyBuilder != null) {
//                            Notification notification = mNotifyBuilder.setProgress(totalBytes, soFarBytes, false)
//                                    .setContentInfo(FileUtils.getFormatSize(soFarBytes) + "/" + soFarBytes / (float) totalBytes * 10000 / 100 + "%")
//                                    .build();
//                            mNotificationManager.notify(NOTIFY_ID, notification);
//                        }
                        if (progressDialog != null) {
                            progressDialog.setMaxProgress(totalBytes);
                            progressDialog.setProgress(soFarBytes);
                            progressDialog.setProgressDetial(FileUtils.getFormatSize(soFarBytes) + "/" + FileUtils.getFormatSize(totalBytes));
                        }
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                        Log.d(TAG, "blockComplete: ");
                    }

                    @Override

                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Log.d(TAG, "completed: ");
                        hideDialog();
                        installApp(mActivity, task.getTargetFilePath());
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.e(TAG, "error: ");
                        e.printStackTrace();
                        if (task != null && task.getTargetFilePath() != null) {
                            File tempFile = new File(task.getTargetFilePath());
                            if (tempFile.exists()) tempFile.delete();
                        }
                        hideDialog();
                        ToastUtils.showString("更新错误，请稍后再试!");
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
    }

    private void notifyInstallAPK(String apkPath) {
        if (apkPath == null || mActivity == null) {
            return;
        }
        File file = new File(apkPath);
        if (!file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(mActivity, "com.chicv.pda.provider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");

        PendingIntent pendingIntent = PendingIntent.getActivity(mActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotifyBuilder.setDefaults(Notification.DEFAULT_ALL)
                .setContentText("点击安装")
                .setContentIntent(pendingIntent);
        mNotificationManager.notify(NOTIFY_ID, mNotifyBuilder.build());
    }

    private void installApp(Context context, String apkPath) {
        if (apkPath == null || mActivity == null) {
            return;
        }
        File file = new File(apkPath);
        if (!file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(mActivity, "com.chicv.pda.provider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    //初始化通知
    private void initNotification() {
        mNotifyBuilder = new NotificationCompat.Builder(mActivity);
        mNotifyBuilder.setContentTitle(mActivity.getResources().getString(R.string.app_name) + "升级")
                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mis_asv))
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX)
                .setOngoing(true);
        mNotificationManager = (NotificationManager) BaseApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void showDialog() {
        //如果未传activity就不显示对话框
        if (mActivity == null) {
            return;
        }
        if (progressDialog == null) {
            progressDialog = new UpdateDialog(mActivity);
        }
        progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
