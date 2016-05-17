package ar.edu.utn.frsfco.garlan.mam.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Default page controller. Static and home views here
 * 
 * <p><a href="PageController.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@Controller
public class PageController {
	private final String INDEX_VIEW = "index";
	
	@RequestMapping("/")
	public String indexPage() {
		return INDEX_VIEW;
	}
}
