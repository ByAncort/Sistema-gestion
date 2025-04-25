package com.app.authjwt.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SubtaskStatus {
    @JsonProperty("Pending")
    Pending,

    @JsonProperty("In Progress")
    In_Progress,

    @JsonProperty("Completed")
    Completed,

    @JsonProperty("Blocked")
    Blocked
}
