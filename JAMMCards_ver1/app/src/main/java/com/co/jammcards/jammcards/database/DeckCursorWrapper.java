package com.co.jammcards.jammcards.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.co.jammcards.jammcards.Deck;
import com.co.jammcards.jammcards.database.JAMMCardsDbSchema.DeckTable;

import java.util.UUID;

public class DeckCursorWrapper extends CursorWrapper {
    public DeckCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Deck getDeck() {
        String uuidString = getString(getColumnIndex(DeckTable.Cols.UUID));
        String title = getString(getColumnIndex(DeckTable.Cols.TITLE));
        int categoryA = getInt(getColumnIndex(DeckTable.Cols.CATEGORY_A));
        int categoryB = getInt(getColumnIndex(DeckTable.Cols.CATEGORY_B));
        int categoryC = getInt(getColumnIndex(DeckTable.Cols.CATEGORY_C));
        int categoryD = getInt(getColumnIndex(DeckTable.Cols.CATEGORY_D));
        int categoryF = getInt(getColumnIndex(DeckTable.Cols.CATEGORY_F));


        Deck deck = new Deck(UUID.fromString(uuidString));
        deck.setTitle(title);
        deck.setCategoryA(categoryA);
        deck.setCategoryB(categoryB);
        deck.setCategoryC(categoryC);
        deck.setCategoryD(categoryD);
        deck.setCategoryF(categoryF);
        return deck;
    }
}
