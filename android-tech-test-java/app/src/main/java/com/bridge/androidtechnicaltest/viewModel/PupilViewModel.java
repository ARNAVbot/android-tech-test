package com.bridge.androidtechnicaltest.viewModel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bridge.androidtechnicaltest.App;
import com.bridge.androidtechnicaltest.daoThreads.DeleteDataThread;
import com.bridge.androidtechnicaltest.daoThreads.SaveDataThread;
import com.bridge.androidtechnicaltest.daoThreads.SaveListThread;
import com.bridge.androidtechnicaltest.daoThreads.UpdateDataDeleteStatusThread;
import com.bridge.androidtechnicaltest.db.Pupil;
import com.bridge.androidtechnicaltest.db.PupilDao;
import com.bridge.androidtechnicaltest.network.PagedResponseWrapper;
import com.bridge.androidtechnicaltest.network.PupilService;
import com.bridge.androidtechnicaltest.utils.NetworkUtil;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * View model that communicates with the list and detail fragment.
 * Both are attached to the same view Model by passing in the activity reference.
 */
public class PupilViewModel extends AndroidViewModel {

    @Inject
    PupilService pupilService;
    @Inject
    PupilDao pupilDao;

    public int TOTAL_PAGES = 0;
    public int currentPageNumber = 0;
    private Context context;

    private Disposable pupilListDisposable, pupilDetailDisposable;
    private static final String TAG = PupilViewModel.class.getCanonicalName();
    private boolean isLoading = false;

    public PupilViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        ((App) application).getApplicationComponent().inject(this);
    }

    //Make call to load pupil from Backend
    private void getPupilFromBE(Long pupilId) {
        if (pupilDetailDisposable != null)
            pupilDetailDisposable.dispose();
        pupilDetailDisposable = pupilService.getPupilById(pupilId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Pupil>() {
                    @Override
                    public void accept(Pupil pupil) throws Exception {
                        // Save the updated list into local DB
                        updatePupilDataToDb(pupil);
                        Log.d(TAG, "Call completed");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "Call failed");
                    }
                });
    }

    // MAke call to Backend the delete the pupil with the specified pupil Id
    private void deletePupil(final Long pupilId) {
        pupilService.deletePupilById(pupilId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Call completed");
                        // Remove this pupil from local DB
                        deletePupilDao(pupilId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Call failed");
                        // Since, the pupil could not be deleted,
                        // hence remove it from deleting queue.
                        Thread t = new Thread(new UpdateDataDeleteStatusThread(pupilId, pupilDao, false));
                        t.start();
                    }
                });
    }

    // make call to backend to get pupil list
    private void getPupilListFromBE(final int pageNumber) {
        if (currentPageNumber >= TOTAL_PAGES && TOTAL_PAGES != 0) {
            // no more pages to load
            return;
        }
        if (isLoading) {
            // currently , a page is still loading. Hence, dont load anything
            return;
        }
        isLoading = true;
        if (pupilListDisposable != null)
            pupilListDisposable.dispose();
        pupilListDisposable = pupilService.getPupils(pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PagedResponseWrapper<Pupil>>() {
                    @Override
                    public void accept(PagedResponseWrapper<Pupil> pupilPagedResponseWrapper) throws Exception {
                        currentPageNumber = pupilPagedResponseWrapper.getPageNumber();
                        isLoading = false;
                        Pupil[] pupilsArray = new Pupil[pupilPagedResponseWrapper.getItems().size()];
                        // udpate the list to local DB
                        addPupilListDataToDb(pupilPagedResponseWrapper.getItems().toArray(pupilsArray), pageNumber);
                        TOTAL_PAGES = pupilPagedResponseWrapper.getTotalPages();
                        Log.d(TAG, "Call completed");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "Call failed");
                        isLoading = false;
                    }
                });
    }

    private void deletePupilDao(Long pupilId) {
        Thread t = new Thread(new DeleteDataThread(pupilId, pupilDao));
        t.start();
    }

    private void updatePupilDataToDb(Pupil pupil) {
        Thread t = new Thread(new SaveDataThread(pupil, pupilDao));
        t.start();
    }

    private void addPupilListDataToDb(Pupil[] pupils, int pageNumber) {
        Thread t = new Thread(new SaveListThread(pageNumber, pupils, pupilDao));
        t.start();
    }

    public LiveData<List<Pupil>> getPupilList() {
        getPupilListFromBE(currentPageNumber + 1);
        return pupilDao.getPupils(currentPageNumber - 1);
    }

    public LiveData<Pupil> getPupilById(Long pupilId) {
        getPupilFromBE(pupilId);
        return pupilDao.getPupilById(pupilId);
    }

    public void deletePupilId(Long pupilId) {
        if (NetworkUtil.getConnectionStatus(context) == NetworkUtil.NOT_CONNECTED) {
            // No connection. Save it with toBeDeleted flag set On.
            // Whenever network comes, check for all the pupils with toBeDeleted flag set ON
            // and then delete them
            Thread t = new Thread(new UpdateDataDeleteStatusThread(pupilId, pupilDao, true));
            t.start();
        } else {
            // connection is there. Delete the id
            deletePupil(pupilId);
        }
    }
}