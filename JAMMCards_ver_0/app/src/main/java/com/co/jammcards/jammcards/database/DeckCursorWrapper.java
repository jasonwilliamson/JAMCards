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

        Deck deck = new Deck(UUID.fromString(uuidString));
        deck.setTitle(title);
        return deck;
    }
}
