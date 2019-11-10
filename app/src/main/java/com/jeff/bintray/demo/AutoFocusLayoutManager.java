package com.jeff.bintray.demo;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author Jeff
 * @describe
 * @date 2019/11/1.
 */
public class AutoFocusLayoutManager extends LinearLayoutManager {
    private static final String TAG = "AutoFocusLayoutManager";

    public AutoFocusLayoutManager(Context context) {
        super(context);
    }

    public AutoFocusLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public AutoFocusLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public View onInterceptFocusSearch(View focused, int direction) {
        // 这里处理上下键，如果处理左右键，使用View.FOCUS_LEFT和View.FOCUS_RIGHT
        if (direction == View.FOCUS_DOWN || direction == View.FOCUS_UP) {
            // 报错，不能直接这么使用
            // int currentPosition = getPosition(focused);
            int currentPosition = getPosition(getFocusedChild());
            int count = getItemCount();
            int lastVisiblePosition = findLastVisibleItemPosition();

            // 当前焦点已经处第1个item（从0开始）,第0个item为文字，如果按键向上，
            // 则先将RecyclerView滚动到顶部，然后通知Fragment将焦点设置到"照片"上
//            if (direction == View.FOCUS_UP && currentPosition == 1) {
//                Log.i(TAG, "onInterceptFocusSearch: fixed Focus to 照片------");
//                scrollToPosition(0);
//
//                return null;
//            }

            switch (direction) {
                case View.FOCUS_DOWN:
                    currentPosition++;
                    break;
                case View.FOCUS_UP:
                    currentPosition--;
                    break;
                default:
                    break;
            }

            Log.i(TAG, "onInterceptFocusSearch: current position=" + currentPosition);
            Log.i(TAG, "onInterceptFocusSearch: item count=" + count);
            Log.i(TAG, "onInterceptFocusSearch: lastVisiblePosition=" + lastVisiblePosition);

            if (direction == View.FOCUS_DOWN && currentPosition > lastVisiblePosition) {
                Log.i(TAG, "onInterceptFocusSearch: update...");
                scrollToPosition(currentPosition);
            }
        }
        return super.onInterceptFocusSearch(focused, direction);
    }
}
