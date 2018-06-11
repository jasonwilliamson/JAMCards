package com.co.jammcards.jammcards;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

public class CardFragment extends Fragment {

    private static final String ARG_CARD_ID = "card_id";

    private Card mCard;
    private Deck mCurrentDeck;
    private EditText mCardText;
    private CheckBox mShowCheckBox;

    public static CardFragment newInstance(UUID cardId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CARD_ID, cardId);

        CardFragment fragment = new CardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mCard = new Card();
        //UUID cardId = (UUID) getActivity().getIntent()
        //        .getSerializableExtra(CardActivity()).
        mCurrentDeck = DeckLab.get(getActivity()).getCurrentDeck();
        //UUID cardId = (UUID) getActivity().getIntent()
        //        .getSerializableExtra(CardActivity.EXTRA_CARD_ID);
        UUID cardId = (UUID) getArguments().getSerializable(ARG_CARD_ID);
        mCard = mCurrentDeck.getCard(cardId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_card, container, false);

        mShowCheckBox = (CheckBox)v.findViewById(R.id.card_show);
        mShowCheckBox.setChecked(mCard.isShow());
        mShowCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCard.setShow(isChecked);
            }
        });

        mCardText = (EditText) v.findViewById(R.id.card_text);
        mCardText.setText(mCard.getText());
        mCardText.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
                 mCard.setText(s.toString());
             }

             @Override
             public void afterTextChanged(Editable s) {

             }
         });


        return v;
    }
}
