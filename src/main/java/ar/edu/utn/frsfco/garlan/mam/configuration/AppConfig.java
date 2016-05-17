package ar.edu.utn.frsfco.garlan.mam.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configurations of all the logic of my app. Data access repositories, bussines
 * logic, models, etc.
 *
 * <p><a href="AppConfig.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@Configuration
@ComponentScan(basePackages = "ar.edu.utn.frsfco.garlan.mam")
public class AppConfig {
}