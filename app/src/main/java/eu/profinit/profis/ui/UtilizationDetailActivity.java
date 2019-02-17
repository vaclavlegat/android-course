package eu.profinit.profis.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
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
        }
    }


}
