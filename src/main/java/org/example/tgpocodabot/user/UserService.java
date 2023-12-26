package org.example.tgpocodabot.user;

import lombok.RequiredArgsConstructor;
import org.example.tgpocodabot.user.entity.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public void save(User user) {
        repository.save(user);
    }

    public void update(User updatedUser) {
        User existingUser = repository.
                findById(updatedUser.getChatId()).orElse(null);

        if (existingUser != null) {
            existingUser.setUserRegion(updatedUser.getUserRegion());
            repository.save(existingUser);
        }
    }

}
