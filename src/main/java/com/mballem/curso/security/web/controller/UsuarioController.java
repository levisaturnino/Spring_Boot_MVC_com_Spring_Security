package com.mballem.curso.security.web.controller;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.MedicoService;
import com.mballem.curso.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("u")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MedicoService medicoService;

    @GetMapping("/novo/cadastro/usuario")
    public String cadastroPorAdminParaAdminMedicoPaciente(Usuario usuario) {
        return "usuario/cadastro";
    }

    @GetMapping("/lista")
    public String listaUsuario() {
        return "usuario/lista";
    }

    @GetMapping("/datatables/server/usuarios")
    public ResponseEntity<?> listaUsuariosDatables(HttpServletRequest request) {
        return ResponseEntity.ok(usuarioService.buscarTodos(request));
    }

    @PostMapping("/cadastro/salvar")
    public String salvarUsuarios(Usuario usuario, RedirectAttributes attr){
        List<Perfil> perfil = usuario.getPerfis();

        if(perfil.size() > 2 ||
        perfil.containsAll(Arrays.asList(new Perfil(1L),new Perfil(3L))) ||
        perfil.containsAll(Arrays.asList(new Perfil(2L),new Perfil(3L)))){
            attr.addFlashAttribute("falha","Paciente não pode ser Admin e/ou Médico.");
            attr.addFlashAttribute("usuario",usuario);
        }else{
            try{
                usuarioService.salvarUsuario(usuario);
                attr.addFlashAttribute("sucesso","Operação realizada com sucesso!");
            }catch (DataIntegrityViolationException e ){
                attr.addFlashAttribute("falha","Cadastro não realizado, email já existente.");
            }
        }
        return "redirect:/u/novo/cadastro/usuario";
    }

    @GetMapping("/editar/credenciais/usuario/{id}")
    public ModelAndView preEditar(@PathVariable Long id){
        return new ModelAndView("usuario/cadastro","usuario",usuarioService.buscarPorId(id));
    }


    @GetMapping("/editar/dados/usuario/{id}/perfis/{perfis}")
    public ModelAndView preEditarCadastroDadosPessoais(@PathVariable(name = "id") Long id,
                                                       @PathVariable (name = "perfis") Long[] perfisId){

        Usuario us = usuarioService.buscarPorIdEPerfis(id,perfisId);

        if(us.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod())) &&
          !us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))){
            return new ModelAndView("usuario/cadastro","usuario",us);
        }else   if(us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))){
            Medico medico = medicoService.buscarPorUsuarioId(id);

            return medico.hasNotId()
                    ? new ModelAndView("medico/cadastro","medico",new Medico(new Usuario(id)))
                    : new ModelAndView("medico/cadastro","medico",medico);

         }else   if(us.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod()))) {
            ModelAndView model = new ModelAndView("error");
            model.addObject("status",403);
            model.addObject("error","Área Restrita.");
            model.addObject("message","Os dados de pacientes são restritos a ele.");
            return model;
        }
        return new ModelAndView("redirect:/u/lista");
    }
}
