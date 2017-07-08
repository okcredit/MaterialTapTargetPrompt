/*
 * Copyright (C) 2016 Samuel Wall
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.samuelwall.materialtaptargetprompt.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.PopupWindowResourceFinder;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private ActionMode mActionMode;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback()
    {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.actionmode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu)
        {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item)
        {
            mActionMode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode)
        {
            mActionMode = null;
        }
    };

    MaterialTapTargetPrompt mFabPrompt;

    public void showFabPrompt(View view)
    {
        if (mFabPrompt != null)
        {
            return;
        }
        mFabPrompt = new MaterialTapTargetPrompt.Builder(MainActivity.this)
                .setTarget(findViewById(R.id.fab))
                .setFocalPadding(R.dimen.dp40)
                .setPrimaryText("Send your first email")
                .setSecondaryText("Tap the envelop to start composing your first email")
                .setBackButtonDismissEnabled(true)
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED
                                || state == MaterialTapTargetPrompt.STATE_DISMISSING)
                        {
                            mFabPrompt = null;
                            //Do something such as storing a value so that this prompt is never shown again
                        }
                    }
                })
                .create();
        mFabPrompt.show();
    }

    public void showNavPrompt(View view)
    {
        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.navfab)
                .setPrimaryText(R.string.example_fab_title)
                .setSecondaryText(R.string.example_fab_description)
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setMaxTextWidth(R.dimen.tap_target_menu_max_width)
                .show();
    }

    public void showSideNavigationPrompt(View view)
    {
        final MaterialTapTargetPrompt.Builder tapTargetPromptBuilder = new MaterialTapTargetPrompt.Builder(this)
                .setPrimaryText(R.string.menu_prompt_title)
                .setSecondaryText(R.string.menu_prompt_description)
                .setFocalPadding(R.dimen.dp40)
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setMaxTextWidth(R.dimen.tap_target_menu_max_width)
                .setIcon(R.drawable.ic_menu);
        final Toolbar tb = (Toolbar) this.findViewById(R.id.toolbar);
        tapTargetPromptBuilder.setTarget(tb.getChildAt(1));

        tapTargetPromptBuilder.setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
        {
            @Override
            public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
            {
                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                {
                    //Do something such as storing a value so that this prompt is never shown again
                }
            }
        });
        tapTargetPromptBuilder.show();
    }

    public void showOverflowPrompt(View view)
    {
        final MaterialTapTargetPrompt.Builder tapTargetPromptBuilder = new MaterialTapTargetPrompt.Builder(this)
                .setPrimaryText(R.string.overflow_prompt_title)
                .setSecondaryText(R.string.overflow_prompt_description)
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setMaxTextWidth(R.dimen.tap_target_menu_max_width)
                .setIcon(R.drawable.ic_more_vert);
        final Toolbar tb = (Toolbar) this.findViewById(R.id.toolbar);
        final View child = tb.getChildAt(2);
        if (child instanceof ActionMenuView)
        {
            final ActionMenuView actionMenuView = ((ActionMenuView) child);
            tapTargetPromptBuilder.setTarget(actionMenuView.getChildAt(actionMenuView.getChildCount() - 1));
        }
        else
        {
            Toast.makeText(this, R.string.overflow_unavailable, Toast.LENGTH_SHORT);
        }
        tapTargetPromptBuilder.show();
    }

    public void showSearchPrompt(View view)
    {
        new MaterialTapTargetPrompt.Builder(this)
                .setPrimaryText(R.string.search_prompt_title)
                .setSecondaryText(R.string.search_prompt_description)
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setMaxTextWidth(R.dimen.tap_target_menu_max_width)
                .setIcon(R.drawable.ic_search)
                .setTarget(R.id.action_search)
                .show();
    }

    public void showBottomSheetDialogPrompt(View view)
    {
        final BottomSheetDialogFragmentExample bottomSheetDialogFragmentExample =
                new BottomSheetDialogFragmentExample();

        bottomSheetDialogFragmentExample.show(getSupportFragmentManager(),
                bottomSheetDialogFragmentExample.getTag());
    }

    public void showStylePrompt(View view)
    {
        final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(this, R.style.MaterialTapTargetPromptTheme_FabTarget);
        final Toolbar tb = (Toolbar) this.findViewById(R.id.toolbar);
        final View child = tb.getChildAt(2);
        if (child instanceof ActionMenuView)
        {
            final ActionMenuView actionMenuView = ((ActionMenuView) child);
            builder.setTarget(actionMenuView.getChildAt(actionMenuView.getChildCount() - 1));
        }
        else
        {
            Toast.makeText(this, R.string.overflow_unavailable, Toast.LENGTH_SHORT);
        }
        builder.setIcon(R.drawable.ic_more_vert)
                .show();
    }

    public void showDialog(View view)
    {
        startActivity(new Intent(this, DialogStyleActivity.class));
    }

    public void showActionModePrompt(View view)
    {
        mActionMode =  this.startSupportActionMode(mActionModeCallback);
        new MaterialTapTargetPrompt.Builder(MainActivity.this)
                .setPrimaryText(R.string.action_mode_prompt_title)
                .setSecondaryText(R.string.action_mode_prompt_description)
                .setFocalPadding(R.dimen.dp40)
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setMaxTextWidth(R.dimen.tap_target_menu_max_width)
                .setTarget(findViewById(android.support.v7.appcompat.R.id.action_mode_close_button))
                .setIcon(R.drawable.ic_back)
                .show();
    }

    public void showActivity(View view)
    {
        startActivity(new Intent(this, EmptyActivity.class));
    }


    public void showCentreActivity(View view)
    {
        startActivity(new Intent(this, CentrePositionActivity.class));
    }

    public void showCardsActivity(View view)
    {
        startActivity(new Intent(this, CardActivity.class));
    }

    public void showNoAutoDismiss(View view)
    {
        if (mFabPrompt != null)
        {
            return;
        }
        mFabPrompt = new MaterialTapTargetPrompt.Builder(MainActivity.this)
                .setTarget(findViewById(R.id.fab))
                .setPrimaryText("No Auto Dismiss")
                .setSecondaryText("This prompt will only be removed after tapping the envelop")
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setAutoDismiss(false)
                .setAutoFinish(false)
                .setCaptureTouchEventOutsidePrompt(true)
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                        {
                            mFabPrompt.finish();
                            mFabPrompt = null;
                        }
                        else if (state == MaterialTapTargetPrompt.STATE_DISMISSING)
                        {
                            mFabPrompt = null;
                        }
                    }
                })
                .show();
    }

    public void showPopupWindow(View view)
    {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.fragment_bottom_sheet, null);
            // create a 300px width and 470px height PopupWindow
            PopupWindow pw = new PopupWindow(layout, 300, 470, true);
            // display the popup in the center
            pw.showAtLocation(view, Gravity.CENTER, 0, 0);

            MaterialTapTargetPrompt prompt = new MaterialTapTargetPrompt.Builder(new PopupWindowResourceFinder(pw, getWindow()), 0)
                    .setTarget(R.id.bs_search)
                    .setPrimaryText("Popup window")
                    .setSecondaryText("Popup window example")
                    .setAnimationInterpolator(new FastOutSlowInInterpolator())
                    .setCaptureTouchEventOutsidePrompt(true)
                    .create();
            prompt.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mFabPrompt != null)
                {
                    mFabPrompt.finish();
                    mFabPrompt = null;
                }
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
