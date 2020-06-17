package usm.web.pretoplus.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import usm.web.pretoplus.model.Contrato;
import usm.web.pretoplus.model.Usuario;
import usm.web.pretoplus.repository.ContratoRepository;
import usm.web.pretoplus.repository.UsuarioRepository;

@Controller
public class ContratoController {
	
	    @Autowired
		private ContratoRepository cr;
	    
	    @Autowired
	    private UsuarioRepository ur;
	/* --------------------------CADASTRO DE CONTRATO------------------------------------------- */
	
//	
//	    @RequestMapping("/teste")
//	    public ModelAndView perfil(HttpSession session) {
//	        Usuario usuarios = ur.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
//	        ModelAndView mv = new ModelAndView("presto/contrato/contratos");
//	        session.setAttribute("user", usuarios);
//	        return mv;
//	    }    
//	    
	    
	//Método para Cadastro de usuario como Cliente através do INSERT
		@GetMapping("/contratos")
		public ModelAndView contrato(Authentication authentication) {                  
			ModelAndView resultado = new ModelAndView("presto/contrato/contratos");
			resultado.addObject("contrato", new Contrato());
			resultado.addObject("user", authentication);
			return resultado;
		}	
		
		//URL que recebe, trata e salva os dados no banco de dados
		@PostMapping("/contratos")
		public String contrato (Contrato contrato) {
			cr.save(contrato);
			return "redirect:/cadsucess";
		}
	
	
		/* ------------------------------------ Listagem --------------------------------------------------- */
		
		
		
		@GetMapping("/teste/{email}")
		public ModelAndView dali(@PathVariable String email, HttpSession session) { //ALTERAR A PÁGINA ASSIM QUE RECEBER O LAYOUT
			Usuario usuarios = ur.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
			ModelAndView resultado = new ModelAndView("presto/contrato/contratos");
			List<Contrato> contratos = cr.findByperfil(email); //Listagem apenas dos prestadores
			session.setAttribute("user", usuarios);
			resultado.addObject("contratos", contratos);
			return resultado;
		}
		
		
		
	
	
}
