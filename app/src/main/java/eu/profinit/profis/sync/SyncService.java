package eu.profinit.profis.sync;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import eu.profinit.profis.R;
import eu.profinit.profis.db.ProfisDatabase;
import eu.profinit.profis.model.UtilizationItem;
import eu.profinit.profis.ui.UtilizationCreateActivity;
import eu.profinit.profis.ui.UtilizationListActivity;

import static eu.profinit.profis.ui.UtilizationListActivity.CHANNEL_ID;

public class SyncService extends IntentService {

    public static final String SYNC_ACTION = "SYNC_ACTION";

    public SyncService() {
        super("SyncService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        boolean hasToday = false;
        List<UtilizationItem> items = ProfisDatabase.getInstance(this).utilizationDao().getAll();
        for (UtilizationItem item : items) {
            if (DateUtils.isToday(item.getDate().getTime()) && item.getHours() < 8) {
                createNotification(8 - item.getHours());
                hasToday = true;
            }

            Log.d("SyncService", "Syncing item id: " + item.getId());
            sleep();
        }

        if (!hasToday) {
            createNotification(8);
        }

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(SYNC_ACTION);
        sendBroadcast(broadcastIntent);
    }

    void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }

    private void createNotification(int hours) {

        Intent intent = new Intent(this, UtilizationCreateActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(UtilizationCreateActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle(getString(R.string.utilization_notification_title))
                .setContentText(getString(R.string.utilization_notification_message, hours))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        notificationBuilder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(4, notificationBuilder.build());

    }


}
