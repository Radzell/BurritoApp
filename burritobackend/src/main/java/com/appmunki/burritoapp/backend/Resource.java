package com.appmunki.burritoapp.backend;

/**
 * Created by radzell on 12/23/14.
 */
public class Resource {
    public String type;
    public String data;

    public Resource setData(String data) {
        this.data =data;
        return this;
    }

    public Resource setType(String type) {
        this.type = type;
        return this;
    }

}
