/*
 * AndroidMaterialViews Copyright 2015 Michael Rapp
 *
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Lesser General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/>. 
 */
package de.mrapp.android.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import de.mrapp.android.view.drawable.CircularProgressDrawable;

/**
 * A circular progress bar, which has been designed according to the Material
 * design guidelines.
 * 
 * Refer to http://www.google.com/design/spec/components/progress-activity.html#
 * progress-activity-types-of-indicators for further information on the Material
 * design guidelines.
 * 
 * @author Michael Rapp
 * 
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
	 *            The attribute set, the view's attribute should be obtained
	 *            from, as an instance of the type {@link AttributeSet}
	 */
	private void initialize(final AttributeSet attributeSet) {
		obtainStyledAttributes(attributeSet);
		initializeDrawable();
	}

	/**
	 * Obtains the view's attributes from a specific attribute set.
	 * 
	 * @param attributeSet
	 *            The attribute set, the view's attributes should be obtained
	 *            from, as an instance of the type {@link AttributeSet}
	 */
	private void obtainStyledAttributes(final AttributeSet attributeSet) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.CircularProgressBar);

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
	 *            The typed array, the color should be obtained from, as an
	 *            instance of the class {@link TypedArray}
	 */
	private void obtainColor(final TypedArray typedArray) {
		int defaultColor = getAccentColor();
		color = typedArray.getColor(R.styleable.CircularProgressBar_android_color, defaultColor);
	}

	/**
	 * Obtains the progress bar's thickness from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the thickness should be obtained from, as an
	 *            instance of the class {@link TypedArray}
	 */
	private void obtainThickness(final TypedArray typedArray) {
		int defaultThickness = getContext().getResources()
				.getDimensionPixelSize(R.dimen.circular_progress_bar_thickness_normal);
		thickness = typedArray.getDimensionPixelSize(R.styleable.CircularProgressBar_android_thickness,
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
	 * Returns the color of the theme attribute <code>R.attr.colorAccent</code>.
	 * 
	 * @return The color of the theme attribute <code>R.attr.colorAccent</code>
	 *         as an {@link Integer} value
	 */
	private int getAccentColor() {
		TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(new int[] { R.attr.colorAccent });
		return typedArray.getColor(0, 0);
	}

	/**
	 * Creates a new circular progress bar, which has been designed according to
	 * the Material design guidelines.
	 * 
	 * @param context
	 *            The context, the view should belong to, as an instance of the
	 *            class {@link Context}
	 */
	public CircularProgressBar(final Context context) {
		this(context, null);
	}

	/**
	 * Creates a new circular progress bar, which has been designed according to
	 * the Material design guidelines.
	 * 
	 * @param context
	 *            The context, the view should belong to, as an instance of the
	 *            class {@link Context}
	 * @param attributeSet
	 *            The attributes of the XML tag that is inflating the view
	 */
	public CircularProgressBar(final Context context, final AttributeSet attributeSet) {
		super(context, attributeSet);
		initialize(attributeSet);
	}

	/**
	 * Creates a new circular progress bar, which has been designed according to
	 * the Material design guidelines.
	 * 
	 * @param context
	 *            The context, the view should belong to, as an instance of the
	 *            class {@link Context}
	 * @param attributeSet
	 *            The attributes of the XML tag that is inflating the view
	 * @param defaultStyle
	 *            The default style to apply to this view. If 0, no style will
	 *            be applied (beyond what is included in the theme). This may
	 *            either be an attribute resource, whose value will be retrieved
	 *            from the current theme, or an explicit style resource
	 */
	public CircularProgressBar(final Context context, final AttributeSet attributeSet, final int defaultStyle) {
		super(context, attributeSet, defaultStyle);
		initialize(attributeSet);
	}

	/**
	 * Creates a new circular progress bar, which has been designed according to
	 * the Material design guidelines.
	 * 
	 * @param context
	 *            The context, which should be used by the view, as an instance
	 *            of the class {@link Context}
	 * @param attributeSet
	 *            The attributes of the XML tag that is inflating the view, as
	 *            an instance of the type {@link AttributeSet}
	 * @param defaultStyle
	 *            The default style to apply to this preference. If 0, no style
	 *            will be applied (beyond what is included in the theme). This
	 *            may either be an attribute resource, whose value will be
	 *            retrieved from the current theme, or an explicit style
	 *            resource
	 * @param defaultStyleResource
	 *            A resource identifier of a style resource that supplies
	 *            default values for the preference, used only if the default
	 *            style is 0 or can not be found in the theme. Can be 0 to not
	 *            look for defaults
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public CircularProgressBar(final Context context, final AttributeSet attributeSet, final int defaultStyle,
			final int defaultStyleResource) {
		super(context, attributeSet, defaultStyle, defaultStyleResource);
		initialize(attributeSet);
	}

	/**
	 * Returns the color of the circular progress bar.
	 * 
	 * @return The color of the circular progress bar as an {@link Integer}
	 *         value
	 */
	public final int getColor() {
		return color;
	}

	/**
	 * Sets the color of the circular progress bar.
	 * 
	 * @param color
	 *            The color, which should be set, as an {@link Integer} value
	 */
	public final void setColor(final int color) {
		this.color = color;
		initializeDrawable();
		invalidate();
	}

	/**
	 * Returns the thickness of the circular progress bar.
	 * 
	 * @return The thickness of the circular progress bar in pixels as an
	 *         {@link Integer} value
	 */
	public final int getThickness() {
		return thickness;
	}

	/**
	 * Sets the thickness of the circular progress bar.
	 * 
	 * @param thickness
	 *            The thickness, which should be set, in pixels as an
	 *            {@link Integer} value
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
	protected final void onVisibilityChanged(final View changedView, final int visibility) {
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
	protected final void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		circularProgressDrawable.setBounds(0, 0, w, h);
	}

	@Override
	protected final boolean verifyDrawable(final Drawable who) {
		return who == circularProgressDrawable || super.verifyDrawable(who);
	}

}