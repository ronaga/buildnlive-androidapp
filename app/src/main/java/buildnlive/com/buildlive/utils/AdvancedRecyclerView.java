package buildnlive.com.buildlive.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import buildnlive.com.buildlive.R;

public class AdvancedRecyclerView extends RecyclerView {
    private int mMaxHeight;

    public AdvancedRecyclerView(Context context) {
        super(context);
    }

    public AdvancedRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public AdvancedRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.AdvancedRecyclerView);
        mMaxHeight = arr.getLayoutDimension(R.styleable.AdvancedRecyclerView_maxHeight, mMaxHeight);
        arr.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMaxHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getmMaxHeight() {
        return mMaxHeight;
    }

    public void setmMaxHeight(int mMaxHeight) {
        this.mMaxHeight = mMaxHeight;
    }
}