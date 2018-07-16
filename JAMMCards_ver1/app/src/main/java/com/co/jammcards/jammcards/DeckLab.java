package com.co.jammcards.jammcards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.co.jammcards.jammcards.database.DeckCursorWrapper;
import com.co.jammcards.jammcards.database.JAMMCardsBaseHelper;
import com.co.jammcards.jammcards.database.JAMMCardsDbSchema.DeckTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeckLab {

    private static DeckLab sDeckLab;

    //private List<Deck> mDecks;

    private Deck mCurrentDeck;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static DeckLab get(Context context) {
        if (sDeckLab == null) {
            sDeckLab = new DeckLab(context);
        }

        return sDeckLab;
    }

    private DeckLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new JAMMCardsBaseHelper(mContext).getWritableDatabase();

        //mDecks = new ArrayList<>();
        /*for(int i = 0; i < 12; i++){
            Deck deck = new Deck();
            deck.setTitle("Deck #" + i);
            deck.createCards();
            mDecks.add(deck);
        }*/
    }

    public void addDeck(Deck d) {
        //mDecks.add(d);
        ContentValues values = getContentValues(d);
        mDatabase.insert(DeckTable.NAME, null, values);
    }

    public List<Deck> getDecks() {
        //return mDecks;
        //return new ArrayList<>();
        List<Deck> decks = new ArrayList<>();

        DeckCursorWrapper cursor = queryDecks(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                decks.add(cursor.getDeck());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return decks;
    }

    public Deck getDeck(UUID id) {
        /*for(Deck deck:mDecks) {
            if(deck.getId().equals(id)) {
                mCurrentDeck = deck;
                return deck;
            }
        }*/
        //return null;

        DeckCursorWrapper cursor = queryDecks(
                DeckTable.Cols.UUID + " = ?",
                new String[] {id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getDeck();
        } finally {
            cursor.close();
        }

    }

    public Deck getCurrentDeck() {
        if(mCurrentDeck == null){
            return null;
        }else{
            return mCurrentDeck;
        }
    }

    public void updateDeck(Deck deck) {
        String uuidString = deck.getId().toString();
        ContentValues values = getContentValues(deck);

        mDatabase.update(DeckTable.NAME, values,
                DeckTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    //private Cursor queryDecks(String whereClause, String[] whereArgs) {
    private DeckCursorWrapper queryDecks(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DeckTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        //return cursor;
        return new DeckCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Deck deck) {
        ContentValues values = new ContentValues();
        values.put(DeckTable.Cols.UUID, deck.getId().toString());
        values.put(DeckTable.Cols.TITLE, deck.getTitle());
        values.put(DeckTable.Cols.CATEGORY_A, deck.getCategoryA());
        values.put(DeckTable.Cols.CATEGORY_B, deck.getCategoryB());
        values.put(DeckTable.Cols.CATEGORY_C, deck.getCategoryC());
        values.put(DeckTable.Cols.CATEGORY_D, deck.getCategoryD());
        values.put(DeckTable.Cols.CATEGORY_F, deck.getCategoryF());

        return values;
    }
}
