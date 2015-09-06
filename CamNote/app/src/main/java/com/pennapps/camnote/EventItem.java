package com.pennapps.camnote;

/**
 * Created by QingxiaoDong on 9/4/15.
 */
public class EventItem {
    int id;
    String name;
    String date;
    String favorite;
    public EventItem(int eventid, String eventName, String eventDate, String eventFavorite) {
        id = eventid;
        name = eventName;
        date = eventDate;
        favorite = eventFavorite;
    }
}
