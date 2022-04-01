package com.User.Management.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.User.Management.dao.UserRepository;
import com.User.Management.entities.User;

public class UserDetailsServiceImpl  implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 
		//fetching use from database
		User user=userRepository.getUserByUserName(username);
		
		if(user == null)
		{
		throw new UsernameNotFoundException("could not find the user");	
		}
		
		CustomUserDetails customUserDetails=new CustomUserDetails(user);
		
		return customUserDetails;
	}
	
	

}
