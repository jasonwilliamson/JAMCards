package com.co.jammcards.jammcards;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class CardFragment extends Fragment {

    private static final String ARG_CARD_ID = "card_id";
    private static final int REQUEST_PHOTO = 0;

    private Card mCard;
    private File mPhotoFile;
    private Deck mCurrentDeck;
    private EditText mCardText;
    private CheckBox mShowCheckBox;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;


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

        mCurrentDeck = DeckLab.get(getActivity()).getCurrentDeck();

        UUID cardId = (UUID) getArguments().getSerializable(ARG_CARD_ID);
        mCard = CardLab.get(getActivity()).getCard(cardId);

        mPhotoFile = CardLab.get(getActivity()).getPhotoFile(mCard);

        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        CardLab.get(getActivity()).updateCard(mCard);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_PHOTO){
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.co.jammcards.jammcards.fileprovider",
                    mPhotoFile);
            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_card, container, false);

        mShowCheckBox = (CheckBox)v.findViewById(R.id.card_show);
        mShowCheckBox.setChecked(mCard.isShown());
        mShowCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCard.setShown(isChecked);
            }
        });

        PackageManager packageManager = getActivity().getPackageManager();

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

        mPhotoButton = (ImageButton) v.findViewById(R.id.card_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.co.jammcards.jammcards.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.card_image);
        updatePhotoView();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_card, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.delete_card:  //TODO implement delete card!!
                CardLab.get(getActivity()).deleteCard(mCard);
                Intent intent = CardListActivity
                        .newIntent(getActivity(), mCurrentDeck.getId());
                startActivity(intent);

                /*mCurrentDeck.deleteCard(mCard);
                Intent intent = CardListActivity
                        .newIntent(getActivity(), mCurrentDeck.getId());
                startActivity(intent);*/
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
