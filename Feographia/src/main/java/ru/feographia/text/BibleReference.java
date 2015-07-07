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

package ru.feographia.text;

public class BibleReference
{
    private String mBookID      = "Gen";
    private int    mChapterId   = 1;
    private int    mFromVerseId = 1;
    private int    mToVerseId   = 1;


    public BibleReference(
            String bookID,
            int chapterId,
            int fromVerseId,
            int toVerseId)
    {
        mBookID = bookID;
        mChapterId = chapterId;
        mFromVerseId = fromVerseId;
        mToVerseId = toVerseId;
    }


    public String getBookID()
    {
        return mBookID;
    }


    public void setBookID(String bookID)
    {
        mBookID = bookID;
    }


    public int getChapterId()
    {
        return mChapterId;
    }


    public void setChapterId(int chapterId)
    {
        mChapterId = chapterId;
    }


    public int getFromVerseId()
    {
        return mFromVerseId;
    }


    public void setFromVerseId(int fromVerseId)
    {
        mFromVerseId = fromVerseId;
    }


    public int getToVerseId()
    {
        return mToVerseId;
    }


    public void setToVerseId(int toVerseId)
    {
        mToVerseId = toVerseId;
    }
}
