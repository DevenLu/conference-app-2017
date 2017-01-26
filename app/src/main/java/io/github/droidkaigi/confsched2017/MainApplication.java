package io.github.droidkaigi.confsched2017;

import com.google.firebase.crash.FirebaseCrash;

import com.squareup.leakcanary.LeakCanary;

import android.app.Application;
import android.support.annotation.NonNull;

import io.github.droidkaigi.confsched2017.di.AndroidModule;
import io.github.droidkaigi.confsched2017.di.AppComponent;
import io.github.droidkaigi.confsched2017.di.AppModule;
import io.github.droidkaigi.confsched2017.di.DaggerAppComponent;
import io.github.droidkaigi.confsched2017.log.CrashLogTree;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MainApplication extends Application {

    AppComponent appComponent;

    @NonNull
    public AppComponent getComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .androidModule(new AndroidModule(this))
                .build();

        initCalligraphy();
        initLeakCanary();

        Timber.plant(new CrashLogTree());
        FirebaseCrash.log("Build info: branch = " + BuildConfig.GIT_BRANCH + ", sha = " + BuildConfig.GIT_SHA); // any equivalent method to crashlytics keys?
    }

    private void initCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.font_noto_cjk_medium))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
