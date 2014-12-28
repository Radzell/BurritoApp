package com.appmunki.burritoapp.backend;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.api.server.spi.response.BadRequestException;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonProperty;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.io.IOException;

@Entity
@Cache
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.DEFAULT,
        getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class Dish extends BModel {

    @JsonProperty
    public String name;

    @JsonProperty
    public String description;

    @JsonProperty
    public float price;


    public static Dish fromJson(String json) throws BadRequestException {
        return fromJson(json, Dish.class);
    }

}
