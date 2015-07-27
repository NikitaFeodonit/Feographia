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

package ru.feographia.FcoreMessage;

import org.capnproto.AnyPointer;
import org.capnproto.ArrayInputStream;
import org.capnproto.ArrayOutputStream;
import org.capnproto.MessageBuilder;
import org.capnproto.MessageReader;
import org.capnproto.Serialize;
import org.zeromq.ZMQ;
import ru.feographia.capnproto.FcMsg;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public abstract class FcoreMsg
{
    protected static final int MSG_TYPE_UNKNOWN = 0;
    protected static final int MSG_TYPE_GET_CHAPTER_TEXT = 1;
    protected static final int MSG_TYPE_ERROR = 9998;
    protected static final int MSG_TYPE_GET_FILE_TEXT = 9999;

    protected static final int SEND_BUFFER_SIZE = 8192;

    protected static final String TAG = FcoreMsg.class.getName();

    protected ZMQ.Socket mZmqFCoreSocket;
    protected int        mMsgType;


    protected abstract void setDataQ(AnyPointer.Builder dataPtrQ);


    public FcoreMsg(ZMQ.Socket zmqFCoreSocket)
    {
        mZmqFCoreSocket = zmqFCoreSocket;
        mMsgType = MSG_TYPE_UNKNOWN;
    }


    protected AnyPointer.Reader msgWorker()
            throws IOException
    {
        // create new query
        MessageBuilder cpnQuery = new MessageBuilder();
        FcMsg.Message.Builder msgQ = cpnQuery.initRoot(FcMsg.Message.factory);

        // set the query type
        msgQ.setMsgType(mMsgType);

        // set the query data
        AnyPointer.Builder dataPtrQ = msgQ.initDataPointer();
        setDataQ(dataPtrQ);

        // send query and receive reply
        sendZmqMessage(cpnQuery);
        MessageReader cpnReply = receiveZmqMessage();

        // get the reply
        FcMsg.Message.Reader msgR = cpnReply.getRoot(FcMsg.Message.factory);

        // check for errors
        if (msgR.getErrorFlag()) {
            throw new IOException(msgR.getMsgText().toString());
        }

        // check the reply type
        int replyType = msgR.getMsgType();
        if (mMsgType != replyType) {
            throw new IOException("Bad reply type: " + replyType + ", must be: " + mMsgType);
        }

        // get the reply data
        AnyPointer.Reader dataPtrR = msgR.getDataPointer();
        if (dataPtrR.isNull()) {
            throw new IOException("dataPtrR.isNull()");
        }

        return dataPtrR;
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
}
