package com.co.jammcards.jammcards;

import android.support.v4.app.Fragment;

public class DeckListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new DeckListFragment();
    }
}
