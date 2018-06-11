package com.co.jammcards.jammcards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DeckListFragment extends Fragment {

    private RecyclerView mDeckRecyclerView;
    private DeckAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_deck_list, container, false);

        mDeckRecyclerView = (RecyclerView) view
                .findViewById(R.id.deck_recycler_view);
        mDeckRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        DeckLab deckLab = DeckLab.get(getActivity());
        List<Deck> decks = deckLab.getDecks();

        mAdapter = new DeckAdapter(decks);
        mDeckRecyclerView.setAdapter(mAdapter);
    }

    private class DeckHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Deck mDeck;
        private TextView mDeckTitleTextView;

        public DeckHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_deck, parent, false));
            itemView.setOnClickListener(this);
            mDeckTitleTextView = (TextView) itemView.findViewById(R.id.deck_title);
        }

        public void bind(Deck deck) {
            mDeck = deck;
            mDeckTitleTextView.setText(mDeck.getTitle());
        }

        @Override
        public void onClick(View view) {
           // Toast.makeText(getActivity(),
           //         mDeck.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();

            //Intent intent = new Intent(getActivity(), CardListActivity.class);
            Intent intent = CardListActivity.newIntent(getActivity(), mDeck.getId());
            startActivity(intent);
        }
    }

    private class DeckAdapter extends RecyclerView.Adapter<DeckListFragment.DeckHolder> {

        private List<Deck> mDecks;

        public DeckAdapter(List<Deck> decks) {
            mDecks = decks;
        }

        @Override
        public DeckListFragment.DeckHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new DeckListFragment.DeckHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(DeckListFragment.DeckHolder holder, int position) {
            Deck deck = mDecks.get(position);
            holder.bind(deck);
        }

        @Override
        public int getItemCount() {
            return mDecks.size();
        }
    }



}
