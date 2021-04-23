package com.arraigntech.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.arraigntech.entity.User;

public class AuthUserDetail extends User implements UserDetails {
	
	
	private static final long serialVersionUID = 1L;

	public AuthUserDetail(User user) {
		super(user);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> grantedAuthority = new ArrayList<>();
		super.getRoles().forEach(role -> {
//			grantedAuthority.add(new SimpleGrantedAuthority(role.getName()));
			role.getPermissions().forEach(permission -> {
				grantedAuthority.add(new SimpleGrantedAuthority(permission.getName()));
			});
		});
		return grantedAuthority;
	}

	@Override
	public String getPassword() {
		return super.getPassword();
	}

	@Override
	public String getUsername() {
		return super.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return super.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return super.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return super.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled();
	}
//	private static final long serialVersionUID = 1L;
//	 private User user;
//	 Set<GrantedAuthority> authorities=null;
//
//
//
//	public Collection<? extends GrantedAuthority> getAuthorities() {
////		List<GrantedAuthority> grantedAuthority = new ArrayList<>();
////		super.getRoles().forEach(role -> {
//////			grantedAuthority.add(new SimpleGrantedAuthority(role.getName()));
////			role.getPermissions().forEach(permission -> {
////				grantedAuthority.add(new SimpleGrantedAuthority(permission.getName()));
////			});
////		});
//		return authorities;
//	}
//	public void setAuthorities(Set<GrantedAuthority> authorities)
//    {
//        this.authorities=authorities;
//    }
//
//	public void setUser(User user) {
//		this.user=user;
//
//	}
//
//
//	public String getPassword() {
//		return super.getPassword();
//	}
//
//	public String getUsername() {
//		return super.getUsername();
//	}
//
//
//	public boolean isAccountNonExpired() {
//		return super.isAccountNonExpired();
//	}
//
//	public boolean isAccountNonLocked() {
//		return super.isAccountNonLocked();
//	}
//
//
//	public boolean isCredentialsNonExpired() {
//		return super.isCredentialsNonExpired();
//	}
//
//
//	public boolean isEnabled() {
//		return super.isEnabled();
//	}

}
