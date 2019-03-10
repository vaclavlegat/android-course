package eu.profinit.profis.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import eu.profinit.profis.R;
import eu.profinit.profis.db.ProfisDatabase;
import eu.profinit.profis.model.UtilizationItem;

import static eu.profinit.profis.ui.UtilizationDetailActivity.ID_KEY;

public class UtilizationDetailFragment extends Fragment {

    private TextView contract;
    private TextView hours;
    private TextView date;
    private TextView note;
    private Button deleteBtn;

    private UtilizationItem selectedItem;

    public UtilizationDetailFragment() {
        // Required empty public constructor
    }

    private long id;

    public static UtilizationDetailFragment newInstance(long itemId) {
        UtilizationDetailFragment fragment = new UtilizationDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ID_KEY, itemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getLong(ID_KEY);
        }

        if (savedInstanceState != null) {
            id = savedInstanceState.getLong(ID_KEY, 0);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(ID_KEY, id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_utilization_detail, container, false);
        contract = view.findViewById(R.id.contract);
        hours = view.findViewById(R.id.hours);
        date = view.findViewById(R.id.date);
        note = view.findViewById(R.id.note);
        deleteBtn = view.findViewById(R.id.delete_btn);

        new LoadItemTask().execute(id);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });
        return view;
    }

    private UtilizationItem loadItem(long id) {
        return ProfisDatabase.getInstance(getContext()).utilizationDao().getById(id);
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
        Toast.makeText(getContext(), R.string.deleted, Toast.LENGTH_SHORT).show();
    }

    private void deleteItem(UtilizationItem item) {
        ProfisDatabase.getInstance(getContext()).utilizationDao().delete(item);
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
            getActivity().finish();
        }
    }

    private void showDeleteDialog() {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
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
