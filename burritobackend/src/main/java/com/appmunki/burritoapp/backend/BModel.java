package com.appmunki.burritoapp.backend;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.server.spi.response.BadRequestException;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonAutoDetect;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.DEFAULT,
        getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public abstract class BModel<T extends BModel> {
    public abstract String toJson() throws BadRequestException;

    public static <T> T fromJson(String json,Class<T> clazz) throws BadRequestException {
        try {
            return JsonUtils.mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new BadRequestException("Error parsing json");
        }
    }
    public  Resource toResource() throws BadRequestException {
        String json = toJson();
        return new Resource().setType(getClass().getSimpleName()).setData(this.toJson());
    }
}

