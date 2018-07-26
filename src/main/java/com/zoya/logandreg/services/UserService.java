package com.zoya.logandreg.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.zoya.logandreg.models.Role;
import com.zoya.logandreg.models.User;
import com.zoya.logandreg.repositories.RoleRepository;
import com.zoya.logandreg.repositories.UserRepository;

@Service
public class UserService {
	private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    
    //Saves a client with only the user role.
    public void saveWithUserRole(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(roleRepository.findByName("ROLE_USER"));
        userRepository.save(user);
    }
    
    public void addAdminRole(User user) {
        List<Role> userroles = user.getRoles();
        List<Role> adminrole = roleRepository.findByName("ROLE_ADMIN");
    	List<Role> newList = Stream.concat(userroles.stream(), adminrole.stream()).collect(Collectors.toList());	
        user.setRoles(newList);
      
        userRepository.save(user);
    }   
    
    //Saves a client with only the admin role.
//    public void saveUserWithAdminRole(User user) {
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        user.setRoles(roleRepository.findByName("ROLE_ADMIN"));
//        userRepository.save(user);
//    }    
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public ArrayList<User> all() {
    	return (ArrayList<User>) userRepository.findAll();
    }
    
    
}
