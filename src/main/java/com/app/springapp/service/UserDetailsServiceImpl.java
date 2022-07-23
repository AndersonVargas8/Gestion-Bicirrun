package com.app.springapp.service;


import java.util.HashSet;
import java.util.Set;

import com.app.springapp.entity.Rol;
import com.app.springapp.entity.Usuario;
import com.app.springapp.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDetailsServiceImpl implements  UserDetailsService{

    @Autowired
    UsuarioRepository repUser;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario appUser = repUser.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username inválido"));

        Set<GrantedAuthority> grantList = new HashSet<GrantedAuthority>();

        Rol role = appUser.getRol();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getDescripcion());
        grantList.add(grantedAuthority);
        

        UserDetails user = (UserDetails) new User(username, appUser.getPassword(),grantList);
        return user;
    }
    
}
