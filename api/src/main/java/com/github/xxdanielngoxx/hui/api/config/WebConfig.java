package com.github.xxdanielngoxx.hui.api.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@EnableWebMvc
@Configuration(proxyBeanMethods = false)
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/webapp/**")
        .addResourceLocations("classpath:/static/webapp/")
        .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS))
        .resourceChain(true)
        .addResolver(new FallbackSPAResourceResolver());
  }

  @Override
  public void configureViewResolvers(ViewResolverRegistry registry) {
    final InternalResourceViewResolver internalResourceViewResolver =
        new InternalResourceViewResolver();

    registry.viewResolver(internalResourceViewResolver);
  }
}

class FallbackSPAResourceResolver extends PathResourceResolver {

  @Override
  public Resource resolveResource(
      @Nullable HttpServletRequest request,
      @NonNull String requestPath,
      @NonNull List<? extends Resource> locations,
      @NonNull ResourceResolverChain chain) {

    final Resource resolvedResource = super.resolveResource(request, requestPath, locations, chain);

    if (Objects.nonNull(resolvedResource)) {
      return resolvedResource;
    }

    return new ClassPathResource("static/webapp/index.html");
  }
}
