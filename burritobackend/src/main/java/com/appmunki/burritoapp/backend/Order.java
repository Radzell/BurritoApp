package com.appmunki.burritoapp.backend;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;

import java.util.Map;

@Entity
@Cache
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.DEFAULT,
        getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class Order extends BModel {
    @JsonProperty
    public String status;
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private Ref<User> orderer;
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private Ref<Deliver> deliver;
    @JsonProperty
    private Map<Key<Dish>, Integer> cart;

    @ApiResourceProperty(name = "orderer")
    public User getOrderer() {
        return orderer.get();
    }

    @ApiResourceProperty(name = "orderer")
    public void setOrderer(String orderer) {
        this.orderer = Ref.create(Key.create(User.class, orderer));
    }

    @ApiResourceProperty(name = "deliver")
    public Deliver getDeliver() {
        return deliver.get();
    }

    @ApiResourceProperty(name = "deliver")
    public void setDeliver(String deliver) {
        this.deliver = Ref.create(Key.create(Deliver.class, deliver));
    }

}
