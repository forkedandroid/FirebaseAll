package com.stockapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class StocklistItemListResponse  extends RealmObject implements Serializable {

  @SerializedName("Symbol")
  @Expose
  public String Symbol="";

  @SerializedName("Name")
  @Expose
  public String Name;

  @SerializedName("Exchange")
  @Expose
  public String Exchange="";

  @SerializedName("isFavorite")
  @Expose
  public String isFavorite = "0";



}