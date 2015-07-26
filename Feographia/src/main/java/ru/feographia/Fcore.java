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

package ru.feographia;

import org.zeromq.ZMQ;
import ru.feographia.text.BibleReference;

import java.io.IOException;


public final class Fcore
{
    protected static final String INPROC_FCORE = "inproc://fcore";

    protected static final String TAG = Fcore.class.getName();


    protected long        mZmqContextPointer;
    protected ZMQ.Context mZmqContext;
    protected ZMQ.Socket  mZmqFCoreSocket;


    // native static and non-static:
    // http://stackoverflow.com/a/15254300/4727406
    private native long fcoreRunMainThread();


    public Fcore()
    {
        mZmqContextPointer = fcoreRunMainThread();
        mZmqContext = ZMQ.existingContext(mZmqContextPointer);

        mZmqFCoreSocket = mZmqContext.socket(ZMQ.PAIR);
        mZmqFCoreSocket.connect(INPROC_FCORE);
    }


    public String getChapterText(BibleReference reference)
            throws IOException
    {
        GetChapterTextMsg msg = new GetChapterTextMsg(mZmqFCoreSocket, reference);
        msg.msgWorker();
        return msg.getChapterText();
    }


    // for test
    public String getFileTextUtf16(String filePath)
            throws IOException
    {
        GetFileTextMsg msg = new GetFileTextMsg(mZmqFCoreSocket, filePath);
        msg.msgWorker();
        return msg.getFileText();
    }
}
