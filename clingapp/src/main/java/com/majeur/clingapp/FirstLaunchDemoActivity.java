package com.majeur.clingapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.SlidingDrawer;

import com.majeur.cling.ActionItemTarget;
import com.majeur.cling.Cling;
import com.majeur.cling.ClingManager;
import com.majeur.cling.ViewTarget;

public class FirstLaunchDemoActivity extends Activity {

    private static final String START_TUTORIAL_KEY = "show_tutorial";

    private SharedPreferences mSharedPreferences;
    private ClingManager mClingManager;

    private SlidingDrawer mSlidingDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstlaunch_demo);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mClingManager = new ClingManager(this);

        mSlidingDrawer = (SlidingDrawer) findViewById(R.id.drawer);

        findViewById(R.id.clear_preference).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSharedPreferences.edit()
                        .remove(START_TUTORIAL_KEY)
                        .apply();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mSharedPreferences.getBoolean(START_TUTORIAL_KEY, true)
                && (mClingManager == null || !mClingManager.isStarted())) {

            mClingManager = new ClingManager(this);

            // When no Target is set, Target.NONE is used
            mClingManager.addCling(new Cling.Builder(this)
                    .setTitle("Welcome to this app")
                    .setContent("This application is meant to be the best app you will ever try on android.")
                    .build());

            mClingManager.addCling(new Cling.Builder(this)
                    .setTitle(R.string.drawer_tutorial_title)
                    .setContent(R.string.drawer_tutorial_message)
                    .setMessageBackground(getResources().getColor(R.color.teal))
                    .setTarget(new ViewTarget(this, R.id.button_container))
                    .build());

            mClingManager.addCling(new Cling.Builder(this)
                    .setTitle("Content sharing")
                    .setContent("You can share the content with your friends by clicking here.")
                    .setTarget(new ActionItemTarget(this, R.id.action_share))
                    .build());

            mClingManager.addCling(new Cling.Builder(this)
                    .setTitle("Here is the drawer")
                    .setTitleTextAppearance(R.style.textAppearanceTitleNice)
                    .setContent("You can access this icon by swipping the handle to the top of the screen.")
                    .setContentTextAppearance(R.style.textAppearanceContentNice)
                    .setTarget(new ViewTarget(this, R.id.drawer_item))
                    .setClingColor(getResources().getColor(R.color.cling))
                    .setMessageBackground(getResources().getColor(R.color.red))
                    .build());

            mClingManager.setCallbacks(new ClingManager.Callbacks() {

                @Override
                public void onClingHide(int position) {
                    switch (position) {
                        // Open the drawer for the next cling.
                        case 2:
                            mSlidingDrawer.animateOpen();
                            break;

                        // Last Cling has been shown, tutorial is ended.
                        case 3:
                            mSharedPreferences.edit()
                                    .putBoolean(START_TUTORIAL_KEY, false)
                                    .apply();

                            mClingManager = null;

                            mSlidingDrawer.animateClose();
                            break;

                    }
                }
            });

            mClingManager.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.demo, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
