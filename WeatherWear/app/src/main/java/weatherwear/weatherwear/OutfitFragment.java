package weatherwear.weatherwear;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


import weatherwear.weatherwear.clothing.ClothingDatabaseHelper;
import weatherwear.weatherwear.clothing.ClothingItem;

/**
 * Created by Emma on 2/16/16.
 */
public class OutfitFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate the view
        View rootView = inflater.inflate(R.layout.current_outfit_fragment, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        // set the actionbar title and menu options
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(R.string.todays_outfit);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // set the new option
        MenuItem menuitem;
        menuitem = menu.add(Menu.NONE, 0,0, "New");
        menuitem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                // start a NewOutfitActivity
                Intent intent = new Intent(getActivity(), NewOutfitActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        String mKey = getString(R.string.preference_name);
        SharedPreferences mPrefs = getActivity().getSharedPreferences(mKey, getActivity().MODE_PRIVATE);

        // Set the user's display name
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String name = sp.getString(PreferenceFragment.PREFERENCE_VALUE_DISPLAY_NAME, "");
        if (!name.equals("")) {
            name += "'s ";
        }

        ((TextView)getView().findViewById(R.id.welcome)).setText(name + "Latest Outfit");

        // set the date
        String date = mPrefs.getString("DATE_INDEX", null);
        if (date != null) ((TextView)getView().findViewById(R.id.outfit_date)).setText(date);

        // set the location
        String location = mPrefs.getString("LOCATION_INDEX", null);
        if (location != null) ((TextView)getView().findViewById(R.id.location)).setText(location);

        // set the High in Fahrenheit or Celsius
        Integer high = mPrefs.getInt("HIGH_INDEX", -500);
        if (high != -500) {
            if (!sp.getString(PreferenceFragment.PREFERENCE_VALUE_TEMP,"-1").equals("Celsius")) {
                ((TextView) (getView().findViewById(R.id.high))).setText("High: " + high + "°F");
            } else {
                ((TextView) (getView().findViewById(R.id.high))).setText("Low: " +  String.valueOf(Math.round((((Double.valueOf(high)-32)*5/9)) * 10) / 10) + "°C");
            }
        }

        // set the Low in Fahrenheit or Celsius
        Integer low = mPrefs.getInt("LOW_INDEX", -500);
        if (low != -500) {
            if (!sp.getString(PreferenceFragment.PREFERENCE_VALUE_TEMP,"-1").equals("Celsius")) {
                ((TextView) (getView().findViewById(R.id.low))).setText("Low: " + low + "°F");
            } else {
               ((TextView) (getView().findViewById(R.id.low))).setText("Low: " +  String.valueOf(Math.round((((Double.valueOf(low)-32)*5/9)) * 10) / 10) + "°C");
            }
        }

        // set the condition
        String condition = mPrefs.getString("CONDITION_INDEX", null);
        if (condition != null)
            ((TextView)getView().findViewById(R.id.condition)).setText(condition);
        else {
            // if no past outfit, set a new user message
            ((TextView)getView().findViewById(R.id.welcome)).setText(R.string.outfit_fragment_no_outfit_message);
        }

        // load the pictures for the outfit
        new LoadOutfitAsyncTask().execute();

    }

    private class LoadOutfitAsyncTask extends AsyncTask<Void, Void, ArrayList<ClothingItem>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<ClothingItem> clothes) {
            super.onPostExecute(clothes);

            getView().findViewById(R.id.outfit_description).setVisibility(View.VISIBLE);

            // set the top picture or set view as invisible
            if (clothes.get(0) != null)  {
                ((getView().findViewById(R.id.top))).setVisibility(View.VISIBLE);
                ((ImageView) (getView().findViewById(R.id.top_image))).setImageBitmap(clothes.get(0).getImage());
                ((getView().findViewById(R.id.top_group))).setVisibility(View.VISIBLE);
            } else {
                ((getView().findViewById(R.id.top))).setVisibility(View.GONE);
                ((getView().findViewById(R.id.top_group))).setVisibility(View.GONE);
            }

            // set the bottom picture or set view as invisible
            if (clothes.get(1) != null)  {
                ((getView().findViewById(R.id.bottom))).setVisibility(View.VISIBLE);
                ((ImageView) (getView().findViewById(R.id.bottom_image))).setImageBitmap(clothes.get(1).getImage());
                ((getView().findViewById(R.id.bottom_group))).setVisibility(View.VISIBLE);
            }else {
                ((getView().findViewById(R.id.bottom))).setVisibility(View.GONE);
                ((getView().findViewById(R.id.bottom_group))).setVisibility(View.GONE);
            }

            // set the shoes picture or set view as invisible
            if (clothes.get(2) != null)  {
                ((getView().findViewById(R.id.shoes))).setVisibility(View.VISIBLE);
                ((ImageView) (getView().findViewById(R.id.shoes_image))).setImageBitmap(clothes.get(2).getImage());
                ((getView().findViewById(R.id.shoes_group))).setVisibility(View.VISIBLE);
            } else {
                ((getView().findViewById(R.id.shoes))).setVisibility(View.GONE);
                ((getView().findViewById(R.id.shoes_group))).setVisibility(View.GONE);
            }

            // set the outerwear picture or set view as invisible
            if (clothes.get(3) != null)  {
                ((getView().findViewById(R.id.outerwear))).setVisibility(View.VISIBLE);
                ((ImageView) (getView().findViewById(R.id.outerwear_image))).setImageBitmap(clothes.get(3).getImage());
                ((getView().findViewById(R.id.outerwear_group))).setVisibility(View.VISIBLE);
            } else {
                ((getView().findViewById(R.id.outerwear))).setVisibility(View.GONE);
                ((getView().findViewById(R.id.outerwear_group))).setVisibility(View.GONE);
            }

            // set the gloves picture or set view as invisible
            if (clothes.get(4) != null)  {
                ((getView().findViewById(R.id.accessories))).setVisibility(View.VISIBLE);
                ((ImageView) (getView().findViewById(R.id.gloves_image))).setImageBitmap(clothes.get(4).getImage());
                ((getView().findViewById(R.id.gloves_group))).setVisibility(View.VISIBLE);
            } else {
                ((getView().findViewById(R.id.gloves_group))).setVisibility(View.GONE);
            }

            // set the hats picture or set view as invisible
            if (clothes.get(5) != null)  {
                ((getView().findViewById(R.id.accessories))).setVisibility(View.VISIBLE);
                ((ImageView) (getView().findViewById(R.id.hats_image))).setImageBitmap(clothes.get(5).getImage());
                ((getView().findViewById(R.id.hats_group))).setVisibility(View.VISIBLE);
            } else {
                ((getView().findViewById(R.id.hats_group))).setVisibility(View.GONE);
            }

            // set the scarves picture or set view as invisible
            if (clothes.get(6) != null)  {
                ((getView().findViewById(R.id.accessories))).setVisibility(View.VISIBLE);
                ((ImageView) (getView().findViewById(R.id.scarves_image))).setImageBitmap(clothes.get(6).getImage());
                ((getView().findViewById(R.id.scarves_group))).setVisibility(View.VISIBLE);
            } else {
                ((getView().findViewById(R.id.scarves_group))).setVisibility(View.GONE);
            }

            // if there are no accessories, set accessories view as invisible
            if (clothes.get(4) == null && clothes.get(5) == null && clothes.get(6) == null) {
                ((getView().findViewById(R.id.accessories))).setVisibility(View.GONE);
            }
        }

        @Override
        protected ArrayList<ClothingItem> doInBackground(Void... params) {
            ClothingDatabaseHelper dbHelper = new ClothingDatabaseHelper(getActivity());
            ArrayList<ClothingItem> clothes = new ArrayList<ClothingItem>();

            // get shared preferences
            String mKey = getString(R.string.preference_name);
            SharedPreferences mPrefs = getActivity().getSharedPreferences(mKey, getActivity().MODE_PRIVATE);

            // for each parameter check if index is -1, if not add clothing item to clothes
            Long top = mPrefs.getLong("TOP_INDEX", -1);
            if (top != -1) {
                clothes.add(dbHelper.fetchItemByIndex(top));
            } else clothes.add(null);

            Long bottom = mPrefs.getLong("BOTTOM_INDEX", -1);
            if (bottom != -1) {
                clothes.add(dbHelper.fetchItemByIndex(bottom));
            } else clothes.add(null);

            Long shoes = mPrefs.getLong("SHOES_INDEX", -1);
            if (shoes != -1) {
                clothes.add(dbHelper.fetchItemByIndex(shoes));
            } else clothes.add(null);

            Long outerwear = mPrefs.getLong("OUTERWEAR_INDEX", -1);
            if (outerwear != -1) {
                clothes.add(dbHelper.fetchItemByIndex(outerwear));
            } else clothes.add(null);

            Long gloves = mPrefs.getLong("GLOVES_INDEX", -1);
            if (gloves != -1) {
                clothes.add(dbHelper.fetchItemByIndex(gloves));
            } else clothes.add(null);

            Long hats = mPrefs.getLong("HATS_INDEX", -1);
            if (hats != -1) {
                clothes.add(dbHelper.fetchItemByIndex(hats));
            } else clothes.add(null);

            Long scarves = mPrefs.getLong("SCARVES_INDEX", -1);
            if (scarves != -1) {
                clothes.add(dbHelper.fetchItemByIndex(scarves));
            } else clothes.add(null);

            // return the ArrayList of clothes to onPostExecute
            return clothes;
        }
    }

}