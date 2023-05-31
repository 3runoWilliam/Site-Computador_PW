package ufrn.project.prova_pw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.http.Cookie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import ufrn.project.prova_pw.model.Computador;
import ufrn.project.prova_pw.service.ComputadorService;
import ufrn.project.prova_pw.service.FileStorageService;

@Controller
public class ComputadorController {
    
    private static int contador;
    ComputadorService service;

    @Autowired
    FileStorageService fileStorageService;

    public ComputadorController(ComputadorService service, FileStorageService fileStorageService){
        this.service = service;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/")
    public String getIndex(Model model, HttpServletResponse response, HttpSession session){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", ""+authentication.getName());

        List<Computador> carrinho = (List<Computador>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new ArrayList<>();
        }
        model.addAttribute("carrinho", carrinho);

        List<Computador> sapatos = service.findByDeletedIsNull();
        model.addAttribute("sapatos",sapatos);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
        String dataHora = dateFormat.format(new Date());
        Cookie c = new Cookie("visita", dataHora);
        c.setMaxAge(86400);
        response.addCookie(c);

        return "index";
    }

    @GetMapping("/cadastrar")
    public String doCadastrar(Model model){
        Computador c = new Computador();
        model.addAttribute("computador", c);

        return "cadastrar";
    }

    @GetMapping("editar/{id}")
    public String getEditarComputador(Model model, @PathVariable Long id, RedirectAttributes redirectAttributes){

        Computador computador = service.findById(id);
        model.addAttribute("computador", computador);

        redirectAttributes.addAttribute("msg2", "Cadastro atualizado com sucesso");
        return "cadastrar";
    }

    @GetMapping("deletar/{id}")
    public String deletarComputador(@PathVariable Long id, RedirectAttributes redirectAttributes){
        Computador c = service.findById(id);
        c.setDeleted(new Date(System.currentTimeMillis()));
        service.create(c);
        redirectAttributes.addAttribute("msg", "Deletado com sucesso");
        return "redirect:/";
    }

    @PostMapping("/salvar")
    public String doSalvaComputador(@ModelAttribute @Valid Computador c, Errors errors,
                                    @RequestParam("file") MultipartFile file,
                                    RedirectAttributes redirectAttributes){

        if (errors.hasErrors()){
            redirectAttributes.addAttribute("msg", "Cadastro fracassado");
            return "redirect:/";
        }else {
            try {
                c.setImagem(fileStorageService.save(file));
                service.create(c);
                redirectAttributes.addFlashAttribute("msgSucesso", "Salvo com sucesso");
                return "redirect:/admin";
            } catch (Exception e) {
                redirectAttributes.addAttribute("msg", "Cadastro FRACASSO!");
                return "redirect:/";
            }
        }
    }
    
}
