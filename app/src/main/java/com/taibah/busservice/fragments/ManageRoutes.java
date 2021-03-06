package com.taibah.busservice.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.taibah.busservice.Helpers.WebServiceHelpers;
import com.taibah.busservice.R;
import com.taibah.busservice.utils.AppGlobals;
import com.taibah.busservice.utils.Helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class ManageRoutes extends Fragment {

    public static int responseCode;

    ArrayList<Integer> routeIdsList;
    HashMap<Integer, ArrayList<String>> hashMapRouteData;


    ListView routesListView;

    View convertView;
    HttpURLConnection connection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.layout_manage_route, null);
        setHasOptionsMenu(true);

        routesListView = (ListView) convertView.findViewById(R.id.lv_list_routes);

        new RetrieveAllRegisteredRoutes().execute();

        routeIdsList = new ArrayList<>();
        hashMapRouteData = new HashMap<>();

        return convertView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_manage, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_button:
                AppGlobals.replaceFragment(getFragmentManager(), new RegisterRoute());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    static class ViewHolder {
        TextView tvRouteListName;
        TextView tvRouteListBusNumber;
        TextView tvRouteListArrivalTime;
        TextView tvRouteListDepartureTime;
    }

    public class RetrieveAllRegisteredRoutes extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Helpers.showProgressDialog(getActivity(), "Retrieving routes list");
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (Helpers.isNetworkAvailable() && Helpers.isInternetWorking()) {
                try {
                    connection = WebServiceHelpers.openConnectionForUrl
                            ("http://46.101.75.194:8080/routes", "GET");
                    connection.setRequestProperty("X-Api-Key", AppGlobals.getToken());
                    connection.connect();
                    responseCode = connection.getResponseCode();
                    String data = WebServiceHelpers.readResponse(connection);
                    JSONArray jsonArray = new JSONArray(data);
                    System.out.println(jsonArray);
                    responseCode = connection.getResponseCode();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (!routeIdsList.contains(jsonObject.getInt("id"))) {
                            routeIdsList.add(jsonObject.getInt("id"));
                            ArrayList<String> arrayListString = new ArrayList<>();
                            arrayListString.add(jsonObject.getString("name"));
                            arrayListString.add(jsonObject.getString("bus_number"));
                            hashMapRouteData.put(jsonObject.getInt("id"), arrayListString);
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Helpers.dismissProgressDialog();
            if (responseCode == 200) {
                CustomRoutesListAdapter customRoutesListAdapter = new CustomRoutesListAdapter(getContext(), R.layout.route_list_row, routeIdsList);
                routesListView.setAdapter(customRoutesListAdapter);
            } else {
                // TODO Implement correct logic here in case of any failure
                Toast.makeText(getActivity(), "Invalid Response " + responseCode, Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        }
    }

    class CustomRoutesListAdapter extends ArrayAdapter<String> {

        ArrayList<Integer> arrayListIntIds;

        public CustomRoutesListAdapter(Context context, int resource, ArrayList<Integer> arrayList) {
            super(context, resource);
            arrayListIntIds = arrayList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.route_list_row, parent, false);
                viewHolder.tvRouteListName = (TextView) convertView.findViewById(R.id.tv_route_list_name);
                viewHolder.tvRouteListBusNumber = (TextView) convertView.findViewById(R.id.tv_route_list_bus_number);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvRouteListName.setText("Route Name: " + hashMapRouteData.get(arrayListIntIds.get(position)).get(0));
            viewHolder.tvRouteListBusNumber.setText("Bus Number: " + hashMapRouteData.get(arrayListIntIds.get(position)).get(1));
            return convertView;
        }

        @Override
        public int getCount() {
            return arrayListIntIds.size();
        }
    }

}