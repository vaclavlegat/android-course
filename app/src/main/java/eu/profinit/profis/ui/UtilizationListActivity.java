package eu.profinit.profis.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import eu.profinit.profis.R;
import eu.profinit.profis.db.ProfisDatabase;
import eu.profinit.profis.model.UtilizationItem;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UtilizationListActivity extends AppCompatActivity {

    private RecyclerView utilizationItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilization_list);

        utilizationItems = findViewById(R.id.utilization_items);
        utilizationItems.setLayoutManager(new LinearLayoutManager(this));
        utilizationItems.setHasFixedSize(true);
        utilizationItems.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreate();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadUtilization().execute();
    }

    private void openDetail(UtilizationItem item) {
        UtilizationDetailActivity.start(this, item.getId());
    }

    private void openCreate() {
        Intent intent = new Intent(this, UtilizationCreateActivity.class);
        startActivity(intent);
    }

    private List<UtilizationItem> loadItems() {
        return ProfisDatabase.getInstance(this).utilizationDao().getAll();
    }

    private class LoadUtilization extends AsyncTask<Void, Void, List<UtilizationItem>> {

        @Override
        protected List<UtilizationItem> doInBackground(Void... voids) {
            return loadItems();
        }

        @Override
        protected void onPostExecute(List<UtilizationItem> items) {
            super.onPostExecute(items);
            UtilizationItemsAdapter adapter = new UtilizationItemsAdapter(items, new UtilizationItemsAdapter.ItemClickListener() {
                @Override
                public void onItemClicked(UtilizationItem item) {
                    openDetail(item);
                }
            });
            adapter.notifyDataSetChanged();
            utilizationItems.setAdapter(adapter);
        }
    }


}
