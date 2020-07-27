package com.bridge.androidtechnicaltest.viewHolders;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.bridge.androidtechnicaltest.databinding.PupilListVhBinding;
import com.bridge.androidtechnicaltest.db.Pupil;
import com.bridge.androidtechnicaltest.views.EventHandler;
import com.bumptech.glide.Glide;

/**
 * View holder to show pupil item on pupil list screen
 */
public class PupilListVH extends RecyclerView.ViewHolder {

    PupilListVhBinding binding;

    public PupilListVH(PupilListVhBinding pupilListVhBinding) {
        super(pupilListVhBinding.getRoot());
        binding = pupilListVhBinding;
    }

    public void bind(final Pupil pupil, Context context, final EventHandler<Pupil> eventHandler, final int position) {

        binding.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventHandler.onViewClicked(v, pupil, position);
            }
        });
        Glide.with(context).load(Uri.parse(pupil.getImage())).into(binding.acivLogo);
        if (pupil.isToBeDeleted()) {
            binding.actvDeletionStatus.setVisibility(View.VISIBLE);
        } else {
            binding.actvDeletionStatus.setVisibility(View.GONE);
        }
        binding.actvName.setText(TextUtils.isEmpty(pupil.getName()) ? "" : pupil.getName());
        binding.actvCountry.setText(TextUtils.isEmpty(pupil.getValue()) ? "" : pupil.getValue());
        String latLng = "(".concat(pupil.getLatitude() != null ? pupil.getLatitude().toString() : "").concat(",")
                .concat(pupil.getLongitude() != null ? pupil.getLongitude().toString() : "").concat(")");

        binding.actvLatLon.setText(latLng);
    }
}