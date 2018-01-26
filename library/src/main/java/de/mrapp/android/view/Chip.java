/*
 * Copyright 2015 - 2018 Michael Rapp
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
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedHashSet;
import java.util.Set;

import de.mrapp.android.util.ViewUtil;

import static de.mrapp.android.util.BitmapUtil.clipCircle;
import static de.mrapp.android.util.BitmapUtil.drawableToBitmap;
import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * A chip, which has been designed according to the Material design guidelines.
 *
 * Refer to http://www.google.com/design/spec/components/chips.html for further information on the
 * Material design guidelines.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class Chip extends FrameLayout {

    /**
     * Defines the interface, a class, which should be notified, when a chip has been closed, must
     * implement.
     */
    public interface CloseListener {

        /**
         * The method, which is invoked, when a chip has been closed.
         *
         * @param chip
         *         The chip, which has been closed, as an instance of the class {@link Chip}
         */
        void onChipClosed(@NonNull Chip chip);

    }

    /**
     * The image view, which is used to show the chip's icon.
     */
    private ImageView imageView;

    /**
     * The text view, which is used to show the chip's text.
     */
    private TextView textView;

    /**
     * The image button, which allows to close the chip.
     */
    private ImageButton closeButton;

    /**
     * The chip's color.
     */
    private int color;

    /**
     * True, if the chip is closable, false otherwise.
     */
    private boolean closable;

    /**
     * A set, which contains the listeners, which should be notified, when the chip has been
     * closed.
     */
    private Set<CloseListener> listeners;

    /**
     * Initializes the view.
     *
     * @param attributeSet
     *         The attribute set, the view's attributes should be obtained from, as an instance of
     *         the type {@link AttributeSet} or null, if no attributes should be obtained
     */
    private void initialize(@Nullable final AttributeSet attributeSet) {
        listeners = new LinkedHashSet<>();
        inflateLayout();
        Drawable background = ContextCompat.getDrawable(getContext(), R.drawable.chip_background);
        ViewUtil.setBackground(this, background);
        obtainStyledAttributes(attributeSet);
    }

    /**
     * Inflates the view's layout.
     */
    private void inflateLayout() {
        View view = View.inflate(getContext(), R.layout.chip, null);
        imageView = view.findViewById(android.R.id.icon);
        textView = view.findViewById(android.R.id.text1);
        closeButton = view.findViewById(android.R.id.closeButton);
        closeButton.setOnClickListener(createCloseButtonListener());
        int height = getResources().getDimensionPixelSize(R.dimen.chip_height);
        addView(view, LayoutParams.WRAP_CONTENT, height);
    }

    /**
     * Creates and returns a listener, which allows to observe, when the button, which allows to
     * close the chip, has been clicked.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnClickListener}
     */
    private OnClickListener createCloseButtonListener() {
        return new OnClickListener() {

            @Override
            public void onClick(final View v) {
                notifyOnChipClosed();
            }

        };
    }

    /**
     * Obtains the view's attributes from a specific attribute set.
     *
     * @param attributeSet
     *         The attribute set, the view's attributes should be obtained from, as an instance of
     *         the type {@link AttributeSet} or null, if no attributes should be obtained
     */
    private void obtainStyledAttributes(@Nullable final AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.Chip);

        try {
            obtainText(typedArray);
            obtainTextColor(typedArray);
            obtainColor(typedArray);
            obtainIcon(typedArray);
            obtainClosable(typedArray);
            obtainCloseIcon(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Obtains the chip's text from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the text should be obtained from, as an instance of the class {@link
     *         TypedArray}. The typed array may not be null
     */
    private void obtainText(@NonNull final TypedArray typedArray) {
        setText(typedArray.getText(R.styleable.Chip_android_text));
    }

    /**
     * Obtains the chip's text color from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the text color should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainTextColor(@NonNull final TypedArray typedArray) {
        int defaultColor = ContextCompat.getColor(getContext(), R.color.chip_text_color_light);
        setTextColor(typedArray.getColor(R.styleable.Chip_android_textColor, defaultColor));
    }

    /**
     * Obtains the chip's color from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the color should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainColor(@NonNull final TypedArray typedArray) {
        int defaultColor = ContextCompat.getColor(getContext(), R.color.chip_color_light);
        setColor(typedArray.getColor(R.styleable.Chip_android_color, defaultColor));
    }

    /**
     * Obtains the chip's icon from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the icon should be obtained from, as an instance of the class {@link
     *         TypedArray}. The typed array may not be null
     */
    private void obtainIcon(@NonNull final TypedArray typedArray) {
        setIcon(typedArray.getDrawable(R.styleable.Chip_android_icon));
    }

    /**
     * Obtains, whether the chip should be closable, or not, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, which should be used to obtain, whether the chip should be closable,
     *         or not, as an instance of the class {@link TypedArray}. The typed array may not be
     *         null
     */
    private void obtainClosable(@NonNull final TypedArray typedArray) {
        setClosable(typedArray.getBoolean(R.styleable.Chip_closable, false));
    }

    /**
     * Obtains the icon of the button, which allows to close the chip, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, which should be used to obtain the icon of the button, which allows
     *         to close the chip, as an instance of the class {@link TypedArray}. The typed array
     *         may not be null
     */
    private void obtainCloseIcon(@NonNull final TypedArray typedArray) {
        Drawable icon = typedArray.getDrawable(R.styleable.Chip_closeButtonIcon);

        if (icon != null) {
            setCloseButtonIcon(icon);
        }
    }

    /**
     * Notifies all listeners, which have been registered to be notified, when the chip has been
     * closed, about the chip being closed.
     */
    private void notifyOnChipClosed() {
        for (CloseListener listener : listeners) {
            listener.onChipClosed(this);
        }
    }

    /**
     * Creates a new chip, which has been designed according to the Material design guidelines.
     *
     * @param context
     *         The context, the view should belong to, as an instance of the class {@link Context}.
     *         The context may not be null
     */
    public Chip(@NonNull final Context context) {
        this(context, null);
    }

    /**
     * Creates a chip, which has been designed according to the Material design guidelines.
     *
     * @param context
     *         The context, the view should belong to, as an instance of the class {@link Context}.
     *         The context may not be null
     * @param attributeSet
     *         The attribute set, the view's attributes should be obtained from, as an instance of
     *         the type {@link AttributeSet} or null, if no attributes should be obtained
     */
    public Chip(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(attributeSet);
    }

    /**
     * Creates a new chip, which has been designed according to the Material design guidelines.
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
    public Chip(final Context context, @Nullable final AttributeSet attributeSet,
                @AttrRes final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize(attributeSet);
    }

    /**
     * Creates a new chip, which has been designed according to the Material design guidelines.
     *
     * @param context
     *         The context, which should be used by the view, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param attributeSet
     *         The attribute set, the view's attributes should be obtained from, as an instance of
     *         the type {@link AttributeSet} or null, if no attributes should be obtained
     * @param defaultStyle
     *         The default style to apply to this view. If 0, no style will be applied (beyond what
     *         is included in the theme). This may either be an attribute resource, whose value will
     *         be retrieved from the current theme, or an explicit style resource
     * @param defaultStyleResource
     *         A resource identifier of a style resource that supplies default values for the view,
     *         used only if the default style is 0 or can not be found in the theme. Can be 0 to not
     *         look for defaults
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Chip(@NonNull final Context context, @Nullable final AttributeSet attributeSet,
                @AttrRes final int defaultStyle, @StyleRes final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize(attributeSet);
    }

    /**
     * Adds a new listener, which should be notified, when the chip has been closed.
     *
     * @param listener
     *         The listener, which should be added, as an instance of the type {@link
     *         CloseListener}. The listener may not be null
     */
    public final void addCloseListener(@NonNull final CloseListener listener) {
        ensureNotNull(listener, "The listener may not be null");
        listeners.add(listener);
    }

    /**
     * Removes a specific listener, which should not be notified, when the chip has been closed,
     * anymore.
     *
     * @param listener
     *         The listener, which should be removed, as an instance of the type {@link
     *         CloseListener}. The listener may not be null
     */
    public final void removeCloseListener(@NonNull final CloseListener listener) {
        ensureNotNull(listener, "The listener may not be null");
        listeners.remove(listener);
    }

    /**
     * Returns the chip's text.
     *
     * @return The chip's text as an instance of the type {@link CharSequence} or null, if no text
     * has been set
     */
    public final CharSequence getText() {
        return textView.getText();
    }

    /**
     * Sets the chip's text.
     *
     * @param resourceId
     *         The resource id of the text, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    public final void setText(@StringRes final int resourceId) {
        setText(getContext().getText(resourceId));
    }

    /**
     * Sets the chip's text.
     *
     * @param text
     *         The text, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no text should be set
     */
    public final void setText(@Nullable final CharSequence text) {
        textView.setText(text);
    }

    /**
     * Sets the chip's text color.
     *
     * @param color
     *         The text color, which should be set, as an {@link Integer} value
     */
    public final void setTextColor(@ColorInt final int color) {
        textView.setTextColor(color);
    }

    /**
     * Sets the chip's text color.
     *
     * @param resourceId
     *         The resource id of the text color, which should be set, as an {@link Integer} value.
     *         The resource id must correspond to a valid color resource
     */
    public final void setTextColorResource(@ColorRes final int resourceId) {
        setTextColor(ContextCompat.getColor(getContext(), resourceId));
    }

    /**
     * Returns the chip's text color.
     *
     * @return The chip's text color as an {@link Integer} value
     */
    public final int getTextColor() {
        return textView.getCurrentTextColor();
    }

    /**
     * Sets the chip's color.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setColor(@ColorInt final int color) {
        this.color = color;
        getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    /**
     * Sets the chip's color.
     *
     * @param resourceId
     *         The resource id of the color, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid color resource
     */
    public final void setColorResource(@ColorRes final int resourceId) {
        setColor(ContextCompat.getColor(getContext(), resourceId));
    }

    /**
     * Returns the chip's color.
     *
     * @return The chip's color as an {@link Integer} value
     */
    public final int getColor() {
        return color;
    }

    /**
     * Sets the chip's icon.
     *
     * @param icon
     *         The icon, which should be set, as an instance of the class {@link Drawable} or null,
     *         if no icon should be set
     */
    public final void setIcon(@Nullable final Drawable icon) {
        setIcon(icon != null ? drawableToBitmap(icon) : null);
    }

    /**
     * Sets the chip's icon.
     *
     * @param icon
     *         The icon, which should be set, as an instance of the class {@link Bitmap} or null, if
     *         no icon should be set
     */
    public final void setIcon(@Nullable final Bitmap icon) {
        if (icon != null) {
            int size = getResources().getDimensionPixelSize(R.dimen.chip_height);
            imageView.setImageBitmap(clipCircle(icon, size));
            imageView.setVisibility(View.VISIBLE);
            textView.setPadding(0, textView.getPaddingTop(), textView.getPaddingRight(),
                    textView.getPaddingBottom());
        } else {
            imageView.setImageBitmap(null);
            imageView.setVisibility(View.GONE);
            textView.setPadding(
                    getResources().getDimensionPixelSize(R.dimen.chip_horizontal_padding),
                    textView.getPaddingTop(), textView.getPaddingRight(),
                    textView.getPaddingBottom());
        }
    }

    /**
     * Returns the chip's icon.
     *
     * @return The chip's icon as an instance of the class {@link Drawable} or null, if no icon has
     * been set
     */
    public final Drawable getIcon() {
        return imageView.getDrawable();
    }

    /**
     * Returns, whether the chip is closable, or not.
     *
     * @return True, if the chip is closable, false otherwise.
     */
    public final boolean isClosable() {
        return closable;
    }

    /**
     * Sets, whether the chip is closable, or not.
     *
     * @param closable
     *         True, if the chip should be closable, false otherwise
     */
    public final void setClosable(final boolean closable) {
        this.closable = closable;

        if (closable) {
            closeButton.setVisibility(View.VISIBLE);
            textView.setPadding(textView.getPaddingLeft(), textView.getPaddingTop(), 0,
                    textView.getPaddingBottom());
        } else {
            closeButton.setVisibility(View.GONE);
            textView.setPadding(textView.getPaddingLeft(), textView.getPaddingTop(),
                    getResources().getDimensionPixelSize(R.dimen.chip_horizontal_padding),
                    textView.getPaddingBottom());
        }
    }

    /**
     * Returns the icon of the button, which allows to close the chip.
     *
     * @return The icon of the button, which allows to close the chip, as an instance of the class
     * {@link Drawable} or null, if the chip is not closable
     */
    public final Drawable getCloseButtonIcon() {
        return closable ? closeButton.getDrawable() : null;
    }

    /**
     * Sets the icon of the button, which allows to close the chip.
     *
     * @param icon
     *         The icon, which should be set, as an instance of the class {@link Drawable}. The icon
     *         may not be null
     */
    public final void setCloseButtonIcon(@NonNull final Drawable icon) {
        ensureNotNull(icon, "The icon may not be null");
        closeButton.setImageDrawable(icon);
    }

    /**
     * Sets the icon of the button, which allows to close the chip.
     *
     * @param icon
     *         The icon, which should be set, as an instance of the class {@link Bitmap}. The icon
     *         may not be null
     */
    public final void setCloseButtonIcon(@NonNull final Bitmap icon) {
        ensureNotNull(icon, "The icon may not be null");
        closeButton.setImageBitmap(icon);
    }

}