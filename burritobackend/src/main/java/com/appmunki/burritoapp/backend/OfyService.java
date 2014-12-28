package com.appmunki.burritoapp.backend;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * Created by radzell on 12/23/14.
 */
public class OfyService {
    static {
        factory().register(User.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();

    }

    public static void register(Class<?> clazz) {
        factory().register(clazz);
    }
}
