package com.co.jammcards.jammcards.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.co.jammcards.jammcards.database.JAMMCardsDbSchema.CardTable;
import com.co.jammcards.jammcards.database.JAMMCardsDbSchema.DeckTable;


public class JAMMCardsBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "JAMMCardsBase.db";

    // Table Create Statements
    // Card table create statement
    private static final String create_table_cards = "create table " + CardTable.NAME + "(" +
            " _id integer primary key autoincrement, " +
            CardTable.Cols.UUID + ", " +
            CardTable.Cols.DECK_UUID + ", " +
            CardTable.Cols.TITLE + ", " +
            CardTable.Cols.TEXT + ", " +
            CardTable.Cols.SHOWN + ")";

    // Deck table create statement
    private static final String create_table_decks = "create table " + DeckTable.NAME + "(" +
            " _id integer primary key autoincrement, " +
            DeckTable.Cols.UUID + ", " +
            DeckTable.Cols.TITLE + ")";

    public JAMMCardsBaseHelper(Context context) {

        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_table_cards);
        db.execSQL(create_table_decks);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}

