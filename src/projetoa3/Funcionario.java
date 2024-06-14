package projetoa3;

import java.util.List;

public class Funcionario extends Usuario {
    private String comentario;
    
    public Funcionario(int id, String tipo, String nome, String email, String senha, String telefone, String usuario, String comentario) {
        super(id, tipo, nome, email, senha, telefone, usuario);
        this.comentario = comentario;
    }

   

    public void atualizacaoProjeto(Projeto projeto) {
        
    }

    /**
     *
     * @param codigo
     * @return
     */
    
    public Projeto consultaProjeto(String codigo) {
        List<Projeto> projetos = super.getProjetos();
        for (Projeto projeto : projetos) {
            if (projeto.getCodigo().equals(codigo) ) {
                return projeto;
            }
        }
        return null; 
    }
}
