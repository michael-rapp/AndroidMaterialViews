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
package de.mrapp.android.view.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import de.mrapp.android.view.Chip;
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
     * Initializes the floating action buttons.
     */
    private void initializeFloatingActionButtons() {
        smallFloatingActionButton = findViewById(R.id.floating_action_button_small);
        smallFloatingActionButton.setOnClickListener(createFloatingActionButtonListener());
        normalFloatingActionButton = findViewById(R.id.floating_action_button_normal);
        normalFloatingActionButton.setOnClickListener(createFloatingActionButtonListener());
        largeFloatingActionButton = findViewById(R.id.floating_action_button_large);
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
        Button button = findViewById(R.id.hide_floating_action_buttons);
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
     * Initializes all closable chips.
     */
    private void initializeChips() {
        Chip closableChipLight = findViewById(R.id.chip_closable_light);
        closableChipLight.addCloseListener(createChipCloseListener());
        Chip closableChipDark = findViewById(R.id.chip_closable_dark);
        closableChipDark.addCloseListener(createChipCloseListener());
        Chip closableIconChipLight = findViewById(R.id.chip_icon_closable_light);
        closableIconChipLight.addCloseListener(createChipCloseListener());
        Chip closableIconChipDark = findViewById(R.id.chip_icon_closable_dark);
        closableIconChipDark.addCloseListener(createChipCloseListener());
    }

    /**
     * Creates and returns a listener, which allows to show a toast, when a chip has been closed.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * Chip.CloseListener}
     */
    private Chip.CloseListener createChipCloseListener() {
        return new Chip.CloseListener() {

            @Override
            public void onChipClosed(@NonNull final Chip chip) {
                chip.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, R.string.chip_closed_toast, Toast.LENGTH_SHORT)
                        .show();
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
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeFloatingActionButtons();
        initializeHideFloatingActionButtonsButton();
        initializeChips();
        handleSavedInstanceState(savedInstanceState);
    }

    @Override
    protected final void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLOATING_ACTION_BUTTONS_VISIBLE_EXTRA, floatingActionButtonsVisible);
    }

}