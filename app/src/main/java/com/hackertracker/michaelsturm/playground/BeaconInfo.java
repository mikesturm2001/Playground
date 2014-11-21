package com.hackertracker.michaelsturm.playground;

import com.google.gson.annotations.SerializedName;
import java.util.List;

import javax.xml.transform.Result;


/**
 * Created by michael sturm on 11/14/14.
 */
public class BeaconInfo {

    public List<Result> results;

    @SerializedName("_id")
    public String id;

    @SerializedName("uuid")
    public String uuid;

    @SerializedName("major")
    public String major;

    @SerializedName("minor")
    public String minor;

    @SerializedName("building")
    public String building;

    @SerializedName("floor")
    public String floor;

    @SerializedName("x")
    public Integer x;

    @SerializedName("y")
    public Integer y;

    @SerializedName("enabled")
    public Boolean enabled;

    @SerializedName("type")
    public String type;

    @SerializedName("location")
    public String location;


}
