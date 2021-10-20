package com.tulin.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA
 * User: Ginger
 * Date: 2018/8/9
 * Time: 5:06 PM
 */
public class SimpleBeanNative implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String url;
    private ArrayList<String> name;

    public int getId() {
        return id;
    }

    public void setName(ArrayList<String> name) {
        this.name = name;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public ArrayList<String> getName() {

        return name;
    }

    public String getUrl() {

        return url;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SimpleBeanNative{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", name=" + name +
                '}';
    }
}
