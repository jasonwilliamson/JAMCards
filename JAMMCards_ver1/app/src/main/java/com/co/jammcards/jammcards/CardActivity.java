package com.co.jammcards.jammcards;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CardActivity extends SingleFragmentActivity {

    private static final String EXTRA_CARD_ID =
            "com.co.jammcards.jammcards.card_id";

    public static Intent newIntent(Context packageContext, UUID cardId) {
        Intent intent = new Intent(packageContext, CardActivity.class);
        intent.putExtra(EXTRA_CARD_ID, cardId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {

        //return new CardFragment();

        UUID cardId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CARD_ID);
        return CardFragment.newInstance(cardId);
    }
}
