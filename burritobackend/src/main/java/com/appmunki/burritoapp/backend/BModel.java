package com.appmunki.burritoapp.backend;


import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.api.server.spi.response.BadRequestException;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonAutoDetect;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnoreProperties;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonProperty;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.io.IOException;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.DEFAULT,
        getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public abstract class BModel<T extends BModel> {
    @Id
    @Index
    @JsonProperty
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public String uid;

    public static <T extends BModel> T  createModel(Class<T> clazz) {
        try {
            T entity = clazz.newInstance();
            entity.generateUID();
            return entity;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String generateUID() {
        this.setUid(UUID.randomUUID().toString());
        return this.getUID();
    }


    public String toJson() throws BadRequestException {
        try {
            return JsonUtils.mapper.writeValueAsString(this);
        } catch (IOException e) {
            throw new BadRequestException("Error parsing json");
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) throws BadRequestException {
        try {
            return JsonUtils.mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new BadRequestException("Error parsing json");
        }
    }

    public Resource toResource() throws BadRequestException {
        String json = toJson();
        return new Resource().setType(getClass().getSimpleName()).setData(this.toJson());
    }

    @ApiResourceProperty(name = "uid")
    public String getUID() {
        return uid;
    }

    public void setUid(String mUID) {
        this.uid = mUID;
    }
}

