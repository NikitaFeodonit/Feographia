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

package ru.feographia.fcore.message;

import org.capnproto.AnyPointer;
import ru.feographia.capnproto.FcConst;
import ru.feographia.capnproto.FcMsg;

import java.io.IOException;


public class GetFileTextMsg
        extends FcoreMsg
{
    protected static final String TAG = GetFileTextMsg.class.getName();

    protected String mFilePath;
    protected String mFileText = null;


    public GetFileTextMsg(String filePath)
    {
        super();
        mMsgType = FcConst.MSG_TYPE_GET_FILE_TEXT;
        mFilePath = filePath;
    }


    @Override
    protected void setDataQ(AnyPointer.Builder dataPtrQ)
    {
        // set the query data
        FcMsg.GetFileTextQ.Builder dataQ = dataPtrQ.initAs(FcMsg.GetFileTextQ.factory);
        dataQ.setFilePath(mFilePath);
    }


    @Override
    protected AnyPointer.Reader msgWorker()
            throws IOException
    {
        // get the reply data
        AnyPointer.Reader dataPtrR = super.msgWorker();
        FcMsg.GetFileTextR.Reader dataR = dataPtrR.getAs(FcMsg.GetFileTextR.factory);
        // TODO: send utf16 text from C++ core
        mFileText = dataR.getFileText().toString();
        return dataPtrR;
    }


    public String getFileText()
            throws IOException
    {
        if (null == mFileText) {
            msgWorker();
        }
        return mFileText;
    }
}
