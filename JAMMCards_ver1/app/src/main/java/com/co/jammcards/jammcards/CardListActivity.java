package com.co.jammcards.jammcards;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CardListActivity extends SingleFragmentActivity {

    public static final String EXTRA_DECK_ID =
            "come.co.jammcards.jammcards.deck_id";

    public static Intent newIntent(Context packageContent, UUID deckId) {
        Intent intent = new Intent(packageContent, CardListActivity.class);
        intent.putExtra(EXTRA_DECK_ID, deckId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new CardListFragment();
    }
}
