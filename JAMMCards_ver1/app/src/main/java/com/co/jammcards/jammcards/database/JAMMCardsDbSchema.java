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
            public static final String CORRECT_COUNT = "correct_count";
            public static final String TOTAL_COUNT = "total_count";
        }
    }

    public static final class DeckTable {
        public static final String NAME = "decks";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String CATEGORY_A = "category_a";
            public static final String CATEGORY_B = "category_b";
            public static final String CATEGORY_C = "category_c";
            public static final String CATEGORY_D = "category_d";
            public static final String CATEGORY_F = "category_f";
        }
    }
}