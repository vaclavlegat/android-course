package eu.profinit.profis.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import eu.profinit.profis.R;
import eu.profinit.profis.model.UtilizationItem;

public class UtilizationDetailActivity extends AppCompatActivity {

    private static final String CONTRACT_KEY = "CONTRACT_KEY";
    private static final String HOURS_KEY = "HOURS_KEY";
    private static final String DATE_KEY = "DATE_KEY";
    private static final String NOTE_KEY = "NOTE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilization_detail);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(R.string.utilization_detail_title);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        TextView contract = findViewById(R.id.contract);
        TextView hours = findViewById(R.id.hours);
        TextView date = findViewById(R.id.date);
        TextView note = findViewById(R.id.note);

        contract.setText(getIntent().getStringExtra(CONTRACT_KEY));
        hours.setText(String.valueOf(getIntent().getIntExtra(HOURS_KEY, 0)));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MM. yyyy", Locale.getDefault());
        date.setText(dateFormat.format(getIntent().getSerializableExtra(DATE_KEY)));
        note.setText(getIntent().getStringExtra(NOTE_KEY));

    }

    public static void start(Context context, UtilizationItem item) {
        Intent intent = new Intent(context, UtilizationDetailActivity.class);
        intent.putExtra(CONTRACT_KEY, item.getContract());
        intent.putExtra(HOURS_KEY, item.getHours());
        intent.putExtra(DATE_KEY, item.getDate());
        intent.putExtra(NOTE_KEY, item.getNote());
        context.startActivity(intent);
    }

}
