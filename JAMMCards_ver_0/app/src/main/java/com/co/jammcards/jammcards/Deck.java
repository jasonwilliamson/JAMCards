package com.co.jammcards.jammcards;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Deck {

    private UUID mId;
    private String mTitle;
    private List<Card> mCards;

    public Deck() {
        this(UUID.randomUUID());
    }

    public Deck(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void createCards(){
        mCards = new ArrayList<>();
        /*for(int i = 0; i < 100; i++){
            Card card = new Card();
            card.setText("Card #" + i);
            card.setShow(i % 2 == 0);  //Every other one
            card.setDeck(mTitle);
            mCards.add(card);
        }*/
    }

    public List<Card> getCards() {
        return mCards;
    }

    public Card getCard(UUID id) {
        for(Card card:mCards) {
            if(card.getId().equals(id)) {
                return card;
            }
        }
        return null;
    }

    public void addCard(Card c) {
        mCards.add(c);
    }

    public void deleteCard(Card c) {
        for(Card card:mCards) {
            if(card.getId().equals(c.getId())) {
                mCards.remove(c);
            }
        }
    }
}
