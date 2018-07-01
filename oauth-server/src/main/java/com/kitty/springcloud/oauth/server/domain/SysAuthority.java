package com.kitty.springcloud.oauth.server.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by wangyunfei on 2017/6/9.
 */

@Data
@EqualsAndHashCode(callSuper = false)
public  class SysAuthority extends AbstractAuditingEntity{

	private static final long serialVersionUID = 1490123943016125270L;
	
    private Long id;
    private String name;
    private String value;
}
