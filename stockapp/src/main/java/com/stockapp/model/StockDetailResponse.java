package com.stockapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockDetailResponse {

  @SerializedName("Status")
  @Expose
  public String Status;

  @SerializedName("Name")
  @Expose
  public String Name;

  @SerializedName("Symbol")
  @Expose
  public String Symbol;

  @SerializedName("LastPrice")
  @Expose
  public String LastPrice;

  @SerializedName("Change")
  @Expose
  public String Change;

  @SerializedName("ChangePercent")
  @Expose
  public String ChangePercent;

  @SerializedName("Timestamp")
  @Expose
  public String Timestamp;

  @SerializedName("MSDate")
  @Expose
  public String MSDate;

  @SerializedName("MarketCap")
  @Expose
  public String MarketCap;

  @SerializedName("Volume")
  @Expose
  public String Volume;

  @SerializedName("ChangeYTD")
  @Expose
  public String ChangeYTD;

  @SerializedName("ChangePercentYTD")
  @Expose
  public String ChangePercentYTD;

  @SerializedName("High")
  @Expose
    public String High;

    @SerializedName("Low")
    @Expose
    public String Low;

    @SerializedName("Open")
    @Expose
    public String Open;

}