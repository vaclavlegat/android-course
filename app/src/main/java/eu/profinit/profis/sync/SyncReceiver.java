package eu.profinit.profis.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import static eu.profinit.profis.sync.SyncService.SYNC_ACTION;

public class SyncReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!SYNC_ACTION.equals(intent.getAction())) {
            return;
        }

        Toast.makeText(context, "Synchronizováno", Toast.LENGTH_SHORT).show();
    }
}
