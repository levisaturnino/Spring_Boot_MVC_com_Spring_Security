package com.mballem.curso.security.service;

import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Transactional(readOnly = true)
    public Medico buscarPorUsuarioId(Long id) {
        return medicoRepository.findByUsuarioId(id).orElse(new Medico());
    }
    @Transactional(readOnly = false)
    public void salvar(Medico medico) {
        medicoRepository.save(medico);
    }

    @Transactional(readOnly = false)
    public void editar(Medico medico) {
        Medico m2 = medicoRepository.findById(medico.getId()).get();
        m2.setCrm(medico.getCrm());
        m2.setDtInscricao(medico.getDtInscricao());
        m2.setNome(medico.getNome());

        if(!medico.getEspecialidades().isEmpty()){
            m2.getEspecialidades().addAll(medico.getEspecialidades());
        }
    }
    @Transactional(readOnly = true)
    public Medico buscarPorEmail(String email) {
        return medicoRepository.findByUsuarioEmail(email).orElse(new Medico());
    }

    @Transactional(readOnly = false)
    public void excluirEspecialidadeProMedico(Long idMed, Long idEsp) {

        Medico medico = medicoRepository.findById(idMed).get();
        medico.getEspecialidades().removeIf(e -> e.getId().equals(idEsp));

    }
    @Transactional(readOnly = true)
    public List<Medico> buscarMedicosPorEspecialidade(String titulo) {
        return medicoRepository.findByMedicosPorEspecialidade(titulo);
    }

    public boolean existeEspecialidadeAgendada(Long idMed, Long idEsp) {
        return medicoRepository.hasEspcialidadeAgendada(idMed,idEsp).isPresent();
    }
}
