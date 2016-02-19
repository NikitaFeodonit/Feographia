/*
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
 */

package ru.feographia.fcore;

import org.zeromq.ZMQ;
import ru.feographia.capnproto.FcConst;
import ru.feographia.fcore.message.CreateTestModuleMsg;
import ru.feographia.fcore.message.GetFileTextMsg;
import ru.feographia.fcore.message.GetFragmentTextMsg;
import ru.feographia.text.BibleReference;

import java.io.IOException;


public final class Fcore
{
    protected static final String TAG = Fcore.class.getName();

    protected long        mZmqContextPointer;
    protected ZMQ.Context mZmqContext;
    protected ZMQ.Socket  mZmqFCoreSocket;


    // native static and non-static:
    // http://stackoverflow.com/a/15254300
    private native long fcoreRunMainThread();


    public Fcore()
    {
        mZmqContextPointer = fcoreRunMainThread();
        mZmqContext = ZMQ.existingContext(mZmqContextPointer);

        mZmqFCoreSocket = mZmqContext.socket(ZMQ.PAIR);
        mZmqFCoreSocket.connect(FcConst.INPROC_FCORE.toString());
    }


    public String getFragmentText(
            BibleReference fromReference,
            BibleReference toReference)
            throws IOException
    {
        return new GetFragmentTextMsg(
                mZmqFCoreSocket, fromReference, toReference).getFragmentText();
    }


    // for test
    public String createTestModule(String modulePath)
            throws IOException
    {
        return new CreateTestModuleMsg(mZmqFCoreSocket, modulePath).getReportText();
    }


    public String getFileTextUtf16(String filePath)
            throws IOException
    {
        return new GetFileTextMsg(mZmqFCoreSocket, filePath).getFileText();
    }
}
