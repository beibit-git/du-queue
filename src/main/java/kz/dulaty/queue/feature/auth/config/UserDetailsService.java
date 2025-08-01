package kz.dulaty.queue.feature.auth.config;

import kz.dulaty.queue.feature.auth.data.entity.User;
import kz.dulaty.queue.feature.auth.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким адресом электронной почты уже существует!"));
        return new UserDetails(user);
    }
}
