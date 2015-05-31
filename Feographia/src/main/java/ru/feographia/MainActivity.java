/*******************************************************************************
 * Project:  Feographia
 * Purpose:  Mobile application to work with the biblical text
 * Authors:  NikitaFeodonit, nfeodonit@yandex.com
 * ****************************************************************************
 * Copyright (C) 2015 NikitaFeodonit
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package ru.feographia;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import org.zeromq.ZMQ;
import ru.feographia.util.SystemUiHider;


/**
 * An example full-screen activity that shows and hides the system UI (i.e. status bar and
 * navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MainActivity
        extends Activity
{
    long mZMQContextPointer;


    /**
     * Whether or not the system UI should be auto-hidden after {@link #AUTO_HIDE_DELAY_MILLIS}
     * milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after user interaction
     * before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise, will show the
     * system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        FApp app = (FApp) getApplication();
        mZMQContextPointer = app.getZMQContextPointer();


        setContentView(R.layout.activity_main);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final TextView contentView = (TextView) findViewById(R.id.fullscreen_content);

        contentView.setText("test");

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider.setOnVisibilityChangeListener(
                new SystemUiHider.OnVisibilityChangeListener()
                {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;


                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible)
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if (TOGGLE_ON_CLICK) {
                            mSystemUiHider.toggle();
                        } else {
                            mSystemUiHider.show();
                        }
                    }
                });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the system UI. This is to
     * prevent the jarring behavior of controls going away while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(
                View view,
                MotionEvent motionEvent)
        {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler  mHideHandler  = new Handler();
    Runnable mHideRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            mSystemUiHider.hide();
        }
    };


    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any previously scheduled
     * calls.
     */
    private void delayedHide(int delayMillis)
    {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        new BackgroundTestTask().execute();
    }


    protected void fcoreTestJeroMqReq()
    {
        ZMQ.Context context = ZMQ.contextExisting(mZMQContextPointer);

//        ZMQ.Socket socket = context.socket(ZMQ.REQ);
//        socket.connect("tcp://127.0.0.1:7575");
        ZMQ.Socket socket = context.socket(ZMQ.PAIR);
        socket.connect("inproc://step3");

        String request = "Hello";
//        byte[] reply;

        for (int ii = 0; ii < 5000; ++ii) {
//            socket.send(request.getBytes(ZMQ.CHARSET), 0);
            socket.send(request.getBytes(), 0);
//            reply = socket.recv(0);
            socket.recv(0);
        }

        socket.close();
        // context.term(); // NOT terminate it !!!
    }


    protected void fcoreTestReq()
    {
        Log.d("JNI", "Sending 5000");

        long time = System.currentTimeMillis();

        fcoreTestJeroMqReq();
//       FCore.fcoreTestZeroMqReq(mZMQContextPointer);

        time = System.currentTimeMillis() - time;

        Log.d("JNI", "Received 5000");
        Log.d("JNI", "time: " + time);
    }


    protected class BackgroundTestTask
            extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids)
        {
            fcoreTestReq();
            return null;
        }
    }
}
