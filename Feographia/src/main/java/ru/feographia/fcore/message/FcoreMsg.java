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
import org.capnproto.ArrayInputStream;
import org.capnproto.ArrayOutputStream;
import org.capnproto.MessageBuilder;
import org.capnproto.MessageReader;
import org.capnproto.Serialize;
import ru.feographia.capnproto.FcConst;
import ru.feographia.capnproto.FcMsg;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public abstract class FcoreMsg
{
  protected static final String TAG = FcoreMsg.class.getName();

  protected int mMsgType;


  private native static byte[] fcoreSendMessage(ByteBuffer segmentsQuery);

  protected abstract void setDataQ(AnyPointer.Builder dataPtrQ);


  public FcoreMsg()
  {
    mMsgType = FcConst.MSG_TYPE_UNKNOWN;
  }


  protected AnyPointer.Reader msgWorker() throws IOException
  {
    // with ZeroMQ
    // (100 iter)  [1000000 bytes] 7800 ms
    // (1000 iter) [9 bytes]        460 ms
    // (1 iter)    [1000000 bytes]   70 ms
    // (1000 iter) [9 bytes]        1-2 ms


    // without ZeroMQ
    // (100 iter)  [1000000 bytes] 7400 ms
    // (1000 iter) [9 bytes]        410 ms
    // (1 iter) [1000000 bytes]      66 ms
    // (1 iter) [9 bytes]           0-1 ms

    // one copy operation add 450 ms for [1000000 bytes] with (100 iter)
    // one copy operation add   8 ms for [1000000 bytes] with (1 iter)

    // ZeroMQ add one copy operation


    // without serialization, with shared memory (approximately)
    // (100 iter)  [1000000 bytes]  6500 ms
    // (1 iter)    [1000000 bytes]    50 ms

    // without String's to utf16
    // (100 iter)  [1000000 bytes]  2320 ms
    // (100 iter)  [1000000 bytes]  2560 ms + one copy operation -- 240 ms

    // (1 iter)    [1000000 bytes]    24 ms
    // (1 iter)    [1000000 bytes]    27 ms + one copy operation --   3 ms

    // (1 iter)   [10000000 bytes]   176 ms
    // (1 iter)   [10000000 bytes]   206 ms + one copy operation --  30 ms


    // create new query
    MessageBuilder        cpnQuery = new MessageBuilder();
    FcMsg.Message.Builder msgQ = cpnQuery.initRoot(FcMsg.Message.factory);

    // set the query type
    msgQ.setMsgType(mMsgType);

    // set the query data
    AnyPointer.Builder dataPtrQ = msgQ.initDataPointer();
    setDataQ(dataPtrQ);

    // send query and receive reply
    MessageReader cpnReply = fcoreSendMessage(cpnQuery);

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

    return (dataPtrR);
  }  /* msgWorker */


  protected MessageReader fcoreSendMessage(MessageBuilder messageQuery) throws IOException
  {
    int        sendBufferSize = (int) Serialize.computeSerializedSizeInWords(messageQuery) * 8;
    ByteBuffer buffer = ByteBuffer.allocateDirect(sendBufferSize);
    buffer.order(ByteOrder.LITTLE_ENDIAN);
    ArrayOutputStream aos = new ArrayOutputStream(buffer);
    Serialize.write(aos, messageQuery);

    ByteBuffer segmentsQuery = aos.getWriteBuffer();
    ByteBuffer segmentsReply = ByteBuffer.wrap(fcoreSendMessage(segmentsQuery));

    ArrayInputStream ais = new ArrayInputStream(segmentsReply);
    return (Serialize.read(ais));
  }  /* fcoreSendMessage */
}
