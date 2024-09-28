package com.github.xxdanielngoxx.hui.api.auth.helper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

import com.github.xxdanielngoxx.hui.api.auth.model.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpRequest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;

@ExtendWith(MockitoExtension.class)
class AccessTokenAuthenticationFilterTest {

  @InjectMocks private AccessTokenAuthenticationFilter filter;

  @Mock private AuthenticationManager authenticationManager;

  @Mock private AccessTokenResolver accessTokenResolver;

  @Mock private SecurityContextHolderStrategy securityContextHolderStrategy;

  @Mock private SecurityContextRepository securityContextRepository;

  @Mock private AccessTokenAuthenticationEntryPoint accessTokenAuthenticationEntryPoint;

  @Mock private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

  private HttpRequest mockHttpRequest;

  private MockHttpServletRequest mockRequest;

  private MockHttpServletResponse mockResponse;

  private MockFilterChain mockFilterChain;

  @BeforeEach
  void setup() {
    mockRequest = new MockHttpServletRequest();
    mockResponse = new MockHttpServletResponse();
    mockFilterChain = spy(new MockFilterChain());
  }

  @Test
  void should_persist_security_context_when_access_token_is_valid()
      throws ServletException, IOException {
    final AccessToken accessToken = new AccessToken("access_token", "fingering");
    given(accessTokenResolver.resolve(mockRequest)).willReturn(Optional.of(accessToken));

    final Object details = new Object();

    final AccessTokenAuthenticatedToken accessTokenAuthenticatedToken =
        new AccessTokenAuthenticatedToken(
            new AccessTokenAuthenticatedPrincipal("danielngo1998@gmail.com", Role.OWNER), details);
    given(authenticationManager.authenticate(any(AccessTokenAuthenticationToken.class)))
        .willReturn(accessTokenAuthenticatedToken);

    given(securityContextHolderStrategy.createEmptyContext())
        .willReturn(SecurityContextHolder.getContextHolderStrategy().createEmptyContext());

    filter.doFilter(mockRequest, mockResponse, mockFilterChain);

    verify(mockFilterChain, times(1)).doFilter(mockRequest, mockResponse);

    then(securityContextHolderStrategy).should(times(1)).createEmptyContext();

    final ArgumentCaptor<SecurityContext> securityContextCaptor1 =
        ArgumentCaptor.forClass(SecurityContext.class);
    verify(securityContextHolderStrategy).setContext(securityContextCaptor1.capture());
    final SecurityContext securityContextCaptured1 = securityContextCaptor1.getValue();
    assertThat(securityContextCaptured1.getAuthentication())
        .isEqualTo(accessTokenAuthenticatedToken);

    final ArgumentCaptor<SecurityContext> securityContextCaptor2 =
        ArgumentCaptor.forClass(SecurityContext.class);
    verify(securityContextRepository, times(1))
        .saveContext(securityContextCaptor2.capture(), eq(mockRequest), eq(mockResponse));
    final SecurityContext securityContextCaptured2 = securityContextCaptor2.getValue();
    assertThat(securityContextCaptured2.getAuthentication())
        .isEqualTo(accessTokenAuthenticatedToken);

    assertThat(securityContextCaptured1).isEqualTo(securityContextCaptured2);

    then(accessTokenResolver).should(times(1)).resolve(mockRequest);

    verify(authenticationDetailsSource, times(1)).buildDetails(mockRequest);

    then(authenticationManager)
        .should(times(1))
        .authenticate(any(AccessTokenAuthenticationToken.class));

    verifyNoInteractions(accessTokenAuthenticationEntryPoint);
  }

  @Test
  void should_do_nothing_when_access_token_is_not_present() throws ServletException, IOException {
    given(accessTokenResolver.resolve(mockRequest)).willReturn(Optional.empty());

    filter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

    verify(mockFilterChain, times(1)).doFilter(mockRequest, mockResponse);

    then(accessTokenResolver).should(times(1)).resolve(mockRequest);

    verifyNoInteractions(
        authenticationManager,
        securityContextHolderStrategy,
        securityContextRepository,
        authenticationDetailsSource,
        accessTokenAuthenticationEntryPoint);
  }

  @Test
  void
      should_clear_security_context_and_delegate_exception_handing_to_access_token_authentication_entry_point()
          throws ServletException, IOException {
    final AccessToken accessToken = new AccessToken("access_token", "fingering");
    given(accessTokenResolver.resolve(mockRequest)).willReturn(Optional.of(accessToken));

    given(authenticationManager.authenticate(any(AccessTokenAuthenticationToken.class)))
        .willThrow(new RuntimeException());

    filter.doFilter(mockRequest, mockResponse, mockFilterChain);

    verify(accessTokenAuthenticationEntryPoint, times(1))
        .commence(eq(mockRequest), eq(mockResponse), any(AuthenticationException.class));

    verify(securityContextHolderStrategy, times(1)).clearContext();

    verifyNoInteractions(mockFilterChain);
  }
}
