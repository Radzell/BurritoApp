package com.appmunki.burritoapp.backend;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.api.server.spi.response.BadRequestException;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonProperty;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.UUID;

@Entity
@Cache
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.DEFAULT,
        getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class User extends BModel {


    @Index
    @JsonProperty("number")
    public String number;
    @Index
    @JsonProperty("password")
    public String password;

    @Index
    @JsonProperty
    public String token;

    public User() {

    }

    public User(String number, String password) {
        this.setNumber(number);
        this.setPassword(password);
    }


    public String generateToken() {
        this.setToken(UUID.randomUUID().toString());
        return this.getToken();
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String mNumber) {
        this.number = mNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String mPassword) {
        this.password = mPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String mToken) {
        this.token = mToken;
    }


    public static User fromJson(String json) throws BadRequestException {
        return fromJson(json, User.class);
    }

    public Resource toResource() throws BadRequestException {
        return new Resource().setType(getClass().getSimpleName()).setData(toJson());
    }
}
