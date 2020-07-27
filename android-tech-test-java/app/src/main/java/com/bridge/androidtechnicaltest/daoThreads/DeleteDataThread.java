package com.bridge.androidtechnicaltest.daoThreads;

import com.bridge.androidtechnicaltest.db.PupilDao;

/**
  * Thread to delete data from local DB
  */
public class DeleteDataThread implements Runnable {

            Long pupilId;
    PupilDao pupilDaoThread;

            public DeleteDataThread(Long pupilId, PupilDao pupilDao) {
                this.pupilId = pupilId;
                this.pupilDaoThread = pupilDao;
            }

            @Override
    public void run() {
                pupilDaoThread.deletePupil(pupilId);
            }
}