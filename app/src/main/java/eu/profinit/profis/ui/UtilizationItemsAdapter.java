package eu.profinit.profis.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import eu.profinit.profis.R;
import eu.profinit.profis.model.UtilizationItem;

public class UtilizationItemsAdapter extends RecyclerView.Adapter<UtilizationItemsAdapter.ViewHolder> {

    private List<UtilizationItem> items;

    public interface ItemClickListener {
        void onItemClicked(UtilizationItem item);
    }

    private ItemClickListener listener;

    public UtilizationItemsAdapter(List<UtilizationItem> items, ItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_utilization, parent, false);
        return new ViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private TextView contract;
        private TextView hours;
        private TextView note;
        private View view;

        private Context ctx;

        ViewHolder(View itemView, Context context) {
            super(itemView);
            view = itemView;
            date = itemView.findViewById(R.id.date);
            contract = itemView.findViewById(R.id.contract);
            hours = itemView.findViewById(R.id.hours);
            note = itemView.findViewById(R.id.note);
            ctx = context;

        }

        void bind(final UtilizationItem item) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MM. yy", Locale.getDefault());
            date.setText(dateFormat.format(item.getDate()));
            contract.setText(item.getContract());
            hours.setText(ctx.getString(R.string.hours, item.getHours()));
            note.setText(item.getNote());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(item);
                }
            });
        }

    }
}
