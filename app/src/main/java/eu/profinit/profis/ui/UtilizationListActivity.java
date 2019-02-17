package eu.profinit.profis.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import eu.profinit.profis.R;
import eu.profinit.profis.model.UtilizationItem;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UtilizationListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilization_list);

        RecyclerView utilizationItems = findViewById(R.id.utilization_items);
        utilizationItems.setLayoutManager(new LinearLayoutManager(this));
        utilizationItems.setHasFixedSize(true);
        utilizationItems.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        List<UtilizationItem> items = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            UtilizationItem item = new UtilizationItem();
            item.setContract("Česká spořitelna");
            item.setDate(new Date());
            item.setHours(8);
            item.setNote("CR-1234, CR-1235, CR-1236, CR-1237, CR-1238, CR-1239");
            items.add(item);
        }

        UtilizationItemsAdapter adapter = new UtilizationItemsAdapter(items, new UtilizationItemsAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(UtilizationItem item) {
                openDetail(item);
            }
        });
        adapter.notifyDataSetChanged();
        utilizationItems.setAdapter(adapter);

    }

    private void openDetail(UtilizationItem item) {
        UtilizationDetailActivity.start(this, item);
    }

}
