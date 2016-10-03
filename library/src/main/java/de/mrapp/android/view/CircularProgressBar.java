/*
 * Copyright 2015 - 2016 Michael Rapp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.mrapp.android.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;

import de.mrapp.android.util.ThemeUtil;
import de.mrapp.android.view.drawable.CircularProgressDrawable;

/**
 * A circular progress bar, which has been designed according to the Material design guidelines.
 *
 * Refer to http://www.google.com/design/spec/components/progress-activity.html#
 * progress-activity-types-of-indicators for further information on the Material design guidelines.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class CircularProgressBar extends View {

    /**
     * The color of the circular progress bar.
     */
    private int color;

    /**
     * The thickness of the circular progress bar.
     */
    private int thickness;

    /**
     * The drawable, which is shown by the view.
     */
    private CircularProgressDrawable circularProgressDrawable;

    /**
     * Initializes the view.
     *
     * @param attributeSet
     *         The attribute set, the view's attributes should be obtained from, as an instance of
     *         the type {@link AttributeSet} or null, if no attributes should be obtained
     */
    private void initialize(@Nullable final AttributeSet attributeSet) {
        obtainStyledAttributes(attributeSet);
        initializeDrawable();
    }

    /**
     * Obtains the view's attributes from a specific attribute set.
     *
     * @param attributeSet
     *         The attribute set, the view's attributes should be obtained from, as an instance of
     *         the type {@link AttributeSet} or null, if no attributes should be obtained
     */
    private void obtainStyledAttributes(@Nullable final AttributeSet attributeSet) {
        TypedArray typedArray =
                getContext().obtainStyledAttributes(attributeSet, R.styleable.CircularProgressBar);

        try {
            obtainColor(typedArray);
            obtainThickness(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Obtains the circular progress bar's color from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the color should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainColor(@NonNull final TypedArray typedArray) {
        int defaultColor = ThemeUtil.getColor(getContext(), R.attr.colorAccent);
        color = typedArray.getColor(R.styleable.CircularProgressBar_android_color, defaultColor);
    }

    /**
     * Obtains the progress bar's thickness from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the thickness should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainThickness(@NonNull final TypedArray typedArray) {
        int defaultThickness = getContext().getResources()
                .getDimensionPixelSize(R.dimen.circular_progress_bar_thickness_normal);
        thickness = typedArray
                .getDimensionPixelSize(R.styleable.CircularProgressBar_android_thickness,
                        defaultThickness);
    }

    /**
     * Initializes the drawable, which is shown by the view.
     */
    private void initializeDrawable() {
        circularProgressDrawable = new CircularProgressDrawable(getColor(), getThickness());
        circularProgressDrawable.setCallback(this);
    }

    /**
     * Creates a new circular progress bar, which has been designed according to the Material design
     * guidelines.
     *
     * @param context
     *         The context, the view should belong to, as an instance of the class {@link Context}.
     *         The context may not be null
     */
    public CircularProgressBar(@NonNull final Context context) {
        this(context, null);
    }

    /**
     * Creates a new circular progress bar, which has been designed according to the Material design
     * guidelines.
     *
     * @param context
     *         The context, the view should belong to, as an instance of the class {@link Context}.
     *         The context may not be null
     * @param attributeSet
     *         The attribute set, the view's attributes should be obtained from, as an instance of
     *         the type {@link AttributeSet} or null, if no attributes should be obtained
     */
    public CircularProgressBar(@NonNull final Context context,
                               @Nullable final AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(attributeSet);
    }

    /**
     * Creates a new circular progress bar, which has been designed according to the Material design
     * guidelines.
     *
     * @param context
     *         The context, the view should belong to, as an instance of the class {@link Context}.
     *         The context may not be null
     * @param attributeSet
     *         The attribute set, the view's attributes should be obtained from, as an instance of
     *         the type {@link AttributeSet} or null, if no attributes should be obtained
     * @param defaultStyle
     *         The default style to apply to this view. If 0, no style will be applied (beyond what
     *         is included in the theme). This may either be an attribute resource, whose value will
     *         be retrieved from the current theme, or an explicit style resource
     */
    public CircularProgressBar(final Context context, @Nullable final AttributeSet attributeSet,
                               @StyleRes final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize(attributeSet);
    }

    /**
     * Creates a new circular progress bar, which has been designed according to the Material design
     * guidelines.
     *
     * @param context
     *         The context, which should be used by the view, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param attributeSet
     *         The attribute set, the view's attributes should be obtained from, as an instance of
     *         the type {@link AttributeSet} or null, if no attributes should be obtained
     * @param defaultStyle
     *         The default style to apply to this  view. If 0, no style will be applied (beyond what
     *         is included in the theme). This may either be an attribute resource, whose value will
     *         be retrieved from the current theme, or an explicit style resource
     * @param defaultStyleResource
     *         A resource identifier of a style resource that supplies default values for the view,
     *         used only if the default style is 0 or can not be found in the theme. Can be 0 to not
     *         look for defaults
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircularProgressBar(@NonNull final Context context,
                               @Nullable final AttributeSet attributeSet,
                               @StyleRes final int defaultStyle, final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize(attributeSet);
    }

    /**
     * Returns the color of the circular progress bar.
     *
     * @return The color of the circular progress bar as an {@link Integer} value
     */
    public final int getColor() {
        return color;
    }

    /**
     * Sets the color of the circular progress bar.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setColor(@ColorInt final int color) {
        this.color = color;
        initializeDrawable();
        invalidate();
    }

    /**
     * Returns the thickness of the circular progress bar.
     *
     * @return The thickness of the circular progress bar in pixels as an {@link Integer} value
     */
    public final int getThickness() {
        return thickness;
    }

    /**
     * Sets the thickness of the circular progress bar.
     *
     * @param thickness
     *         The thickness, which should be set, in pixels as an {@link Integer} value
     */
    public final void setThickness(final int thickness) {
        this.thickness = thickness;
        initializeDrawable();
        invalidate();
    }

    @Override
    public final void draw(final Canvas canvas) {
        super.draw(canvas);
        circularProgressDrawable.draw(canvas);
    }

    @Override
    protected final void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (circularProgressDrawable != null) {
            circularProgressDrawable.start();
        }
    }

    @Override
    protected final void onVisibilityChanged(@NonNull final View changedView,
                                             final int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (circularProgressDrawable != null) {
            if (visibility == VISIBLE) {
                circularProgressDrawable.start();
            } else {
                circularProgressDrawable.stop();
            }
        }
    }

    @Override
    protected final void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        int measuredWidth =
                MeasureSpec.getSize(widthMeasureSpec) + getPaddingLeft() + getPaddingRight();
        int measuredHeight =
                MeasureSpec.getSize(heightMeasureSpec) + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected final void onSizeChanged(final int width, final int height, final int oldWidth,
                                       final int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        circularProgressDrawable
                .setBounds(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(),
                        height - getPaddingBottom());
    }

    @Override
    protected final boolean verifyDrawable(@NonNull final Drawable drawable) {
        return drawable == circularProgressDrawable || super.verifyDrawable(drawable);
    }

}