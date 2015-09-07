package com.pennapps.instanote;

import android.util.Log;

import java.util.Comparator;

/**
 * Created by QingxiaoDong on 9/5/15.
 */
public class CustomEventComparator implements Comparator<EventItem> {

    private static final String LOG_TAG = CustomEventComparator.class.getSimpleName();

    private int sortCode;

    public static final int AZ_ORDER = R.id.sort_title;
    public static final int ST_ORDER = R.id.sort_favorite;

    public CustomEventComparator(int sortCode) {
        this.sortCode = sortCode;
    }

    @Override
    public int compare(EventItem event1, EventItem event2) {
        if (sortCode == AZ_ORDER) {
            return event1.name.compareTo(event2.name);
        } else {
            double dif = 0;
            if (sortCode == ST_ORDER) {
                dif = event2.favorite.compareTo(event1.favorite);
            } else {
                Log.d(LOG_TAG, "SortCode is undefined!");
            }

            if (dif < 0) {
                return -1;
            } else if (dif > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
