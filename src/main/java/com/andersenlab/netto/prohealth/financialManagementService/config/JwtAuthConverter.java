package com.andersenlab.netto.prohealth.financialManagementService.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Custom jwtToken converter for adding Keycloak realm roles to the token
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    /**
     * Retrieves present roles from jwtToken token and adds new ones from 'realm_access->roles' jwtToken entry headed with
     * 'ROLE_' prefix
     *
     * @param jwt the source object to convert, which must be an instance of {@code S} (never {@code null})
     * @return jwtToken token containing both previous and added roles
     */
    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Set<GrantedAuthority> authorities = Stream.concat(
                        getAuthorities(jwt).stream(),
                        extractResourceRoles(jwt).stream())
                .collect(Collectors.toSet());
        var jwtAuthenticationToken = new JwtAuthenticationToken(jwt, authorities, jwt.getClaim("preferred_username"));
        log.debug("Username: {}", jwtAuthenticationToken.getName());
        log.debug("Authorities: {}", authorities);
        return jwtAuthenticationToken;
    }

    @NonNull
    private Collection<GrantedAuthority> getAuthorities(Jwt jwt) {
        var authorityCollection = jwtGrantedAuthoritiesConverter.convert(jwt);

        return authorityCollection == null ? Set.of() : authorityCollection;
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null) return Set.of();
        Collection<String> resourceRoles = (Collection<String>) realmAccess.get("roles");
        if (resourceRoles == null) return Set.of();
        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}

