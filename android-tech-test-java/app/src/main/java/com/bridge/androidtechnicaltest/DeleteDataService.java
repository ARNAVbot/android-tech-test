package com.bridge.androidtechnicaltest;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.bridge.androidtechnicaltest.daoThreads.DeleteDataThread;
import com.bridge.androidtechnicaltest.daoThreads.UpdateDataDeleteStatusThread;
import com.bridge.androidtechnicaltest.db.Pupil;
import com.bridge.androidtechnicaltest.db.PupilDao;
import com.bridge.androidtechnicaltest.network.PupilService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Service to delete pending pupils from BackEnd whenever internet connection comes back
 */
public class DeleteDataService extends IntentService {

    @Inject
    PupilDao pupilDao;
    @Inject
    PupilService pupilService;

    private static final String TAG = DeleteDataService.class.getCanonicalName();

    public DeleteDataService() {
        super(TAG);
    }

    public DeleteDataService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ((App) getApplication()).getApplicationComponent().inject(this);
        List<Pupil> pupilToBeDeleted = pupilDao.getPupilsToBeDeleted(true);
        if (pupilToBeDeleted == null || pupilToBeDeleted.size() == 0) {
            // If there are no pupils to be deleted, then return
            return;
        }
        for (final Pupil pupil : pupilToBeDeleted) {
            pupilService.deletePupilById(pupil.getPupilId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "Call completed");
                            // Remove these pupils from local DB as well
                            Thread t = new Thread(new DeleteDataThread(pupil.getPupilId(), pupilDao));
                            t.start();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "Call failed");
                            // Since, the pupils have not been deleted. Hence, reset the toBeDeleted flag to true
                            Thread t = new Thread(new UpdateDataDeleteStatusThread(pupil.getPupilId(), pupilDao, false));
                            t.start();
                        }
                    });
        }
    }
}