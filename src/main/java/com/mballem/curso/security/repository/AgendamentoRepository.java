package com.mballem.curso.security.repository;

import com.mballem.curso.security.domain.Agendamento;
import com.mballem.curso.security.domain.Horario;
import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.repository.projection.HistoricoPaciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento,Long> {

     @Query("select h " +
             "from Horario h " +
             "where not exists(" +
                    "select a.horario.id " +
                         "from Agendamento a " +
                         "where " +
                              "a.medico.id = :id and " +
                              "a.dataConsulta = :data and " +
                              "a.horario.id = h.id" +
             ") " +
             "order by h.horaMinuto asc")
     List<Horario> findByMedicosPorEspecialidade(Long id, LocalDate data);

     @Query("select a.id as id," +
             "a.paciente as paciente," +
             "CONCAT(a.dataConsulta, ' ', a.horario.horaMinuto) as dataConsulta," +
             "a.especialidade as especialidade, " +
             "a.medico as medico " +
             "from Agendamento a " +
             "where a.paciente.usuario.email like :email")
     Page<HistoricoPaciente> findByHistoricoByPacienteEmail(String email, Pageable pageable);


     @Query("select a.id as id," +
             "a.paciente as paciente," +
             "CONCAT(a.dataConsulta, ' ', a.horario.horaMinuto) as dataConsulta," +
             "a.especialidade as especialidade, " +
             "a.medico as medico " +
             "from Agendamento a " +
             "where a.medico.usuario.email like :email")
     Page<HistoricoPaciente> findByHistoricoByMedicoEmail(String email, Pageable pageable);
}
