package com.stockapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StocklistItemListResponse implements Serializable {

  @SerializedName("Symbol")
  @Expose
  public String Symbol;

  @SerializedName("Name")
  @Expose
  public String Name;

  @SerializedName("Exchange")
  @Expose
  public String Exchange;

}