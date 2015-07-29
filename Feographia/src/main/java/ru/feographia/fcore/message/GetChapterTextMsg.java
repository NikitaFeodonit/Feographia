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
import org.zeromq.ZMQ;
import ru.feographia.capnproto.FcConst;
import ru.feographia.capnproto.FcMsg;
import ru.feographia.text.BibleReference;

import java.io.IOException;


public class GetChapterTextMsg
        extends FcoreMsg
{
    protected static final String TAG = GetChapterTextMsg.class.getName();

    BibleReference mReference;
    String mChapterText = null;


    public GetChapterTextMsg(
            ZMQ.Socket zmqFCoreSocket,
            BibleReference reference)
    {
        super(zmqFCoreSocket);
        mMsgType = FcConst.MSG_TYPE_GET_CHAPTER_TEXT;
        mReference = reference;
    }


    @Override
    protected void setDataQ(AnyPointer.Builder dataPtrQ)
    {
        // set the query data
        FcMsg.GetChapterQ.Builder dataQ = dataPtrQ.initAs(FcMsg.GetChapterQ.factory);
        FcMsg.Reference.Builder ref = dataQ.initReference();

        ref.setBookId(mReference.getBookID());
        ref.setChapterId(mReference.getChapterId());
        ref.setFromVerseId(mReference.getFromVerseId());
        ref.setToVerseId(mReference.getToVerseId());
    }


    @Override
    protected AnyPointer.Reader msgWorker()
            throws IOException
    {
        // get the reply data
        AnyPointer.Reader dataPtrR = super.msgWorker();
        FcMsg.GetChapterR.Reader dataR = dataPtrR.getAs(FcMsg.GetChapterR.factory);
        mChapterText = dataR.getChapterText().toString();
        return dataPtrR;
    }


    public String getChapterText()
            throws IOException
    {
        if (null == mChapterText) {
            msgWorker();
        }
        return mChapterText;
    }
}
