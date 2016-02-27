/*
 * Project:  Feographia
 * Purpose:  Mobile application to work with the biblical text
 * Authors:  NikitaFeodonit, nfeodonit@yandex.com
 * ****************************************************************************
 * Copyright (C) 2016 NikitaFeodonit
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
import org.zeromq.ZMQ;
import ru.feographia.capnproto.FcConst;
import ru.feographia.capnproto.FcMsg;

import java.io.IOException;


public class GetTestTextMsg
        extends FcoreMsg
{
    protected static final String TAG = GetTestTextMsg.class.getName();

    protected String mTestPath;
    protected String mTestText = null;


    public GetTestTextMsg(
            ZMQ.Socket zmqFCoreSocket,
            String testPath)
    {
        super(zmqFCoreSocket);
        mMsgType = FcConst.MSG_TYPE_GET_TEST_TEXT;
        mTestPath = testPath;
    }


    @Override
    protected void setDataQ(AnyPointer.Builder dataPtrQ)
    {
        // set the query data
        FcMsg.GetTestTextQ.Builder dataQ = dataPtrQ.initAs(FcMsg.GetTestTextQ.factory);
        dataQ.setTestPath(mTestPath);
    }


    @Override
    protected AnyPointer.Reader msgWorker()
            throws IOException
    {
        // get the reply data
        AnyPointer.Reader dataPtrR = super.msgWorker();
        FcMsg.GetTestTextR.Reader dataR = dataPtrR.getAs(FcMsg.GetTestTextR.factory);

        // TODO: send utf16 text from C++ core
        mTestText = dataR.getTestText().toString();

        // for test without String's conversion to UTF-16
//        org.capnproto.Text.Reader r = dataR.getTestText();
//        mTestText = "";

        return dataPtrR;
    }


    public String getTestText()
            throws IOException
    {
        if (null == mTestText) {
            msgWorker();
        }
        return mTestText;
    }
}
