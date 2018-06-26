package com.co.jammcards.jammcards.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.co.jammcards.jammcards.Card;
import com.co.jammcards.jammcards.database.JAMMCardsDbSchema.CardTable;

import java.util.UUID;

public class CardCursorWrapper extends CursorWrapper {
    public CardCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Card getCard() {
        String uuidString = getString(getColumnIndex(CardTable.Cols.UUID));
        String title = getString(getColumnIndex(CardTable.Cols.TITLE));
        String text = getString(getColumnIndex(CardTable.Cols.TEXT));
        String deck_uuidString = getString(getColumnIndex(CardTable.Cols.DECK_UUID));
        int isShown = getInt(getColumnIndex(CardTable.Cols.SHOWN));

        Card card = new Card(UUID.fromString(uuidString));  //TODO deck UUID too
        card.setTitle(title);
        card.setDECK_uuid(UUID.fromString(deck_uuidString));
        card.setText(text);
        card.setShown(isShown != 0);
        return card;

    }
}

