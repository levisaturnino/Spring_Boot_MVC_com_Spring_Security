package com.mballem.curso.security.web.controller;

import com.mballem.curso.security.domain.*;
import com.mballem.curso.security.service.AgendamentoService;
import com.mballem.curso.security.service.EspecialidadeService;
import com.mballem.curso.security.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Arrays;


@Controller
@RequestMapping("agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private EspecialidadeService especialidadeService;

    @GetMapping("/agendar")
    public String cadastrar(Agendamento agendamento) {
        return "agendamento/cadastro";
    }

    @GetMapping("/horario/medico/{id}/data/{data}")
    public ResponseEntity<?> getHorarios(@PathVariable("id") Long id,
                                         @PathVariable("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(agendamentoService.buscarHorariosNaoAgendadosPorMedicoIdEData(id,data));
    }

    @PostMapping("/salvar")
    public String salvar(Agendamento agendamento, RedirectAttributes attr, @AuthenticationPrincipal User user) {
        Paciente paciente = pacienteService.buscarPorUsuarioEmail(user.getUsername());

        String titulo = agendamento.getEspecialidade().getTitulo();

        Especialidade especialidade = especialidadeService
                .buscarPorTitulos(new String[] {titulo})
                .stream().findFirst().get();
        agendamento.setEspecialidade(especialidade);
        agendamento.setPaciente(paciente);
        agendamentoService.salvar(agendamento);
        return "redirect:/agendamentos/agendar";
    }


    @GetMapping({"/historico/paciente","/historico/consultas"})
    public String historico() {
        return "agendamento/historico-paciente";
    }


    @GetMapping("/datatables/server/historico")
    public ResponseEntity<?>  historicoAgendamentosPorPaciente(HttpServletRequest request, @AuthenticationPrincipal User user) {

        if(user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.PACIENTE.getDesc()))){
            return ResponseEntity.ok(agendamentoService.buscarHistoricoPorPacienteEmail(user.getUsername(),request));
        }

        if(user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.MEDICO.getDesc()))){
            return ResponseEntity.ok(agendamentoService.buscarHistoricoPorMedicoEmail(user.getUsername(),request));
        }
        return ResponseEntity.notFound().build();
    }
}