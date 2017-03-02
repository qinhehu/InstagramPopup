package com.example.qinhe.inspopup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by QinHe on 2017/3/1.
 */

public class InsDialog extends Dialog {

    private LinearLayout tapbar;
    private ImageView imag;
    private int mLastChose;
    private String pathUrl;

    private PopupWindow mPopupWindow;
    private String[] toasts = {"Like", "View Profile", "Share", "Options"};

    public InsDialog(final Context context) {
        super(context);
        mLastChose = -1;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.insdialog);
        setCanceledOnTouchOutside(true);

        tapbar = (LinearLayout) findViewById(R.id.tapbar);
        imag = (ImageView) findViewById(R.id.image_meizi);
    }

    @Override
    public void show() {
        super.show();
        int padding = (int) getContext().getResources().getDimension(R.dimen.activity_horizontal_margin);
        getWindow().getDecorView().setPadding(padding, padding, padding, padding);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int rectX = (int) (event.getX() - (width - getWindow().getDecorView().getWidth()) / 2);
        int rectY = (int) (event.getY() - (height - getWindow().getDecorView().getHeight()) / 2 - tapbar.getY() - statusBarHeight);

        Rect llRect = new Rect();
        tapbar.getHitRect(llRect);
        if (!llRect.contains(rectX, rectY + (int) tapbar.getY())) {
            mLastChose = -1;
            if (mPopupWindow != null) {
                mPopupWindow.dismiss();
            }
        }

        int childCount = tapbar.getChildCount();
        for (int i = 0; i < childCount; i++) {
            Rect rect = new Rect();
            View view = tapbar.getChildAt(i);
            view.getHitRect(rect);
            if (rect.contains(rectX, rectY)) {
                if (mLastChose != i) {
                    mLastChose = i;
                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

                    View popupView = getLayoutInflater().inflate(R.layout.popupwindow, null);
                    TextView tv = (TextView)popupView.findViewById(R.id.toast_txt);
                    tv.setText(toasts[i]);
                    if (mPopupWindow != null) {
                        mPopupWindow.dismiss();
                    }
                    mPopupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT
                            , WindowManager.LayoutParams.WRAP_CONTENT, true);
                    mPopupWindow.setAnimationStyle(R.style.AnimationToast);
                    mPopupWindow.setTouchable(true);
                    mPopupWindow.setOutsideTouchable(true);
                    mPopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    mPopupWindow.showAtLocation(view
                            , Gravity.LEFT
                            , (int) view.getX() + view.getWidth() / 2-mPopupWindow.getWidth()/2-mPopupWindow.getContentView().getMeasuredWidth()/4
                            , (int) tapbar.getY() / 3);
                }
            }
        }


        return super.onTouchEvent(event);
    }

    @Override
    public void dismiss() {
        mLastChose = -1;
        super.dismiss();
    }

    public void setImag(String url) {
        pathUrl = url;
        loadImageView(pathUrl, R.drawable.img_default_meizi, imag);
    }

    private void loadImageView(String url, int defresId, ImageView img) {
        if (null != img) {
            if (!TextUtils.isEmpty(url)) {
                DrawableTypeRequest request = Glide.with(getContext()).load(url);
                request.crossFade();
                request.placeholder(defresId);
                request.diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img);
            } else {
                img.setImageResource(defresId);
            }
        }
    }
}
