package ubivelox.com.jumping.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by UBIVELOX on 2018-04-12.
 */

public class CircleAnimIndicator extends LinearLayout {
    /*******************************************************************************
     * Variable.
     *******************************************************************************/
    private Context mContext;
    private int itemMargin = 10;
    private int aniDuration = 250;
    private int mDefaultCircle;
    private int mSelectCircle;
    private ImageView[] imageDot;

    /*******************************************************************************
     * Constructor.
     *******************************************************************************/
    public CircleAnimIndicator(Context context) {
        super(context);
        mContext = context;
    }

    public CircleAnimIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    /*******************************************************************************
     * public Method.
     *******************************************************************************/
    public void setAnimationDuration(int time) {
        aniDuration = time;
    }

    public void setItemMargin(int margin) {
        itemMargin = margin;
    }

    /**
     * 기본 점 생성
     * @param count 점의 갯수
     * @param defaultCircle 점의 이미지
     */
    public void createDotPanel(int count , int defaultCircle , int selectCircle) {

        mDefaultCircle = defaultCircle;
        mSelectCircle = selectCircle;

        imageDot = new ImageView[count];

        for (int i = 0; i < count; i++) {

            imageDot[i] = new ImageView(mContext);
            LayoutParams params = new LayoutParams
                    (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.topMargin = itemMargin;
            params.bottomMargin = itemMargin;
            params.leftMargin = itemMargin;
            params.rightMargin = itemMargin;
            params.gravity = Gravity.CENTER;

            imageDot[i].setLayoutParams(params);
            imageDot[i].setImageResource(defaultCircle);
            imageDot[i].setTag(imageDot[i].getId(), false);
            this.addView(imageDot[i]);
        }


        //첫인덱스 선택
        selectDot(0);
    }

    /**
     * 선택된 점 표시
     * @param position
     */
    public void selectDot(int position) {

        for (int i = 0; i < imageDot.length; i++) {
            if (i == position) {
                imageDot[i].setImageResource(mSelectCircle);
                selectScaleAnim(imageDot[i],1f,1.5f);
            } else {

                if((boolean) imageDot[i].getTag(imageDot[i].getId())){
                    imageDot[i].setImageResource(mDefaultCircle);
                    defaultScaleAnim(imageDot[i], 1.5f, 1f);
                }
            }
        }
    }

    /*******************************************************************************
     * Inner Method.
     *******************************************************************************/
    /**
     * 선택된 점의 애니메이션
     * @param view
     * @param startScale
     * @param endScale
     */
    private void selectScaleAnim(View view, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                startScale, endScale,
                startScale, endScale,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true);
        anim.setDuration(aniDuration);
        view.startAnimation(anim);
        view.setTag(view.getId(),true);
    }

    /**
     * 선택되지 않은 점의 애니메이션
     * @param view
     * @param startScale
     * @param endScale
     */
    private void defaultScaleAnim(View view, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                startScale, endScale,
                startScale, endScale,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true);
        anim.setDuration(aniDuration);
        view.startAnimation(anim);
        view.setTag(view.getId(),false);
    }

}
