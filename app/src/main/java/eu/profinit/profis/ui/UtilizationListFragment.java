package eu.profinit.profis.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import eu.profinit.profis.R;
import eu.profinit.profis.db.ProfisDatabase;
import eu.profinit.profis.model.UtilizationItem;

public class UtilizationListFragment extends Fragment {

    private RecyclerView utilizationItems;
    boolean isMutliPane;

    public UtilizationListFragment() {
        // Required empty public constructor
    }

    public static UtilizationListFragment newInstance() {
        UtilizationListFragment fragment = new UtilizationListFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_utilization_list, container, false);

        View detailsFrame = getActivity().findViewById(R.id.detail_placeholder);
        isMutliPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        utilizationItems = view.findViewById(R.id.utilization_items);
        utilizationItems.setLayoutManager(new LinearLayoutManager(getContext()));
        utilizationItems.setHasFixedSize(true);
        utilizationItems.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadUtilization().execute();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void openDetail(UtilizationItem item) {
        if (isMutliPane) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.detail_placeholder, UtilizationDetailFragment.newInstance(item.getId()))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        } else {
            UtilizationDetailActivity.start(getContext(), item.getId());
        }

    }

    private List<UtilizationItem> loadItems() {
        return ProfisDatabase.getInstance(getContext()).utilizationDao().getAll();
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
