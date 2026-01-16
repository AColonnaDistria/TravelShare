package com.travel.travelshare.repositories.travelpath;

import android.content.Context;
import com.travel.travelshare.model.travelpath.TravelPath_Activity;

public class TravelPath_ActivityRepository extends TravelPath_SimpleRepository<TravelPath_Activity> {
    public TravelPath_ActivityRepository(Context context) {
        super(context, TravelPath_Activity.class, "activites");
    }
}