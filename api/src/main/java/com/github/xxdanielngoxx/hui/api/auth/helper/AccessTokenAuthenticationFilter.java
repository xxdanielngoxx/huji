package com.github.xxdanielngoxx.hui.api.auth.helper;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class AccessTokenAuthenticationFilter extends OncePerRequestFilter {

  private final SecurityContextHolderStrategy securityContextHolderStrategy;

  private final SecurityContextRepository securityContextRepository;

  private final AccessTokenResolver accessTokenResolver;

  private final AuthenticationManager authenticationManager;

  private final AccessTokenAuthenticationEntryPoint accessTokenAuthenticationEntryPoint;

  private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

  @Override
  protected void doFilterInternal(
      @Nonnull final HttpServletRequest request,
      @Nonnull final HttpServletResponse response,
      @Nonnull final FilterChain filterChain)
      throws ServletException, IOException {

    final Optional<AccessToken> optionalAccessToken = accessTokenResolver.resolve(request);

    if (optionalAccessToken.isEmpty()) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      authenticate(request, response, optionalAccessToken.get());
      filterChain.doFilter(request, response);
    } catch (Exception exception) {
      logger.error("Failed to authenticate request", exception);
      securityContextHolderStrategy.clearContext();
      accessTokenAuthenticationEntryPoint.commence(
          request, response, new BadCredentialsException("Invalid access token"));
    }
  }

  private void authenticate(
      @Nonnull final HttpServletRequest request,
      @Nonnull HttpServletResponse response,
      @Nonnull final AccessToken accessToken) {
    final Authentication authenticationResult =
        authenticationManager.authenticate(
            new AccessTokenAuthenticationToken(
                accessToken.token(),
                accessToken.fingering(),
                authenticationDetailsSource.buildDetails(request)));

    final SecurityContext securityContext = securityContextHolderStrategy.createEmptyContext();
    securityContext.setAuthentication(authenticationResult);
    securityContextHolderStrategy.setContext(securityContext);
    securityContextRepository.saveContext(securityContext, request, response);
  }
}
