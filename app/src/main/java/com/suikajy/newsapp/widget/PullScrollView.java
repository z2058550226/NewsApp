package com.suikajy.newsapp.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.suikajy.core.animation.AnimationHelper;
import com.suikajy.newsapp.R;

/**
 * Created by suikajy on 2017/4/18.
 * 可以拉动的ScrollView, 支持NestedScroll
 */

public class PullScrollView extends NestedScrollView {

    private View mFootView;
    private OnPullListener mPullListener;
    private boolean mIsPullStatus = false;
    private float mLastY;
    private int mPullCriticalDistance;


    public PullScrollView(Context context) {
        super(context, null);
    }

    public PullScrollView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public PullScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPullCriticalDistance = getResources().getDimensionPixelSize(R.dimen.pull_critical_distance);
    }

    /**
     * 当滑动的位置改变时, 如果滑动的距离已经到底, 就会触发拉动监听的正在拉动状态
     *
     * @param l    水平的Scroll距离
     * @param t    竖直的Scroll距离
     * @param oldl 上一个水平距离
     * @param oldt 上一个竖直距离
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (t >= (getChildAt(0).getMeasuredHeight() - getHeight()) && mPullListener != null) {
            mPullListener.isDoPull();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 在处理滑动的时候如果滑动到底, 会将mIsPullStatus置为true
     * 如果继续向上滑的话就会改变footerView的高度, 进而实现拉出footerView的效果
     * <p>
     * 如果footerView的高度超过mPullCriticalDistance就会判断是否执行打开下一则新闻的功能
     * <p>
     * 对Action Cancel做出了一些处理
     * 当手势抬起/取消的时候如果没有下则新闻, 则footerView会通过自动向下消失的属性动画退出界面
     * 如果有下则新闻, 则什么也不做. 打开下一则新闻的逻辑交由接口来实现
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_MOVE:
                if (!mIsPullStatus) {
                    if (getScrollY() >= (getChildAt(0).getMeasuredHeight() - getHeight()) || getChildAt(0).getMeasuredHeight() < getHeight()) {
                        if (mPullListener != null && mPullListener.isDoPull()) {
                            mIsPullStatus = true;
                            mLastY = ev.getY();
                        }
                    }
                } else if (mLastY < ev.getY()) {
                    mIsPullStatus = false;
                    _pullFootView(0);
                } else {
                    float offsetY = mLastY - ev.getY();
                    _pullFootView(offsetY);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mIsPullStatus) {
                    if (mFootView.getHeight() > mPullCriticalDistance && mPullListener != null) {
                        if (!mPullListener.handlePull()) {
                            AnimationHelper.doClipViewHeight(mFootView, mFootView.getHeight(), 0, 200);
                        }
                    } else {
                        AnimationHelper.doClipViewHeight(mFootView, mFootView.getHeight(), 0, 200);
                    }
                    mIsPullStatus = false;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    public void setFootView(View footView) {
        mFootView = footView;
    }

    public void setPullListener(OnPullListener pullListener) {
        mPullListener = pullListener;
    }

    /**
     * 对拉动做了点改良, 如果拉动距离太大的话就不会再往上拉动
     * @param offsetY 拉动距离, 单位px
     */
    private void _pullFootView(float offsetY) {
        if (mFootView != null) {
            ViewGroup.LayoutParams layoutParams = mFootView.getLayoutParams();
            layoutParams.height = (int) (offsetY * 1 / 2);
            mFootView.setLayoutParams(layoutParams);
        }
    }

    public interface OnPullListener {
        /**
         * 当滑动到底的时候会回调这个函数
         *
         * @return true就是可以执行拉出footer的动作, false的话就和正常的ScrollView没有区别了
         */
        boolean isDoPull();

        /**
         * 当拉动footer的手势动作抬起(Action up)的时候
         *
         * @return 返回true表示处理了这个拉动事件, false的话就由当前类做默认处理
         */
        boolean handlePull();
    }

}
