package pl.futurecollars.invoicing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;

    @Value("${invoicing-system.csrf.disable:false}")
    private boolean disableCsrf;

    public WebSecurityConfig(CorsFilter corsFilter) {
        this.corsFilter = corsFilter;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .httpBasic()
            .and()
            .addFilterBefore(corsFilter, ChannelProcessingFilter.class);

        if (disableCsrf) {
            httpSecurity.csrf().disable();
        } else {
            httpSecurity.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
        }
    }
}
