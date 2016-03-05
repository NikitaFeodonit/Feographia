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
package ru.feographia.text;

import ru.feographia.capnproto.FcConst;


public class BibleReference
{
  protected static final String TAG = BibleReference.class.getName();

  protected String mBookId        = "Gen";
  protected byte   mChapterId     = 1;
  protected byte   mVerseId       = 1;
  protected byte   mWordInVerseId = FcConst.UNKNOWN_ID;


  public BibleReference(
      String bookId,
      byte   chapterId,
      byte   verseId,
      byte   wordInVerseId)
  {
    mBookId = bookId;
    mChapterId = chapterId;
    mVerseId = verseId;
    mWordInVerseId = wordInVerseId;
  }


  public String getBookId()
  {
    return (mBookId);
  }


  public void setBookId(String bookId)
  {
    mBookId = bookId;
  }


  public byte getChapterId()
  {
    return (mChapterId);
  }


  public void setChapterId(byte chapterId)
  {
    mChapterId = chapterId;
  }


  public byte getVerseId()
  {
    return (mVerseId);
  }


  public void setVerseId(byte verseId)
  {
    mVerseId = verseId;
  }


  public byte getWordInVerseId()
  {
    return (mWordInVerseId);
  }


  public void setWordInVerseId(byte wordInVerseId)
  {
    mWordInVerseId = wordInVerseId;
  }
}
