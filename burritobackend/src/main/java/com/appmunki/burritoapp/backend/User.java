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
import java.util.UUID;

@Entity
@Cache
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.DEFAULT,
        getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class User extends BModel {

    @Id
    @Index
    @JsonProperty
    public String uid;
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

    public String generateUID() {
        this.setUid(UUID.randomUUID().toString());
        return this.getUID();
    }

    public String generateToken() {
        this.setToken(UUID.randomUUID().toString());
        return this.getToken();
    }


    public String getUID() {
        return uid;
    }

    public void setUid(String mUID) {
        this.uid = mUID;
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

    @Override
    public String toJson() throws BadRequestException {
        try {
            return JsonUtils.mapper.writeValueAsString(this);
        } catch (IOException e) {
            throw new BadRequestException("Error parsing json");
        }
    }


    public static User fromJson(String json) throws BadRequestException {
        return fromJson(json, User.class);
    }
    public Resource toResource() throws BadRequestException {
        String json = toJson();
        return new Resource().setType(getClass().getSimpleName()).setData(toJson());
    }
}
