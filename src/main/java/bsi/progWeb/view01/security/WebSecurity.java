package bsi.progWeb.view01.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{
    
    @Autowired
    private UsuarioDetailsService usuarioDetailsService;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/", "/clientes").hasRole("comum")
                .antMatchers(HttpMethod.GET,"/clientes/cadastrar","/clientes/alterar","/clientes/excluir").hasRole("admin")
                .antMatchers(HttpMethod.POST,"/clientes/cadastrar").hasRole("admin")
                .and()
                .formLogin();
    }
    
//    @Override
//    protected void configure(AuthenticationManagerBuilder builder) throws Exception{
//        builder.inMemoryAuthentication()
//                .withUser("cliente").password("123").roles("comum")
//                .and()
//                .withUser("admin").password("123").roles("admin");
//    }
    
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(usuarioDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
}
