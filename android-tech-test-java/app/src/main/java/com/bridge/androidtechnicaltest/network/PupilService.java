package com.bridge.androidtechnicaltest.network;

import com.bridge.androidtechnicaltest.db.Pupil;
import com.bridge.androidtechnicaltest.db.PupilList;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PupilService {
    @POST("reset")
    Completable reset();

    @GET("pupils")
    Observable<PagedResponseWrapper<Pupil>> getPupils(@Query("page") Integer page);

    @GET("pupils/{pupilId}")
    Observable<Pupil> getPupilById(@Path("pupilId") Long pupilId);

    @DELETE("pupils/{pupilId}")
    Completable deletePupilById(@Path("pupilId") Long pupilId);
}
