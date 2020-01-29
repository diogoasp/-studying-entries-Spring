package bsi.progWeb.view01.repository;

import bsi.progWeb.view01.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    
    public Usuario findByLogin(String login);
}
