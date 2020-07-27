package com.bridge.androidtechnicaltest.daoThreads;

import com.bridge.androidtechnicaltest.db.Pupil;
import com.bridge.androidtechnicaltest.db.PupilDao;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Thread to save an entire list into the local DB
 */
public class SaveListThread implements Runnable {

    PupilDao pupilDaoThread;
    int pageNumber;
    Pupil[] pupils;

    public SaveListThread(int pageNumber, Pupil[] pupils, PupilDao pupilDao) {
        this.pageNumber = pageNumber;
        this.pupils = pupils;
        this.pupilDaoThread = pupilDao;
    }

    @Override
    public void run() {
        if (pageNumber == 1) {
            // Empty the entire database
            pupilDaoThread.deleteAll();
            // Add just the above elements
            pupilDaoThread.insert(pupils);
        } else {
            // Fetch all list
            List<Pupil> pupilList = pupilDaoThread.getAllPupils();
            // Replace pupils that belong to this page number with the pupils that we just received
            int startIndex = (pageNumber - 1) *5;
            int endIndex = startIndex + 5;
            List<Pupil> finalPupilList = new LinkedList<>();
            for (int i = 0; i < pupilList.size(); i++) {
                if (i == startIndex) {
                    break;
                }
                finalPupilList.add(pupilList.get(i));
            }
            finalPupilList.addAll(Arrays.asList(pupils));
            for (int i = endIndex; i < pupilList.size(); i++) {
                finalPupilList.add(pupilList.get(i));
            }

            // Delete all list
            pupilDaoThread.deleteAll();
            // Insert back the updated list
            Pupil[] pupilsArray = new Pupil[finalPupilList.size()];
            pupilDaoThread.insert(finalPupilList.toArray(pupilsArray));
        }
    }
}