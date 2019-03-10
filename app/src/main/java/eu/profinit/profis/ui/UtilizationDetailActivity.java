package eu.profinit.profis.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import eu.profinit.profis.R;

public class UtilizationDetailActivity extends AppCompatActivity {

    public static final String ID_KEY = "ID_KEY";
    private static final int CONTACTS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilization_detail);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(R.string.utilization_detail_title);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.detail_placeholder, UtilizationDetailFragment.newInstance(getIntent().getLongExtra(ID_KEY, 0))).commit();
        }

        checkPermission();

    }

    public static void start(Context context, long id) {
        Intent intent = new Intent(context, UtilizationDetailActivity.class);
        intent.putExtra(ID_KEY, id);
        context.startActivity(intent);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CONTACTS},
                CONTACTS_REQUEST);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                // show explanation
                new AlertDialog.Builder(this).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermission();
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setTitle(R.string.permission_contacts_title).setMessage(R.string.permission_contacts_message).create().show();

            } else {
                //request permission
                requestPermission();
            }
        } else {
            // we have permission
            showContacts();
        }
    }

    private void showContacts() {
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                ContactsContract.Contacts.DISPLAY_NAME + " like ?",
                new String[]{"A%"},
                null);

        int contactsSize = 0;
        while (cursor != null && cursor.moveToNext()) {
            contactsSize++;
        }

        if (cursor != null) {
            cursor.close();
        }

        Toast.makeText(this, "Contacts size " + contactsSize, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (CONTACTS_REQUEST == requestCode && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showContacts();
        } else {
            // permission denied
        }
    }

}
