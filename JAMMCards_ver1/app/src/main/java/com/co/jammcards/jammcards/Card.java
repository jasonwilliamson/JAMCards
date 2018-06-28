package com.co.jammcards.jammcards;

import java.util.UUID;

public class Card {

    private UUID mId;
    private String mTitle;
    private String mText;
    private boolean mIsShown;
    private UUID mDECK_uuid;

    public Card() {
        this(UUID.randomUUID());
    }

    public Card(UUID id) {
        mId = id;
        //mDECK_uuid = deckId;
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

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public boolean isShown() {
        return mIsShown;
    }

    public void setShown(boolean show) {
        mIsShown = show;
    }

    public UUID getDECK_uuid() {
        return mDECK_uuid;
    }

    public void setDECK_uuid(UUID DECK_uuid) {
        mDECK_uuid = DECK_uuid;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
