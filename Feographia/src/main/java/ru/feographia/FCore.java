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

import android.util.Log;
import org.capnproto.ArrayInputStream;
import org.capnproto.ArrayOutputStream;
import org.capnproto.MessageBuilder;
import org.capnproto.MessageReader;
import org.capnproto.Serialize;
import org.zeromq.ZMQ;
import ru.feographia.capnproto.FCoreMessages;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class FCore
{
    protected long        mZMQContextPointer;
    protected ZMQ.Context mZmqContext;
    protected ZMQ.Socket  mZmqFCoreSocket;


    public FCore(long ZMQContextPointer)
    {
        mZMQContextPointer = ZMQContextPointer;
        mZmqContext = ZMQ.existingContext(mZMQContextPointer);
        mZmqFCoreSocket = mZmqContext.socket(ZMQ.PAIR);
        mZmqFCoreSocket.connect("inproc://fcore");
    }


    public static native long fcoreRunMainThread();


    public String loadFileUtf16(String filePath)
    {
        MessageBuilder messageBuilder = new MessageBuilder();
        FCoreMessages.LoadFileReq.Builder loadFileReq =
                messageBuilder.initRoot(FCoreMessages.LoadFileReq.factory);
        loadFileReq.setPath(filePath);

        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        ArrayOutputStream aos = new ArrayOutputStream(buffer);
        try {
            Serialize.write(aos, messageBuilder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mZmqFCoreSocket.send(aos.getWriteBuffer().array(), 0);


        byte[] replyCapnp;
        replyCapnp = mZmqFCoreSocket.recv(0);

        ArrayInputStream ais = new ArrayInputStream(ByteBuffer.wrap(replyCapnp));
        String text = null;
        try {
            MessageReader messageReader = Serialize.read(ais);
            FCoreMessages.LoadFileRep.Reader reply = messageReader.getRoot(
                    FCoreMessages.LoadFileRep.factory);
            text = reply.getText().toString();
            Log.d("REPLY", "text: " + text);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // mZmqFCoreSocket.close();
        // zmqContext.term(); // NOT terminate it with ZMQ.existingContext !!!

        return text;


/*
        // for get raw bytes of the UTF8 text
        byte[] bytes = {1, 2};
        CharBuffer cb = ByteBuffer.wrap(bytes).asCharBuffer();
        // cb.toString(); // slow, java code
        return new String(cb.array()); // fast, native arraycopyCharUnchecked()
*/
    }
}
