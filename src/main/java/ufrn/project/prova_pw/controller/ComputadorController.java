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

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

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

        List<Computador> computador = service.findAll();
        model.addAttribute("computador", computador);

        model.addAttribute("computador", computador);

        Cookie cookie = new Cookie("visita","cookie-value");
        cookie.setMaxAge(60*60*24);
        response.addCookie(cookie);

        return "index";
    }

    @GetMapping("/cadastrar")
    public String doCadastrar(Model model){
        Computador c = new Computador();
        model.addAttribute("computador", c);
        
        return "cadastrar";
    }
    
    @PostMapping("/salvar")
    public String doSalvaComputador(@ModelAttribute @Valid Computador c, Errors errors,
                                    @RequestParam("file") MultipartFile file,
                                    RedirectAttributes redirectAttributes, HttpServletRequest request){

        if (errors.hasErrors()){
            redirectAttributes.addAttribute("msg", "Cadastro fracassado");
            return "index";
        }else {
            try {
                c.setImagem(file.getOriginalFilename());
                service.update(c);
                fileStorageService.save(file);

                redirectAttributes.addFlashAttribute("msgSucesso", "Salvo com sucesso");
                return "index";
            } catch (Exception e) {
                redirectAttributes.addAttribute("msg", "Cadastro FRACASSO!");
                return "index";
            }
        }
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


    @GetMapping("/admin")
    public String adminPage(Model model){
        List<Computador> computador = service.findAll();
        model.addAttribute("computador", computador);

        model.addAttribute("computador", computador);

        return "admin";
    }

    @GetMapping("/vercarrinho")
    public String getVerCarrinho(Model model){
        List<Computador> computador = service.findAll();
        model.addAttribute("computador", computador);

        model.addAttribute("computador", computador);
        
        return "vercarrinho";
    }

    @GetMapping("/visualizarCarrinho")
    public String visualizarCarrinho(HttpServletRequest request, Model model) throws ServletException, IOException {
        Cookie carrinhoCompras = new Cookie("carrinhoCompras", "");
        Cookie[] requestCookies = request.getCookies();
        boolean achouCarrinho = false;
        if (requestCookies != null) {
            for (var c : requestCookies) {
                achouCarrinho = true;
                carrinhoCompras = c;
                break;
            }
        }
        Computador computador = null;
        var i = 0;
        ArrayList<Computador> lista_computadores = new ArrayList();

        if(achouCarrinho == true) {
            StringTokenizer tokenizer = new StringTokenizer(carrinhoCompras.getValue(), "|");
            while (tokenizer.hasMoreTokens()) {
                String input = tokenizer.nextToken();
                int number = Integer.parseInt(input);
                String numericString = input.replaceAll("[^0-9]", ""); // Remove caracteres não numéricos
                computador = service.findById((long) number);

                lista_computadores.add(computador);
            }
            model.addAttribute("computador", lista_computadores);
            return "vercarrinho";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/adicionarCarrinho/{id}")
    public String doAdicionarItem(Model model, @PathVariable (name = "id") Long id,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int contCarrinho = 0;

        HttpSession session = request.getSession();
        List<Computador> carrinho = (List<Computador>) session.getAttribute("carrinho");
        Computador computador = service.findById(id);
        if(carrinho == null){
            carrinho = new ArrayList<>();
        }

        carrinho.add(computador);
        contCarrinho = carrinho.size();
        session.setAttribute("carrinho", carrinho);


        List<Computador> computadores = service.findAll();
        List<Computador> computadorUtil = new ArrayList<>();
        
        computadores.forEach(computador1 -> {
            if (computador1.getDeleted() != null){
                computadorUtil.add(computador1);
            }
        });
        model.addAttribute("computador", computadorUtil);

        return "index";
    }

}
