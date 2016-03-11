package com.blackcat.library;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

public class FlowTagLayout extends ViewGroup {

    private final int default_border_color = Color.rgb(0x49, 0xC1, 0x20);
    private final int default_text_color = Color.rgb(0x49, 0xC1, 0x20);
    private final int default_background_color = Color.WHITE;
    private final int default_dash_border_color = Color.rgb(0xAA, 0xAA, 0xAA);
    private final int default_input_hint_color = Color.argb(0x80, 0x00, 0x00, 0x00);
    private final int default_input_text_color = Color.argb(0xDE, 0x00, 0x00, 0x00);
    private final int default_checked_border_color = Color.rgb(0x49, 0xC1, 0x20);
    private final int default_checked_text_color = Color.WHITE;
    private final int default_checked_marker_color = Color.WHITE;
    private final int default_checked_background_color = Color.rgb(0x49, 0xC1, 0x20);
    private final int default_pressed_background_color = Color.rgb(0xED, 0xED, 0xED);
    private final float default_border_stroke_width;
    private final float default_text_size;
    private final float default_horizontal_spacing;
    private final float default_vertical_spacing;
    private final float default_horizontal_padding;
    private final float default_vertical_padding;

    /**
     * The tag outline border color.
     */
    private int borderColor;

    /**
     * The tag text color.
     */
    private int textColor;

    /**
     * The tag background color.
     */
    private int backgroundColor;

    /**
     * The dash outline border color.
     */
    private int dashBorderColor;

    /**
     * The  input tag hint text color.
     */
    private int inputHintColor;

    /**
     * The input tag type text color.
     */
    private int inputTextColor;

    /**
     * The checked tag outline border color.
     */
    private int checkedBorderColor;

    /**
     * The check text color
     */
    private int checkedTextColor;

    /**
     * The checked marker color.
     */
    private int checkedMarkerColor;

    /**
     * The checked tag background color.
     */
    private int checkedBackgroundColor;

    /**
     * The tag background color, when the tag is being pressed.
     */
    private int pressedBackgroundColor;

    /**
     * The tag outline border stroke width, default is 0.5dp.
     */
    private float borderStrokeWidth;

    /**
     * The tag text size, default is 13sp.
     */
    private float textSize;

    /**
     * The horizontal tag spacing, default is 8.0dp.
     */
    private int horizontalSpacing;

    /**
     * The vertical tag spacing, default is 4.0dp.
     */
    private int verticalSpacing;

    /**
     * The horizontal tag padding, default is 12.0dp.
     */
    private int horizontalPadding;

    /**
     * The vertical tag padding, default is 3.0dp.
     */
    private int verticalPadding;


    /** Listener used to dispatch tag change event. */
//    private OnTagChangeListener mOnTagChangeListener;

    /**
     * Listener used to dispatch tag click event.
     */
//    private OnTagClickListener mOnTagClickListener;
    public FlowTagLayout(Context context) {
        this(context, null);
    }

