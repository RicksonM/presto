package usm.web.pretoplus.controller;

//Importando bibliotecas necessarias

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import usm.web.pretoplus.model.Contrato;
import usm.web.pretoplus.model.Descricao;
import usm.web.pretoplus.model.Usuario;
import usm.web.pretoplus.repository.DescricaoRepository;
import usm.web.pretoplus.repository.UsuarioRepository;

@Controller
public class UsuarioController {

	//Injeção do repositorio UsuarioRepository
	@Autowired
	private UsuarioRepository ur;
	@Autowired
	private DescricaoRepository dr;

	
	
	/* -------------------- CADASTRAR CLIENTE---------------------------------- */
	
	
	//Método para Cadastro de usuario como Cliente através do INSERT
	@GetMapping("/inseriruser")
	public ModelAndView inserir() {                  
		ModelAndView resultado = new ModelAndView("presto/cadastro/caduser");
		resultado.addObject("user", new Usuario());
		return resultado;
	}	
	
	//URL que recebe, trata e salva os dados no banco de dados
	@PostMapping("/inseriruser")
	public String inserir (Usuario user) {
		user.setSenha(new BCryptPasswordEncoder().encode(user.getPassword())); //Criptografando a senha para salvar no banco de dados
		user.setTipo("cliente");    //Definido FLAG, se é cliente ou prestador
		user.setEmail(user.getEmail());
		ur.save(user);
		return "redirect:/cadsucess";
	}
	
	
	/* ----------------------CADASTRAR PRESTADOR-------------------------------- */
	
	
	//Método de cadastro do usuario como Prestador através do INSERT
	@GetMapping("/cadP/{email}")
	public ModelAndView cadP(@PathVariable String email) {
		Usuario usuario = ur.getOne(email);
		ModelAndView resultado = new ModelAndView("presto/cadastro/cadprest");
		resultado.addObject("user", usuario);
		return resultado;
	}
	
	//URL que recebe o cadastro de prestador e salva
	@PostMapping("/cad")
	public String cadP(Usuario user) {
		user.setEmail(user.getEmail());
		user.setTipo("prestador");  //Definindo FLAG como prestador
		ur.save(user);
		return "redirect:/";
	}
	
	
	/* ------------------------EDITANDO CADASTRO DE CLIENTE------------------------------ */

	
	//Método que recebe o objeto usuario com pelo email e edita os dados
	@GetMapping("/editarS/{email}")
	public ModelAndView edit(@PathVariable String email) {
		Usuario usuario = ur.getOne(email);
		ModelAndView resultado = new ModelAndView("presto/edicao/editar");
		resultado.addObject("user", usuario);
		return resultado;
	}
	
	//Salvando as edições
	@PostMapping("/edit")
	public String editarus (Usuario user) {
		user.setSenha(new BCryptPasswordEncoder().encode(user.getPassword()));
		user.setEmail(user.getEmail());
		
		//Verificando o tipo do usuario (cliente ou prestador) 
		if(user.getTipo() == "cliente") {
			user.setTipo("cliente");
		} else if (user.getTipo().toString() == "prestador") {
			user.setTipo("prestador");
		}
		ur.save(user);
		return "redirect:/";
	}
	
	
	/* -----------------------EDITAR CADASTRO PERSTADOR------------------------------- */
	
	
	//Método de editar o cadastro de prestador
	@GetMapping("/editarP/{email}")
	public ModelAndView editar(@PathVariable String email) {
		Usuario usuario = ur.getOne(email);
		ModelAndView resultado = new ModelAndView("presto/cadastro/editarP");
		resultado.addObject("user", usuario);
		return resultado;
	}
	//URL que recebe o cadastro de prestador e salva
	@PostMapping("/editP")
	public String edits(Usuario user) {
		user.setEmail(user.getEmail());
		user.setTipo("prestador");
		ur.save(user);
		return "redirect:/";
	}
	
	
	/* ------------------------METODO DE CADASTRO ERRO E SUCESSO------------------------------ */
	
	
	//Método de erro no cadastro
	@RequestMapping("/caderror")
	public String error() {
		return "/presto/cadastro/cadastroerror";
	}
	
	//Método de sucesso no cadastro
	@RequestMapping("/cadsucess")
	public String sucess() {
		return "/presto/cadastro/cadastrosucesso";
	}
	 
	
	/* -----------------------LSTAGEM DE PRESTADORES------------------------------- */


	//Método da listagem
	@GetMapping("/busca")
	public ModelAndView busca(Authentication authentication) {  //ALTERAR A PÁGINA ASSIM QUE RECEBER O LAYOUT
		ModelAndView resultado = new ModelAndView("presto/busca/buscar");
		List<Usuario> usuarios = ur.findBytipo("prestador"); //Listagem apenas dos prestadores
		resultado.addObject("log", authentication);
		resultado.addObject("user", usuarios);
		return resultado;
	}
	
	
	/* -----------------------BUSCA PELO INPUT DE BUSCAR------------------------------- */
	
	
	@PostMapping("/pesquisaProfissao")
	public ModelAndView findpro(@RequestParam("profissao") String profissao, Authentication authentication){
		ModelAndView resultado = new ModelAndView("presto/busca/buscar");
		resultado.addObject("user", ur.findByprofissao(profissao));
		resultado.addObject("log", authentication);
		return resultado;
		
	}
	
	
	
	@GetMapping("/prestador/{email}")
	public ModelAndView visualizarPrest(@PathVariable String email, Authentication authentication) {  //ALTERAR A PÁGINA ASSIM QUE RECEBER O LAYOUT
		Usuario usuario = ur.getOne(email);
		ModelAndView resultado = new ModelAndView("presto/visPrestador/visualizarpres");
		Descricao avalia = new Descricao();
		List<Descricao> descr = dr.findByperfil(email);
		resultado.addObject("prestador", usuario);
		resultado.addObject("avaliacao", avalia);
		resultado.addObject("desc", descr);
		resultado.addObject("contrato", new Contrato()); // CONTRATO PARA SER ACEITO NO MOLDAL DE CONTRATO
		resultado.addObject("log", authentication);
		return resultado;
	}
	
	
	
}


