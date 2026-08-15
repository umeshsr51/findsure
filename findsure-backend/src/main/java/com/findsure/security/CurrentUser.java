package com.findsure.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Put on a Long controller-method parameter to have it resolved to the
 * authenticated user's id (pulled from the JWT by JwtAuthFilter). E.g.:
 *
 *   @GetMapping("/api/items")
 *   public ... list(@CurrentUser Long userId) { ... }
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}