    public FlowTagLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.flowTagStyle);
    }

    public FlowTagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_border_stroke_width = dp2px(0.5f);
        default_text_size = sp2px(13.0f);
        default_horizontal_spacing = dp2px(8.0f);
        default_vertical_spacing = dp2px(4.0f);
        default_horizontal_padding = dp2px(12.0f);
        default_vertical_padding = dp2px(3.0f);

        // Load styled attributes.
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowTag, defStyleAttr, R.style.FlowTag);
        try {
            borderColor = a.getColor(R.styleable.FlowTag_ftag_borderColor, default_border_color);
            textColor = a.getColor(R.styleable.FlowTag_ftag_textColor, default_text_color);
            backgroundColor = a.getColor(R.styleable.FlowTag_ftag_backgroundColor, default_background_color);
/*            dashBorderColor = a.getColor(R.styleable.TagGroup_atg_dashBorderColor, default_dash_border_color);*/

            checkedBorderColor = a.getColor(R.styleable.FlowTag_ftag_checkedBorderColor, default_checked_border_color);
            checkedTextColor = a.getColor(R.styleable.FlowTag_ftag_checkedTextColor, default_checked_text_color);
            checkedMarkerColor = a.getColor(R.styleable.FlowTag_ftag_checkedMarkerColor, default_checked_marker_color);
            checkedBackgroundColor = a.getColor(R.styleable.FlowTag_ftag_checkedBackgroundColor, default_checked_background_color);
            pressedBackgroundColor = a.getColor(R.styleable.FlowTag_ftag_pressedBackgroundColor, default_pressed_background_color);
            borderStrokeWidth = a.getDimension(R.styleable.FlowTag_ftag_borderStrokeWidth, default_border_stroke_width);
            textSize = a.getDimension(R.styleable.FlowTag_ftag_textSize, default_text_size);
            horizontalSpacing = (int) a.getDimension(R.styleable.FlowTag_ftag_horizontalSpacing, default_horizontal_spacing);
            verticalSpacing = (int) a.getDimension(R.styleable.FlowTag_ftag_verticalSpacing, default_vertical_spacing);
            horizontalPadding = (int) a.getDimension(R.styleable.FlowTag_ftag_horizontalPadding, default_horizontal_padding);
            verticalPadding = (int) a.getDimension(R.styleable.FlowTag_ftag_verticalPadding, default_vertical_padding);
        } finally {
            a.recycle();
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //获取Padding
        // 获得它的父容器为它设置的测量模式和大小
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //FlowLayout最终的宽度和高度值
        int resultWidth = 0;
        int resultHeight = 0;

        int row = 0; // The row counter.
        int rowWidth = 0; // Calc the current row width.
        int rowMaxHeight = 0; // Calc the max tag height, in current row.

        final int count = getChildCount();
        for (int i=0; i< count; i++) {
            View childView = getChildAt(i);
            //测量每一个子view的宽和高
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            //获取到测量的宽和高
            final int childWidth = childView.getMeasuredWidth();
            final int childHeight = childView.getMeasuredHeight();

            if (childView.getVisibility() != GONE) {
                rowWidth += childWidth;
                if (rowWidth > widthSize) {  // //如果当前一行的宽度加上要加入的子view的宽度大于父容器给的宽度，就换行
                    rowWidth = childWidth; // The next row width.
                    resultHeight += rowMaxHeight + verticalSpacing;
                    rowMaxHeight = childHeight; // The next row max height.
                } else {  // This line.
                    rowMaxHeight = Math.max(rowMaxHeight, childHeight);
                }
                rowWidth += horizontalSpacing;
            }
            // Account for the last row height.
            resultHeight += rowMaxHeight;

            // Account for the padding too.
            resultHeight += getPaddingTop() + getPaddingBottom();

            // If the tags grouped in one row, set the width to wrap the tags.
            if (row == 0) {
                resultWidth = rowWidth;
                resultWidth += getPaddingLeft() + getPaddingRight();
            } else {// If the tags grouped exceed one line, set the width to match the parent.
                resultWidth = widthSize;
            }

            setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : resultWidth,
                    heightMode == MeasureSpec.EXACTLY ? heightSize : resultHeight);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int parentLeft = getPaddingLeft();
        final int parentRight = r - l - getPaddingRight();
        final int parentTop = getPaddingTop();
        final int parentBottom = b - t - getPaddingBottom();

        int childLeft = parentLeft;
        int childTop = parentTop;

        int rowMaxHeight = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View childView = getChildAt(i);
            final int childWidth = childView.getMeasuredWidth();
            final int childHeight = childView.getMeasuredHeight();
            if (childView.getVisibility() != GONE) {
                if (childLeft + childWidth > parentRight) { // Next line
                    childLeft = parentLeft;
                    childTop += rowMaxHeight + verticalSpacing;
                    rowMaxHeight = childHeight;
                } else {
                    rowMaxHeight = Math.max(rowMaxHeight, childHeight);
                }

                childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

                childLeft += childWidth + horizontalSpacing;
            }
        }
    }


    public float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public float sp2px(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getResources().getDisplayMetrics());
    }
}
