
package com.unidrive.file_dropbox;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "read_only",
    "parent_shared_folder_id",
    "shared_folder_id",
    "modified_by",
    "traverse_only",
    "no_access"
})
public class SharingInfo {

    @JsonProperty("read_only")
    private Boolean readOnly;
    @JsonProperty("parent_shared_folder_id")
    private String parentSharedFolderId;
    @JsonProperty("modified_by")
    private String modifiedBy;
    @JsonProperty("traverse_only")
    private Boolean traverseOnly;
    @JsonProperty("no_access")
    private Boolean noAccess;
    @JsonProperty("shared_folder_id")
    private String shared_folder_id;

    @JsonProperty("shared_folder_id")
    public String getShared_folder_id() {
		return shared_folder_id;
	}
    @JsonProperty("shared_folder_id")
	public void setShared_folder_id(String shared_folder_id) {
		this.shared_folder_id = shared_folder_id;
	}

    @JsonProperty("read_only")
    public Boolean getReadOnly() {
        return readOnly;
    }

    @JsonProperty("read_only")
    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    @JsonProperty("parent_shared_folder_id")
    public String getParentSharedFolderId() {
        return parentSharedFolderId;
    }

    @JsonProperty("parent_shared_folder_id")
    public void setParentSharedFolderId(String parentSharedFolderId) {
        this.parentSharedFolderId = parentSharedFolderId;
    }

    @JsonProperty("modified_by")
    public String getModifiedBy() {
        return modifiedBy;
    }

    @JsonProperty("modified_by")
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @JsonProperty("traverse_only")
    public Boolean getTraverseOnly() {
        return traverseOnly;
    }

    @JsonProperty("traverse_only")
    public void setTraverseOnly(Boolean traverseOnly) {
        this.traverseOnly = traverseOnly;
    }

    @JsonProperty("no_access")
    public Boolean getNoAccess() {
        return noAccess;
    }

    @JsonProperty("no_access")
    public void setNoAccess(Boolean noAccess) {
        this.noAccess = noAccess;
    }

}
