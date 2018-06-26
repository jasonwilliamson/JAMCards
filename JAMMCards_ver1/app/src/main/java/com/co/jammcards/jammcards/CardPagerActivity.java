package com.co.jammcards.jammcards;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class CardPagerActivity extends AppCompatActivity {

    private static final String EXTRA_CARD_ID =
            "com.co.jammcards.jammcards.card_id";

    private ViewPager mViewPager;
    private List<Card> mCards;
    private Card mCard;
    private Deck mCurrentDeck;

    public static Intent newIntent(Context packageContext, UUID cardId) {
        Intent intent = new Intent(packageContext, CardPagerActivity.class);
        intent.putExtra(EXTRA_CARD_ID, cardId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_pager);

        UUID cardId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CARD_ID);

        mViewPager = (ViewPager) findViewById(R.id.card_view_pager);

        //mCurrentDeck = DeckLab.get(this).getCurrentDeck();
        //mCards = mCurrentDeck.getCards();
        mCard = CardLab.get(this).getCard(cardId);
        this.setTitle(mCard.getTitle());

        mCards = CardLab.get(this).getCards(mCard.getDECK_uuid());

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Card card = mCards.get(position);
                return CardFragment.newInstance(card.getId());
            }

            @Override
            public int getCount() {
                return mCards.size();
            }
        });

        for (int i = 0; i < mCards.size(); i++) {
            if (mCards.get(i).getId().equals(cardId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }
}
