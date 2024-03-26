package ru.otus.hw.models.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.otus.hw.models.entities.Privilege.*;

@RequiredArgsConstructor
public enum Role {
    EDITOR(Set.of(READ_PRIVILEGE, CREATE_PRIVILEGE, UPDATE_PRIVILEGE, DELETE_PRIVILEGE)),
    USER(Set.of(READ_PRIVILEGE)),
    SOMEONE(Set.of(CREATE_PRIVILEGE, DELETE_PRIVILEGE)),;

    @Getter
    private final Set<Privilege> privileges;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPrivileges()
                .stream()
                .map(privilege -> new SimpleGrantedAuthority(privilege.name()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return authorities;
    }

}
