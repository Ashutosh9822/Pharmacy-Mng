
package com.becoder.serviceImpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.becoder.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        com.becoder.entity.User appUser = userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                    new UsernameNotFoundException(
                        "No account found with email: " + email
                    )
                );

        if (!appUser.isActive()) {
            throw new DisabledException("This account is disabled");
        }

        return User.withUsername(appUser.getEmail())
                .password(appUser.getPassword())
                .roles(appUser.getRole())
                .build();
    }
}
