package ar.edu.utn.frsfco.garlan.mam.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Confguration for wevmvc project. Resources handlers here
 * 
 * <p><a href="WevMVCConfig.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@Configuration
@EnableWebMvc
public class WevMVCConfig extends WebMvcConfigurerAdapter{

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/bootstrap/**").addResourceLocations("/WEB-INF/resources/bootstrap/");
        registry.addResourceHandler("/dist/**").addResourceLocations("/WEB-INF/resources/dist/");
        registry.addResourceHandler("/plugins/**").addResourceLocations("/WEB-INF/resources/plugins/");
        registry.addResourceHandler("/mam/**").addResourceLocations("/WEB-INF/resources/mam/");
        registry.addResourceHandler("/word_cloud_images/**").addResourceLocations("file:/mam/output/");
    }
}
