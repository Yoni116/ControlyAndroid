package net.controly.controly.activity;

/**
 * This is an activity for showing the user's locations on the map.
 */
public class MyLocationsActivity extends BaseLocationMapActivity {
    @Override
    protected boolean placeAutoCompleteEnabled() {
        return false;
    }

    @Override
    protected boolean fetchLocationsEagerly() {
        return true;
    }
}
