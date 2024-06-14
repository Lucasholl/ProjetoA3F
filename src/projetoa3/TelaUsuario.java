package projetoa3;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class TelaUsuario extends javax.swing.JFrame {

    private List<Projeto> projetos = new ArrayList<>();
    private int qtdProjeto = 0;

    /**
     * Creates new form TelaUsuario
     */
    public TelaUsuario(String tipo, String usuario) throws SQLException {
        initComponents();
        setLocationRelativeTo(null);
        SelecionarProjeto(tipo, usuario);
        TodosProjetos();

        AtualizarProjeto_btn.addActionListener((ActionEvent e) -> {
            int i = TabelaMeusProjetos.getSelectedRow();
            if (i != -1) {
                int id = (int) TabelaMeusProjetos.getValueAt(i, 6);
                String codigo = (String) CodigoOds_CB.getSelectedItem();
                String descricao = DescriçaoTextField.getText();
                String nomeUsuario = NomeUsuarioTextField.getText();
                String telefone = TelefoneTextField.getText();
                String dataCriacao = DataDeCriaçaoTextField.getText();
                String status = (String) Status_CB.getSelectedItem();
                AtualizarProjeto(id, codigo, descricao, nomeUsuario, telefone, dataCriacao, status);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um projeto para atualizar.");
            }
        });

        ExcluirProjeto_btn.addActionListener((ActionEvent e) -> {
            int i = TabelaMeusProjetos.getSelectedRow();
            if (i != -1) {
                int id = (int) TabelaMeusProjetos.getValueAt(i, 6);
                DeletarProjeto(id);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um projeto para excluir.");
            }
        });

        CriarProjeto_btn.addActionListener((ActionEvent e) -> {
            if (this.qtdProjeto < 5) {
                String codigo = (String) CodigoOds_CB.getSelectedItem();
                String descricao = DescriçaoTextField.getText();
                String nomeUsuario = NomeUsuarioTextField.getText();
                String telefone = TelefoneTextField.getText();
                String dataCriacao = DataDeCriaçaoTextField.getText();
                String status = (String) Status_CB.getSelectedItem();
                NovoProjeto(codigo, descricao, nomeUsuario, telefone, dataCriacao, status);
            } else {
                JOptionPane.showMessageDialog(this, "Chegou ao limite de 5 projetos");
            }
        });
    }

    private void DeletarProjeto(int id) {
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = obterConexao();
            String sql = "DELETE FROM projeto WHERE id = ?";
            stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(this, "Projeto excluído com sucesso!");
                atualizarTabelaMeusProjetos(); // Atualiza a tabela de projetos do usuário
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível excluir o projeto.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir projeto: " + e.getMessage());
        } finally {
            fecharConexao(conexao, stmt);
        }
        atualizarTabelaMeusProjetos();
    }

    private void AtualizarProjeto(int id, String codigo, String descricao, String nomeusuario, String telefone, String datacriacao, String status) {
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = obterConexao();
            String sql = "UPDATE projeto SET codigo = ?, descriçao = ?, usuario = ?, telefone = ?, datadecriaçao = ?, statusprojeto = ? WHERE id = ?";
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, codigo);
            stmt.setString(2, descricao);
            stmt.setString(3, nomeusuario);
            stmt.setString(4, telefone);
            stmt.setString(5, datacriacao);
            stmt.setString(6, status);
            stmt.setInt(7, id);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(this, "Projeto atualizado com sucesso!");
                atualizarTabelaMeusProjetos(); // Atualiza a tabela de projetos do usuário
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível atualizar o projeto.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar projeto: " + e.getMessage());
        } finally {
            fecharConexao(conexao, stmt);
        }
        atualizarTabelaMeusProjetos();
    }

    private void NovoProjeto(String codigo, String descricao, String nomeUsuario, String telefone, String dataCriacao,
            String status) {
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = obterConexao();
            String sql = "INSERT INTO projeto (codigo, descriçao, usuario, telefone, datadecriaçao, statusprojeto) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, codigo);
            stmt.setString(2, descricao);
            stmt.setString(3, nomeUsuario);
            stmt.setString(4, telefone);
            stmt.setString(5, dataCriacao);
            stmt.setString(6, status);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(this, "Projeto cadastrado com sucesso!");
                atualizarTabelaMeusProjetos(); // Atualiza a tabela de projetos do usuário
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível cadastrar o projeto.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao inserir projeto: " + e.getMessage());
        } finally {
            fecharConexao(conexao, stmt);
        }
        atualizarTabelaMeusProjetos();
        TodosProjetos();
    }
    

  private void atualizarTabelaMeusProjetos() {
    try (Connection conexao = obterConexao();
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM projeto WHERE usuario = ?")) {
        stmt.setString(1, "usuario");
        ResultSet resultSet = stmt.executeQuery();
        
        DefaultTableModel model = (DefaultTableModel) TabelaMeusProjetos.getModel();
        model.setRowCount(0); // Limpa a tabela antes de atualizar
        
        projetos.clear(); // Limpa a lista de projetos

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String codigo = resultSet.getString("codigo");
            String descricao = resultSet.getString("descriçao");
            String nomeusuario = resultSet.getString("usuario");
            String telefone = resultSet.getString("telefone");
            String datacriacao = resultSet.getString("datadecriaçao");
            String status = resultSet.getString("statusprojeto");
            String comentario = resultSet.getString("comentario");

            Projeto projeto = new Projeto(id, codigo, descricao, nomeusuario, telefone, datacriacao, status, comentario);
            adicionarProjeto(projeto); // Adiciona projeto à lista e à tabela
        }
        
        // Após adicionar os projetos, atualize a exibição da tabela
        model.fireTableDataChanged(); // Notifica a tabela que os dados foram alterados
        resultSet.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Erro ao carregar projetos: " + e.getMessage());
    }
}



    private void TodosProjetos() {
        try (Connection conexao = obterConexao();
                PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM projeto")) {
            ResultSet resultSet = stmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) TabelaProjetosUsuarios.getModel();
            model.setRowCount(0); // Limpa a tabela antes de atualizar
            projetos.clear(); // Limpa a lista de projetos

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String codigo = resultSet.getString("codigo");
                String descricao = resultSet.getString("descriçao");
                String nomeUsuario = resultSet.getString("usuario");
                String telefone = resultSet.getString("telefone");
                String dataCriacao = resultSet.getString("datadecriaçao");
                String status = resultSet.getString("statusprojeto");
                String comentario = resultSet.getString("comentario");

                Projeto projeto = new Projeto(id, codigo, descricao, nomeUsuario, telefone, dataCriacao, status,
                        comentario);
                adicionarProjetoTodos(projeto); // Adiciona projeto à lista e à tabela
            }
            model.fireTableDataChanged();
            resultSet.close();
        } catch                 (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar projetos: " + e.getMessage());
        }
    }

    private void adicionarProjeto(Projeto projeto) {
    DefaultTableModel model = (DefaultTableModel) TabelaMeusProjetos.getModel();
    model.addRow(new Object[]{
        projeto.getCodigo(),
        projeto.getDescricao(),
        projeto.getNomeUsuario(),
        projeto.getTelefone(),
        projeto.getDataCriacao(),
        projeto.getStatus(),
        projeto.getId(),
        projeto.getComentario()
    });
    projetos.add(projeto);
}

    private void adicionarProjetoTodos(Projeto projeto) {
        DefaultTableModel model = (DefaultTableModel) TabelaProjetosUsuarios.getModel();
        model.addRow(new Object[]{
            projeto.getCodigo(),
            projeto.getDescricao(),
            projeto.getNomeUsuario(),
            projeto.getTelefone(),
            projeto.getDataCriacao(),
            projeto.getStatus(),
            projeto.getId(),
            projeto.getComentario()
        });
        projetos.add(projeto);
    }

    private Connection obterConexao() throws SQLException {
        String usuario = "root";
        String senha = "Beteelias25@";
        String url = "jdbc:mysql://localhost:3306/LRGMECH";
        return DriverManager.getConnection(url, usuario, senha);
    }

     private void SelecionarProjeto(String tipo, String usuario) throws SQLException {
        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            conexao = obterConexao();

            if (tipo.equals("usuario")) {
                String sql = "SELECT * FROM projeto WHERE usuario = ?";
                stmt = conexao.prepareStatement(sql);
                stmt.setString(1, usuario);
                resultSet = stmt.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String codigo = resultSet.getString("codigo");
                    String descricao = resultSet.getString("descriçao");
                    String nomeusuario = resultSet.getString("usuario");
                    String telefone = resultSet.getString("telefone");
                    String datacriacao = resultSet.getString("datadecriaçao");
                    String status = resultSet.getString("statusprojeto");
                    String comentario = resultSet.getString("comentario");

                    Projeto projeto = new Projeto(id, codigo, descricao, nomeusuario, telefone, datacriacao, status, comentario);
                    adicionarProjeto(projeto); // Adiciona projeto à lista e à tabela
                    this.qtdProjeto++;
                }

            } else {
                String sql = "SELECT * FROM projeto";
                stmt = conexao.prepareStatement(sql);
                resultSet = stmt.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String codigo = resultSet.getString("codigo");
                    String descricao = resultSet.getString("descricao");
                    String nomeUsuario = resultSet.getString("usuario");
                    String telefone = resultSet.getString("telefone");
                    String dataCriacao = resultSet.getString("datadecriacao");
                    String status = resultSet.getString("statusprojeto");
                    String comentario = resultSet.getString("comentario");

                    Projeto projeto = new Projeto(id, codigo, descricao, nomeUsuario, telefone, dataCriacao, status, comentario);
                    adicionarProjetoTodos(projeto); // Adiciona projeto à lista e à tabela
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar projetos: " + e.getMessage());
        } finally {
            fecharConexao(conexao, stmt);
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Erro ao fechar ResultSet: " + e.getMessage());
                }
            }
        }
    }

    private void fecharConexao(Connection conexao, PreparedStatement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao fechar PreparedStatement: " + e.getMessage());
            }
        }
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        CriarProjeto_btn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelaMeusProjetos = new javax.swing.JTable();
        MeusProjetos_LB = new javax.swing.JLabel();
        DescriçaoTextField = new javax.swing.JTextField();
        NomeUsuarioTextField = new javax.swing.JTextField();
        TelefoneTextField = new javax.swing.JTextField();
        DataDeCriaçaoTextField = new javax.swing.JTextField();
        CodigoOds_CB = new javax.swing.JComboBox<>();
        Status_CB = new javax.swing.JComboBox<>();
        CodigoOds_LB = new javax.swing.JLabel();
        Descriçao_LB = new javax.swing.JLabel();
        NomeUsuario_LB = new javax.swing.JLabel();
        Telefone_LB = new javax.swing.JLabel();
        DataDeCriaçao_LB = new javax.swing.JLabel();
        Status_LB = new javax.swing.JLabel();
        AtualizarProjeto_btn = new javax.swing.JButton();
        ExcluirProjeto_btn = new javax.swing.JButton();
        TodosProjetos_LB = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TabelaProjetosUsuarios = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        CriarProjeto_btn.setText("Criar Novo Projeto");
        CriarProjeto_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CriarProjeto_btnActionPerformed(evt);
            }
        });

        TabelaMeusProjetos.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        TabelaMeusProjetos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TabelaMeusProjetos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo/ODS", "Descrição", "Nome Usuario", "Telefone", "Data de criação", "Status", "ID Banco", "Comentario"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TabelaMeusProjetos.setRowHeight(50);
        jScrollPane1.setViewportView(TabelaMeusProjetos);
        if (TabelaMeusProjetos.getColumnModel().getColumnCount() > 0) {
            TabelaMeusProjetos.getColumnModel().getColumn(0).setResizable(false);
            TabelaMeusProjetos.getColumnModel().getColumn(1).setResizable(false);
            TabelaMeusProjetos.getColumnModel().getColumn(2).setResizable(false);
            TabelaMeusProjetos.getColumnModel().getColumn(3).setResizable(false);
            TabelaMeusProjetos.getColumnModel().getColumn(4).setResizable(false);
            TabelaMeusProjetos.getColumnModel().getColumn(5).setResizable(false);
            TabelaMeusProjetos.getColumnModel().getColumn(6).setResizable(false);
        }

        MeusProjetos_LB.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        MeusProjetos_LB.setText("Meus Projetos");

        TelefoneTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TelefoneTextFieldActionPerformed(evt);
            }
        });

        CodigoOds_CB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "\"1- Erradicação da Pobreza\"", "\"2- Fome Zero e Agricultura Sustentável\"", "\"3- Saúde e Bem-Estar\"", "\"4- Educação de Qualidade\"", "\"5- Igualdade de Gênero\"", "\"6- Água Potável e Saneamento\"", "\"7- Energia Limpa e Acessível\"", "\"8- Trabalho Decente e Crescimento Econômico\"", "\"9- Indústria  Inovação e Infraestrutura\"", "\"10- Redução das Desigualdades\"", "\"11- Cidades e Comunidades Sustentáveis\"", "\"12- Consumo e Produção Responsáveis\"", "\"13- Ação Contra a Mudança Global do Clima\"", "\"14- Vida na Água\"", "\"15- Vida Terrestre\"", "\"16- Paz  Justiça e Instituições Eficazes\"", "\"17- Parcerias e Meios de Implementação\"" }));
        CodigoOds_CB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CodigoOds_CBActionPerformed(evt);
            }
        });

        Status_CB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Em Progresso" }));

        CodigoOds_LB.setText("Codigo/ODS");

        Descriçao_LB.setText("Descriçao");

        NomeUsuario_LB.setText("NomeUsuario");

        Telefone_LB.setText("Telefone");

        DataDeCriaçao_LB.setText("DataDeCriaçao");

        Status_LB.setText("Status");

        AtualizarProjeto_btn.setText("Atualizar Projeto");

        ExcluirProjeto_btn.setText("Excluir Projeto");

        TodosProjetos_LB.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        TodosProjetos_LB.setText("Todos Projetos");

        TabelaProjetosUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo/ODS", "Descrição", "Nome Usuario", "Telefone", "Data de criação", "Status", "ID Banco", "Comentario"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(TabelaProjetosUsuarios);
        if (TabelaProjetosUsuarios.getColumnModel().getColumnCount() > 0) {
            TabelaProjetosUsuarios.getColumnModel().getColumn(0).setResizable(false);
            TabelaProjetosUsuarios.getColumnModel().getColumn(1).setResizable(false);
            TabelaProjetosUsuarios.getColumnModel().getColumn(2).setResizable(false);
            TabelaProjetosUsuarios.getColumnModel().getColumn(3).setResizable(false);
            TabelaProjetosUsuarios.getColumnModel().getColumn(4).setResizable(false);
            TabelaProjetosUsuarios.getColumnModel().getColumn(5).setResizable(false);
            TabelaProjetosUsuarios.getColumnModel().getColumn(6).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)
                            .addComponent(MeusProjetos_LB, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 612, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TodosProjetos_LB, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(CodigoOds_LB, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(CodigoOds_CB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(DataDeCriaçao_LB, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                                            .addComponent(NomeUsuario_LB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(Descriçao_LB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(Telefone_LB, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(DescriçaoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(NomeUsuarioTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(TelefoneTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(DataDeCriaçaoTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(Status_LB, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(Status_CB, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(CriarProjeto_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(AtualizarProjeto_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ExcluirProjeto_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(MeusProjetos_LB)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(CodigoOds_LB, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CodigoOds_CB, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(Descriçao_LB, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(DescriçaoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(NomeUsuario_LB, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(NomeUsuarioTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(Telefone_LB, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TelefoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(DataDeCriaçao_LB, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(DataDeCriaçaoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(Status_LB, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Status_CB, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addComponent(TodosProjetos_LB, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CriarProjeto_btn)
                            .addComponent(AtualizarProjeto_btn)
                            .addComponent(ExcluirProjeto_btn))
                        .addGap(29, 29, 29)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TelefoneTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TelefoneTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TelefoneTextFieldActionPerformed

    private void CodigoOds_CBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CodigoOds_CBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CodigoOds_CBActionPerformed

    private void CriarProjeto_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CriarProjeto_btnActionPerformed
        // TODO add your handling code here:
      
    }//GEN-LAST:event_CriarProjeto_btnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {

        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AtualizarProjeto_btn;
    private javax.swing.JComboBox<String> CodigoOds_CB;
    private javax.swing.JLabel CodigoOds_LB;
    private javax.swing.JButton CriarProjeto_btn;
    private javax.swing.JTextField DataDeCriaçaoTextField;
    private javax.swing.JLabel DataDeCriaçao_LB;
    private javax.swing.JTextField DescriçaoTextField;
    private javax.swing.JLabel Descriçao_LB;
    private javax.swing.JButton ExcluirProjeto_btn;
    private javax.swing.JLabel MeusProjetos_LB;
    private javax.swing.JTextField NomeUsuarioTextField;
    private javax.swing.JLabel NomeUsuario_LB;
    private javax.swing.JComboBox<String> Status_CB;
    private javax.swing.JLabel Status_LB;
    private javax.swing.JTable TabelaMeusProjetos;
    private javax.swing.JTable TabelaProjetosUsuarios;
    private javax.swing.JTextField TelefoneTextField;
    private javax.swing.JLabel Telefone_LB;
    private javax.swing.JLabel TodosProjetos_LB;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

    

    
}

