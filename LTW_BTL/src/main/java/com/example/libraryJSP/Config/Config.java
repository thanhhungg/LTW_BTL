package com.example.libraryJSP.Config;

import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.context.support.ResourceBundleMessageSource;
@Configuration
public class Config implements WebMvcConfigurer {
	@Bean
	public LocaleResolver localeResolver() {
	SessionLocaleResolver localeResolver = new SessionLocaleResolver();
	localeResolver.setDefaultLocale(new Locale("vi"));
	return localeResolver;
	}
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
	LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
	lci.setParamName("lang") ;
	return lci;
	}
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	registry.addInterceptor(localeChangeInterceptor());
	}

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
