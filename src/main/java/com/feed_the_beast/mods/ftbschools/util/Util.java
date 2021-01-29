package com.feed_the_beast.mods.ftbschools.util;

import com.feed_the_beast.mods.ftbschools.FTBSchools;

public class Util {

    @SuppressWarnings("unchecked")
    public static <X> X cast(Object o)
    {
        try {
            return (X) o;
        } catch (ClassCastException e) {
            FTBSchools.LOGGER.fatal("Class cast using Util.cast FAILED! ", e);
            throw e;
        }
    }
}
