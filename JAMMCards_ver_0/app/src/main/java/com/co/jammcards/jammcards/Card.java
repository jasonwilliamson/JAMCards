package com.co.jammcards.jammcards;

import java.util.UUID;

public class Card {

    private UUID mId;
    private String mText;
    private boolean mShow;
    private String mDeck;

    public String getDeck() {
        return mDeck;
    }

    public void setDeck(String deck) {
        mDeck = deck;
    }

    public Card() {
        mId = UUID.randomUUID();
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public boolean isShow() {
        return mShow;
    }

    public void setShow(boolean show) {
        mShow = show;
    }
}
