package com.co.jammcards.jammcards;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DeckListFragment extends Fragment {

    private RecyclerView mDeckRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private DeckAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_deck_list, container, false);
        mDeckRecyclerView = (RecyclerView) view
                .findViewById(R.id.deck_recycler_view);
        mDeckRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mFloatingActionButton = (FloatingActionButton) view
                .findViewById(R.id.floating_add_deck_action_button);

        updateUI();

        mDeckRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0 && mFloatingActionButton.getVisibility() == View.VISIBLE) {
                    mFloatingActionButton.hide();
                }else if (dy < 0 && mFloatingActionButton.getVisibility() != View.VISIBLE) {
                    mFloatingActionButton.show();
                }
            }
        });

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDeckDialog();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_deck_list, menu);
    }

    public void showAddDeckDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        final EditText editText = new EditText(getActivity());

        //Limit input size here...
        InputFilter[] fa = new InputFilter[1];
        fa[0] = new InputFilter.LengthFilter(20);
        editText.setFilters(fa);

        alert.setMessage("Enter Deck Name (Required!)");
        alert.setTitle("New Deck");
        alert.setView(editText);
        alert.setPositiveButton("Add Deck", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
                String deckTitle = editText.getText().toString();
                if(!deckTitle.isEmpty()){
                    Deck deck = new Deck();
                    deck.setTitle(deckTitle);
                    DeckLab.get(getActivity()).addDeck(deck);

                    Intent intent = CardListActivity.newIntent(getActivity(), deck.getId());
                    startActivity(intent);
                }else{
                    //nothing
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //nothing
            }
        });

        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_deck:

                showAddDeckDialog();
                return true;

        }
        return true;
    }

    private void newDeckAlert(){
        //final View view = getLayoutInflater().inflate(R.layout.alert_new_deck, null);
        //AlertDialog alertDialog = new AlertDialog().Builder(getContext()).
    }

    private void updateUI() {
        DeckLab deckLab = DeckLab.get(getActivity());
        List<Deck> decks = deckLab.getDecks();

        if (mAdapter == null) {
            mAdapter = new DeckAdapter(decks);
            mDeckRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setDecks(decks);
            mAdapter.notifyDataSetChanged();
        }

        if( 0 == mAdapter.getItemCount()){
            //make button visible
        }else{
            //hide button
        }
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

        public void setDecks(List<Deck> decks) {
            mDecks = decks;
        }
    }



}
