package eu.profinit.profis.sync;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import androidx.annotation.Nullable;
import eu.profinit.profis.db.ProfisDatabase;
import eu.profinit.profis.model.UtilizationItem;

public class SyncService extends IntentService {

    public static final String SYNC_ACTION = "SYNC_ACTION";

    public SyncService() {
        super("SyncService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        List<UtilizationItem> items = ProfisDatabase.getInstance(this).utilizationDao().getAll();
        for (UtilizationItem item : items) {
            Log.d("SyncService", "Syncing item id: " + item.getId());
            sleep();
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

}
