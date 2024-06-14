package projetoa3;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class TelaFuncionario extends javax.swing.JFrame {
    private List<Projeto> projetos = new ArrayList<>();

    public TelaFuncionario() throws SQLException {
        initComponents();
        setLocationRelativeTo(null);
        carregarProjetos();
        
        AtualizarProjeto_btn.addActionListener((ActionEvent e) -> {
            int selectedRow = UsuarioProjeto_TB.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) UsuarioProjeto_TB.getValueAt(selectedRow, 0);
                String status = (String) Status_CB.getSelectedItem();
                String comentario = ComentarTextField.getText();
                atualizarProjeto(comentario, status, id);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um projeto para atualizar.");
            }
        });
    }
    
    private Connection obterConexao() throws SQLException {
        String usuario = "root";
        String senha = "Beteelias25@";
        String url = "jdbc:mysql://localhost:3306/LRGMECH";
        return DriverManager.getConnection(url, usuario, senha);
    }
    
    private void atualizarProjeto(String comentario, String status, int id) {
        Connection conexao = null;
        PreparedStatement stmt = null;

        try {
            conexao = obterConexao();
            String sql = "UPDATE projeto SET statusprojeto = ?, comentario = ? WHERE id = ?";
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setString(2, comentario);
            stmt.setInt(3, id);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(this, "Projeto atualizado com sucesso!");
                carregarProjetos(); // Atualiza a tabela
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível atualizar.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar dados no banco de dados: " + e.getMessage());
        } finally {
            fecharConexao(conexao, stmt);
        }
    }

    private void carregarProjetos() throws SQLException {
        Connection conexao = null;
        PreparedStatement stmt = null;

        try {
            conexao = obterConexao();
            String sql = "SELECT * FROM projeto";
            stmt = conexao.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();

            // Limpa a tabela antes de adicionar novos dados
            DefaultTableModel model = (DefaultTableModel) UsuarioProjeto_TB.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                
                int id = resultSet.getInt("id");
                String codigo = resultSet.getString("codigo");
                String descricao = resultSet.getString("descriçao");
                String nomeusuario = resultSet.getString("usuario");
                String telefone = resultSet.getString("telefone");
                String status = resultSet.getString("statusprojeto");
                String comentario = resultSet.getString("comentario");
                String datacriacao = resultSet.getString("datadecriaçao");
                
                System.out.println(status);

                
                Projeto projeto = new Projeto(id, codigo, descricao, nomeusuario, telefone, datacriacao, status, comentario);
                adicionarProjeto(projeto);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do banco de dados: " + e.getMessage());
        } finally {
            fecharConexao(conexao, stmt);
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

    private void adicionarProjeto(Projeto projeto) {
        System.out.println(this.projetos);
        projetos.add(projeto);
        DefaultTableModel model = (DefaultTableModel) UsuarioProjeto_TB.getModel();
        model.addRow(new Object[]{
            projeto.getId(),
            projeto.getCodigo(),
            projeto.getDescricao(),
            projeto.getNomeUsuario(),
            projeto.getStatus(),
            projeto.getComentario()
        });
        System.out.println(this.projetos);
    }
      
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        UsuarioProjeto_TB = new javax.swing.JTable();
        TodosProjetosCriados_LB = new javax.swing.JLabel();
        AtualizarProjeto_btn = new javax.swing.JButton();
        ComentarTextField = new javax.swing.JTextField();
        Status_LB = new javax.swing.JLabel();
        Status_CB = new javax.swing.JComboBox<>();
        Comentar_LB = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        UsuarioProjeto_TB.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID Banco", "Codigo/ODS", "Descriçao", "Usuario", "Status", "Comentario"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(UsuarioProjeto_TB);
        if (UsuarioProjeto_TB.getColumnModel().getColumnCount() > 0) {
            UsuarioProjeto_TB.getColumnModel().getColumn(0).setResizable(false);
            UsuarioProjeto_TB.getColumnModel().getColumn(1).setResizable(false);
            UsuarioProjeto_TB.getColumnModel().getColumn(2).setResizable(false);
            UsuarioProjeto_TB.getColumnModel().getColumn(3).setResizable(false);
            UsuarioProjeto_TB.getColumnModel().getColumn(4).setResizable(false);
            UsuarioProjeto_TB.getColumnModel().getColumn(5).setResizable(false);
        }

        TodosProjetosCriados_LB.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        TodosProjetosCriados_LB.setText("Todos Projetos Criados");

        AtualizarProjeto_btn.setText("Atualizar Projeto");

        Status_LB.setText("Status");

        Status_CB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Atualizado" }));

        Comentar_LB.setText("Comente qual foi a atualização");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TodosProjetosCriados_LB, javax.swing.GroupLayout.DEFAULT_SIZE, 1012, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(AtualizarProjeto_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Status_LB, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Comentar_LB, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ComentarTextField)
                                    .addComponent(Status_CB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TodosProjetosCriados_LB, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Status_LB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Status_CB, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ComentarTextField)
                            .addComponent(Comentar_LB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(AtualizarProjeto_btn))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(TelaFuncionario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaFuncionario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaFuncionario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaFuncionario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new TelaFuncionario().setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(TelaFuncionario.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AtualizarProjeto_btn;
    private javax.swing.JTextField ComentarTextField;
    private javax.swing.JLabel Comentar_LB;
    private javax.swing.JComboBox<String> Status_CB;
    private javax.swing.JLabel Status_LB;
    private javax.swing.JLabel TodosProjetosCriados_LB;
    private javax.swing.JTable UsuarioProjeto_TB;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
 
 }

   
        






