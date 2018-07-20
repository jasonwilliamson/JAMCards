package com.co.jammcards.jammcards;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static android.graphics.Bitmap.createBitmap;

public class CardFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private static final String ARG_CARD_ID = "card_id";
    private static final int REQUEST_PHOTO = 0;

    private Card mCard;
    private File mPhotoFile;
    private File mBackPhotoFile;
    private File mDrawingFile;
    private Deck mCurrentDeck;
    private EditText mCardText;
    private CheckBox mShowCheckBox;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private DrawingView mDrawingView;
    private ImageView mSave;
    private FloatingActionButton mFloatingActionButton;
    private boolean mIsFrontView;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Paint mPaint;
    private int prvX, prvY;
    private boolean mDelete;
    private boolean mIsChanged;


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
        mBackPhotoFile = CardLab.get(getActivity()).getBackPhotoFile(mCard);
        //mDrawingFile = CardLab.get(getActivity()).getPhotoFile(mCard);

        //((CardPagerActivity) getActivity()).setSubTitile("Showing front");
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
            Uri uri = null;
            if(mIsFrontView){
                uri = FileProvider.getUriForFile(getActivity(),
                        "com.co.jammcards.jammcards.fileprovider",
                        mPhotoFile);
            }else{
                uri = FileProvider.getUriForFile(getActivity(),
                        "com.co.jammcards.jammcards.fileprovider",
                        mBackPhotoFile);
            }

            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_card, container, false);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(10);

        mDelete = false;
        mIsChanged = false;

        mShowCheckBox = (CheckBox)v.findViewById(R.id.card_show);
        mShowCheckBox.setChecked(mCard.isShown());
        mShowCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCard.setShown(isChecked);
            }
        });

        PackageManager packageManager = getActivity().getPackageManager();

        mIsFrontView = true;
        if(savedInstanceState != null){
            mIsFrontView = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateSubtitle();

        mCardText = (EditText) v.findViewById(R.id.card_text);
        if(mIsFrontView) {
            mCardText.setText(mCard.getText());
        }else{
            mCardText.setText(mCard.getBackText());
        }
        mCardText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mIsFrontView) {
                    mCard.setText(s.toString());
                }else{
                    mCard.setBackText(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mFloatingActionButton = (FloatingActionButton)
                v.findViewById(R.id.floating_flip_card_action_button);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipCard();
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
                Uri uri = null;
                if(mIsFrontView) {
                    uri = FileProvider.getUriForFile(getActivity(),
                            "com.co.jammcards.jammcards.fileprovider",
                            mPhotoFile);
                    captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                }else{
                    uri = FileProvider.getUriForFile(getActivity(),
                            "com.co.jammcards.jammcards.fileprovider",
                            mBackPhotoFile);
                    captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                }

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

        //mDrawingView = (DrawingView) v.findViewById(R.id.drawing_image);
        //mSave = (ImageView) v.findViewById(R.id.drawing_image2);
        //updateDrawingView();

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mIsFrontView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_card, menu);

        MenuItem subtitleItem = menu.findItem(R.id.card_subtitle);
        if(mIsFrontView){
            subtitleItem.setTitle(R.string.front_subtitle);
        }else{
            subtitleItem.setTitle(R.string.back_subtitle);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.delete_card:  //TODO implement delete card!!
                showDeleteCardDialog();
                /*CardLab.get(getActivity()).deleteCard(mCard);
                Intent intent = CardListActivity
                        .newIntent(getActivity(), mCurrentDeck.getId());
                startActivity(intent);*/
                return true;

            case R.id.card_subtitle: {
                flipCard();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        saveBitmap();;
    }

    private void saveBitmap(){
        if(mBitmap != null && mIsChanged) {
            if (mIsFrontView) {
                CardLab.get(getActivity()).saveBitmap(mCard, mBitmap);
                Log.d("CardFrag", "ActionUP");
            } else {
                CardLab.get(getActivity()).saveBackBitmap(mCard, mBitmap);
            }
            mIsChanged = false;
        }
    }

    private void flipCard(){
        saveBitmap();
        mIsFrontView = !mIsFrontView;
        getActivity().invalidateOptionsMenu();;
        updateSubtitle();
        updatePhotoView();
        updateCardText();
    }

    private void showDeleteCardDialog(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        if(!mDelete) {  //in event of stall while deleting ...prevent double clicks
                            mDelete = true;
                            /*CardLab.get(getActivity()).deleteCard(mCard);
                            Intent intent = CardListActivity
                                    .newIntent(getActivity(), mCurrentDeck.getId());


                            getActivity().getFragmentManager().popBackStack();
                            startActivity(intent);*/
                            CardLab.get(getActivity()).deleteCard(mCard);
                            getActivity().finish();
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };


        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage("This action will delete the current card. Continue?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
        alert.setTitle("Delete Card");


    }

    private void updateCardText() {

        if(mIsFrontView) {
            mCardText.setText(mCard.getText());
        }else{
            mCardText.setText(mCard.getBackText());
        }
    }

    private void updateSubtitle() {

        String subtitle = "Front View";
        if(mIsFrontView){
            subtitle = "Front View";
        }else{
            subtitle = "Back View";
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    public Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void updatePhotoView() {

        Bitmap.Config config;
        Bitmap bitmap = null;
        String imagePath  = "";
        mCanvas = null;
        if(mIsFrontView){
            if (mPhotoFile == null || !mPhotoFile.exists()) {
                mPhotoView.setImageDrawable(null);
            } else {
                bitmap = PictureUtils.getScaledBitmap(
                        mPhotoFile.getPath(), getActivity());
                imagePath = mPhotoFile.getAbsolutePath();
            }
        }else{
            if (mBackPhotoFile == null || !mBackPhotoFile.exists()) {
                mPhotoView.setImageDrawable(null);
            } else {
                bitmap = PictureUtils.getScaledBitmap(
                        mBackPhotoFile.getPath(), getActivity());
                imagePath = mBackPhotoFile.getAbsolutePath();
            }
        }

        if(bitmap != null){


            /*if(bitmap.getConfig() != null){
                config = bitmap.getConfig();
            }else{
                config = Bitmap.Config.ARGB_8888;
            }*/
            //mBitmap = createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
            //modifyOrientation
            Bitmap workingBitmap = null;
            try {
                workingBitmap = modifyOrientation(bitmap, imagePath);
            }catch (IOException e){
                e.printStackTrace();
            }

            mBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
            mCanvas = new Canvas(mBitmap);
            //mCanvas.drawBitmap(bitmap, 0, 0, null);

            mPhotoView.setImageBitmap(mBitmap);

            mPhotoView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                    int action = event.getAction();
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            prvX = x;
                            prvY = y;
                            drawOnProjectedBitMap((ImageView) v,mBitmap , prvX, prvY, x, y);
                            mIsChanged = true;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            drawOnProjectedBitMap((ImageView) v, mBitmap, prvX, prvY, x, y);
                            prvX = x;
                            prvY = y;
                            break;
                        case MotionEvent.ACTION_UP:
                            drawOnProjectedBitMap((ImageView) v, mBitmap, prvX, prvY, x, y);
                            //CardLab.saveBitmap(mBitmap);
                            break;
                    }
                    /*
                     * Return 'true' to indicate that the event have been consumed.
                     * If auto-generated 'false', your code can detect ACTION_DOWN only,
                     * cannot detect ACTION_MOVE and ACTION_UP.
                     */
                    return true;
                }
            });
        }
    }

    /*
    Project position on ImageView to position on Bitmap draw on it
     */

    private void drawOnProjectedBitMap(ImageView iv, Bitmap bm,
                                       float x0, float y0, float x, float y){
        if(x<0 || y<0 || x > iv.getWidth() || y > iv.getHeight()){
            //outside ImageView
            return;
        }else{

            float ratioWidth = (float)bm.getWidth()/(float)iv.getWidth();
            float ratioHeight = (float)bm.getHeight()/(float)iv.getHeight();

            mCanvas.drawLine(
                    x0 * ratioWidth,
                    y0 * ratioHeight,
                    x * ratioWidth,
                    y * ratioHeight,
                    mPaint);
            mPhotoView.invalidate();
        }
    }

    private void updateDrawingView() {
        View content = mDrawingView;
        content.setDrawingCacheEnabled(true);
        content.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = content.getDrawingCache();
        mSave.setImageBitmap(bitmap);

    }




}
