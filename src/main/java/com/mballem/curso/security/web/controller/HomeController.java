package com.mballem.curso.security.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

	// abrir pagina home
	@GetMapping({"/","/home"})
	public String home() {
		return "home";
	}

	// abrir pagina login
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	// abrir pagina login
	@GetMapping("/login-error")
	public String loginError(ModelMap modelMap) {
		modelMap.addAttribute("alerta","erro");
		modelMap.addAttribute("titulo","Crendenciais inválidas!");
		modelMap.addAttribute("texto","Login ou senha incorretos, tente novamente.");
		modelMap.addAttribute("subtexto","Acesso permitido apenas para cadasgros já ativados.");
		return "login";
	}

	// abrir pagina login
	@GetMapping("/acesso-negado")
	public String acessoNegado(ModelMap modelMap, HttpServletResponse resp) {
		modelMap.addAttribute("status",resp.getStatus());
		modelMap.addAttribute("error","Acesso Negado.");
		modelMap.addAttribute("message","Você não tem permissão para acesso a esta área ou ação.");
		return "error";
	}
}
