package projetoa3;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private int id;
    private String tipo;

    
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String usuario;
    private final List<Projeto> projetos;
    private String comentario;
    private String codigo;
    private String descriçao;
    private String status;

    public Usuario(int ID, String tipo, String nome, String email, String senha, String telefone, String usuario) {
        this.id = ID;
        this.tipo = tipo;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.usuario = usuario;
        this.projetos = new ArrayList<>();
    }

    public Usuario(int id1, String codigo1, String descriçao1, String usuario1, String status1, String comentario1) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    

    

    

   

    
     public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

   

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
     public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public List<Projeto> getProjetos() {
        return projetos;
    }

    

    public void cadastroProjeto(Projeto projeto) {
        projetos.add(projeto);
    }

    public Projeto consultaProjeto(String codigo) {
        for (Projeto projeto : projetos) {
            if (projeto.getCodigo().equals(codigo)) {
                return projeto;
            }
        }
        return null; 
    }

    public void alteracaoProjeto(Projeto projeto) {
        
    }

    public void exclusaoProjeto(String codigo) {
        projetos.removeIf(projeto -> projeto.getCodigo() == codigo);
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setDescriçao(String descriçao) {
        this.descriçao = descriçao;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescriçao() {
        return descriçao;
    }

    public String getStatus() {
        return status;
    }

    
        
    
}

