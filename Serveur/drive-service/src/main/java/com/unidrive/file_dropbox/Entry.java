
package com.unidrive.file_dropbox;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    ".tag",
    "name",
    "id",
    "parent_shared_folder_id",
    "shared_folder_id",
    "client_modified",
    "server_modified",
    "rev",
    "size",
    "path_lower",
    "path_display",
    "sharing_info",
    "property_groups",
    "has_explicit_shared_members",
    "content_hash"
})
public class Entry {

    @JsonProperty(".tag")
    private String tag;
    @JsonProperty("name")
    private String name;
    @JsonProperty("id")
    private String id;
    @JsonProperty("client_modified")
    private String clientModified;
    @JsonProperty("server_modified")
    private String serverModified;
    @JsonProperty("rev")
    private String rev;
    @JsonProperty("size")
    private Integer size;
    @JsonProperty("path_lower")
    private String pathLower;
    @JsonProperty("path_display")
    private String pathDisplay;
    @JsonProperty("sharing_info")
    private SharingInfo sharingInfo;
    @JsonProperty("property_groups")
    private List<PropertyGroup> propertyGroups = null;
    @JsonProperty("has_explicit_shared_members")
    private Boolean hasExplicitSharedMembers;
    @JsonProperty("content_hash")
    private String contentHash;
    @JsonProperty("shared_folder_id")
    private String shared_folder_id;
    @JsonProperty("parent_shared_folder_id")
    private String parent_shared_folder_id;
    
    @JsonProperty("parent_shared_folder_id")
    public String getParent_shared_folder_id() {
		return parent_shared_folder_id;
	}
    @JsonProperty("parent_shared_folder_id")
	public void setParent_shared_folder_id(String parent_shared_folder_id) {
		this.parent_shared_folder_id = parent_shared_folder_id;
	}
	@JsonProperty("shared_folder_id")
    public String getShared_folder_id() {
		return shared_folder_id;
	}
    @JsonProperty("shared_folder_id")
	public void setShared_folder_id(String shared_folder_id) {
		this.shared_folder_id = shared_folder_id;
	}

	@JsonProperty(".tag")
    public String getTag() {
        return tag;
    }

    @JsonProperty(".tag")
    public void setTag(String tag) {
        this.tag = tag;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("client_modified")
    public String getClientModified() {
        return clientModified;
    }

    @JsonProperty("client_modified")
    public void setClientModified(String clientModified) {
        this.clientModified = clientModified;
    }

    @JsonProperty("server_modified")
    public String getServerModified() {
        return serverModified;
    }

    @JsonProperty("server_modified")
    public void setServerModified(String serverModified) {
        this.serverModified = serverModified;
    }

    @JsonProperty("rev")
    public String getRev() {
        return rev;
    }

    @JsonProperty("rev")
    public void setRev(String rev) {
        this.rev = rev;
    }

    @JsonProperty("size")
    public Integer getSize() {
        return size;
    }

    @JsonProperty("size")
    public void setSize(Integer size) {
        this.size = size;
    }

    @JsonProperty("path_lower")
    public String getPathLower() {
        return pathLower;
    }

    @JsonProperty("path_lower")
    public void setPathLower(String pathLower) {
        this.pathLower = pathLower;
    }

    @JsonProperty("path_display")
    public String getPathDisplay() {
        return pathDisplay;
    }

    @JsonProperty("path_display")
    public void setPathDisplay(String pathDisplay) {
        this.pathDisplay = pathDisplay;
    }

    @JsonProperty("sharing_info")
    public SharingInfo getSharingInfo() {
        return sharingInfo;
    }

    @JsonProperty("sharing_info")
    public void setSharingInfo(SharingInfo sharingInfo) {
        this.sharingInfo = sharingInfo;
    }

    @JsonProperty("property_groups")
    public List<PropertyGroup> getPropertyGroups() {
        return propertyGroups;
    }

    @JsonProperty("property_groups")
    public void setPropertyGroups(List<PropertyGroup> propertyGroups) {
        this.propertyGroups = propertyGroups;
    }

    @JsonProperty("has_explicit_shared_members")
    public Boolean getHasExplicitSharedMembers() {
        return hasExplicitSharedMembers;
    }

    @JsonProperty("has_explicit_shared_members")
    public void setHasExplicitSharedMembers(Boolean hasExplicitSharedMembers) {
        this.hasExplicitSharedMembers = hasExplicitSharedMembers;
    }

    @JsonProperty("content_hash")
    public String getContentHash() {
        return contentHash;
    }

    @JsonProperty("content_hash")
    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

}
