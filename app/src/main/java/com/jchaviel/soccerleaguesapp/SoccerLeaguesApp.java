package com.jchaviel.soccerleaguesapp;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.firebase.client.Firebase;
import com.jchaviel.soccerleaguesapp.domain.di.DomainModule;
import com.jchaviel.soccerleaguesapp.lib.di.LibsModule;
import com.jchaviel.soccerleaguesapp.login.di.DaggerLoginComponent;
import com.jchaviel.soccerleaguesapp.login.di.LoginComponent;
import com.jchaviel.soccerleaguesapp.login.di.LoginModule;
import com.jchaviel.soccerleaguesapp.login.ui.LoginView;
import com.jchaviel.soccerleaguesapp.main.di.DaggerMainComponent;
import com.jchaviel.soccerleaguesapp.main.di.MainComponent;
import com.jchaviel.soccerleaguesapp.main.di.MainModule;
import com.jchaviel.soccerleaguesapp.main.ui.MainView;
import com.jchaviel.soccerleaguesapp.news.di.DaggerNewsComponent;
import com.jchaviel.soccerleaguesapp.news.di.NewsComponent;
import com.jchaviel.soccerleaguesapp.news.di.NewsModule;
import com.jchaviel.soccerleaguesapp.news.ui.NewsFragment;
import com.jchaviel.soccerleaguesapp.news.ui.NewsView;
import com.jchaviel.soccerleaguesapp.news.ui.adapter.OnItemClickListener;
import com.jchaviel.soccerleaguesapp.schedule.di.DaggerScheduleComponent;
import com.jchaviel.soccerleaguesapp.schedule.di.ScheduleComponent;
import com.jchaviel.soccerleaguesapp.schedule.di.ScheduleModule;
import com.jchaviel.soccerleaguesapp.schedule.ui.ScheduleFragment;
import com.jchaviel.soccerleaguesapp.schedule.ui.ScheduleView;

/**
 * Created by jchavielreyes On 12/07/2016.
 */
public class SoccerLeaguesApp extends Application {

    private final static String SHARED_PREFERENCES_NAME = "UserPrefs";
    private final static String FIREBASE_URL = "https://android-share-photo.firebaseio.com";

    private DomainModule domainModule;
    private SoccerLeaguesAppModule soccerLeaguesAppModule;

    @Override
    public void onCreate() {
        super.onCreate();
        initFirebase();
        initModules();
    }

    private void initModules() {
        soccerLeaguesAppModule = new SoccerLeaguesAppModule(this);
        domainModule = new DomainModule(FIREBASE_URL);
    }

    private void initFirebase() {
        Firebase.setAndroidContext(this);
    }

    public String getSharedPreferencesName() {
        return SHARED_PREFERENCES_NAME;
    }

    public LoginComponent getLoginComponent(LoginView view){
        return DaggerLoginComponent
                .builder()
                .soccerLeaguesAppModule(soccerLeaguesAppModule)
                .domainModule(domainModule)
                .libsModule(new LibsModule(null))
                .loginModule(new LoginModule(view))
                .build();
    }

    public MainComponent getMainComponent(MainView view, FragmentManager manager, Fragment[] fragments, String[] titles){
        return DaggerMainComponent
                .builder()
                .soccerLeaguesAppModule(soccerLeaguesAppModule)
                .domainModule(domainModule)
                .libsModule(new LibsModule(null))
                .mainModule(new MainModule(view, titles, fragments, manager))
                .build();
    }

    public NewsComponent getNewsComponent(NewsFragment fragment, NewsView view, OnItemClickListener onItemClickListener){
        return DaggerNewsComponent
                .builder()
                .soccerLeaguesAppModule(soccerLeaguesAppModule)
                .domainModule(domainModule)
                .libsModule(new LibsModule(fragment))
                .newsModule(new NewsModule(view, onItemClickListener))
                .build();
    }

    public ScheduleComponent getScheduleComponent(ScheduleFragment fragment, ScheduleView view){
        return DaggerScheduleComponent
                .builder()
                .soccerLeaguesAppModule(soccerLeaguesAppModule)
                .domainModule(domainModule)
                .libsModule(new LibsModule(fragment))
                .scheduleModule(new ScheduleModule(view))
                .build();
    }

}