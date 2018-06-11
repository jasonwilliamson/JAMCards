package com.co.jammcards.jammcards;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeckLab {

    private static DeckLab sDeckLab;

    private List<Deck> mDecks;

    private Deck mCurrentDeck;

    public static DeckLab get(Context context) {
        if (sDeckLab == null) {
            sDeckLab = new DeckLab(context);
        }

        return sDeckLab;
    }

    private DeckLab(Context context) {
        mDecks = new ArrayList<>();
        for(int i = 0; i < 12; i++){
            Deck deck = new Deck();
            deck.setTitle("Deck #" + i);
            deck.createCards();
            mDecks.add(deck);
        }
    }

    public List<Deck> getDecks() {
        return mDecks;
    }

    public Deck getDeck(UUID id) {
        for(Deck deck:mDecks) {
            if(deck.getId().equals(id)) {
                mCurrentDeck = deck;
                return deck;
            }
        }
        return null;
    }

    public Deck getCurrentDeck() {
        if(mCurrentDeck == null){
            return null;
        }else{
            return mCurrentDeck;
        }
    }
}
