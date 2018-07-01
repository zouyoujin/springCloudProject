package com.kitty.springcloud.oauth.server.domain;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * Base abstract class for entities which will hold definitions for created, last modified by and created,
 * last modified by date.
 */
@Data
public abstract class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private String createdBy;

    @JsonIgnore
    private Instant createdDate = Instant.now();

    @JsonIgnore
    private String lastModifiedBy;

    @JsonIgnore
    private Instant lastModifiedDate = Instant.now();

}
