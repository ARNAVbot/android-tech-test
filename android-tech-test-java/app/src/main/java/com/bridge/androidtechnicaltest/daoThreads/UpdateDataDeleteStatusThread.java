package com.bridge.androidtechnicaltest.daoThreads;

import com.bridge.androidtechnicaltest.db.Pupil;
import com.bridge.androidtechnicaltest.db.PupilDao;

/**
 * Thread to update the status of the thread
 */
public class UpdateDataDeleteStatusThread implements Runnable {

    Long pupilId;
    PupilDao pupilDao;
    boolean toBeDeleted;

    public UpdateDataDeleteStatusThread(Long pupilId, PupilDao pupilDao, boolean toBeDeleted) {
        this.pupilId = pupilId;
        this.pupilDao = pupilDao;
        this.toBeDeleted = toBeDeleted;
    }

    @Override
    public void run() {
        Pupil pupil = pupilDao.getSimplePupilById(pupilId);
        pupil.setToBeDeleted(toBeDeleted);
        pupilDao.update(pupil);
    }
}