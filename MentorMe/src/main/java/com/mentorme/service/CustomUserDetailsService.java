package com.mentorme.service;
import com.mentorme.model.UserAccount;
import com.mentorme.repository.UserAccountRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class CustomUserDetailsService implements UserDetailsService {
 private final UserAccountRepository users;
 public CustomUserDetailsService(UserAccountRepository users){this.users=users;}
 @Override public UserDetails loadUserByUsername(String email){
  UserAccount u=users.findByEmailIgnoreCase(email).orElseThrow(()->new UsernameNotFoundException("Account not found"));
  return new org.springframework.security.core.userdetails.User(u.getEmail(),u.getPassword(),List.of(new SimpleGrantedAuthority("ROLE_"+u.getRole())));
 }
}