package com.letv.spo.mediaplayerex.PlayerFilter.app;

import android.content.Context;

import com.letv.spo.mediaplayerex.PlayerFilter.filter.GlBlueBlindFilter;
import com.letv.spo.mediaplayerex.PlayerFilter.filter.GlFilter;
import com.letv.spo.mediaplayerex.PlayerFilter.filter.GlGreenBlindFilter;
import com.letv.spo.mediaplayerex.PlayerFilter.filter.GlRedBlindFilter;

import java.util.ArrayList;
import java.util.List;


public enum FilterType {
    DEFAULT,
    RED_BLIND_FILTER,
    GREEN_BLIND_FILTER,
    BLUE_BLIND_FILTER;


    public static List<FilterType> createFilterList() {
        List<FilterType> filters = new ArrayList<>();

        filters.add(DEFAULT);
        filters.add(RED_BLIND_FILTER);
        filters.add(GREEN_BLIND_FILTER);
        filters.add(BLUE_BLIND_FILTER);

        return filters;
    }

    public static GlFilter createGlFilter(FilterType filterType, Context context) {
        switch (filterType) {
            case DEFAULT:
                return new GlFilter();
            case RED_BLIND_FILTER:
                return new GlRedBlindFilter();
            case GREEN_BLIND_FILTER:
                return new GlGreenBlindFilter();
            case BLUE_BLIND_FILTER:
                return new GlBlueBlindFilter();
            default:
                return new GlFilter();
        }
    }


}
