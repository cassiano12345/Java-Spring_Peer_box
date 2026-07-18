package pt.ipb.dsys.sd.sender;

import pt.ipb.dsys.sd.comum.ficheiros.Arquivo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicReference;

public class Janela extends JFrame {

    private JLabel lblNome;
    private JTextField txtCaminho;
    private JTable tabela;
    private DefaultTableModel modelo;
    private JButton btnPesquisar;
    private JButton btnPeerativos;
    private JButton btnEnviar;
    private JButton btnRecuperar;
    private JButton btnApagar;
    private JButton btnInfo_local;
    private JTextPane txtLog;

    public Janela() {

        setTitle("Seleção de Ficheiro");
        setSize(650, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        lblNome = new JLabel("Nome: Nenhum ficheiro selecionado");

        txtCaminho = new JTextField();
        txtCaminho.setEditable(false);
        AtomicReference<String> nome_ficheiro = new AtomicReference<>("");
        btnPesquisar = new JButton("Pesquisar");
        btnPeerativos = new JButton("Buscar peers");
        btnEnviar = new JButton("Enviar");
        btnRecuperar = new JButton("Recuperar");
        btnApagar = new JButton("Apagar");
        btnInfo_local = new JButton("Informações locais");

        btnPesquisar.addActionListener(e -> {

            JFileChooser chooser = new JFileChooser();

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

                File ficheiro = chooser.getSelectedFile();
                nome_ficheiro.set(ficheiro.getName());
                lblNome.setText("Nome: " + ficheiro.getName());
                txtCaminho.setText(ficheiro.getAbsolutePath());
            }
        });

        btnPeerativos.addActionListener(e -> {
            Funcionalidades_User funcionalidadesUser = new Funcionalidades_User();
            try {
                funcionalidadesUser.peersAtivos();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        btnEnviar.addActionListener(e -> {
            if(!txtCaminho.getText().isEmpty()){
                Arquivo arquivo = new Arquivo();
                //Criar SHA256 e enviar chunks
                try {
                    Funcionalidades_User funcionalidadesUser = new Funcionalidades_User();
                    funcionalidadesUser.enviarFicheiro(Arquivo.Criar_chunks(nome_ficheiro.get(),txtCaminho.getText()));
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                }else {
                JOptionPane.showMessageDialog(
                        this,
                        "Selecione um ficheiro primeiro.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
            }

            limpartabela();
        });

        btnRecuperar.addActionListener(e -> {
            int linha = tabela.getSelectedRow();

            if (linha != -1) {

                String nome = (String) modelo.getValueAt(linha, 0);

                Funcionalidades_User funcionalidadesUser = new Funcionalidades_User();
                try {
                    funcionalidadesUser.recuperarFicheiro(nome);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

            }else{
                mostrarAlerta("Selecione na tabela o item e recuperar!");
            }
            limpartabela();
        });

        btnApagar.addActionListener(e -> {
            // Fazer depois
            int linha = tabela.getSelectedRow();

            if (linha != -1) {
                String nome = (String) modelo.getValueAt(linha, 0);

                Funcionalidades_User funcionalidadesUser = new Funcionalidades_User();
            try {
                funcionalidadesUser.apagarFicheiro(nome);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            }else{
                mostrarAlerta("Selecione na tabela o item e apagar!");
            }
            limpartabela();
        });

        btnInfo_local.addActionListener(e -> {
            // Fazer depois
        });

        setTitle("Ficheiros Distribuídos");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Nome das colunas
        String[] colunas = {
                "Nome",
                "Bytes",
                "Numero de chunks",
                "Peer"
        };

        modelo = new DefaultTableModel(colunas, 0);

        tabela = new JTable(modelo);

        JScrollPane scrollTabela = new JScrollPane(tabela);

// =====================
// Área de logs
// =====================
        txtLog = new JTextPane();
        txtLog.setEditable(false);

        JScrollPane scrollLog = new JScrollPane(txtLog);
        scrollLog.setPreferredSize(new Dimension(700, 120));

// =====================
// Tabela + Log
// =====================
        JSplitPane split = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                scrollTabela,
                scrollLog);

        split.setResizeWeight(0.75);
        split.setDividerSize(5);

        add(split, BorderLayout.CENTER);


        JPanel info = new JPanel(new GridLayout(2,1,5,5));
        info.add(lblNome);
        info.add(txtCaminho);

        JPanel botoes = new JPanel();
        botoes.add(btnPesquisar);
        botoes.add(btnPeerativos);
        botoes.add(btnEnviar);
        botoes.add(btnRecuperar);
        botoes.add(btnApagar);
        botoes.add(btnInfo_local);

        add(info, BorderLayout.NORTH);
        add(botoes, BorderLayout.SOUTH);
    }
    public void limpartabela(){
        modelo.setRowCount(0);
        // Fazer depois
        Funcionalidades_User funcionalidadesUser = new Funcionalidades_User();
        try {
            funcionalidadesUser.listarFicheiros();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    public void adicionarFicheiro(String nome, String bytes, int chunks, String peer) {
        modelo.addRow(new Object[]{
                nome,
                bytes,
                chunks,
                peer
        });
    }

    public void mostrarAlerta(String mensagem) {
        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
    }

    public void adicionarLog(String mensagem) {
        txtLog.setText(txtLog.getText() + mensagem + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }

    public void limparLog() {
        txtLog.setText("");
    }

    public JTextPane getTxtLog() {
        return txtLog;
    }
}