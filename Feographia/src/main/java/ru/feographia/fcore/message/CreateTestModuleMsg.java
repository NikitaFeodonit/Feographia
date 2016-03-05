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
package ru.feographia.fcore.message;

import org.capnproto.AnyPointer;
import ru.feographia.capnproto.FcConst;
import ru.feographia.capnproto.FcMsg;

import java.io.IOException;


public class CreateTestModuleMsg extends FcoreMsg
{
  protected static final String TAG = CreateTestModuleMsg.class.getName();

  protected String mModulePath;
  protected String mReportText = null;


  public CreateTestModuleMsg(String modulePath)
  {
    super();
    mMsgType = FcConst.MSG_TYPE_CREATE_TEST_MODULE;
    mModulePath = modulePath;
  }


  @Override
  protected void setDataQ(AnyPointer.Builder dataPtrQ)
  {
    // set the query data
    FcMsg.CreateTestModuleQ.Builder dataQ = dataPtrQ.initAs(FcMsg.CreateTestModuleQ.factory);
    dataQ.setModulePath(mModulePath);
  }


  @Override
  protected AnyPointer.Reader msgWorker() throws IOException
  {
    // get the reply data
    AnyPointer.Reader              dataPtrR = super.msgWorker();
    FcMsg.CreateTestModuleR.Reader dataR = dataPtrR.getAs(FcMsg.CreateTestModuleR.factory);
    mReportText = dataR.getReportText().toString();
    return (dataPtrR);
  }


  public String getReportText() throws IOException
  {
    if (null == mReportText) {
      msgWorker();
    }
    return (mReportText);
  }
}
