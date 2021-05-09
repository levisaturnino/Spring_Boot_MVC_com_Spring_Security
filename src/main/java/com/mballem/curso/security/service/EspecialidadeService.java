package com.mballem.curso.security.service;

import com.mballem.curso.security.datatables.Datatables;
import com.mballem.curso.security.datatables.DatatablesColunas;
import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.repository.EspecialidadeRepository;
import com.mballem.curso.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class EspecialidadeService{

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Autowired
    private Datatables datatables;

    @Transactional(readOnly = false)
    public void salvar(Especialidade especialidade) {
        especialidadeRepository.save(especialidade);
    }

    @Transactional(readOnly = true)
    public Map<String,Object> buscarEspecialidades(HttpServletRequest request){
        datatables.setRequest(request);
        datatables.setColunas(DatatablesColunas.ESPECIALIDADES);
        Page<?> paga = datatables.getSearch().isEmpty()
                ? especialidadeRepository.findAll(datatables.getPageable())
                : especialidadeRepository.findAllByTitulo(datatables.getSearch(),datatables.getPageable());

        return datatables.getResponse(paga);
    }

    public Object buscarPorId(Long id) {
        return especialidadeRepository.findById(id);
    }

    public void remover(Long id) {
        especialidadeRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    public List<String> buscarEspecialidadeByTermo(String termo) {

        return especialidadeRepository.findEspecialidadesByTermo(termo);
    }

    @Transactional(readOnly = true)
    public Set<Especialidade> buscarPorTitulos(String[] titulos) {
        return especialidadeRepository.findByTitulos(titulos);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> buscarEspecialidadesPorMedico(Long id, HttpServletRequest request) {
        datatables.setRequest(request);
        datatables.setColunas(DatatablesColunas.ESPECIALIDADES);
        Page<Especialidade> page = especialidadeRepository.findByIdMedico(id, datatables.getPageable());
        return datatables.getResponse(page);
    }
}
