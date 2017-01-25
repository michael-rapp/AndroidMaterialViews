/*
 * Copyright 2015 - 2017 Michael Rapp
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

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import de.mrapp.android.util.ThemeUtil;
import de.mrapp.android.util.ViewUtil;

import static de.mrapp.android.util.Condition.ensureAtLeast;
import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * A floating action button, which has been designed according to the Material design guidelines.
 *
 * Refer to http://www.google.com/design/spec/components/buttons-floating-action- button.html for
 * further information on the Material design guidelines.
 *
 * @author Michael Rapp
 * @since 1.0.0
 * @deprecated Since version 2.1.3 this class is deprecated in favor of the
 * <code>FloatingActionButton</code>, which is now part of Android's official Design support
 * library
 */
@Deprecated
public class FloatingActionButton extends RelativeLayout {

    /**
     * Contains all possible sizes of a floating action button.
     */
    public enum Size {

        /**
         * A floating action button's normal size.
         */
        NORMAL(0),

        /**
         * A floating action button's small size.
         */
        SMALL(1),

        /**
         * A floating action button's large size.
         */
        LARGE(2);

        /**
         * The value, which represents the size.
         */
        private final int value;

        /**
         * Creates a new size of a floating action button.
         *
         * @param value
         *         The value, which represents the size, as an {@link Integer} value
         */
        Size(final int value) {
            this.value = value;
        }

        /**
         * Returns the value, which represents the size.
         *
         * @return The value, which represents the size, as an {@link Integer} value
         */
        protected int getValue() {
            return value;
        }

        /**
         * Returns the size, which corresponds to a specific value.
         *
         * @param value
         *         The value, which represents the size, as an {@link Integer} value
         * @return The size, which corresponds to the given value, as a value of the enum {@link
         * Size}
         */
        protected static Size fromValue(final int value) {
            for (Size currentSize : values()) {
                if (currentSize.value == value) {
                    return currentSize;
                }
            }

            throw new IllegalArgumentException("Invalid enum value: " + value);
        }

    }

    /**
     * The image button, which is used to show the floating action button's background and icon.
     */
    private ImageButton imageButton;

    /**
     * The floating action button's size.
     */
    private Size size;

    /**
     * The floating action button's color.
     */
    private int color;

    /**
     * The color, which is used as an overlay, when the floating action button is activated.
     */
    private int activatedColor;

    /**
     * The color, which is used as an overlay, when the floating action button is pressed.
     */
    private int pressedColor;

    /**
     * The color, which is used as an overlay, when the floating action button is disabled.
     */
    private int disabledColor;

    /**
     * The duration of the animation, which may be used to change the visibility of the floating
     * action button, in milliseconds.
     */
    private long visibilityAnimationDuration;

    /**
     * The animator, which may be used to change the visibility of the floating action button.
     */
    private ViewPropertyAnimator visibilityAnimator;

    /**
     * Initializes the view.
     *
     * @param attributeSet
     *         The attribute set, the view's attributes should be obtained from, as an instance of
     *         the type {@link AttributeSet} or null, if no attributes should be obtained
     */
    private void initialize(@Nullable final AttributeSet attributeSet) {
        inflateLayout();
        obtainStyledAttributes(attributeSet);
        adaptShadow();
        adaptImageButtonSize();
        adaptImageButtonBackground();
    }

