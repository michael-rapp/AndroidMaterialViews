/*
 * AndroidMaterialViews Copyright 2015 Michael Rapp
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package de.mrapp.android.view.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import de.mrapp.android.view.FloatingActionButton;

/**
 * The example app's main activity.
 *
 * @author Michael Rapp
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Then name of the extra, which is used to store, whether the floating action buttons are
     * currently visible, within a bundle.
     */
    private static final String FLOATING_ACTION_BUTTONS_VISIBLE_EXTRA =
            MainActivity.class.getSimpleName() + "FloatingActionButtonsVisible";

    /**
     * The small sized floating action button.
     */
    private FloatingActionButton smallFloatingActionButton;

    /**
     * The normal sized floating action button.
     */
    private FloatingActionButton normalFloatingActionButton;

    /**
     * The large sized floating action button.
     */
    private FloatingActionButton largeFloatingActionButton;

    /**
     * True, if the floating action buttons are currently visible, false otherwise.
     */
    private boolean floatingActionButtonsVisible = true;

    /**
     * Initializes the activity's toolbar.
     */
    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Initializes the floating action buttons.
     */
    private void initializeFloatingActionButtons() {
        smallFloatingActionButton =
                (FloatingActionButton) findViewById(R.id.floating_action_button_small);
        smallFloatingActionButton.setOnClickListener(createFloatingActionButtonListener());
        normalFloatingActionButton =
                (FloatingActionButton) findViewById(R.id.floating_action_button_normal);
        normalFloatingActionButton.setOnClickListener(createFloatingActionButtonListener());
        largeFloatingActionButton =
                (FloatingActionButton) findViewById(R.id.floating_action_button_large);
        largeFloatingActionButton.setOnClickListener(createFloatingActionButtonListener());
    }

    /**
     * Creates and returns a listener, which allows to show a toast when a floating action button
     * has been clicked.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnClickListener}
     */
    private OnClickListener createFloatingActionButtonListener() {
        return new OnClickListener() {

            @Override
            public void onClick(final View v) {
                Toast toast =
                        Toast.makeText(MainActivity.this, R.string.floating_action_button_toast,
                                Toast.LENGTH_SHORT);
                toast.show();
            }

        };
    }

    /**
     * Initializes the button, which allows to show or hide the floating action buttons.
     */
    private void initializeHideFloatingActionButtonsButton() {
        Button button = (Button) findViewById(R.id.hide_floating_action_buttons);
        button.setOnClickListener(createHideFloatingActionButtonsListener());
    }

    /**
     * Creates and returns a listener, which allows to show or hide the floating action buttons.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnClickListener}
     */
    private OnClickListener createHideFloatingActionButtonsListener() {
        return new OnClickListener() {

            @Override
            public void onClick(final View v) {
                int visibility = floatingActionButtonsVisible ? View.INVISIBLE : View.VISIBLE;
                smallFloatingActionButton.setVisibility(visibility, true);
                normalFloatingActionButton.setVisibility(visibility, true);
                largeFloatingActionButton.setVisibility(visibility, true);
                floatingActionButtonsVisible = !floatingActionButtonsVisible;
            }

        };
    }

    /**
     * Adapts the activity's views depending on the saved instance state, which has been passed to
     * the activity.
     *
     * @param savedInstanceState
     *         The saved instance state as an instance of the class {@link Bundle} or null, if no
     *         saved instance state is available
     */
    private void handleSavedInstanceState(@Nullable final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            floatingActionButtonsVisible =
                    savedInstanceState.getBoolean(FLOATING_ACTION_BUTTONS_VISIBLE_EXTRA);
            int visibility = floatingActionButtonsVisible ? View.VISIBLE : View.INVISIBLE;
            smallFloatingActionButton.setVisibility(visibility);
            normalFloatingActionButton.setVisibility(visibility);
            largeFloatingActionButton.setVisibility(visibility);
        }
    }

    @Override
    protected final void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeToolbar();
        initializeFloatingActionButtons();
        initializeHideFloatingActionButtonsButton();
        handleSavedInstanceState(savedInstanceState);
    }

    @Override
    protected final void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLOATING_ACTION_BUTTONS_VISIBLE_EXTRA, floatingActionButtonsVisible);
    }

}