package ro.valerian.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ro.valerian.smart.dao.UserRepository;
import ro.valerian.smart.entities.User;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //fetching user from database
        User user=userRepository.getUserByUsername(username);

        if (user==null){
            throw  new UsernameNotFoundException("Could not found user!!");
        }
        CustomUserDetails customUserDetails=new CustomUserDetails(user);

        return customUserDetails;
    }
}
