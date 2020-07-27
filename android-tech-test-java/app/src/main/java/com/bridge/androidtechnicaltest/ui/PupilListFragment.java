package com.bridge.androidtechnicaltest.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bridge.androidtechnicaltest.R;
import com.bridge.androidtechnicaltest.adapter.PupilListAdapter;
import com.bridge.androidtechnicaltest.databinding.FragmentPupillistBinding;
import com.bridge.androidtechnicaltest.db.Pupil;
import com.bridge.androidtechnicaltest.viewModel.PupilViewModel;
import com.bridge.androidtechnicaltest.views.DataProvider;
import com.bridge.androidtechnicaltest.views.EventHandler;

import java.util.LinkedList;
import java.util.List;

import static android.widget.LinearLayout.VERTICAL;

/**
 * Fragment to show the list of pupils
 */
public class PupilListFragment extends Fragment implements DataProvider<Pupil>, EventHandler<Pupil> {

    private static final String TAG = PupilListFragment.class.getCanonicalName();

    private FragmentPupillistBinding binding;
    private NavController navController;

    private List<Pupil> pupilList;
    private PupilListAdapter adapter;

    private Observer<List<Pupil>> observer;
    private PupilViewModel pupilViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPupillistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        //Setting the recycler view with the adapter
        pupilList = new LinkedList<Pupil>();
        binding.pupilListRv.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));
        adapter = new PupilListAdapter(this, this, view.getContext());
        binding.pupilListRv.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pupilViewModel = ViewModelProviders.of(getActivity()).get(PupilViewModel.class);
        observer = new Observer<List<Pupil>>() {
            @Override
            public void onChanged(List<Pupil> pupils) {
                if (pupilViewModel.currentPageNumber == 1) {
                    pupilList = new LinkedList<>();
                }
                pupilList.addAll(pupils);
                adapter.notifyDataSetChanged();
            }
        };
        makeCall();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pupilViewModel.getPupilList().removeObserver(observer);
    }

    private void makeCall() {
        if (pupilViewModel.currentPageNumber >= pupilViewModel.TOTAL_PAGES && pupilViewModel.TOTAL_PAGES != 0) {
            return;
        }
        pupilViewModel.getPupilList().observe(getViewLifecycleOwner(), observer);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Pupil get(int position) {
        return pupilList.get(position);
    }

    @Override
    public int getCount() {
        return pupilList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int indexOf(Pupil item) {
        return pupilList.indexOf(item);
    }

    @Override
    public boolean contains(Pupil item) {
        return pupilList.contains(item);
    }

    @Override
    public void remove(int position) {

    }

    @Override
    public boolean isChecked(int position) {
        return false;
    }

    @Override
    public void onViewClicked(View view, Pupil item, int position) {
        PupilListFragmentDirections.ActionPupilListFragmentToPupilDetailFragment action =
                PupilListFragmentDirections.actionPupilListFragmentToPupilDetailFragment(item.getPupilId());
        navController.navigate(action);
    }

    @Override
    public void loadMore() {
        makeCall();
    }
}
