package com.project5.rcrsms.Entity;

import java.util.stream.Stream;

public enum Role {
    ADMIN,
    CHAIR,
    PARTICIPANT;

    Stream<UserEntity> stream() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stream'");
    }
}
