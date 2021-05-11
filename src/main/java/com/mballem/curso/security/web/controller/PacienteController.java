package com.mballem.curso.security.web.controller;

import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.MedicoService;
import com.mballem.curso.security.service.PacienteService;
import com.mballem.curso.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/dados")
    public String cadastrar(Paciente paciente, ModelMap modelMap, @AuthenticationPrincipal User user) {

        paciente = pacienteService.buscarPorUsuarioEmail(user.getUsername());

        if(paciente.hasNotId()){
            paciente.setUsuario(new Usuario(user.getUsername()));
        }
        modelMap.addAttribute("paciente",paciente);
        return "paciente/cadastro";
    }


    @PostMapping("/salvar")
    public String salvar(Paciente paciente, ModelMap modelMap, @AuthenticationPrincipal User user) {

        Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());

        if(UsuarioService.isSenhaCorreta(paciente.getUsuario().getSenha(),usuario.getSenha())){
            paciente.setUsuario(usuario);
            pacienteService.salvar(paciente);
            modelMap.addAttribute("sucesso","Seus dados foram inseridos com sucesso.");
        }else{
            modelMap.addAttribute("falha","Sua senha não confere, tente novamente.");
        }

        return "paciente/cadastro";
    }

    @PostMapping("/editar")
    public String editar(Paciente paciente, ModelMap modelMap, @AuthenticationPrincipal User user) {

        Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());

        if(UsuarioService.isSenhaCorreta(paciente.getUsuario().getSenha(),usuario.getSenha())){
            pacienteService.editar(paciente);
            modelMap.addAttribute("sucesso","Seus dados foram alterado com sucesso.");
        }else{
            modelMap.addAttribute("falha","Sua senha não confere, tente novamente.");
        }

        return "paciente/cadastro";
    }
}
