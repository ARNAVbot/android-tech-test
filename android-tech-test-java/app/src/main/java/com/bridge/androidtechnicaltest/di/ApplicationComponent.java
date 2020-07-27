package com.bridge.androidtechnicaltest.di;


import com.bridge.androidtechnicaltest.DeleteDataService;
import com.bridge.androidtechnicaltest.viewModel.PupilViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {
        ApplicationModule.class,
        NetworkModule.class,
        DatabaseModule.class,
})
@Singleton
public abstract class ApplicationComponent {
    public abstract void inject(PupilViewModel pupilViewModel);

    public abstract void inject(DeleteDataService deleteDataService);
}
