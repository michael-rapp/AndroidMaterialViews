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
package de.mrapp.android.view.drawable;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.Property;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import de.mrapp.android.view.CircularProgressBar;

import static de.mrapp.android.util.Condition.ensureAtLeast;

/**
 * An animated drawable, which is used by the view {@link CircularProgressBar}.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class CircularProgressDrawable extends Drawable implements Animatable {

    /**
     * The duration of the angle animation in milliseconds.
     */
    private static final long ANGLE_ANIMATION_DURATION = 2000L;

    /**
     * The duration of the sweep animation in milliseconds.
     */
    private static final long SWEEP_ANIMATION_DURATION = 600L;

    /**
     * The minimum angle of the sweep animation.
     */
    private static final int MIN_SWEEP_ANGLE = 30;

    /**
     * The number of degrees in a circle.
     */
    private static final int MAX_DEGREES = 360;

    /**
     * The color of the progress drawable.
     */
    private final int color;

    /**
     * The thickness of the progress drawable.
     */
    private final int thickness;

    /**
     * The paint, which is used for drawing.
     */
    private Paint paint;

    /**
     * The bounds of the drawable.
     */
    private RectF bounds;

    /**
     * The sweep animator.
     */
    private ObjectAnimator sweepAnimator;

    /**
     * The angle animator.
     */
    private ObjectAnimator angleAnimator;

    /**
     * The current angle of the sweep animation.
     */
    private float currentSweepAngle;

    /**
     * The current angle of the angle animation.
     */
    private float currentGlobalAngle;

    /**
     * The current offset of the angle animation.
     */
    private float currentGlobalAngleOffset;

    /**
     * True, if the progress bar is currently animated to be appearing, false. This value will
     * toggle each time the animation is repeated.
     */
    private boolean appearing;

    /**
     * Initializes the paint, which is used for drawing.
     */
    private void initializePaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getThickness());
        paint.setColor(getColor());
    }

    /**
     * Initializes the animators.
     */
    private void initializeAnimators() {
        initializeAngleAnimator();
        initializeSweepAnimator();
    }

    /**
     * Initializes the angle animator.
     */
    private void initializeAngleAnimator() {
        angleAnimator = ObjectAnimator.ofFloat(this, createAngleProperty(), MAX_DEGREES);
        angleAnimator.setInterpolator(new LinearInterpolator());
        angleAnimator.setDuration(ANGLE_ANIMATION_DURATION);
        angleAnimator.setRepeatMode(ValueAnimator.RESTART);
        angleAnimator.setRepeatCount(ValueAnimator.INFINITE);
    }

    /**
     * Creates and returns a property, which allows to animate the global angle of the progress
     * drawable.
     *
     * @return The property, which has been created, as an instance of the class {@link Property}
     */
    private Property<CircularProgressDrawable, Float> createAngleProperty() {
        return new Property<CircularProgressDrawable, Float>(Float.class, "angle") {

            @Override
            public Float get(final CircularProgressDrawable object) {
                return currentGlobalAngle;
            }

            @Override
            public void set(final CircularProgressDrawable object, final Float value) {
                currentGlobalAngle = value;
                invalidateSelf();
            }

        };
    }

    /**
     * Initializes the sweep animator.
     */
    private void initializeSweepAnimator() {
        sweepAnimator = ObjectAnimator
                .ofFloat(this, createSweepProperty(), MAX_DEGREES - MIN_SWEEP_ANGLE * 2);
        sweepAnimator.setInterpolator(new DecelerateInterpolator());
        sweepAnimator.setDuration(SWEEP_ANIMATION_DURATION);
        sweepAnimator.setRepeatMode(ValueAnimator.RESTART);
        sweepAnimator.setRepeatCount(ValueAnimator.INFINITE);
        sweepAnimator.addListener(createSweepAnimatorListener());
    }

    /**
     * Creates and returns a property, which allows to animate the sweep angle of the progress
     * drawable.
     *
     * @return The property, which has been created, as an instance of the class {@link Property}
     */
    private Property<CircularProgressDrawable, Float> createSweepProperty() {
        return new Property<CircularProgressDrawable, Float>(Float.class, "arc") {

            @Override
            public Float get(final CircularProgressDrawable object) {
                return currentSweepAngle;
            }

            @Override
            public void set(final CircularProgressDrawable object, final Float value) {
                currentSweepAngle = value;
                invalidateSelf();
            }

        };
    }

    /**
     * Creates and returns a listener, which allows to restart the progress drawable's animation,
     * when it has been finished.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * AnimatorListener}
     */
    private AnimatorListener createSweepAnimatorListener() {
        return new AnimatorListener() {

            @Override
            public void onAnimationStart(final Animator animation) {

            }

            @Override
            public void onAnimationEnd(final Animator animation) {

            }

            @Override
            public void onAnimationCancel(final Animator animation) {

            }

            @Override
            public void onAnimationRepeat(final Animator animation) {
                appearing = !appearing;

                if (appearing) {
                    currentGlobalAngleOffset =
                            (currentGlobalAngleOffset + MIN_SWEEP_ANGLE * 2) % MAX_DEGREES;
                }
            }

        };
    }

    /**
     * Creates a new animated drawable, which is used by the view {@link CircularProgressBar}.
     *
     * @param color
     *         The color of the progress drawable as an {@link Integer} value
     * @param thickness
     *         The thickness of the progress drawable as an {@link Integer} value in pixels
     */
    public CircularProgressDrawable(@ColorInt final int color, final int thickness) {
        ensureAtLeast(thickness, 1, "The thickness must be at least 1");
        this.color = color;
        this.thickness = thickness;
        this.bounds = new RectF();
        initializePaint();
        initializeAnimators();
    }

    /**
     * Returns the color of the progress drawable.
     *
     * @return The color of the progress drawable as an {@link Integer} value
     */
    public final int getColor() {
        return color;
    }

    /**
     * Returns the thickness of the progress drawable.
     *
     * @return The thickness of the progress drawable in pixels as an {@link Integer}
     */
    public final int getThickness() {
        return thickness;
    }

    @Override
    public final void setAlpha(final int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public final void setColorFilter(final ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public final int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    @Override
    public final void draw(@NonNull final Canvas canvas) {
        float startAngle = currentGlobalAngle - currentGlobalAngleOffset;
        float sweepAngle = currentSweepAngle;

        if (!appearing) {
            startAngle = startAngle + sweepAngle;
            sweepAngle = MAX_DEGREES - sweepAngle - MIN_SWEEP_ANGLE;
        } else {
            sweepAngle += MIN_SWEEP_ANGLE;
        }

        canvas.drawArc(bounds, startAngle, sweepAngle, false, paint);
    }

    @Override
    public final void start() {
        if (!isRunning()) {
            angleAnimator.start();
            sweepAnimator.start();
            invalidateSelf();
        }
    }

    @Override
    public final void stop() {
        if (isRunning()) {
            angleAnimator.cancel();
            sweepAnimator.cancel();
            invalidateSelf();
        }
    }

    @Override
    public final boolean isRunning() {
        return angleAnimator.isRunning();
    }

    @Override
    protected final void onBoundsChange(final Rect bounds) {
        super.onBoundsChange(bounds);
        int width = bounds.right - bounds.left;
        int height = bounds.bottom - bounds.top;

        if (width < height) {
            int diff = height - width;
            this.bounds.left = bounds.left + thickness / 2.0f + 0.5f;
            this.bounds.right = bounds.right - thickness / 2.0f - 0.5f;
            this.bounds.top = bounds.top + diff / 2.0f + thickness / 2.0f + 0.5f;
            this.bounds.bottom = bounds.bottom - diff / 2.0f - thickness / 2.0f - 0.5f;
        } else {
            int diff = width - height;
            this.bounds.left = bounds.left + diff / 2.0f + thickness / 2.0f + 0.5f;
            this.bounds.right = bounds.right - diff / 2.0f - thickness / 2.0f - 0.5f;
            this.bounds.top = bounds.top + thickness / 2.0f + 0.5f;
            this.bounds.bottom = bounds.bottom - thickness / 2.0f - 0.5f;
        }
    }

}