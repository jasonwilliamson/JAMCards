package com.co.jammcards.jammcards.database;

public class JAMMCardsDbSchema {

    public static final class CardTable {
        public static final String NAME = "cards";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String DECK_UUID = "deck_uuid";
            public static final String TITLE = "title";
            public static final String TEXT = "text";
            public static final String BACK_TEXT = "back_text";
            public static final String SHOWN = "shown";
        }
    }

    public static final class DeckTable {
        public static final String NAME = "decks";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
        }
    }
}