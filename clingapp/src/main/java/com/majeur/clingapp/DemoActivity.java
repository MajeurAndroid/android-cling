package com.majeur.clingapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.majeur.cling.ActionItemTarget;
import com.majeur.cling.Cling;
import com.majeur.cling.ClingManager;
import com.majeur.cling.ViewTarget;

public class DemoActivity extends Activity implements View.OnClickListener {

    private ClingManager mClingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        mClingManager = new ClingManager(this);

        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.button_container);
        for (int i = 0; i < viewGroup.getChildCount(); i++)
            viewGroup.getChildAt(i).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.demo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                showClings(getNoTargetCling());
                break;

            case R.id.btn_2:
                showClings(getViewTargetCling(view));
                break;

            case R.id.btn_3:
                showClings(getActionViewTargetCling());
                break;

            case R.id.btn_4:
                showClings(getNoTargetCling(),
                        getViewTargetCling(view),
                        getActionViewTargetCling());
                break;
        }
    }

    private void showClings(Cling... clings) {
        mClingManager.stop();
        for (Cling cling : clings)
            mClingManager.addCling(cling);
        mClingManager.start();
    }

    Cling getNoTargetCling() {
        return new Cling.Builder(this)
                .setTitle("Welcome to this app")
                .setContent("This application is meant to be the best app you will ever try on android.")
                .build();
    }

    Cling getViewTargetCling(View view) {
        return new Cling.Builder(this)
                .setTitle(R.string.drawer_tutorial_title)
                .setContent(R.string.drawer_tutorial_message)
                .setMessageBackground(getResources().getColor(R.color.teal))
                .setTarget(new ViewTarget(view))
                .build();
    }

    Cling getActionViewTargetCling() {
        return new Cling.Builder(this)
                .setTitle("Here is another feature")
                .setTitleTextAppearance(R.style.textAppearanceTitleNice)
                .setContent("Look how amazing this feature is, blablabla blablabla bla bla.")
                .setContentTextAppearance(R.style.textAppearanceContentNice)
                .setTarget(new ActionItemTarget(this, R.id.action_share))
                .setClingColor(getResources().getColor(R.color.cling))
                .setMessageBackground(getResources().getColor(R.color.red))
                .build();
    }
}