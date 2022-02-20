package com.greatlearning.week8assignment.Restraunt.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.greatlearning.week8assignment.Restraunt.exception.MailAlreadyExistsException;
import com.greatlearning.week8assignment.Restraunt.exception.MailNotFoundException;
import com.greatlearning.week8assignment.Restraunt.model.Role;
import com.greatlearning.week8assignment.Restraunt.model.User;
import com.greatlearning.week8assignment.Restraunt.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		if (user == null)
			throw new UsernameNotFoundException("Invalid username or password.");
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	public User findByEmail(String email) {
		return Optional.ofNullable(userRepository.findByEmail(email))
				.orElseThrow(() -> new MailNotFoundException("No user with this email found"));
	}

	@Override
	public User save(User user) {
		User checkUser = null;
		try {
			 checkUser = findByEmail(user.getEmail());
		}catch (MailNotFoundException e) {
			log.info("User email not found in database go ahead and save the user to DB");
		}
		
		if (checkUser != null)
			throw new MailAlreadyExistsException("Mail id already present in database");

		return userRepository.save(user);
	}

	@Override
	public List<User> getUsers() {
		List<User> users = new ArrayList<>();
		userRepository.findAll().forEach(users::add);
		return users;
	}

	@Override
	public User updateUser(String email, User updatedUser) {
		User user = findByEmail(email);
		user.setEmail(updatedUser.getEmail());
		user.setFirstName(updatedUser.getFirstName());
		user.setLastName(updatedUser.getLastName());
		userRepository.save(user);
		return user;
	}

	@Override
	public void deleteUser(String email) {
		userRepository.deleteByEmail(email);
	}

}
