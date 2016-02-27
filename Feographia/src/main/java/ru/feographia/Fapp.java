/*
 * Project:  Feographia
 * Purpose:  Mobile application to work with the biblical text
 * Authors:  NikitaFeodonit, nfeodonit@yandex.com
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

import android.app.Application;
import ru.feographia.fcore.Fcore;


public class Fapp extends Application
{
  protected static final String TAG = Fapp.class.getName();

  protected Fcore mFcore;


  static {
    //System.loadLibrary("crystax"); // need for shared linking on some devices
    //System.loadLibrary("gnustl_shared"); // need for shared linking on some devices
    System.loadLibrary("fcore");
  }


  @Override
  public void onCreate()
  {
    // For service debug
//        android.os.Debug.waitForDebugger();

    super.onCreate();

    mFcore = new Fcore();
  }


  public Fcore getFcore()
  {
    return (mFcore);
  }
}
