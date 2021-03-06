package com.co.jammcards.jammcards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.co.jammcards.jammcards.database.CardCursorWrapper;
import com.co.jammcards.jammcards.database.JAMMCardsDbSchema.CardTable;
import com.co.jammcards.jammcards.database.JAMMCardsBaseHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardLab {

    private static CardLab sCardLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CardLab get(Context context) {
        if (sCardLab == null) {
            sCardLab = new CardLab(context);
        }

        return sCardLab;
    }

    private CardLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new JAMMCardsBaseHelper(mContext).getWritableDatabase();
    }

    public void addCard(Card c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(CardTable.NAME, null, values);
    }

    public void deleteCard(Card c) {
        String selection = CardTable.Cols.UUID + " =?";
        String[] selectionArgs = new String[] { c.getId().toString() };

        mDatabase.delete(CardTable.NAME,
                selection,
                selectionArgs);
    }

    public List<Card> getCards(UUID deckId) {

        List<Card> cards = new ArrayList<>();

        String selection = CardTable.Cols.DECK_UUID + " =?";
        String[] selectionArgs = new String[] { deckId.toString() };

        CardCursorWrapper cursor = queryCards(selection, selectionArgs);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                cards.add(cursor.getCard());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return cards;
    }

    public Card getCard(UUID id) {
        CardCursorWrapper cursor = queryCards(
                CardTable.Cols.UUID + " = ?",
                new String[] {id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCard();
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Card card) {
        File filesDir = mContext.getFilesDir();
        return  new File(filesDir, card.getPhotoFilename());
    }

    public void saveBitmap(Card card, Bitmap bm){
        File filesDir = mContext.getFilesDir();
        File newFile =  new File(filesDir, card.getPhotoFilename());
        Log.d("CardLab", "saveBitmap");
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBackBitmap(Card card, Bitmap bm){
        File filesDir = mContext.getFilesDir();
        File newFile =  new File(filesDir, card.getBackPhotoFilename());
        Log.d("CardLab", "saveBitmap");
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getBackPhotoFile(Card card) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, card.getBackPhotoFilename());
    }

    public void updateCard(Card card) {
        String uuidString = card.getId().toString();
        ContentValues values = getContentValues(card);

        mDatabase.update(CardTable.NAME, values,
                CardTable.Cols.UUID + " =?",
                new String[] {uuidString});
    }


    private CardCursorWrapper queryCards(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CardTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CardCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Card card){
        ContentValues values = new ContentValues();
        values.put(CardTable.Cols.UUID, card.getId().toString());
        values.put(CardTable.Cols.TITLE, card.getTitle());
        values.put(CardTable.Cols.TEXT, card.getText());
        values.put(CardTable.Cols.BACK_TEXT, card.getBackText());
        values.put(CardTable.Cols.DECK_UUID, card.getDECK_uuid().toString());
        values.put(CardTable.Cols.SHOWN, card.isShown() ? 1 : 0 );
        values.put(CardTable.Cols.CORRECT_COUNT, card.getCorrectCount());
        values.put(CardTable.Cols.TOTAL_COUNT, card.getTotalCount());

        return values;
    }
}
