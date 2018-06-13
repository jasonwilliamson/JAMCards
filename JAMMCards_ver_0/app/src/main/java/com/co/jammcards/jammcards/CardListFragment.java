package com.co.jammcards.jammcards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

public class CardListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mCardRecyclerView;
    private CardAdapter mAdapter;
    private Deck mDeck;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID deckId = (UUID) getActivity().getIntent()
                .getSerializableExtra(CardListActivity.EXTRA_DECK_ID);
        mDeck = DeckLab.get(getActivity()).getDeck(deckId);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_list, container, false);

        mCardRecyclerView = (RecyclerView) view
                .findViewById(R.id.card_recycler_view);
        mCardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_card_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_card:
                Card card = new Card();
                mDeck.addCard(card);
                Intent intent = CardPagerActivity
                        .newIntent(getActivity(), card.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        int cardCount = mDeck.getCards().size();
        String subtitle = getString(R.string.subtite_format, cardCount);

        if(!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        //we need cards here...
        List<Card> cards = mDeck.getCards();

        if (mAdapter == null) {
            mAdapter = new CardAdapter(cards);
            mCardRecyclerView.setAdapter(mAdapter);
        } else {
            //mAdapter.setCards(cards);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    private class CardHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{

        private Card mCard;
        private TextView mTitleTextView;
        private ImageView mShownImageView;

        public CardHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_card, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.card_text);
            mShownImageView = (ImageView) itemView.findViewById(R.id.card_shown);
        }

        public void bind(Card card) {
            mCard = card;
            mTitleTextView.setText(mCard.getText());
            mShownImageView.setVisibility(card.isShow() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View view){
           // Toast.makeText(getActivity(),
           //         mCard.getText() + " clicked!", Toast.LENGTH_SHORT).show();

            //Intent intent = new Intent(getActivity(), CardActivity.class);

            //Intent intent = CardActivity.newIntent(getActivity(), mCard.getId());

            Intent intent = CardPagerActivity.newIntent(getActivity(), mCard.getId());
            startActivity(intent);
        }
    }

    private class CardAdapter extends RecyclerView.Adapter<CardHolder> {

        private List<Card> mCards;

        public CardAdapter(List<Card> cards) {
            mCards = cards;
        }

        public void setCards(List<Card> cards) {
            mCards = cards;
        }

        @Override
        public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CardHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CardHolder holder, int position) {
            Card card = mCards.get(position);
            holder.bind(card);
        }

        @Override
        public int getItemCount() {
            return mCards.size();
        }
    }



}
