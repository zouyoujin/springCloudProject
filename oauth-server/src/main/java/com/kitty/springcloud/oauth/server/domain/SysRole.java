package com.kitty.springcloud.oauth.server.domain;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by wangyunfei on 2017/6/9.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRole extends AbstractAuditingEntity{
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String value;

    @JsonIgnore
    private Set<SysAuthority> authorities = new HashSet<>();
}
