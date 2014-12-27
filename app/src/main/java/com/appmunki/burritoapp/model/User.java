package com.appmunki.burritoapp.model;

import android.provider.BaseColumns;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

/**
 * Created by radzell on 12/23/14.
 */
@Table(name = "user", id = BaseColumns._ID)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.DEFAULT,
        getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class User extends BModel<User> {

    @Column(notNull = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    @JsonProperty
    private String uid;
    @Column(notNull = true)
    @JsonProperty("number")
    private String number;
    @Column(notNull = true)
    @JsonProperty
    private String password;
    @Column
    @JsonProperty
    private String token;

    public User(String phonenumber, String password) {
        super();
        this.number = phonenumber;
        this.password = password;
    }

    public User() {
        super();
    }

    @Override
    public String toJson() throws JsonProcessingException {
        return JsonUtils.mapper.writeValueAsString(this);
    }


    public static User fromJson(String json) throws IOException {
        return fromJson(json, User.class);
    }
}