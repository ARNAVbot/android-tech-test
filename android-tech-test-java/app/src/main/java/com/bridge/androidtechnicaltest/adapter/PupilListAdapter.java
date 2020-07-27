package com.bridge.androidtechnicaltest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bridge.androidtechnicaltest.db.Pupil;
import com.bridge.androidtechnicaltest.viewHolders.PupilListVH;
import com.bridge.androidtechnicaltest.views.DataProvider;
import com.bridge.androidtechnicaltest.views.EventHandler;

/**
 * Adapter to render the pupil list on pupil list screen
 */
public class PupilListAdapter extends RecyclerView.Adapter<PupilListVH> {

    private final DataProvider<Pupil> dataProvider;
    private final EventHandler<Pupil> eventHandler;
    private final Context context;

    // Whenever, we are on an item which is 3rd from the last,
    // a call has to be made to fetch more Pupils.
    // This threshold signifies the same thing.
    private final int THRESHOLD = 3;

    public PupilListAdapter(DataProvider<Pupil> dataProvider, EventHandler<Pupil> eventHandler, Context context) {
        this.dataProvider = dataProvider;
        this.eventHandler = eventHandler;
        this.context = context;
    }

    @NonNull
    @Override
    public PupilListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PupilListVH(com.bridge.androidtechnicaltest.databinding.PupilListVhBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PupilListVH holder, int position) {
        holder.bind(dataProvider.get(position), context, eventHandler, position);
        if (dataProvider.getCount() - position <= THRESHOLD) {
            // Make the call to load more pupils
            eventHandler.loadMore();
        }
    }

    @Override
    public int getItemCount() {
        return dataProvider.getCount();
    }
}
