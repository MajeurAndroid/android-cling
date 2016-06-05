# Android-Cling

![alt tag](https://raw.githubusercontent.com/MajeurAndroid/Android-Cling/master/web_art/device_example.png)

## What is Cling library
Cling is a library that allows you to create overlays that will allow the user to be informed of how your application works.

There is already a bunch of libraries to do that, but I wanted a simple and light library that do this quickly, simply and fastly. And also without creating a Bitmap of the size of the screen !

You can display one unique overlay or sequence of overlays. Overlays are represented by Cling objects.
Clings point to a Target, if Target isn't Target.NONE, the overlay will have a hole just on the provided Target. This allows user to focus on the ui element you want to describe.

## How to use Cling library
Clings are managed through ClingManager class.
```java

    private SharedPreferences mSharedPreferences;
    private ClingManager mClingManager;

    @Override
    protected void onResume() {
        super.onResume();

        if (mSharedPreferences.getBoolean(START_TUTORIAL_KEY, true)
                && (mClingManager == null || !mClingManager.isStarted()))

        mClingManager = new ClingManager(this);

        mClingManager.addCling(new Cling.Builder(this)
                .setTitle("Welcome to this app")
                .setContent("This application is meant to be the best app you will ever try on android.")
                .setTarget(Target.NONE)
                .build());

        mClingManager.addCling(new Cling.Builder(this)
                .setTitle(R.string.drawer_tutorial_title)
                .setContent(R.string.drawer_tutorial_message)
                .setMessageBackground(getResources().getColor(R.color.teal))
                .setTarget(new ViewTarget(this, R.id.drawer))
                .build());

        mClingManager.addCling(new Cling.Builder(this)
                .setTitle("Here is another feature")
                .setTitleTextAppearance(R.style.textAppearanceTitleNice)
                .setContent("Look how amazing this feature is, blablabla blablabla bla bla.")
                .setContentTextAppearance(R.style.textAppearanceContentNice)
                .setTarget(new ViewTarget(mFeatureView))
                .setClingColor(getColor(R.color.cling))
                .setMessageBackground(getColor(R.color.red))
                .build());

        mClingManager.setCallbacks(new ClingManager.Callbacks() {
            @Override
            public boolean onClingClick(int position) {
                // We return false to tell to cling manager that we didn't handle this,
                // so it can perform the default action (ie. showing the next Cling)
                return false;
            }

            @Override
            public void onClingShow(int position) {
                Toast.makeText(MyActivity.this, "Cling #" + position + " is shown", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClingHide(int position) {
                Toast.makeText(MyActivity.this, "Cling #" + position + " is hidden", Toast.LENGTH_SHORT).show();

                // Last Cling has been shown, tutorial is ended.
                if (position == 2) {
                    mSharedPreferences.edit()
                            .putBoolean(START_TUTORIAL_KEY, false)
                            .apply();

                    mClingManager = null;
                }
            }
        });

        mClingManager.start();
    }
```

##License

	 Copyright 2016 MajeurAndroid

	 Licensed under the Apache License, Version 2.0 (the "License");
	 you may not use this file except in compliance with the License.
	 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

	 Unless required by applicable law or agreed to in writing, software
	 distributed under the License is distributed on an "AS IS" BASIS,
	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 See the License for the specific language governing permissions and
	 limitations under the License.
