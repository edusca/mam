package ar.edu.utn.frsfco.garlan.mam.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 * Thymeleaf template view configuration
 * 
 * <p><a href="ThymeleafConfig.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@Configuration
public class ThymeleafConfig {

    @Bean
    public ServletContextTemplateResolver mContextTemplateResolver() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        
        return templateResolver;
    }
    
    @Bean
    public SpringTemplateEngine mTemplateEngine() {
        SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
        springTemplateEngine.setTemplateResolver(mContextTemplateResolver());
        
        return springTemplateEngine;
    }
    
    @Bean
    public ThymeleafViewResolver mThymeleafViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(mTemplateEngine());
        
        return resolver;
    }
}
