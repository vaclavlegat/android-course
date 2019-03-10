package eu.profinit.profis.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import eu.profinit.profis.R;
import eu.profinit.profis.sync.SyncReceiver;
import eu.profinit.profis.sync.SyncService;

import static eu.profinit.profis.sync.SyncService.SYNC_ACTION;

public class UtilizationListActivity extends AppCompatActivity {

    public static final String CHANNEL_ID = "profis";

    private SyncReceiver receiver = new SyncReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilization_list);

        createNotificationChannel();

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreate();
            }
        });

        Intent syncService = new Intent(this, SyncService.class);
        startService(syncService);

        boolean isMultiPane = findViewById(R.id.detail_placeholder) != null;

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.list_placeholder, UtilizationListFragment.newInstance()).commit();

            if (isMultiPane) {
                getSupportFragmentManager().beginTransaction().replace(R.id.detail_placeholder, UtilizationDetailFragment.newInstance(1)).commit();
            }

        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(SYNC_ACTION);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void openCreate() {
        Intent intent = new Intent(this, UtilizationCreateActivity.class);
        startActivity(intent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = getString(R.string.notification_channel_name);
            String description = getString(R.string.notification_channel_desc);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
