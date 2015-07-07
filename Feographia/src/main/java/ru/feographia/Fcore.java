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

import org.capnproto.AnyPointer;
import org.capnproto.ArrayInputStream;
import org.capnproto.ArrayOutputStream;
import org.capnproto.MessageBuilder;
import org.capnproto.MessageReader;
import org.capnproto.Serialize;
import org.zeromq.ZMQ;
import ru.feographia.capnproto.FcMsg;
import ru.feographia.text.BibleReference;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public final class Fcore
{
    protected static final String INPROC_FCORE = "inproc://fcore";

    protected static final int MSG_TYPE_GET_CHAPTER_TEXT = 1;
    protected static final int MSG_TYPE_GET_FILE_TEXT    = 9999;

    protected static final int SEND_BUFFER_SIZE = 8192;

    protected static final String TAG = "Fcore";


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


    protected boolean sendZmqMessage(MessageBuilder messageBuilder)
            throws IOException
    {
        ByteBuffer buffer = ByteBuffer.allocateDirect(SEND_BUFFER_SIZE);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        ArrayOutputStream aos = new ArrayOutputStream(buffer);
        Serialize.write(aos, messageBuilder);
        return mZmqFCoreSocket.send(aos.getWriteBuffer().array(), 0);
    }


    protected MessageReader receiveZmqMessage()
            throws IOException
    {
        byte[] replyCapnp = mZmqFCoreSocket.recv(0);
        ArrayInputStream ais = new ArrayInputStream(ByteBuffer.wrap(replyCapnp));
        return Serialize.read(ais);

        // mZmqFCoreSocket.close(); // we do not need close it
        // zmqContext.term(); // NOT terminate it with ZMQ.existingContext !!!
    }


    public String getChapterText(BibleReference reference)
            throws IOException
    {
        MessageBuilder cpnQuery = new MessageBuilder();
        FcMsg.Message.Builder message = cpnQuery.initRoot(FcMsg.Message.factory);

        // set the query type
        message.setType(MSG_TYPE_GET_CHAPTER_TEXT);
        AnyPointer.Builder dataPointer = message.initDataPointer();
        FcMsg.GetChapterQ.Builder queryData = dataPointer.initAs(FcMsg.GetChapterQ.factory);

        // set the query data
        FcMsg.Reference.Builder ref = queryData.initReference();
        ref.setBookId(reference.getBookID());
        ref.setChapterId(reference.getChapterId());
        ref.setFromVerseId(reference.getFromVerseId());
        ref.setToVerseId(reference.getToVerseId());

        // send query and receive reply
        sendZmqMessage(cpnQuery);
        MessageReader messageReader = receiveZmqMessage();

        FcMsg.GetChapterR.Reader replyData = messageReader.getRoot(FcMsg.GetChapterR.factory);

        // get the reply data
        if (!replyData.getIsError()) {
            return replyData.getChapterText().toString();
        } else {
            // TODO: work error
            return replyData.getChapterText().toString();
        }
    }


    // for test
    public String getFileTextUtf16(String filePath)
            throws IOException
    {
        MessageBuilder cpnQuery = new MessageBuilder();
        FcMsg.Message.Builder message = cpnQuery.initRoot(FcMsg.Message.factory);

        // set the query type
        message.setType(MSG_TYPE_GET_FILE_TEXT);
        AnyPointer.Builder dataPointer = message.initDataPointer();
        FcMsg.GetFileTextQ.Builder queryData = dataPointer.initAs(FcMsg.GetFileTextQ.factory);

        // set the query data
        queryData.setFilePath(filePath);

        // send query and receive reply
        sendZmqMessage(cpnQuery);
        MessageReader cpnReply = receiveZmqMessage();

        FcMsg.GetFileTextR.Reader replyData = cpnReply.getRoot(FcMsg.GetFileTextR.factory);

        // get the reply data
        if (!replyData.getIsError()) {
            return replyData.getFileText().toString();
        } else {
            // TODO: work error
            return replyData.getFileText().toString();
        }
    }
}
