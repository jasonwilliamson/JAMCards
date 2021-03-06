package com.co.jammcards.jammcards;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.co.jammcards.jammcards.database.JAMMCardsDbSchema;
import com.co.jammcards.jammcards.database.JAMMCardsDbSchema.CardTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Deck {

    private UUID mId;
    private String mTitle;
    private int mCategoryA;
    private int mCategoryB;
    private int mCategoryC;
    private int mCategoryD;
    private int mCategoryF;
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

    public int getCategoryA() {
        return mCategoryA;
    }

    public void setCategoryA(int categoryA) {
        mCategoryA = categoryA;
    }

    public int getCategoryB() {
        return mCategoryB;
    }

    public void setCategoryB(int categoryB) {
        mCategoryB = categoryB;
    }

    public int getCategoryC() {
        return mCategoryC;
    }

    public void setCategoryC(int categoryC) {
        mCategoryC = categoryC;
    }

    public int getCategoryD() {
        return mCategoryD;
    }

    public void setCategoryD(int categoryD) {
        mCategoryD = categoryD;
    }

    public int getCategoryF() {
        return mCategoryF;
    }

    public void setCategoryF(int categoryF) {
        mCategoryF = categoryF;
    }

   // public void createCards(){
        //mCards = new ArrayList<>();
        /*for(int i = 0; i < 100; i++){
            Card card = new Card();
            card.setText("Card #" + i);
            card.setShow(i % 2 == 0);  //Every other one
            card.setDeck(mTitle);
            mCards.add(card);
        }*/
  //  }

   // public List<Card> getCards() {
        //TODO: Temporary fix
        /*if (mCards == null){
            mCards = new ArrayList<>();
        }
        return mCards;
        */
   //     return null;
  //  }

  //  public Card getCard(UUID id) {
        /*for(Card card:mCards) {
            if(card.getId().equals(id)) {
                return card;
            }
        }*/
//        return null;
 //   }

 //   public void addCard(Card c) {
        //mCards.add(c);
        //ContentValues values = getContentValues(c);

 //   }

 //   public void deleteCard(Card c) {
        /*for(Card card:mCards) {
            if(card.getId().equals(c.getId())) {
                mCards.remove(c);
            }
        }*/
 //   }




}
