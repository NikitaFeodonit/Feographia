/*
 * Project:  Feographia
 * Purpose:  Mobile application to work with the biblical text
 * Author:   NikitaFeodonit, nfeodonit@yandex.com
 * ****************************************************************************
 * Copyright (C) 2015-2016 NikitaFeodonit
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
 */
package ru.feographia;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import ru.feographia.fcore.Fcore;
import ru.feographia.text.BibleReference;
import ru.feographia.util.Flog;
import ru.feographia.util.SystemUiHider;

import java.io.IOException;


/**
 * An example full-screen activity that shows and hides the system UI (i.e. status bar and
 * navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MainActivity
    extends Activity
{
  protected static final String TAG = MainActivity.class.getName();


  /**
   * Whether or not the system UI should be auto-hidden after {@link #AUTO_HIDE_DELAY_MILLIS}
   * milliseconds.
   */
  private static final boolean AUTO_HIDE = false;


  /**
   * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after user interaction before
   * hiding the system UI.
   */
  private static final int AUTO_HIDE_DELAY_MILLIS = 3000;


  /**
   * If set, will toggle the system UI visibility upon interaction. Otherwise, will show the system
   * UI visibility upon interaction.
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


  @SuppressLint("SetJavaScriptEnabled")
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    final Fapp app = (Fapp) getApplication();
    final Fcore fcore = app.getFcore();

    setContentView(R.layout.activity_main);

    final View controlsView = findViewById(R.id.fullscreen_content_controls);
    final WebView webView = (WebView) findViewById(R.id.web_view);
    final Button btnLoadFile = (Button) findViewById(R.id.btn_load_file);

    WebSettings settings = webView.getSettings();
    settings.setDefaultTextEncodingName("utf-8");
    webView.getSettings().setJavaScriptEnabled(true);
    webView.setWebViewClient(
        new WebViewClient()
        {
          @Override
          public boolean shouldOverrideUrlLoading(
              WebView view,
              String url)
          {
            view.loadUrl(url);
            return (true);
          }
        });

    btnLoadFile.setOnClickListener(
        new View.OnClickListener()
        {
          @Override
          public void onClick(View v)
          {
            long time = System.currentTimeMillis();

            String path = "/sdcard/Feographia/modules/";
            String text = getFragmentText(fcore);

//            String path = "/sdcard/Feographia/modules/";
//            String text = createTestModule(fcore);

// for test
//            String path = "/sdcard/Feographia/test_html/";
//            String text = getFileTextUtf16(fcore);

//            String path = "/sdcard/Feographia/test_html/";
//            String text = "<br>" + getTestTextUtf16(fcore);

//            String path = null;
//            String text = null;
//            for (int i = 0; i < 100; ++i) {
//              path = "/sdcard/Feographia/test_html/";
//              text = getTestTextUtf16(fcore);
//            }

            time = System.currentTimeMillis() - time;
            Flog.d(TAG, "time: " + time);
//                        Flog.d(TAG, "java byte size: " + text.getBytes().length);

            // TODO: use widgets from
            // android.support.v4.widget
            // android.support.v7.widget

            webView.loadDataWithBaseURL(
                "file://" + path, text, "text/html", "UTF-8", "about:config");
          }
        });


    // Set up an instance of SystemUiHider to control the system UI for
    // this activity.
    mSystemUiHider = SystemUiHider.getInstance(this, webView, HIDER_FLAGS);
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
                mShortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
              }
              controlsView.animate().translationY(visible ? 0 : mControlsHeight).setDuration(
                  mShortAnimTime);
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
    webView.setOnClickListener(
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
    btnLoadFile.setOnTouchListener(mDelayHideTouchListener);
  }  /* onCreate */


  public String getFragmentText(Fcore fcore)
  {
    try {
      BibleReference fromReference = new BibleReference("Rom", (byte) 4, (byte) 24, (byte) 0);
      BibleReference toReference = new BibleReference("Rom", (byte) 5, (byte) 3, (byte) 0);
      String fragmentText = fcore.getFragmentText(fromReference, toReference);

//            Flog.d(TAG, "fragmentText: " + fragmentText);

      StringBuilder html = new StringBuilder();
      html.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n");
      html.append("<html>\n");
      html.append("<head>\n");
      html.append("<meta http-equiv=Content-Type content=\"text/html; charset=UTF-8\">\n");
      html.append("</head>\n");
      html.append("<body>\n");
      html.append("<br>");
      html.append(fragmentText);
      html.append("</body>\n");
      html.append("</html>");

      return (html.toString());
    } catch (IOException e) {
      Flog.d(TAG, e.getLocalizedMessage());
      e.printStackTrace();
      return (null);
    }
  }  /* getFragmentText */


  public String createTestModule(Fcore fcore)
  {
    try {
      String path = "/sdcard/Feographia/modules/";
      String modulePath = path + "testmodule";

      StringBuilder html = new StringBuilder();
      html.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n");
      html.append("<html>\n");
      html.append("<head>\n");
      html.append("<meta http-equiv=Content-Type content=\"text/html; charset=UTF-8\">\n");
      html.append("</head>\n");
      html.append("<body>\n");
      html.append("<br>");
      html.append(fcore.createTestModule(modulePath));
      html.append("</body>\n");
      html.append("</html>");

      return (html.toString());
    } catch (IOException e) {
      Flog.d(TAG, e.getLocalizedMessage());
      e.printStackTrace();
      return (null);
    }
  }  /* createTestModule */


  public String getFileTextUtf16(Fcore fcore)
  {
    try {
      String path = "/sdcard/Feographia/test_html/";
      String filePath = path + "64-big.htm";
      return (fcore.getFileTextUtf16(filePath));
    } catch (IOException e) {
      Flog.d(TAG, e.getLocalizedMessage());
      e.printStackTrace();
      return (null);
    }
  }  /* getFileTextUtf16 */


  public String getTestTextUtf16(Fcore fcore)
  {
    try {
      String testPath = "/sdcard/Feographia/test_html/";
      return (fcore.getTestTextUtf16(testPath));
    } catch (IOException e) {
      Flog.d(TAG, e.getLocalizedMessage());
      e.printStackTrace();
      return (null);
    }
  }  /* getTestTextUtf16 */


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
      return (false);
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
   * Schedules a call to hide() in [delay] milliseconds, canceling any previously scheduled calls.
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
  }
}
