package com.bridge.androidtechnicaltest.ui;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bridge.androidtechnicaltest.R;
import com.bridge.androidtechnicaltest.databinding.FragmentPupildetailBinding;
import com.bridge.androidtechnicaltest.db.Pupil;
import com.bridge.androidtechnicaltest.viewModel.PupilViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

/**
 * Pupil Detail fragment which shows the detail of the pupil.
 * Also,has an option to delete the pupil
 */

public class PupilDetailFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = PupilListFragment.class.getCanonicalName();


    private FragmentPupildetailBinding binding;
    private Long pupilId;
    private ProgressDialog progressDialog;

    private PupilViewModel pupilViewModel;
    private Observer<Pupil> observer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPupildetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    public void hideProgressDialog() {
        if ((progressDialog != null) && (progressDialog.isShowing())) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.acbDelete.setOnClickListener(this);
        pupilId = PupilDetailFragmentArgs.fromBundle(getArguments()).getPupilId();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pupilViewModel = ViewModelProviders.of(getActivity()).get(PupilViewModel.class);
        showProgressDialog();
        observer = new Observer<Pupil>() {
            @Override
            public void onChanged(Pupil pupil) {
                hideProgressDialog();
                if (pupil == null) {
                    Snackbar.make(binding.getRoot(),
                            R.string.pupil_deleted, Snackbar.LENGTH_SHORT).show();
                    binding.pupilId.setText("");
                    binding.country.setText("");
                    binding.pupilName.setText("");
                    Glide.with(getContext()).load(R.drawable.ic_baseline_error_24).
                            into(binding.pupilImage);
                    binding.acbDelete.setVisibility(View.GONE);
                } else {
                    if (pupil.isToBeDeleted()) {
                        binding.acbDelete.setVisibility(View.GONE);
                        binding.actvDeletionStatus.setVisibility(View.VISIBLE);
                    } else {
                        binding.acbDelete.setVisibility(View.VISIBLE);
                        binding.actvDeletionStatus.setVisibility(View.GONE);
                    }
                    binding.pupilId.setText(String.valueOf(pupilId));
                    binding.country.setText(TextUtils.isEmpty(pupil.getValue()) ? "" : pupil.getValue());
                    binding.pupilName.setText(TextUtils.isEmpty(pupil.getName()) ? "" : pupil.getName());
                    Glide.with(getContext()).load(Uri.parse(pupil.getImage())).
                            into(binding.pupilImage);
                }
            }
        };
        makeCall();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pupilViewModel.getPupilById(pupilId).removeObserver(observer);
    }

    private void makeCall() {
        pupilViewModel.getPupilById(pupilId).observe(getViewLifecycleOwner(), observer);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.acb_delete) {
            // delete the current pupil
            pupilViewModel.deletePupilId(pupilId);
        }
    }
}