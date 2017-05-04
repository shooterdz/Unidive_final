package com.unidrive.file_dropbox;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"url",
"visibility",
"path"
})
public class Share {

@JsonProperty("url")
private String url;
@JsonProperty("visibility")
private Visibility visibility;
@JsonProperty("path")
private String path;

@JsonProperty("url")
public String getUrl() {
return url;
}

@JsonProperty("url")
public void setUrl(String url) {
this.url = url;
}

@JsonProperty("visibility")
public Visibility getVisibility() {
return visibility;
}

@JsonProperty("visibility")
public void setVisibility(Visibility visibility) {
this.visibility = visibility;
}

@JsonProperty("path")
public String getPath() {
return path;
}

@JsonProperty("path")
public void setPath(String path) {
this.path = path;
}

}