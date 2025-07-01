package kz.dulaty.queue.core.configs.security;

import kz.dulaty.queue.feature.data.entity.auth.User;
import kz.dulaty.queue.feature.data.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DulatyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public DulatyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким адресом электронной почты уже существует!"));
        return new DulatyUserDetails(user);
    }
}
