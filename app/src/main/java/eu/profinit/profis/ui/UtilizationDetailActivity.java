package eu.profinit.profis.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import eu.profinit.profis.R;
import eu.profinit.profis.db.ProfisDatabase;
import eu.profinit.profis.model.UtilizationItem;

public class UtilizationDetailActivity extends AppCompatActivity {

    private static final String ID_KEY = "ID_KEY";

    private TextView contract;
    private TextView hours;
    private TextView date;
    private TextView note;
    private Button deleteBtn;

    private UtilizationItem selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilization_detail);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(R.string.utilization_detail_title);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        contract = findViewById(R.id.contract);
        hours = findViewById(R.id.hours);
        date = findViewById(R.id.date);
        note = findViewById(R.id.note);
        deleteBtn = findViewById(R.id.delete_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });


        new LoadItemTask().execute(getIntent().getLongExtra(ID_KEY, 0));

    }

    public static void start(Context context, long id) {
        Intent intent = new Intent(context, UtilizationDetailActivity.class);
        intent.putExtra(ID_KEY, id);
        context.startActivity(intent);
    }

    private UtilizationItem loadItem(long id) {
        return ProfisDatabase.getInstance(this).utilizationDao().getById(id);
    }

    private class LoadItemTask extends AsyncTask<Long, Void, UtilizationItem> {

        @Override
        protected UtilizationItem doInBackground(Long... ids) {
            return loadItem(ids[0]);
        }

        @Override
        protected void onPostExecute(UtilizationItem item) {
            super.onPostExecute(item);
            contract.setText(item.getContract());
            hours.setText(getString(R.string.hours, item.getHours()));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MM. yyyy", Locale.getDefault());
            date.setText(dateFormat.format(item.getDate()));
            note.setText(item.getNote());
            selectedItem = item;
        }
    }

    private void showSuccess() {
        Toast.makeText(this, R.string.deleted, Toast.LENGTH_SHORT).show();
    }

    private void deleteItem(UtilizationItem item) {
        ProfisDatabase.getInstance(this).utilizationDao().delete(item);
    }

    private class DeleteItemTask extends AsyncTask<UtilizationItem, Void, Void> {

        @Override
        protected Void doInBackground(UtilizationItem... items) {
            deleteItem(items[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showSuccess();
            finish();
        }
    }

    private void showDeleteDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_utilization_title))
                .setMessage(getString(R.string.delete_utilization_message))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new DeleteItemTask().execute(selectedItem);
                    }
                }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).create();

        dialog.show();
    }


}
