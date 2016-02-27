/*
 * Project:  Feographia
 * Purpose:  Mobile application to work with the biblical text
 * Authors:  NikitaFeodonit, nfeodonit@yandex.com
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
import ru.feographia.text.BibleReference;

import java.io.IOException;


public class GetFragmentTextMsg extends FcoreMsg
{
  protected static final String TAG = GetFragmentTextMsg.class.getName();

  protected BibleReference mFromReference;
  protected BibleReference mToReference;
  protected String         mFragmentText = null;


  public GetFragmentTextMsg(
      BibleReference fromReference,
      BibleReference toReference)
  {
    super();
    mMsgType = FcConst.MSG_TYPE_GET_FRAGMENT_TEXT;
    mFromReference = fromReference;
    mToReference = toReference;
  }


  @Override
  protected void setDataQ(AnyPointer.Builder dataPtrQ)
  {
    // set the query data
    FcMsg.GetFragmentTextQ.Builder dataQ = dataPtrQ.initAs(FcMsg.GetFragmentTextQ.factory);

    FcMsg.Reference.Builder fromReference = dataQ.getFromReference();
    fromReference.setBookId(mFromReference.getBookId());
    fromReference.setChapterId(mFromReference.getChapterId());
    fromReference.setVerseId(mFromReference.getVerseId());
    fromReference.setWordInVerseId(mFromReference.getWordInVerseId());

    FcMsg.Reference.Builder toReference = dataQ.getToReference();
    toReference.setBookId(mToReference.getBookId());
    toReference.setChapterId(mToReference.getChapterId());
    toReference.setVerseId(mToReference.getVerseId());
    toReference.setWordInVerseId(mToReference.getWordInVerseId());
  }  /* setDataQ */


  @Override
  protected AnyPointer.Reader msgWorker() throws IOException
  {
    // get the reply data
    AnyPointer.Reader             dataPtrR = super.msgWorker();
    FcMsg.GetFragmentTextR.Reader dataR = dataPtrR.getAs(FcMsg.GetFragmentTextR.factory);
    mFragmentText = dataR.getFragmentText().toString();
    return (dataPtrR);
  }


  public String getFragmentText() throws IOException
  {
    if (null == mFragmentText) {
      msgWorker();
    }
    return (mFragmentText);
  }
}
