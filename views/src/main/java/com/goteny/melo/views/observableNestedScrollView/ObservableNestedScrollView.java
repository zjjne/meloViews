package com.goteny.melo.views.observableNestedScrollView;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.goteny.melo.utils.log.LogMelo;


/**
 * 可监听滑动越界的NestedScrollView
 */
public class ObservableNestedScrollView extends NestedScrollView
{

    private OnOverScrolledListener mOnOverScrolledListener;
    private OnOverScrollEndedListener mOnOverScrollEndedListener;

    private float firstClampedY;
    private float downY;
    private float upY;

    private boolean isActionUp;
    private boolean isClampedY;
    private boolean isFirstClampedY;
    private boolean isCalledEndedCallback;


    public ObservableNestedScrollView(Context context)
    {
        super(context);
        resetVaules();
    }

    public ObservableNestedScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        resetVaules();
    }

    public ObservableNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        resetVaules();
    }


    /**
     * 重置各种值
     */
    private void resetVaules()
    {
        firstClampedY = 0;
        downY = 0;
        upY = 0;

        isActionUp = false;
        isClampedY = false;
        isFirstClampedY = true;
        isCalledEndedCallback = true;
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        boolean result = super.onTouchEvent(ev);

        int actionMasked = MotionEventCompat.getActionMasked(ev);

        isActionUp = false;

        float currentY = ev.getY();

        switch (actionMasked)
        {
            case MotionEvent.ACTION_DOWN:
                downY = currentY;
                break;
            case MotionEvent.ACTION_UP:
                isActionUp = true;
                upY = currentY;
                break;
        }


        LogMelo.i(getClass().getSimpleName(), "-----------currentY: " + currentY);

        if (isClampedY)
        {
            LogMelo.i(getClass().getSimpleName(), "isFirstClampedY: " + isFirstClampedY);

            isCalledEndedCallback = false;

            if(isFirstClampedY)
            {
                firstClampedY = currentY;
                isFirstClampedY = false;
            }

            float clampedDistanceY = currentY - firstClampedY;              //越界的长度，向下滑动时值为正，向上滑时为负值
            float distanceY = currentY - downY;                             //滑动的长度
            boolean isScrollUp = (distanceY < 0);                           //向上滑动时，Y的变化值(即滑动距离)为负数

            LogMelo.i(getClass().getSimpleName(), "--------------------------------------------------");
            LogMelo.i(getClass().getSimpleName(), "firstClampedY: " + firstClampedY);
            LogMelo.i(getClass().getSimpleName(), "origin clampedDistanceY: " + clampedDistanceY);
            LogMelo.i(getClass().getSimpleName(), "currentY: " + currentY);
            LogMelo.i(getClass().getSimpleName(), "upY: " + upY);
            LogMelo.i(getClass().getSimpleName(), "downY: " + downY);
            LogMelo.i(getClass().getSimpleName(), "distanceY: " + distanceY);

            if (mOnOverScrolledListener != null)
            {
                mOnOverScrolledListener.onOverScrolled(clampedDistanceY, isScrollUp);
            }

            if (isActionUp)
            {
                if (mOnOverScrollEndedListener != null)
                {
                    mOnOverScrollEndedListener.onOverScrollEnded(isScrollUp);
                }

                resetVaules();
            }
        }else
        {
            if (!isCalledEndedCallback)
            {
                float clampedDistanceY = currentY - firstClampedY;              //越界的长度，向下滑动时值为正，向上滑时为负值
                float distanceY = currentY - downY;                             //滑动的长度
                boolean isScrollUp = (distanceY < 0);                           //向上滑动时，Y的变化值(即滑动距离)为负数

                LogMelo.i(getClass().getSimpleName(), "--------------------------------------------------");
                LogMelo.i(getClass().getSimpleName(), "firstClampedY: " + firstClampedY);
                LogMelo.i(getClass().getSimpleName(), "origin clampedDistanceY: " + clampedDistanceY);
                LogMelo.i(getClass().getSimpleName(), "currentY: " + currentY);
                LogMelo.i(getClass().getSimpleName(), "upY: " + upY);
                LogMelo.i(getClass().getSimpleName(), "downY: " + downY);
                LogMelo.i(getClass().getSimpleName(), "distanceY: " + distanceY);

                if (mOnOverScrolledListener != null)
                {
                    mOnOverScrolledListener.onOverScrolled(clampedDistanceY, isScrollUp);
                }

                if (isActionUp)
                {
                    if (mOnOverScrollEndedListener != null)
                    {
                        mOnOverScrollEndedListener.onOverScrollEnded(isScrollUp);
                    }

                    resetVaules();
                }

                return true;
            }
        }

        return result;
    }


    /**
     * 滑动过程中，无论是否越界，此方法会一直被调用（若要判断是否越界，可用下面参数clampedX或clampedY判定）
     * 手指离开屏幕后就不会被执行
     * @param scrollX
     * @param scrollY Y轴方向的滚动距离（即【此View当前屏幕可见部分的顶部】到【此View自身顶部】的距离）
     * @param clampedX
     * @param clampedY 当Y轴拖动越界时clampedY会为true（只有在手指滑动过程中的越界才会为true。
     *                 在快速滑动手指离开屏幕后，view自动滚动到底部而出现的回弹效果，不算越界，此值不会变为true）
     */
    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY)
    {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);

        isClampedY = clampedY;
    }



    public OnOverScrolledListener getOnOverScrolledListener()
    {
        return mOnOverScrolledListener;
    }

    public void setOnOverScrolledListener(OnOverScrolledListener onOverScrolledListener)
    {
        this.mOnOverScrolledListener = onOverScrolledListener;
    }

    public OnOverScrollEndedListener getOnOverScrollEndedListener()
    {
        return mOnOverScrollEndedListener;
    }

    public void setOnOverScrollEndedListener(OnOverScrollEndedListener onOverScrollEndedListener)
    {
        this.mOnOverScrollEndedListener = onOverScrollEndedListener;
    }



    //滑动越界的监听
    public interface OnOverScrolledListener
    {
        /**
         * 只要在在手指滑动过程中View有产生越界，就一直会回调onOverScrolled
         * @param clampedDistanceY 滑动越界的长度
         * @param isScrollUp 是否为向上滑动，true向上滑动，false向下滑动
         */
        void onOverScrolled(float clampedDistanceY, boolean isScrollUp);
    }


    //滑动越界结束的事件监听（即滑动越界后手指离开屏幕监听）
    public interface OnOverScrollEndedListener
    {
        /**
         * 滑动越界后手指离开屏幕的事件回调
         * @param isScrollUp 是否为向上滑动，true向上滑动，false向下滑动
         */
        void onOverScrollEnded(boolean isScrollUp);
    }
}
