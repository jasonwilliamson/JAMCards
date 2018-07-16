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
        String backText = getString(getColumnIndex(CardTable.Cols.BACK_TEXT));
        int correctCount = getInt(getColumnIndex(CardTable.Cols.CORRECT_COUNT));
        int totalCount = getInt(getColumnIndex(CardTable.Cols.TOTAL_COUNT));
        String deck_uuidString = getString(getColumnIndex(CardTable.Cols.DECK_UUID));
        int isShown = getInt(getColumnIndex(CardTable.Cols.SHOWN));

        Card card = new Card(UUID.fromString(uuidString));  //TODO deck UUID too
        card.setTitle(title);
        card.setDECK_uuid(UUID.fromString(deck_uuidString));
        card.setText(text);
        card.setBackText(backText);
        card.setCorrectCount(correctCount);
        card.setTotalCount(totalCount);
        card.setShown(isShown != 0);
        return card;

    }
}