    /**
     * Inflates the view's layout.
     */
    private void inflateLayout() {
        imageButton = new ImageButton(getContext());
        LayoutParams layoutParams = new LayoutParams(0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        addView(imageButton, layoutParams);
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
                getContext().obtainStyledAttributes(attributeSet, R.styleable.FloatingActionButton);

        try {
            obtainSize(typedArray);
            obtainColor(typedArray);
            obtainActivatedColor(typedArray);
            obtainPressedColor(typedArray);
            obtainDisabledColor(typedArray);
            obtainIcon(typedArray);
            obtainVisibilityAnimationDuration(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Obtains the floating action button's size from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the color should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainSize(@NonNull final TypedArray typedArray) {
        Size defaultSize = Size.NORMAL;
        size = Size.fromValue(
                typedArray.getInt(R.styleable.FloatingActionButton_size, defaultSize.getValue()));
    }

    /**
     * Obtains the floating action button's color from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the color should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainColor(@NonNull final TypedArray typedArray) {
        int defaultColor = ThemeUtil.getColor(getContext(), R.attr.colorAccent);
        color = typedArray.getColor(R.styleable.FloatingActionButton_android_color, defaultColor);
    }

    /**
     * Obtains the floating action button's activated color from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the activated color should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainActivatedColor(@NonNull final TypedArray typedArray) {
        int defaultActivatedColor = getControlActivatedColor();
        activatedColor = typedArray
                .getColor(R.styleable.FloatingActionButton_activatedColor, defaultActivatedColor);
    }

    /**
     * Obtains the floating action button's pressed color from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the pressed color should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainPressedColor(@NonNull final TypedArray typedArray) {
        int defaultPressedColor = getControlHighlightColor();
        pressedColor = typedArray
                .getColor(R.styleable.FloatingActionButton_pressedColor, defaultPressedColor);
    }

    /**
     * Obtains the floating action button's disabled color from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the disabled color should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainDisabledColor(@NonNull final TypedArray typedArray) {
        int defaultDisabledColor =
                ContextCompat.getColor(getContext(), R.color.floating_action_button_disabled_color);
        disabledColor = typedArray
                .getColor(R.styleable.FloatingActionButton_disabledColor, defaultDisabledColor);
    }

    /**
     * Obtains the floating action button's icon from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the ripple color should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainIcon(@NonNull final TypedArray typedArray) {
        Drawable icon = typedArray.getDrawable(R.styleable.FloatingActionButton_android_icon);
        setIcon(icon);
    }

    /**
     * Obtains the duration of the animation, which may be used to changed the visibility of the
     * floating action button, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the animation duration should be obtained from, as an instance of
     *         the class {@link TypedArray}. The typed array may not be null
     */
    private void obtainVisibilityAnimationDuration(@NonNull final TypedArray typedArray) {
        int defaultAnimationDuration = getResources()
                .getInteger(R.integer.floating_action_button_visibility_animation_duration);
        int duration = typedArray
                .getInteger(R.styleable.FloatingActionButton_visibilityAnimationDuration,
                        defaultAnimationDuration);
        setVisibilityAnimationDuration(duration);
    }

    /**
     * Adapts the shadow of the floating action button, depending on its size.
     */
    private void adaptShadow() {
        if (getSize() == Size.NORMAL) {
            setBackgroundResource(R.drawable.floating_action_button_shadow_normal);
        } else if (getSize() == Size.SMALL) {
            setBackgroundResource(R.drawable.floating_action_button_shadow_small);
        } else {
            setBackgroundResource(R.drawable.floating_action_button_shadow_large);
        }
    }

    /**
     * Adapts the size of the image button, which is used to show the floating image button's
     * background and icon, depending on the floating button's size.
     */
    private void adaptImageButtonSize() {
        int pixelSize = getPixelSize();
        LayoutParams layoutParams = (LayoutParams) imageButton.getLayoutParams();
        layoutParams.width = pixelSize;
        layoutParams.height = pixelSize;
        imageButton.setLayoutParams(layoutParams);
        imageButton.requestLayout();
    }

    /**
     * Adapts the background of the image button, which is used to show the floating image button's
     * background and icon, depending on the floating button's colors.
     */
    @SuppressLint("NewApi")
    private void adaptImageButtonBackground() {
        Drawable background = createStateListBackgroundDrawable();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable rippleDrawable = new RippleDrawable(
                    new ColorStateList(new int[][]{{}}, new int[]{getPressedColor()}), background,
                    null);
            ViewUtil.setBackground(imageButton, rippleDrawable);
        } else {
            ViewUtil.setBackground(imageButton, background);
        }
    }

    /**
     * Creates and returns a state list drawable, which can be used as the floating action button
     * background and adapts the background color depending on the button's current state.
     *
     * @return The drawable, which has been created, as an instance of the class {@link Drawable}
     */
    private Drawable createStateListBackgroundDrawable() {
        StateListDrawable drawable = new StateListDrawable();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed},
                    createPressedBackgroundDrawable());
        }

        drawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_activated},
                createActivatedBackgroundDrawable());
        drawable.addState(new int[]{android.R.attr.state_enabled},
                createBackgroundDrawable(getColor()));
        drawable.addState(new int[]{}, createDisabledBackgroundDrawable());
        return drawable;
    }

    /**
     * Creates and returns a drawable, which can be used as the floating action button's background,
     * when it is activated.
     *
     * @return The drawable, which has been created, as an instance of the class {@link Drawable}
     */
    private Drawable createActivatedBackgroundDrawable() {
        Drawable drawable = createBackgroundDrawable(getColor());
        Drawable hoverDrawable = createBackgroundDrawable(getActivatedColor());
        return new LayerDrawable(new Drawable[]{drawable, hoverDrawable});
    }

    /**
     * Creates and returns a drawable, which can be used as the floating action button's background,
     * when it is pressed.
     *
     * @return The drawable, which has been created, as an instance of the class {@link Drawable}
     */
    private Drawable createPressedBackgroundDrawable() {
        Drawable drawable = createBackgroundDrawable(getColor());
        Drawable hoverDrawable = createBackgroundDrawable(getPressedColor());
        return new LayerDrawable(new Drawable[]{drawable, hoverDrawable});
    }

    /**
     * Creates and returns a drawable, which can be used as the floating action button's background,
     * when it is disabled.
     *
     * @return The drawable, which has been created, as an instance of the class {@link Drawable}
     */
    private Drawable createDisabledBackgroundDrawable() {
        Drawable drawable = createBackgroundDrawable(getColor());
        Drawable hoverDrawable = createBackgroundDrawable(getDisabledColor());
        return new LayerDrawable(new Drawable[]{drawable, hoverDrawable});
    }

    /**
     * Creates and returns a drawable with a specific color, which can be used as the floating
     * action button's background.
     *
     * @param color
     *         The color of the background as an {@link Integer} value
     * @return The drawable, which has been created, as an instance of the class {@link Drawable}
     */
    private Drawable createBackgroundDrawable(@ColorInt final int color) {
        OvalShape shape = new OvalShape();
        ShapeDrawable drawable = new ShapeDrawable(shape);
        drawable.getPaint().setColor(color);
        return drawable;
    }

    /**
     * Returns the size of the floating action button in pixels, depending on its current size.
     *
     * @return The size of the floating action button in pixels as an {@link Integer} value
     */
    private int getPixelSize() {
        if (getSize() == Size.NORMAL) {
            return getResources().getDimensionPixelSize(R.dimen.floating_action_button_size_normal);
        } else if (getSize() == Size.SMALL) {
            return getResources().getDimensionPixelSize(R.dimen.floating_action_button_size_small);
        } else {
            return getResources().getDimensionPixelSize(R.dimen.floating_action_button_size_large);
        }
    }

    /**
     * Returns the color of the theme attribute <code>R.attr.colorControlHighlight</code>.
     *
     * @return The color of the theme attribute <code>R.attr.colorControlHighlight</code> as an
     * {@link Integer} value
     */
    private int getControlHighlightColor() {
        TypedArray typedArray = getContext().getTheme()
                .obtainStyledAttributes(new int[]{R.attr.colorControlHighlight});
        return typedArray.getColor(0, 0);
    }

    /**
     * Returns the color of the theme attribute <code>R.attr.colorControlActivated</code>.
     *
     * @return The color of the theme attribute <code>R.attr.colorControlActivated</code> as an
     * {@link Integer} value
     */
    private int getControlActivatedColor() {
        TypedArray typedArray = getContext().getTheme()
                .obtainStyledAttributes(new int[]{R.attr.colorControlActivated});
        return typedArray.getColor(0, 0);
    }

    /**
     * Animates changing the visibility of the floating action button.
     *
     * @param visibility
     *         The visibility, which should be set, as an {@link Integer} value. The visibility may
     *         be <code>View.VISIBLE</code>, <code>View.INVISIBLE</code> or <code>View.GONE</code>
     * @param duration
     *         The duration of the animation in milliseconds as a {@link Long} value
     */
    private void animateVisibility(final int visibility, final long duration) {
        if (visibilityAnimator != null) {
            visibilityAnimator.cancel();
        }

        AnimatorListener listener = createVisibilityAnimatorListener(visibility);
        float targetScale = visibility == View.VISIBLE ? 1 : 0;
        long animationDuration = Math.round(Math.abs(getScaleX() - targetScale) * duration);
        Interpolator interpolator = new AccelerateDecelerateInterpolator();
        visibilityAnimator =
                animate().setInterpolator(interpolator).scaleX(targetScale).scaleY(targetScale)
                        .setDuration(animationDuration).setListener(listener);
    }

    /**
     * Creates and returns a listener, which allows to adapt the visibility of the floating action
     * button, depending on the progress of an animation, which is used to change the visibility.
     *
     * @param visibility
     *         The visibility, which is set by the observed animation, as an {@link Integer} value.
     *         The visibility may be <code>View.VISIBLE</code>, <code>View.INVISIBLE</code> or
     *         <code>View.GONE</code>
     * @return The listener, which has been created, as an instance of the type {@link
     * AnimatorListener}
     */
    private AnimatorListener createVisibilityAnimatorListener(final int visibility) {
        return new AnimatorListener() {

            @Override
            public void onAnimationStart(final Animator animation) {
                if (visibility == View.VISIBLE) {
                    FloatingActionButton.super.setVisibility(visibility);
                }
            }

            @Override
            public void onAnimationRepeat(final Animator animation) {

            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                FloatingActionButton.super.setVisibility(visibility);
                visibilityAnimator = null;
            }

            @Override
            public void onAnimationCancel(final Animator animation) {
                visibilityAnimator = null;
            }

        };
    }

    /**
     * Creates a new floating action button, which has been designed according to the Material
     * design guidelines.
     *
     * @param context
     *         The context, which should be used by the view, as an instance of the class {@link
     *         Context}. The context may not be null
     */
    public FloatingActionButton(@NonNull final Context context) {
        this(context, null);
    }

    /**
     * Creates a new floating action button, which has been designed according to the Material
     * design guidelines.
     *
     * @param context
     *         The context, which should be used by the view, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param attributeSet
     *         The attribute set, the view's attributes should be obtained from, as an instance of
     *         the type {@link AttributeSet} or null, if no attributes should be obtained
     */
    public FloatingActionButton(@NonNull final Context context,
                                @Nullable final AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(attributeSet);
    }

    /**
     * Creates a new floating action button, which has been designed according to the Material
     * design guidelines.
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
     */
    public FloatingActionButton(@NonNull final Context context,
                                @Nullable final AttributeSet attributeSet,
                                @StyleRes final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize(attributeSet);
    }

    /**
     * Creates a new floating action button, which has been designed according to the Material
     * design guidelines.
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
    public FloatingActionButton(@NonNull final Context context,
                                @Nullable final AttributeSet attributeSet,
                                @StyleRes final int defaultStyle, final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize(attributeSet);
    }

    /**
     * Returns the floating action button's size.
     *
     * @return The floating action button's size as a value of the enum {@link Size}. The size may
     * either be <code>NORMAL</code> or <code>SMALL</code>
     */
    public final Size getSize() {
        return size;
    }

    /**
     * Sets the floating action button's size.
     *
     * @param size
     *         The size, which should be set, as a value of the enum {@link Size}. The size must
     *         either be <code>NORMAL</code> or <code>SMALL</code>
     */
    public final void setSize(@NonNull final Size size) {
        ensureNotNull(size, "The size may not be null");
        this.size = size;
        adaptShadow();
        adaptImageButtonSize();
        requestLayout();
    }

    /**
     * Returns the floating action button's icon.
     *
     * @return The floating action button's icon as an instance of the class {@link Drawable} or
     * null, if no icon has been set
     */
    public final Drawable getIcon() {
        return imageButton.getDrawable();
    }

    /**
     * Sets the floating action button's icon.
     *
     * @param icon
     *         The icon, which should be set, as an instance of the class {@link Drawable} or null,
     *         if no icon should be set
     */
    public final void setIcon(@Nullable final Drawable icon) {
        imageButton.setImageDrawable(icon);
    }

    /**
     * Sets the floating action button's icon.
     *
     * @param resourceId
     *         The resource id of the icon, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid drawable resource
     */
    public final void setIcon(@DrawableRes final int resourceId) {
        imageButton.setImageResource(resourceId);
    }

    /**
     * Returns the floating action button's color.
     *
     * @return The floating action button's color as an {@link Integer} value
     */
    public final int getColor() {
        return color;
    }

    /**
     * Sets the floating action button's color.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setColor(@ColorInt final int color) {
        this.color = color;
        adaptImageButtonBackground();
    }

    /**
     * Returns the color, which is used as an overlay, when the floating action button is
     * activated.
     *
     * @return The color, which is used as an overlay, when the floating action button is activated,
     * as an {@link Integer} value
     */
    public final int getActivatedColor() {
        return activatedColor;
    }

    /**
     * Sets the color, which should be used as an overlay, when the floating action button is
     * activated.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setActivatedColor(@ColorInt final int color) {
        this.activatedColor = color;
        adaptImageButtonBackground();
    }

    /**
     * Returns the color, which is used as an overlay, when the floating action button is pressed.
     *
     * @return The color, which is used as an overlay, when the floating action button is pressed,
     * as an {@link Integer} value
     */
    public final int getPressedColor() {
        return pressedColor;
    }

    /**
     * Sets the color, which should be used as an overlay, when the floating action button is
     * pressed.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setPressedColor(@ColorInt final int color) {
        this.pressedColor = color;
        adaptImageButtonBackground();
    }

    /**
     * Returns the color, which is used as an overlay, when the floating action button is disabled.
     *
     * @return The color, which is used as an overlay, when the floating action button is disabled,
     * as an {@link Integer} value
     */
    public final int getDisabledColor() {
        return disabledColor;
    }

    /**
     * Sets the color, which should be used as an overlay, when the floating action button is
     * disabled.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setDisabledColor(@ColorInt final int color) {
        this.disabledColor = color;
        adaptImageButtonBackground();
    }

    /**
     * Returns the duration of the animation, which may be used to change the visibility of the
     * floating action button.
     *
     * @return The duration of the animation in milliseconds as a {@link Long} value
     */
    public final long getVisibilityAnimationDuration() {
        return visibilityAnimationDuration;
    }

    /**
     * Sets the duration of the animation, which may be used to change the visibility of the
     * floating action button.
     *
     * @param duration
     *         The duration, which should be set, in milliseconds as a {@link Long} value
     */
    public final void setVisibilityAnimationDuration(final long duration) {
        ensureAtLeast(duration, 0, "The animation duration must be at least 0");
        this.visibilityAnimationDuration = duration;
    }

    /**
     * Sets the visibility of the floating action button.
     *
     * @param visibility
     *         The visibility, which should be set, as an {@link Integer} value. The visibility may
     *         be <code>View.VISIBLE</code>, <code>View.INVISIBLE</code> or <code>View.GONE</code>
     * @param animate
     *         True, if changing the visibility should be animated, false otherwise
     */
    public final void setVisibility(final int visibility, final boolean animate) {
        if (animate) {
            animateVisibility(visibility, getVisibilityAnimationDuration());
        } else {
            setVisibility(visibility);
        }
    }

    @Override
    public final void setVisibility(final int visibility) {
        super.setVisibility(visibility);
        float scale = visibility == View.VISIBLE ? 1 : 0;
        setScaleX(scale);
        setScaleY(scale);
    }

    @Override
    public final void setOnClickListener(final OnClickListener listener) {
        imageButton.setOnClickListener(listener);
    }

    @Override
    public final void setOnLongClickListener(final OnLongClickListener listener) {
        imageButton.setOnLongClickListener(listener);
    }

    @Override
    public final void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        imageButton.setEnabled(enabled);
    }

    @Override
    protected final void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int shadowSize =
                getResources().getDimensionPixelSize(R.dimen.floating_action_button_shadow_size);
        int pixelSize = getPixelSize() + shadowSize;
        setMeasuredDimension(pixelSize, pixelSize);
    }

}