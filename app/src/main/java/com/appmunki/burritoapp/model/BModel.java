package com.appmunki.burritoapp.model;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Table;
import com.appmunki.burritoapp.backend.userApi.model.Resource;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

/**
 * Created by radzell on 12/23/14.
 */
@Table(name="",id = BaseColumns._ID)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.DEFAULT,
        getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public abstract class BModel<T extends Model> extends Model {
    public BModel() {

    }


    public abstract String toJson() throws JsonProcessingException;

    public static <T> T fromJson(String json, Class<T> clazz) throws IOException {
        return JsonUtils.mapper.readValue(json, clazz);
    }


    public Resource toResource() throws JsonProcessingException {
        return new Resource().setType(getClass().getSimpleName()).setData(toJson());
    }
}
