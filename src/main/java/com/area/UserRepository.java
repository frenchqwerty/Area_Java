package com.area;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserInfo, Long> {
    UserInfo findByEmail(String email);
    UserInfo findByConfirmationToken(String confirmationToken);
}
