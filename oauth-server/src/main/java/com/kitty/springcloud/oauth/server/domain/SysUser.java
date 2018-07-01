package com.kitty.springcloud.oauth.server.domain;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by wangyunfei on 2017/6/9.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SysUser extends AbstractAuditingEntity{
  
	private static final long serialVersionUID = 1734452357832100752L;

    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String username;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;


    @Size(max = 256)
    private String imageUrl;


    @JsonIgnore
    private Set<SysRole> roles = new HashSet<>();

    private Set<GrantedAuthority> authorities = new HashSet<>();

    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> userAuthotities = new HashSet<>();
        for(SysRole role : this.roles){
            for(SysAuthority authority : role.getAuthorities()){
                userAuthotities.add(new SimpleGrantedAuthority(authority.getValue()));
            }
        }

        return userAuthotities;
    }
}
