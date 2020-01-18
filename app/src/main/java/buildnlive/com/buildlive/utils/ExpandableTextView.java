package buildnlive.com.buildlive.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import buildnlive.com.buildlive.R;

public class ExpandableTextView extends TextView {
    private static final int DEFAULT_TRIM_LENGTH = 150;
    private static final String ELLIPSIS = "...";
    private static final String TAG = "ExpandableTextView";
    private CharSequence originalText;
    private CharSequence trimmedText;
    private BufferType bufferType;
    private boolean trim = true;
    private int trimLength;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        this.trimLength = typedArray.getInt(R.styleable.ExpandableTextView_trimLength, DEFAULT_TRIM_LENGTH);
        typedArray.recycle();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                trim = !trim;
                setText();
                requestFocusFromTouch();
            }
        });
    }

    private void setText() {
        super.setText(getDisplayableText(), bufferType);
    }

    private CharSequence getDisplayableText() {
        return trim ? trimmedText : originalText;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        originalText = text;
        trimmedText = getTrimmedText(text);
        bufferType = type;
        setText();
    }

    private CharSequence getTrimmedText(CharSequence text) {
        if (originalText != null && originalText.length() > trimLength) {
            return new SpannableStringBuilder(originalText, 0, trimLength + 1).append(ELLIPSIS);
        } else {
            return originalText;
        }
    }

    private void init(Context context) {
        try {
            if (context!=null) {
                Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpanSans-Regular.ttf");
                setTypeface(myTypeface);
            }
        }
        catch (Exception e) {

            Log.e(TAG, e.getMessage(), e);
        }
    }

    public CharSequence getOriginalText() {
        return originalText;
    }

    public void setTrimLength(int trimLength) {
        this.trimLength = trimLength;
        trimmedText = getTrimmedText(originalText);
        setText();
    }

    public int getTrimLength() {
        return trimLength;
    }
}