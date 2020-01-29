package bsi.progWeb.view01.security;

import bsi.progWeb.view01.models.Role;
import bsi.progWeb.view01.models.Usuario;
import bsi.progWeb.view01.repository.UsuarioRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService{
    
    
    private UsuarioRepository repo;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repo.findByLogin(username);
        if(usuario == null){
            throw new UsernameNotFoundException(username);
        }
        
        return new User (usuario.getLogin(), usuario.getPassword(), authorities(usuario.getRoles()));
    }
    
    public List<? extends GrantedAuthority> authorities(List<Role> lista){
        List<GrantedAuthority> auths = new ArrayList<>();
        for(Role role : lista){
            auths.add(new SimpleGrantedAuthority("ROLE_"+role.getNome()));
        }
        return auths;
    }

}  