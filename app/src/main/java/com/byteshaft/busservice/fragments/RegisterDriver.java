package com.byteshaft.busservice.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.byteshaft.busservice.R;
import com.byteshaft.busservice.utils.Helpers;

public class RegisterDriver extends Fragment {

    View convertView;
    EditText etDriverFirstName;
    EditText etDriverLastName;
    EditText etDriverContactNumber;
    Spinner etDriverRouteSpinner;
    String firstNameDriver;
    String lastNameDriver;
    String contactNumberDriver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.layout_register_driver, null);
        setHasOptionsMenu(true);

        etDriverFirstName = (EditText) convertView.findViewById(R.id.et_driver_first_name);
        etDriverLastName = (EditText) convertView.findViewById(R.id.et_driver_last_name);
        etDriverContactNumber = (EditText) convertView.findViewById(R.id.et_driver_contact);

        return convertView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_done, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done_button:

                firstNameDriver = etDriverFirstName.getText().toString();
                lastNameDriver = etDriverLastName.getText().toString();
                contactNumberDriver = etDriverContactNumber.getText().toString();

                register();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void register() {

        if (!validate()) {
            onRegistrationFailed();
            return;
        }

        Helpers.showProgressDialog(getActivity(), "Registering");

        // TODO: Implement registration here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onRegistrationSuccess();
                        Helpers.dismissProgressDialog();
                    }
                }, 2000);
    }

    public boolean validate() {
        boolean valid = true;

        if (firstNameDriver.trim().isEmpty() || firstNameDriver.length() < 3) {
            etDriverFirstName.setError("at least 3 characters");
            valid = false;
        } else {
            etDriverFirstName.setError(null);
        }

        if (lastNameDriver.trim().isEmpty() || lastNameDriver.length() < 3) {
            etDriverLastName.setError("at least 3 characters");
            valid = false;
        } else {
            etDriverLastName.setError(null);
        }

        if (contactNumberDriver.trim().isEmpty() || contactNumberDriver.length() < 4) {
            etDriverContactNumber.setError("Contact is required");
            valid = false;
        } else {
            etDriverContactNumber.setError(null);
        }

        if (valid && !PhoneNumberUtils.isGlobalPhoneNumber(contactNumberDriver)) {
            etDriverContactNumber.setError("Number is Invalid");
            valid = false;
        }

        return valid;
    }

    public void onRegistrationSuccess() {
        Helpers.closeKeyboard(getActivity(), etDriverContactNumber.getWindowToken());
        getActivity().onBackPressed();
    }

    public void onRegistrationFailed() {
        Toast.makeText(getActivity(), "Registration failed", Toast.LENGTH_LONG).show();
    }
}
