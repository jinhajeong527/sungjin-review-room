package com.sungjin.reviewroom.security;

import javax.transaction.Transactional;

import com.sungjin.reviewroom.dao.ReviewerRepository;
import com.sungjin.reviewroom.entity.Reviewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    ReviewerRepository reviewerRepository;
    
    @Autowired
    public UserDetailsServiceImpl(ReviewerRepository reviewerRepository) {
        this.reviewerRepository = reviewerRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Reviewer reviewer = reviewerRepository.findByEmail(email)
                            .orElseThrow(() -> new UsernameNotFoundException("No reviewer found with this email: " + email));
        
        return UserDetailsImpl.build(reviewer);
    }
    
}
