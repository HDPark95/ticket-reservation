package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.core.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
