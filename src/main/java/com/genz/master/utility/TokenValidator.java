package com.genz.master.utility;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import org.eclipse.microprofile.jwt.JsonWebToken;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TokenValidator {

    @Inject
    JWTParser jwtParser;

    public JsonWebToken validateToken(String token) throws ParseException {
        return jwtParser.parse(token);
    }
}