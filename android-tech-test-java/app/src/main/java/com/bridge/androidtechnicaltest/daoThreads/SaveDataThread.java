package com.bridge.androidtechnicaltest.daoThreads;

import com.bridge.androidtechnicaltest.db.Pupil;
import com.bridge.androidtechnicaltest.db.PupilDao;

/**
 * Thread to save data into the local DB
 */
public class SaveDataThread implements Runnable {

    PupilDao pupilDaoThread;
    Pupil pupil;

    public SaveDataThread(Pupil pupil, PupilDao pupilDao) {
        this.pupil = pupil;
        this.pupilDaoThread = pupilDao;
    }

    @Override
    public void run() {
        pupilDaoThread.update(pupil);
    }
}
