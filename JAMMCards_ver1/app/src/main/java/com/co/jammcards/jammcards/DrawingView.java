package com.co.jammcards.jammcards;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.*;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera.Size;
import android.media.ThumbnailUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class DrawingView extends View implements OnTouchListener {
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Path mPath;
    private Paint mPaint;

    ArrayList<Pair<Path, Paint>> paths = new ArrayList<Pair<Path, Paint>>();
    //ArrayList<Pair<Path, Paint>> undonePaths = new ArrayList<Pair<Path, Paint>>();

    private float mX, mY;

    private static final float TOUCH_TOLERANCE = 4;

    public static boolean isEraserActive = false;

    public DrawingView(Context context, AttributeSet attr) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setBackgroundColor(Color.TRANSPARENT);
        this.setOnTouchListener(this);
        onCanvasInitialization();
        setDrawingCacheEnabled( true );
    }

    public void onCanvasInitialization() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.parseColor("#000000"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20);

        //mCanvas = new Canvas();
        //mCanvas.drawColor(Color.TRANSPARENT);
        //mCanvas.drawColor(Color.argb(0, 255, 255, 255));

        mPath = new Path();
        Paint newPaint = new Paint(mPaint);
        paths.add(new Pair<Path, Paint>(mPath, newPaint));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.argb(0, 255, 255, 255));
    }

    public boolean onTouch(View arg0, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Pair<Path, Paint> p : paths) {
            canvas.drawPath(p.first, p.second);
        }
    }

    private void touch_start(float x, float y) {

        if (isEraserActive) {
            mPaint.setColor(Color.WHITE);
            mPaint.setStrokeWidth(6);
            Paint newPaint = new Paint(mPaint); // Clones the mPaint object
            paths.add(new Pair<Path, Paint>(mPath, newPaint));

        } else {
            mPaint.setColor(Color.BLACK);
            mPaint.setStrokeWidth(20);
            Paint newPaint = new Paint(mPaint); // Clones the mPaint object
            paths.add(new Pair<Path, Paint>(mPath, newPaint));

        }


        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);

        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);

        // kill this so we don't double draw
        mPath = new Path();
        Paint newPaint = new Paint(mPaint); // Clones the mPaint object
        paths.add(new Pair<Path, Paint>(mPath, newPaint));
    }

    /*public Bitmap updateDrawingView() {

        Bitmap save = getDrawingCache();
        save = ThumbnailUtils.extractThumbnail(whatTheUserDrewBitmap, 256, 256);
        return save;

        //ImageView testarea = findViewById(R.id.drawing_image);
        //testarea.setImageBitmap( save );
    }*/

}