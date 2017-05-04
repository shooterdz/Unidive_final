package com.unidrive.drive_service;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"use",
"available"
})
public class Info {

@JsonProperty("use")
private String use;
@JsonProperty("available")
private String available;

@JsonProperty("use")
public String getUse() {
return use;
}

@JsonProperty("use")
public void setUse(String use) {
this.use = use;
}

@JsonProperty("available")
public String getAvailable() {
return available;
}

@JsonProperty("available")
public void setAvailable(String available) {
this.available = available;
}

}