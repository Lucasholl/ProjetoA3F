package projetoa3;
public class ADM {
    
    private String usuario;
    private String tipo;

    public ADM(String usuario, String tipo) {
        this.usuario = usuario;
        this.tipo = tipo;
        
    }

    

    public void cadastroUsuario(Usuario novoUsuario) {
        
    }

    public void alteracaoUsuario(Usuario usuario) {
       
    }

    public Usuario consultaUsuario(String usuario) {
        
        return null; 
    }

    public void exclusaoUsuario(String usuario) {
        
    }

    

    
    public String getUsuario() {
        return this.usuario;
    }

    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    
    public String getTipo(){
        return this.tipo;
    }
   public void setTipo(String tipo){
       this.tipo = tipo;
   }

    }

