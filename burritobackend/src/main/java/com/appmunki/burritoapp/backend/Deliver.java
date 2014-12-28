package com.appmunki.burritoapp.backend;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Subclass;

import java.util.List;

@Subclass(index=true)
public class Deliver extends User {
    List<Ref<Order>> orders;
}
