package eu.profinit.profis.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.app.TaskStackBuilder;
import eu.profinit.profis.R;
import eu.profinit.profis.db.ProfisDatabase;
import eu.profinit.profis.model.UtilizationItem;

public class UtilizationCreateActivity extends AppCompatActivity {

    private EditText contract;
    private EditText hours;
    private EditText note;
    private TextView date;
    private Button dateBtn;
    private Date selectedDate;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilization_create);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(getString(R.string.utilization_create_title));
            ab.setDisplayHomeAsUpEnabled(true);
        }

        contract = findViewById(R.id.contract);
        note = findViewById(R.id.note);
        hours = findViewById(R.id.hours);
        date = findViewById(R.id.date);
        dateBtn = findViewById(R.id.date_btn);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });

        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MM. yyyy", Locale.getDefault());
        final Calendar calendar = Calendar.getInstance();
        date.setText(dateFormat.format(calendar.getTime()));
        selectedDate = calendar.getTime();

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date.setText(dateFormat.format(newDate.getTime()));
                selectedDate = newDate.getTime();
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent) || isTaskRoot()) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }

                return true;
        }

        return false;
    }

    private void saveItem() {
        UtilizationItem item = new UtilizationItem();
        item.setDate(selectedDate);
        item.setNote(note.getText().toString());
        item.setContract(contract.getText().toString());
        item.setHours(Integer.valueOf(hours.getText().toString()));
        new InsertUtilization().execute(item);
    }

    private void insertItem(UtilizationItem item) {
        ProfisDatabase.getInstance(this).utilizationDao().insert(item);
    }

    private void showSuccess() {
        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
    }

    private class InsertUtilization extends AsyncTask<UtilizationItem, Void, Void> {

        @Override
        protected Void doInBackground(UtilizationItem... items) {
            insertItem(items[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showSuccess();
            finish();
        }
    }

}
