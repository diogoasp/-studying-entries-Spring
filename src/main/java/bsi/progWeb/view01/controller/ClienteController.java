package bsi.progWeb.view01.controller;

import bsi.progWeb.view01.models.Cliente;
import bsi.progWeb.view01.models.Telefone;
import bsi.progWeb.view01.repository.ClienteRepository;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository repo;
    
    @GetMapping
    public String getAll(Model model){
      model.addAttribute("lista", repo.findAll());
      return "listar";
    }
    
    @GetMapping("/cadastrar")
    public String register(Model model){
       model.addAttribute("cliente", new Cliente());
       return "cadastrar"; 
    }
    
    @GetMapping("/excluir/{id}")
    public String deleteById(@PathVariable Long id, Model model){
        repo.deleteById(id);
        return "redirect:../../clientes";
    }
    
    @PostMapping("/cadastrar")
    public String post(@Valid @ModelAttribute Cliente cliente, BindingResult bindingResult, @RequestParam("file") MultipartFile file, @RequestParam("pdf") MultipartFile pdf, Model model){
        if(bindingResult.hasErrors())
            return "cadastrar";
        
        if(file.isEmpty() || pdf.isEmpty()){
            model.addAttribute("msgFile", "Arquivo não carregado.");
            return "cadastrar";
        }else if(!file.getContentType().equals(MediaType.IMAGE_JPEG_VALUE) || !pdf.getContentType().equals(MediaType.APPLICATION_PDF_VALUE)){
            model.addAttribute("msgFile", "Tipo de arquivo inválido.");
            return "cadastrar";
        }
        try {
            String name = Calendar.getInstance().getTimeInMillis() + file.getOriginalFilename();
            file.transferTo(Paths.get("/home/ryzen/Imagens/teste/img/" + name));
            cliente.setFoto("/files/jpg/" + name);
            repo.save(cliente);
        } catch (Exception e) {
            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, e);
        }
          try {
            String name = Calendar.getInstance().getTimeInMillis() + pdf.getOriginalFilename();
            pdf.transferTo(Paths.get("/home/ryzen/Imagens/teste/pdf/" + name));
            cliente.setCurriculo("/files/pdf/" + name);
            repo.save(cliente);
        } catch (Exception e) {
            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, e);
        }
        return "redirect:../clientes";
    }
    
    @PostMapping(path = "/cadastrar", params = "addTel")
    public String addTel(@ModelAttribute Cliente cliente, @RequestParam("file") MultipartFile file, @RequestParam("pdf") MultipartFile pdf, Model model){
        cliente.getTelefones().add(new Telefone());
        return "cadastrar";
    }
    
    @GetMapping("/alterar/{id}")
    public String put(@PathVariable Long id, Model model){ 
        if(repo.findById(id).isPresent()){
            Cliente cliente = repo.findById(id).get();
            model.addAttribute(cliente);
            return "cadastrar";
        }
        return "listar";
    }
    
}
