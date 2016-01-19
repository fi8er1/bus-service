package com.byteshaft.busservice.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.byteshaft.busservice.R;

public class ManageDrivers extends Fragment {


    View convertView;
    LinearLayout layoutCreateDriver;
    LinearLayout layoutListDriver;

    MenuItem menuItemDone;
    MenuItem menuItemAdd;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.layout_manage_drivers, null);
        setHasOptionsMenu(true);

        layoutCreateDriver = (LinearLayout) convertView.findViewById(R.id.layout_create_driver_form);
        layoutListDriver = (LinearLayout) convertView.findViewById(R.id.layout_drivers_list);

        return convertView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_manage, menu);
        menuItemDone = menu.findItem(R.id.action_done_button);
        menuItemAdd = menu.findItem(R.id.action_add_button);
        menuItemDone.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done_button:
                menuItemDone.setVisible(false);
                menuItemAdd.setVisible(true);
                layoutListDriver.setVisibility(View.VISIBLE);
                layoutCreateDriver.setVisibility(View.GONE);
                return true;
            case R.id.action_add_button:
                menuItemAdd.setVisible(false);
                menuItemDone.setVisible(true);
                layoutListDriver.setVisibility(View.GONE);
                layoutCreateDriver.setVisibility(View.VISIBLE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
