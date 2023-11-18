package vista;


import ActionListeners.CustomChangeListener;
import ActionListeners.SliderMouseListener;
import Interface.IPokemonCalculator;
import bd.Conexion;
//import static bd.Conexion.getConnection;
import controlador.InteractuarPokepaste;
import controlador.PoblarTablas;
import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import javax.swing.JPanel;
import modelo.Ability;
import modelo.Item;
import modelo.Move;
import modelo.Nature;
import modelo.Pokemon;
import modelo.PokemonPaste;
import modelo.PokemonType;
import modelo.Gender;
import modelo.MoveInfo;
import modelo.PokemonPasteInfo;
import oracle.jdbc.OracleConnection;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author F776
 */
public class MainWindow extends javax.swing.JFrame implements IPokemonCalculator {
    
    private String emptyString = "";
    private Gender gender = new Gender(3, "Genderless");
    private ArrayList<Item> listaItems;
    private ArrayList<Pokemon> listaPokemon;
    private ArrayList<Ability> listaAbility;
    private ArrayList<PokemonType> listaPkmnType;
    private ArrayList<Nature> listaNature;
    private ArrayList<Move> listaMove;
    private PoblarTablas tb = new PoblarTablas();
    private InteractuarPokepaste ip = new InteractuarPokepaste();
    private SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, 31, 1);
    private final JLabel[] listStatLabels;
    private final JLabel[] listFinalStatLabels;
    private final JSpinner[] listIvSPN;
    private final JComboBox[] listMoveCBOXS;
    private final JSlider[] listStatSliders;
   
    
    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        
        try (Connection connection = Conexion.getInstance().getConnection()) {
            
            // Retrieve moves
            this.listaMove = tb.getMoves(connection);
            
            // Retrieve nature
            this.listaNature = tb.getNature(connection);
          
            // Retrieve Pokemon types
            this.listaPkmnType = tb.getPokemonType(connection);
            
            // Retrieve abilities
            this.listaAbility = tb.getAbilities(connection);
    
            // Retrieve Pokemon
            this.listaPokemon = tb.getPokemon(connection);
           
            // Retrieve items
            this.listaItems = tb.getItems(connection);
           

            // Perform other operations as needed
            Conexion.getInstance().releaseConnection(connection);

        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(this, "Error en la conexión a la base de datos", "El programa no pudo ser inicializado correctamente",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);   
        } 
       
        initComponents(); // Initialize all Swing components first
        
        // Then, initialize the statSliders array after the Swing components are created
        this.listStatSliders = new JSlider[] {sldHP, sldATK, sldDEF, sldSPATK, sldSPDEF, sldSPD};
        this.listStatLabels = new JLabel[] {lblHP, lblATK, lblDEF, lblSPATK, lblSPDEF, lblSPD};
        this.listFinalStatLabels = new JLabel[] {lblFinalHP, lblFinalATK, lblFinalDEF, lblFinalSPATK, lblFinalSPDEF, lblFinalSPD};
        this.listIvSPN = new JSpinner[] {spnIVHP, spnIVATK, spnIVDEF, spnIVSPATK, spnIVSPDEF, spnIVSPD};
        this.listMoveCBOXS = new JComboBox[] {cboMove1, cboMove2, cboMove3, cboMove4};

        // Call the methods directly without SwingWorker
        initializeSpinners(listIvSPN);
        addLimit(listStatSliders);
        setLabels(listStatLabels, listStatSliders);
        llenarCboMoves(listMoveCBOXS);
        llenarCboItem();
        llenarCboSpecies();
        llenarCboAbility();
        llenarPkmnType();
        llenarNature();
        fillMoveTbl();
        updateRemainingEvs();
        initializeEventListeners();
       
    }

    /**
     * Este método permite actualizar los labels de EVs para que se modifiquen
     * de acuerdo a los nuevos valores que vayan tomando los sliders de EVs.
     * @param statLabels lista de labels que muestran los EVs asignados.
     * @param statSliders lista de sliders que reciben los valores de EVs.
     */
    private void setLabels(JLabel[] statLabels, JSlider[] statSliders) {
        for (int i = 0; i < statLabels.length; i++) {
            int index = i; // Final or effectively final variable
            statLabels[i].setText(String.valueOf(statSliders[i].getValue()));
            statSliders[i].addChangeListener(e -> {
                statLabels[index].setText(String.valueOf(statSliders[index].getValue()));
            });
        }
    }
    /**
     * Este método permite averiguar si se han asignado o no valores a los spinners 
     * de IVs (por defecto 31).
     * @return true si hay alguno distinto a 31. false si todos son 31.
     * 
     */
    private boolean isSpnIV31() {
        for (JSpinner i : listIvSPN) {
            if((int)i.getValue()!=31) {
                return true;
            }
        }
        return false;
    }
    /**
     * Este método permite averiguar si se han asignado o no valores a los sliders 
     * de EVs.
     * @return true si hay alguno distinto a 0. false si todos son 0.
     * 
     */
    private boolean isSldEV0(){
        for (JSlider s : listStatSliders) {
            if(s.getValue()!=0) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Este método asigna a cada slider de EVs un Change Listener que ejecuta la actualización
     * del label lblEVSum.
     */
    private void updateRemainingEvs() {
        for (JSlider slider : listStatSliders) {
            slider.addChangeListener(e -> {
            if (!slider.getValueIsAdjusting()) {
                updateLabel();
            }
        });
        }
    }
    /**
     * Este método actualiza el label en el que se guarda la cantidad restante de EVs
     * que se pueden asignar a un Pokémon.
     */
    private void updateLabel() {
        int hp = sldHP.getValue();
        int atk = sldATK.getValue();
        int def = sldDEF.getValue();
        int spatk = sldSPATK.getValue();
        int spdef = sldSPDEF.getValue();
        int spd = sldSPD.getValue();
        int evSum = hp + atk + def + spatk + spdef + spd;
        lblEVSum.setText(String.valueOf(508 - evSum));
    }

    /**
     * Este método obtiene los valores de los campos en la GUI para generar un pokepaste.
     * Un pokepaste es la representación textual de los atributos de un Pokémon
     * que se usan en el simulador de batallas Pokémon Showdown para importar o
     * exportar sets.
     * @return la String del pokepaste.
     */
    public String generatePokepaste(){
        int hp = sldHP.getValue();
        int atk = sldATK.getValue();
        int def = sldDEF.getValue();
        int spatk = sldSPATK.getValue();
        int spdef = sldSPDEF.getValue();
        int spd = sldSPD.getValue();
        StringBuilder pokepaste = new StringBuilder();
        
        if (!txtNickname.getText().isEmpty()) { // if the pokemon has a nickname
            pokepaste.append(txtNickname.getText()).append(' ');
            pokepaste.append("(").append(cboSpecies.getSelectedItem().toString()).append(")");
        } else { // else just show the species' name
            pokepaste.append(cboSpecies.getSelectedItem().toString());        
        }
        if (gender.getId()!=3) {
            pokepaste.append(" (").append(gender.getName().charAt(0)).append(")");
        }
        pokepaste.append(" @ ").append(cboItem.getSelectedItem().toString());
        pokepaste.append("\n").append("Ability: ").append(cboAbility.getSelectedItem().toString());
        if ((int)spnLevel.getValue()!=100) {
            pokepaste.append("\nLevel: ").append(spnLevel.getValue());
        }
        if (ckbShiny.isSelected()) {
            pokepaste.append("\nShiny: Yes");
        }
        pokepaste.append("\nTera Type: ").append(cboTeraType.getSelectedItem().toString());
        if(isSldEV0()) {
            pokepaste.append("\nEVs: ");
            if(sldHP.getValue()!=0) {
                pokepaste.append(String.valueOf(hp)).append(" HP / ");
            }
            if(sldATK.getValue()!=0) {
                pokepaste.append(String.valueOf(atk)).append(" Atk / ");
            }
            if(sldDEF.getValue()!=0) {
                pokepaste.append(String.valueOf(def)).append(" Def / ");
            }
            if(sldSPATK.getValue()!=0) {
                pokepaste.append(String.valueOf(spatk)).append(" SpA / ");
            }
            if(sldSPDEF.getValue()!=0) {
                pokepaste.append(String.valueOf(spdef)).append(" SpDef / ");
            }
            if(sldSPD.getValue()!=0) {
                pokepaste.append(String.valueOf(spd)).append(" Spe");
            }
            if (pokepaste.substring(pokepaste.length() - 3).equals(" / ")) {
                pokepaste.delete(pokepaste.length() - 3, pokepaste.length());
            }
        }
        
        pokepaste.append("\n").append(cboNature.getSelectedItem().toString()).append(" Nature");
        // 
        
        if (isSpnIV31()) {
            
            pokepaste.append("\nIVs:");
            if((int)spnIVHP.getValue()!=31) {
                pokepaste.append(String.valueOf(spnIVHP.getValue())).append(" HP / ");
            }
            if((int)spnIVATK.getValue()!=31) {
                pokepaste.append(String.valueOf(spnIVATK.getValue())).append(" Atk / ");
            }
            if((int)spnIVDEF.getValue()!=31) {
                pokepaste.append(String.valueOf(spnIVDEF.getValue())).append(" Def / ");
            }
            if((int)spnIVSPATK.getValue()!=31) {
                pokepaste.append(String.valueOf(spnIVSPATK.getValue())).append(" SpA / ");
            }
            if((int)spnIVSPDEF.getValue()!=31) {
                pokepaste.append(String.valueOf(spnIVSPDEF.getValue())).append(" SpDef / ");
            }
            if((int)spnIVSPD.getValue()!=31) {
                pokepaste.append(String.valueOf(spnIVSPD.getValue())).append(" Spe");
            }
            if (pokepaste.substring(pokepaste.length() - 3).equals(" / ")) {
                pokepaste.delete(pokepaste.length() - 3, pokepaste.length());
            }
        }
        if (!cboMove1.getSelectedItem().toString().isEmpty()){
            pokepaste.append("\n- ").append(cboMove1.getSelectedItem().toString());
        }
        if (!cboMove2.getSelectedItem().toString().isEmpty()){
            pokepaste.append("\n- ").append(cboMove2.getSelectedItem().toString());
        }
        if (!cboMove3.getSelectedItem().toString().isEmpty()){
            pokepaste.append("\n- ").append(cboMove3.getSelectedItem().toString());
        }
        if (!cboMove4.getSelectedItem().toString().isEmpty()){
            pokepaste.append("\n- ").append(cboMove4.getSelectedItem().toString());
        }

        return String.valueOf(pokepaste);
    }
    
   
    // ImageIcon created for the frame
    ImageIcon greatTuskIcon = new ImageIcon("great_tusk_icon.png");
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgpGender = new javax.swing.ButtonGroup();
        jSlider1 = new javax.swing.JSlider();
        jtpPokemonGeneration = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPokemon = new javax.swing.JTable();
        btnConsultar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblMoves = new javax.swing.JTable();
        jPanelPokemon = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtNickname = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cboTeraType = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        cboNature = new javax.swing.JComboBox<>();
        rbnFemale = new javax.swing.JRadioButton();
        rbnMale = new javax.swing.JRadioButton();
        rdnGenderless = new javax.swing.JRadioButton();
        cboSpecies = new javax.swing.JComboBox<>();
        cboMove1 = new javax.swing.JComboBox<>();
        cboMove2 = new javax.swing.JComboBox<>();
        cboMove3 = new javax.swing.JComboBox<>();
        cboMove4 = new javax.swing.JComboBox<>();
        cboItem = new javax.swing.JComboBox<>();
        cboAbility = new javax.swing.JComboBox<>();
        btnGeneratePokepaste = new javax.swing.JButton();
        spnLevel = new javax.swing.JSpinner();
        jLabel20 = new javax.swing.JLabel();
        ckbShiny = new javax.swing.JCheckBox();
        btnSavePokepaste = new javax.swing.JButton();
        btnImportPaste = new javax.swing.JButton();
        btnAbilityDesc = new javax.swing.JButton();
        btnMove1Desc = new javax.swing.JButton();
        btnMove2Desc = new javax.swing.JButton();
        btnMove3Desc = new javax.swing.JButton();
        btnMove4Desc = new javax.swing.JButton();
        btnItemDesc = new javax.swing.JButton();
        btnResetFields = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblPokemonSprite = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtPokepaste = new javax.swing.JTextArea();
        btnProbarConexion = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jPanelEVs = new javax.swing.JPanel();
        lblDEF = new javax.swing.JLabel();
        lblSPDEF = new javax.swing.JLabel();
        sldATK = new javax.swing.JSlider();
        sldDEF = new javax.swing.JSlider();
        lblSPATK = new javax.swing.JLabel();
        sldSPD = new javax.swing.JSlider();
        lblSPD = new javax.swing.JLabel();
        lblHP = new javax.swing.JLabel();
        sldSPDEF = new javax.swing.JSlider();
        lblATK = new javax.swing.JLabel();
        sldHP = new javax.swing.JSlider();
        jLabel9 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        sldSPATK = new javax.swing.JSlider();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        pgbHP = new javax.swing.JProgressBar();
        pgbATK = new javax.swing.JProgressBar();
        pgbDEF = new javax.swing.JProgressBar();
        pgbSPATK = new javax.swing.JProgressBar();
        pgbSPDEF = new javax.swing.JProgressBar();
        pgbSPD = new javax.swing.JProgressBar();
        lblFinalHP = new javax.swing.JLabel();
        lblFinalATK = new javax.swing.JLabel();
        lblFinalDEF = new javax.swing.JLabel();
        lblFinalSPATK = new javax.swing.JLabel();
        lblFinalSPDEF = new javax.swing.JLabel();
        lblFinalSPD = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        spnIVHP = new javax.swing.JSpinner();
        spnIVATK = new javax.swing.JSpinner();
        spnIVDEF = new javax.swing.JSpinner();
        spnIVSPATK = new javax.swing.JSpinner();
        spnIVSPDEF = new javax.swing.JSpinner();
        spnIVSPD = new javax.swing.JSpinner();
        lblEVSum = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pokemon Generator");
        setIconImage(greatTuskIcon.getImage());
        setName("mainFrame"); // NOI18N
        setSize(new java.awt.Dimension(1920, 1080));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtpPokemonGeneration.setTabPlacement(javax.swing.JTabbedPane.RIGHT);
        jtpPokemonGeneration.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jPanel3.setBackground(new java.awt.Color(255, 204, 255));

        tblPokemon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Tera Type", "Ability", "HP", "ATK", "DEF", "SPATK", "SPDEF", "SPD", "Level", "Shiny"
            }
        ));
        jScrollPane2.setViewportView(tblPokemon);

        btnConsultar.setText("Consultar");
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(btnConsultar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 970, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(btnConsultar)
                .addGap(32, 32, 32)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(109, Short.MAX_VALUE))
        );

        jtpPokemonGeneration.addTab("Saved Pokémon", jPanel3);

        jPanel5.setBackground(new java.awt.Color(204, 255, 204));

        tblMoves.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Type", "Category", "Power", "Accuracy", "PP", "Desciption", "Effect Probability"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblMoves);
        if (tblMoves.getColumnModel().getColumnCount() > 0) {
            tblMoves.getColumnModel().getColumn(0).setMaxWidth(35);
            tblMoves.getColumnModel().getColumn(1).setMinWidth(200);
            tblMoves.getColumnModel().getColumn(1).setMaxWidth(300);
            tblMoves.getColumnModel().getColumn(2).setMaxWidth(70);
            tblMoves.getColumnModel().getColumn(3).setMaxWidth(100);
            tblMoves.getColumnModel().getColumn(4).setMaxWidth(70);
            tblMoves.getColumnModel().getColumn(5).setMaxWidth(70);
            tblMoves.getColumnModel().getColumn(6).setMaxWidth(70);
            tblMoves.getColumnModel().getColumn(8).setMaxWidth(35);
        }

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 946, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(122, Short.MAX_VALUE))
        );

        jtpPokemonGeneration.addTab("Moves Table", jPanel5);

        jPanelPokemon.setBackground(new java.awt.Color(255, 255, 255));
        jPanelPokemon.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nickname:");
        jPanelPokemon.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 160, -1, -1));

        jLabel3.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Species:");
        jPanelPokemon.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 210, -1, -1));

        jLabel4.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Move 1:");
        jPanelPokemon.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 310, -1, -1));

        jLabel5.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Move 2:");
        jPanelPokemon.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 360, -1, -1));

        jLabel6.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Move 4:");
        jPanelPokemon.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 460, -1, -1));

        jLabel7.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Move 3:");
        jPanelPokemon.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 410, -1, -1));

        jLabel8.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Items:");
        jPanelPokemon.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 510, -1, -1));
        jPanelPokemon.add(txtNickname, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 180, 180, -1));

        jLabel10.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Ability:");
        jPanelPokemon.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 260, -1, -1));

        jLabel11.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Tera Type:");
        jPanelPokemon.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 560, -1, -1));

        cboTeraType.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboTeraType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTeraTypeActionPerformed(evt);
            }
        });
        jPanelPokemon.add(cboTeraType, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 580, 300, -1));

        jLabel12.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Nature:");
        jPanelPokemon.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 610, -1, -1));

        cboNature.setToolTipText("");
        cboNature.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboNature.setName(""); // NOI18N
        cboNature.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboNatureItemStateChanged(evt);
            }
        });
        jPanelPokemon.add(cboNature, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 630, 300, -1));

        rbnFemale.setBackground(new java.awt.Color(189, 15, 52));
        bgpGender.add(rbnFemale);
        rbnFemale.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        rbnFemale.setForeground(new java.awt.Color(255, 255, 255));
        rbnFemale.setText("Female");
        rbnFemale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbnFemale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbnFemaleActionPerformed(evt);
            }
        });
        jPanelPokemon.add(rbnFemale, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 520, 70, -1));

        rbnMale.setBackground(new java.awt.Color(189, 15, 52));
        bgpGender.add(rbnMale);
        rbnMale.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        rbnMale.setForeground(new java.awt.Color(255, 255, 255));
        rbnMale.setText("Male");
        rbnMale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbnMale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbnMaleActionPerformed(evt);
            }
        });
        jPanelPokemon.add(rbnMale, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 560, -1, -1));

        rdnGenderless.setBackground(new java.awt.Color(189, 15, 52));
        bgpGender.add(rdnGenderless);
        rdnGenderless.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        rdnGenderless.setForeground(new java.awt.Color(255, 255, 255));
        rdnGenderless.setSelected(true);
        rdnGenderless.setText("Random");
        rdnGenderless.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rdnGenderless.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdnGenderlessActionPerformed(evt);
            }
        });
        jPanelPokemon.add(rdnGenderless, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 600, 80, -1));

        cboSpecies.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboSpecies.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboSpeciesItemStateChanged(evt);
            }
        });
        jPanelPokemon.add(cboSpecies, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 230, 300, -1));

        cboMove1.setSelectedItem(new String(""));
        cboMove1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanelPokemon.add(cboMove1, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 330, 300, -1));

        cboMove2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanelPokemon.add(cboMove2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 380, 300, -1));

        cboMove3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanelPokemon.add(cboMove3, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 430, 300, -1));

        cboMove4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanelPokemon.add(cboMove4, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 480, 300, -1));

        cboItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanelPokemon.add(cboItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 530, 300, -1));

        cboAbility.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cboAbility.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanelPokemon.add(cboAbility, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 280, 300, -1));

        btnGeneratePokepaste.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        btnGeneratePokepaste.setText("Generate");
        btnGeneratePokepaste.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGeneratePokepaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGeneratePokepasteActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnGeneratePokepaste, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 510, 90, -1));

        spnLevel.setModel(new javax.swing.SpinnerNumberModel(100, 1, 100, 1));
        spnLevel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        spnLevel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnLevelStateChanged(evt);
            }
        });
        jPanelPokemon.add(spnLevel, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 520, -1, -1));

        jLabel20.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Level:");
        jPanelPokemon.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 500, -1, -1));

        ckbShiny.setBackground(new java.awt.Color(189, 15, 52));
        ckbShiny.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        ckbShiny.setForeground(new java.awt.Color(255, 255, 255));
        ckbShiny.setText("Shiny");
        ckbShiny.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ckbShiny.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckbShinyActionPerformed(evt);
            }
        });
        jPanelPokemon.add(ckbShiny, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 580, -1, -1));

        btnSavePokepaste.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        btnSavePokepaste.setText("Save");
        btnSavePokepaste.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSavePokepaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSavePokepasteActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnSavePokepaste, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 570, 90, -1));

        btnImportPaste.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        btnImportPaste.setText("Import");
        btnImportPaste.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImportPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportPasteActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnImportPaste, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 600, 90, -1));

        btnAbilityDesc.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        btnAbilityDesc.setForeground(new java.awt.Color(206, 17, 49));
        btnAbilityDesc.setText("?");
        btnAbilityDesc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAbilityDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbilityDescActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnAbilityDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 280, 40, 20));

        btnMove1Desc.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        btnMove1Desc.setForeground(new java.awt.Color(206, 17, 49));
        btnMove1Desc.setText("?");
        btnMove1Desc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMove1Desc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMove1DescActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnMove1Desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 330, 40, 20));

        btnMove2Desc.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        btnMove2Desc.setForeground(new java.awt.Color(206, 17, 49));
        btnMove2Desc.setText("?");
        btnMove2Desc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMove2Desc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMove2DescActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnMove2Desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 380, 40, 20));

        btnMove3Desc.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        btnMove3Desc.setForeground(new java.awt.Color(206, 17, 49));
        btnMove3Desc.setText("?");
        btnMove3Desc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMove3Desc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMove3DescActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnMove3Desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 430, 40, 20));

        btnMove4Desc.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        btnMove4Desc.setForeground(new java.awt.Color(206, 17, 49));
        btnMove4Desc.setText("?");
        btnMove4Desc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMove4Desc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMove4DescActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnMove4Desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 480, 40, 20));

        btnItemDesc.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        btnItemDesc.setForeground(new java.awt.Color(206, 17, 49));
        btnItemDesc.setText("?");
        btnItemDesc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnItemDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemDescActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnItemDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 530, 40, 20));

        btnResetFields.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        btnResetFields.setText("Reset");
        btnResetFields.setToolTipText("");
        btnResetFields.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnResetFields.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetFieldsActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnResetFields, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 540, 90, -1));

        jLabel1.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Pokepaste:");
        jPanelPokemon.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 170, -1, -1));
        jPanelPokemon.add(lblPokemonSprite, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 110, 100));

        txtPokepaste.setBackground(new java.awt.Color(239, 240, 208));
        txtPokepaste.setColumns(20);
        txtPokepaste.setFont(new java.awt.Font("Eras Bold ITC", 0, 14)); // NOI18N
        txtPokepaste.setRows(5);
        txtPokepaste.setBorder(null);
        txtPokepaste.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(txtPokepaste);

        jPanelPokemon.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, 310, 220));

        btnProbarConexion.setText("Probar Conexion");
        btnProbarConexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProbarConexionActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnProbarConexion, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 50, -1, -1));

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pokedex/PokedexOpen.jpg"))); // NOI18N
        jPanelPokemon.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, 920, 700));

        jtpPokemonGeneration.addTab("Pokémon", jPanelPokemon);

        jPanelEVs.setBackground(new java.awt.Color(204, 204, 204));
        jPanelEVs.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelEVs.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblDEF.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanelEVs.add(lblDEF, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 360, 42, 54));

        lblSPDEF.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanelEVs.add(lblSPDEF, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 500, 42, 54));

        sldATK.setMajorTickSpacing(36);
        sldATK.setMaximum(252);
        sldATK.setPaintLabels(true);
        sldATK.setValue(0);
        sldATK.setCursor(new java.awt.Cursor(java.awt.Cursor.E_RESIZE_CURSOR));
        jPanelEVs.add(sldATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 290, 220, 46));

        sldDEF.setMajorTickSpacing(36);
        sldDEF.setMaximum(252);
        sldDEF.setPaintLabels(true);
        sldDEF.setValue(0);
        sldDEF.setCursor(new java.awt.Cursor(java.awt.Cursor.E_RESIZE_CURSOR));
        jPanelEVs.add(sldDEF, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 360, 220, 46));

        lblSPATK.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanelEVs.add(lblSPATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 430, 42, 54));

        sldSPD.setMajorTickSpacing(36);
        sldSPD.setMaximum(252);
        sldSPD.setPaintLabels(true);
        sldSPD.setValue(0);
        sldSPD.setCursor(new java.awt.Cursor(java.awt.Cursor.E_RESIZE_CURSOR));
        jPanelEVs.add(sldSPD, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 570, 220, 46));

        lblSPD.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanelEVs.add(lblSPD, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 570, 42, 54));

        lblHP.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanelEVs.add(lblHP, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 220, 42, 54));

        sldSPDEF.setMajorTickSpacing(36);
        sldSPDEF.setMaximum(252);
        sldSPDEF.setPaintLabels(true);
        sldSPDEF.setValue(0);
        sldSPDEF.setCursor(new java.awt.Cursor(java.awt.Cursor.E_RESIZE_CURSOR));
        jPanelEVs.add(sldSPDEF, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 496, 220, 50));

        lblATK.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanelEVs.add(lblATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 290, 42, 54));

        sldHP.setMajorTickSpacing(36);
        sldHP.setMaximum(252);
        sldHP.setPaintLabels(true);
        sldHP.setValue(0);
        sldHP.setCursor(new java.awt.Cursor(java.awt.Cursor.E_RESIZE_CURSOR));
        sldHP.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                sldHPMouseDragged(evt);
            }
        });
        jPanelEVs.add(sldHP, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 220, 220, 46));

        jLabel9.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("EVs: ");
        jPanelEVs.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 100, -1, 54));

        jLabel13.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel13.setText(" HP");
        jPanelEVs.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 240, 20, 30));

        sldSPATK.setMajorTickSpacing(36);
        sldSPATK.setMaximum(252);
        sldSPATK.setPaintLabels(true);
        sldSPATK.setValue(0);
        sldSPATK.setCursor(new java.awt.Cursor(java.awt.Cursor.E_RESIZE_CURSOR));
        jPanelEVs.add(sldSPATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 430, 220, 46));

        jLabel14.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel14.setText("ATK");
        jPanelEVs.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 300, 30, 30));

        jLabel15.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel15.setText("DEF");
        jPanelEVs.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 360, 30, 30));

        jLabel16.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel16.setText("SPATK");
        jPanelEVs.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 240, 40, 30));

        jLabel17.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel17.setText("SPDEF");
        jPanelEVs.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 300, -1, 30));

        jLabel18.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel18.setText("SPD");
        jPanelEVs.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 360, 30, 30));

        pgbHP.setMaximum(255);
        pgbHP.setString("");
        pgbHP.setStringPainted(true);
        jPanelEVs.add(pgbHP, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, 100, 40));

        pgbATK.setMaximum(255);
        pgbATK.setString("");
        pgbATK.setStringPainted(true);
        jPanelEVs.add(pgbATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 270, 100, 40));

        pgbDEF.setMaximum(255);
        pgbDEF.setString("");
        pgbDEF.setStringPainted(true);
        jPanelEVs.add(pgbDEF, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 270, 100, 40));

        pgbSPATK.setMaximum(255);
        pgbSPATK.setString("");
        pgbSPATK.setStringPainted(true);
        jPanelEVs.add(pgbSPATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 210, 100, 40));

        pgbSPDEF.setMaximum(255);
        pgbSPDEF.setRequestFocusEnabled(false);
        pgbSPDEF.setString("");
        pgbSPDEF.setStringPainted(true);
        jPanelEVs.add(pgbSPDEF, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 330, 100, 40));

        pgbSPD.setMaximum(255);
        pgbSPD.setString("");
        pgbSPD.setStringPainted(true);
        jPanelEVs.add(pgbSPD, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 330, 100, 40));
        jPanelEVs.add(lblFinalHP, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 220, 30, 46));
        jPanelEVs.add(lblFinalATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 290, 30, 46));
        jPanelEVs.add(lblFinalDEF, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 360, 30, 46));
        jPanelEVs.add(lblFinalSPATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 430, 30, 46));
        jPanelEVs.add(lblFinalSPDEF, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 500, 30, 46));
        jPanelEVs.add(lblFinalSPD, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 570, 30, 46));

        jLabel19.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel19.setText("IVs");
        jPanelEVs.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 80, -1, -1));
        jPanelEVs.add(spnIVHP, new org.netbeans.lib.awtextra.AbsoluteConstraints(784, 220, 60, 46));
        jPanelEVs.add(spnIVATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(784, 290, 60, 46));
        jPanelEVs.add(spnIVDEF, new org.netbeans.lib.awtextra.AbsoluteConstraints(784, 360, 60, 46));
        jPanelEVs.add(spnIVSPATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(784, 430, 60, 46));
        jPanelEVs.add(spnIVSPDEF, new org.netbeans.lib.awtextra.AbsoluteConstraints(784, 496, 60, 50));
        jPanelEVs.add(spnIVSPD, new org.netbeans.lib.awtextra.AbsoluteConstraints(784, 570, 60, 46));

        lblEVSum.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 18)); // NOI18N
        lblEVSum.setText("508");
        jPanelEVs.add(lblEVSum, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 430, 40, 20));

        jLabel21.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel21.setText("Remaining EV's");
        jPanelEVs.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 410, -1, -1));

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pokedex/PokedexOpenEVs.jpg"))); // NOI18N
        jPanelEVs.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, 920, 700));

        jtpPokemonGeneration.addTab("EV's", jPanelEVs);

        getContentPane().add(jtpPokemonGeneration, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 690));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Este método permite añadir las limitaciones a los sliders de EVs. Sea el mouse
     * clickeado o el knob de los sliders arrastrados, la suma de los sliders no será
     * nunca mayor a 508. Además, al ser el mouse soltado, se asegura que el valor de
     * EVs seleccionado para cada estadística siempre será múltiplo de 4.
     * @param statSliders 
     */
    public static void addLimit(JSlider[] statSliders) {
        SliderMouseListener sliderMouseListener = new SliderMouseListener(statSliders);
        for (JSlider statSlider : statSliders) {
            statSlider.addMouseListener(sliderMouseListener);
            statSlider.addMouseMotionListener(sliderMouseListener);
        }
    }

    private void rbnFemaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnFemaleActionPerformed
        // TODO add your handling code here:
        gender.setId(2);
        gender.setName("Female");
    }//GEN-LAST:event_rbnFemaleActionPerformed

    private void rbnMaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnMaleActionPerformed
        // TODO add your handling code here:
        gender.setId(1);
        gender.setName("Male");
    }//GEN-LAST:event_rbnMaleActionPerformed

    private void rdnGenderlessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdnGenderlessActionPerformed
        // TODO add your handling code here:
        gender.setId(3);
        gender.setName("Genderless");
    }//GEN-LAST:event_rdnGenderlessActionPerformed

    private void cboTeraTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTeraTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboTeraTypeActionPerformed

    private void btnProbarConexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProbarConexionActionPerformed
        // TODO add your handling code here:
        try {
            Connection connection = Conexion.getInstance().getConnection();
            System.out.println("Bien");
            Conexion.getInstance().releaseConnection(connection);
        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }//GEN-LAST:event_btnProbarConexionActionPerformed

    private void btnGeneratePokepasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGeneratePokepasteActionPerformed
        // TODO add your handling code here:
        String generatePoketext = generatePokepaste();
        txtPokepaste.setText(generatePoketext);
    }//GEN-LAST:event_btnGeneratePokepasteActionPerformed

    private void cboSpeciesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboSpeciesItemStateChanged
        // TODO add your handling code here:
        lblPokemonSprite.setIcon(getPokemonSprite());
        if (evt.getStateChange() == ItemEvent.SELECTED) {
        String selectedPokemonName = cboSpecies.getSelectedItem().toString();
        for (Pokemon p : this.listaPokemon) {
            if (p.getName().equals(selectedPokemonName)) {
                updateProgressBars(p);
                
                break;  // You need to break here after updating the progress bars for the selected Pokemon
            }
        }
    }
    }//GEN-LAST:event_cboSpeciesItemStateChanged

    private void ckbShinyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckbShinyActionPerformed
        // TODO add your handling code here:
        this.ckbShiny.isSelected();
    }//GEN-LAST:event_ckbShinyActionPerformed

    private void cboNatureItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboNatureItemStateChanged
        // TODO add your handling code here:
        setFinalStatLabels();

    }//GEN-LAST:event_cboNatureItemStateChanged

    private void btnSavePokepasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSavePokepasteActionPerformed
        // TODO add your handling code here:
        PokemonPaste buildPokepaste = buildPokepaste();
        try (Connection connection = Conexion.getInstance().getConnection()) {
            if(ip.insertPokepaste(connection, buildPokepaste)){
                System.out.println("Insertado correctamente");
            } else {
                System.out.println("Inserción fallada");
            }
            Conexion.getInstance().releaseConnection(connection);
        // Use the itemList as needed
        } catch (SQLException e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_btnSavePokepasteActionPerformed

    private void spnLevelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnLevelStateChanged
        // TODO add your handling code here:
        setFinalStatLabels();
    }//GEN-LAST:event_spnLevelStateChanged

    private void sldHPMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sldHPMouseDragged
        // TODO add your handling code here:

    }//GEN-LAST:event_sldHPMouseDragged

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        // TODO add your handling code here:
        llenarTabla();
    }//GEN-LAST:event_btnConsultarActionPerformed

    private void btnImportPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportPasteActionPerformed
        // TODO add your handling code here:
        resetFields();
        importPokepaste(txtPokepaste.getText());
    }//GEN-LAST:event_btnImportPasteActionPerformed

    private void btnAbilityDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbilityDescActionPerformed
        // TODO add your handling code here:
        
        String abilityDescription = getAbilityDescription();
        if (abilityDescription != null) {
            JOptionPane.showMessageDialog(this, abilityDescription, "Ability Description", JOptionPane.INFORMATION_MESSAGE);
        }
        // Show a pop-up message with the ability description
        
    }//GEN-LAST:event_btnAbilityDescActionPerformed

    private void btnMove1DescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMove1DescActionPerformed
        // TODO add your handling code here:
        String moveDesc = getMoveDescription(cboMove1);
        // Show a pop-up message with the ability description
        if (moveDesc != null) {
            JOptionPane.showMessageDialog(this, moveDesc, "Move Description", JOptionPane.INFORMATION_MESSAGE);
        }
        
    }//GEN-LAST:event_btnMove1DescActionPerformed

    private void btnMove2DescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMove2DescActionPerformed
        // TODO add your handling code here:
        String moveDesc = getMoveDescription(cboMove2);
        if (moveDesc != null) {
            JOptionPane.showMessageDialog(this, moveDesc, "Move Description", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnMove2DescActionPerformed

    private void btnMove3DescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMove3DescActionPerformed
        // TODO add your handling code here:
        String moveDesc = getMoveDescription(cboMove3);
        if (moveDesc != null) {
            JOptionPane.showMessageDialog(this, moveDesc, "Move Description", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnMove3DescActionPerformed

    private void btnMove4DescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMove4DescActionPerformed
        // TODO add your handling code here:
        String moveDesc = getMoveDescription(cboMove4);
        if (moveDesc != null) {
            JOptionPane.showMessageDialog(this, moveDesc, "Move Description", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnMove4DescActionPerformed

    private void btnItemDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemDescActionPerformed
        // TODO add your handling code here:
        String itemDesc = getItemDescription();
        // Show a pop-up message with the ability description
        JOptionPane.showMessageDialog(this, itemDesc, "Item Description", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnItemDescActionPerformed

    private void btnResetFieldsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetFieldsActionPerformed
        // TODO add your handling code here:
        resetFields();
    }//GEN-LAST:event_btnResetFieldsActionPerformed
    
    
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
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainWindow window = new MainWindow();
                window.setVisible(true);
                
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgpGender;
    private javax.swing.JButton btnAbilityDesc;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnGeneratePokepaste;
    private javax.swing.JButton btnImportPaste;
    private javax.swing.JButton btnItemDesc;
    private javax.swing.JButton btnMove1Desc;
    private javax.swing.JButton btnMove2Desc;
    private javax.swing.JButton btnMove3Desc;
    private javax.swing.JButton btnMove4Desc;
    private javax.swing.JButton btnProbarConexion;
    private javax.swing.JButton btnResetFields;
    private javax.swing.JButton btnSavePokepaste;
    private javax.swing.JComboBox<String> cboAbility;
    private javax.swing.JComboBox<String> cboItem;
    private javax.swing.JComboBox<String> cboMove1;
    private javax.swing.JComboBox<String> cboMove2;
    private javax.swing.JComboBox<String> cboMove3;
    private javax.swing.JComboBox<String> cboMove4;
    private javax.swing.JComboBox<String> cboNature;
    private javax.swing.JComboBox<String> cboSpecies;
    private javax.swing.JComboBox<String> cboTeraType;
    private javax.swing.JCheckBox ckbShiny;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelEVs;
    private javax.swing.JPanel jPanelPokemon;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTabbedPane jtpPokemonGeneration;
    private javax.swing.JLabel lblATK;
    private javax.swing.JLabel lblDEF;
    private javax.swing.JLabel lblEVSum;
    private javax.swing.JLabel lblFinalATK;
    private javax.swing.JLabel lblFinalDEF;
    private javax.swing.JLabel lblFinalHP;
    private javax.swing.JLabel lblFinalSPATK;
    private javax.swing.JLabel lblFinalSPD;
    private javax.swing.JLabel lblFinalSPDEF;
    private javax.swing.JLabel lblHP;
    private javax.swing.JLabel lblPokemonSprite;
    private javax.swing.JLabel lblSPATK;
    private javax.swing.JLabel lblSPD;
    private javax.swing.JLabel lblSPDEF;
    private javax.swing.JProgressBar pgbATK;
    private javax.swing.JProgressBar pgbDEF;
    private javax.swing.JProgressBar pgbHP;
    private javax.swing.JProgressBar pgbSPATK;
    private javax.swing.JProgressBar pgbSPD;
    private javax.swing.JProgressBar pgbSPDEF;
    private javax.swing.JRadioButton rbnFemale;
    private javax.swing.JRadioButton rbnMale;
    private javax.swing.JRadioButton rdnGenderless;
    private javax.swing.JSlider sldATK;
    private javax.swing.JSlider sldDEF;
    private javax.swing.JSlider sldHP;
    private javax.swing.JSlider sldSPATK;
    private javax.swing.JSlider sldSPD;
    private javax.swing.JSlider sldSPDEF;
    private javax.swing.JSpinner spnIVATK;
    private javax.swing.JSpinner spnIVDEF;
    private javax.swing.JSpinner spnIVHP;
    private javax.swing.JSpinner spnIVSPATK;
    private javax.swing.JSpinner spnIVSPD;
    private javax.swing.JSpinner spnIVSPDEF;
    private javax.swing.JSpinner spnLevel;
    private javax.swing.JTable tblMoves;
    private javax.swing.JTable tblPokemon;
    private javax.swing.JTextField txtNickname;
    private javax.swing.JTextArea txtPokepaste;
    // End of variables declaration//GEN-END:variables
    
    
    /**
     * Este método usa los datos de la tabla Item para llenar su combobox
     */
    private void llenarCboItem() {
        //definicion variables para rescatar datos
        String nombre;
        DefaultComboBoxModel cboModelo = (DefaultComboBoxModel) this.cboItem.getModel();
        for (Item i : this.listaItems) {
            nombre = i.getName();
            cboModelo.addElement(nombre);
        }      
    }
    
    /**
     * Este método usa los datos de la tabla pokémon para llenar su combobox
     */
    private void llenarCboSpecies() {
        //definicion variables para rescatar datos
        String nombre;
        DefaultComboBoxModel cboModelo = (DefaultComboBoxModel) this.cboSpecies.getModel();
        for (Pokemon p : this.listaPokemon) {
            nombre = p.getName();
            cboModelo.addElement(nombre);
        }
    }
    
    /**
     * Este método sirve para llenar un combobox con todos los movimientos de 
     * Pokémon
     * @param cbo un combobox
     */
    private void obtenerCBOMoves(JComboBox cbo) {
        //definicion variables para rescatar datos
        String nombre = "";
        DefaultComboBoxModel cboModelo = (DefaultComboBoxModel) cbo.getModel();
        cboModelo.addElement(nombre);
        for (Move m : this.listaMove) {
            nombre = m.getName();
            cboModelo.addElement(nombre);
        }      
    }
    
    /**
     * Este método itera sobre todos los combobox de movimientos para pasarles
     * todos los movimientos.
     * @param cboxes lista que almacena todos los combobox
     */
    private void llenarCboMoves(JComboBox[] cboxes) {
        for (JComboBox cboxe : cboxes) {
            obtenerCBOMoves(cboxe);
        }
    }
    /**
     * Este método llena el combobox de habilidades con todas las habilidades.
     */
    private void llenarCboAbility() {
        //definicion variables para rescatar datos
        String nombre;
        DefaultComboBoxModel cboModelo = (DefaultComboBoxModel) this.cboAbility.getModel();
        for (Ability a : this.listaAbility) {
            nombre = a.getName();
            cboModelo.addElement(nombre);
        }  
    }
    
    /**
     * Este método llena el combobox de tipos de Pokémon con todos los tipos.
     */
    private void llenarPkmnType() {
        //definicion variables para rescatar datos
        String nombre;
        DefaultComboBoxModel cboModelo = (DefaultComboBoxModel) this.cboTeraType.getModel();
        for (PokemonType p : this.listaPkmnType) {
            nombre = p.getName();
            cboModelo.addElement(nombre);
        }      
    }
    /**
     * Este método llena el combobox de naturalezas con todas las naturalezas.
     */
    private void llenarNature() {
        //definicion variables para rescatar datos
        String nombre;
        DefaultComboBoxModel cboModelo = (DefaultComboBoxModel) this.cboNature.getModel();
        for (Nature n : this.listaNature) {
            nombre = n.getName();
            cboModelo.addElement(nombre);
        }      
    }
    
    private void updateProgressBars(Pokemon p) {
        pgbHP.setValue(p.getHp());
        pgbHP.setString(String.valueOf(p.getHp()));
        pgbATK.setValue(p.getAtk());
        pgbATK.setString(String.valueOf(p.getAtk()));
        pgbDEF.setValue(p.getDef());
        pgbDEF.setString(String.valueOf(p.getDef()));
        pgbSPATK.setValue(p.getSpatk());
        pgbSPATK.setString(String.valueOf(p.getSpatk()));
        pgbSPDEF.setValue(p.getSpdef());
        pgbSPDEF.setString(String.valueOf(p.getSpdef()));
        pgbSPD.setValue(p.getSpd());
        pgbSPD.setString(String.valueOf(p.getSpd()));    
    }
    
    /**
     * Este método calcula y publica en los FinalStatLabels las estadísticas 
     * reales y finales de los Pokémon. Primero se calculan sin tomar en cuenta
     * las naturalezas, y luego se les aplica el modificador de las naturalezas
     * definidos en la IPokemonCalculator
     */
    private void setFinalStatLabels() {
        int hp;
        int atk;
        int def;
        int spatk;
        int spdef;
        int spd;
        int level = (int) spnLevel.getValue();
        int natureID = matchNature().getId();
        
        
        hp = calculateModifiedStat(pgbHP.getValue(), (int) spnIVHP.getValue(), sldHP.getValue(), level);
        atk = calculateModifiedStat(pgbATK.getValue(), (int) spnIVATK.getValue(), sldATK.getValue(), level);               
        def = calculateModifiedStat(pgbDEF.getValue(), (int) spnIVDEF.getValue(), sldDEF.getValue(), level);           
        spatk = calculateModifiedStat(pgbSPATK.getValue(), (int) spnIVSPATK.getValue(), sldSPATK.getValue(), level);
        spdef = calculateModifiedStat(pgbSPDEF.getValue(), (int) spnIVSPDEF.getValue(), sldSPDEF.getValue(), level);
        spd = calculateModifiedStat(pgbSPD.getValue(), (int) spnIVSPD.getValue(), sldSPD.getValue(), level);
  
        int[] newStats = modifyStatNature(natureID, atk, def, spatk, spdef, spd);
        
        lblFinalHP.setText(String.valueOf(hp));
        lblFinalATK.setText(String.valueOf(newStats[0]));
        lblFinalDEF.setText(String.valueOf(newStats[1]));
        lblFinalSPATK.setText(String.valueOf(newStats[2]));
        lblFinalSPDEF.setText(String.valueOf(newStats[3]));
        lblFinalSPD.setText(String.valueOf(newStats[4]));
    }
    
    /**
     * Este método modifica las estadísticas de los Pokémon dependiendo en
     * sus naturalezas.
     * @param natureID el id de la naturaleza en la base de datos.
     * @param atk el stat de ataque
     * @param def el stat de defensa
     * @param spatk el stat de ataque especial
     * @param spdef el stat de defensa especial
     * @param spd el stat de velocidad
     * @return devuelve la lista de stats ya modificados tras pasar por el switch.
     * Nota: el stat de HP (puntos de vida) no es afectado por ninguna naturaleza.
     */
    private int[] modifyStatNature(int natureID, int atk, int def, int spatk, int spdef, int spd) {
        int[] effects = {atk, def, spatk, spdef, spd};
        double h = HINDERING_NATURE;
        double b = BENEFICIAL_NATURE;

        switch (natureID) {
            case 1: // Hardy
                return effects;

            case 2: // Lonely
                effects[0] = (int) (effects[0] * b);
                effects[1] = (int) (effects[1] * h);
                return effects;

            case 3: // Brave
                effects[0] = (int) (effects[0] * b);
                effects[4] = (int) (effects[4] * h);
                return effects;

            case 4: // Adamant
                effects[0] = (int) (effects[0] * b);
                effects[2] = (int) (effects[2] * h);
                return effects;

            case 5: // Naughty
                effects[0] = (int) (effects[0] * b);
                effects[3] = (int) (effects[3] * h);
                return effects;

            case 6: // Bold
                effects[1] = (int) (effects[1] * b);
                effects[0] = (int) (effects[0] * h);
                return effects;

            case 7: // Docile
                return effects;

            case 8: // Relaxed
                effects[1] = (int) (effects[1] * b);
                effects[4] = (int) (effects[4] * h);
                return effects;

            case 9: // Impish
                effects[1] = (int) (effects[1] * b);
                effects[2] = (int) (effects[2] * h);
                return effects;

            case 10: // Lax
                effects[1] = (int) (effects[1] * b);
                effects[3] = (int) (effects[3] * h);
                return effects;

            case 11: // Timid
                effects[4] = (int) (effects[4] * b);
                effects[0] = (int) (effects[0] * h);
                return effects;

            case 12: // Hasty
                effects[4] = (int) (effects[4] * b);
                effects[1] = (int) (effects[1] * h);
                return effects;

            case 13: // Serious
                return effects;

            case 14: // Jolly
                effects[4] = (int) (effects[4] * b);
                effects[2] = (int) (effects[2] * h);
                return effects;

            case 15: // Naive
                effects[4] = (int) (effects[4] * b);
                effects[3] = (int) (effects[3] * h);
                return effects;

            case 16: // Modest
                effects[2] = (int) (effects[2] * b);
                effects[0] = (int) (effects[0] * h);
                return effects;

            case 17: // Mild
                effects[2] = (int) (effects[2] * b);
                effects[1] = (int) (effects[1] * h);
                return effects;

            case 18: // Quiet
                effects[2] = (int) (effects[2] * b);
                effects[4] = (int) (effects[4] * h);
                return effects;

            case 19: // Bashful
                return effects;

            case 20: // Rash
                effects[2] = (int) (effects[2] * b);
                effects[3] = (int) (effects[3] * h);
                return effects;

            case 21: // Calm
                effects[3] = (int) (effects[3] * b);
                effects[0] = (int) (effects[0] * h);
                return effects;

            case 22: // Gentle
                effects[3] = (int) (effects[3] * b);
                effects[1] = (int) (effects[1] * h);
                return effects;

            case 23: // Sassy
                effects[3] = (int) (effects[3] * b);
                effects[4] = (int) (effects[4] * h);
                return effects;

            case 24: // Careful
                effects[3] = (int) (effects[3] * b);
                effects[2] = (int) (effects[2] * h);
                return effects;

            case 25: // Quirky
                return effects;
            default:
                throw new AssertionError("Invalid nature ID: " + natureID);
        }
    }

    /**
     * Método que al ser invocado actualiza un stat entero y sus JLabel basado en el cálculo del
     * método calculateModifiedStat(int baseStat, int IV, int EV, int level, double natureValue)
     * de la interfaz.
     * @param listaPokemon
     * @param cbo
     * @param finalStat
     * @param iv
     * @param slider
     * @param level

     */
    public void updateStats(ArrayList<Pokemon> listaPokemon, JComboBox cbo, JLabel finalStat, JSpinner iv, JSlider slider, int level){
        for (Pokemon p : listaPokemon) {
            if (p.getName().equals(cbo.getSelectedItem().toString())) {   
                finalStat.setText(String.valueOf(calculateModifiedStat(p.getHp(), (int) iv.getValue(), slider.getValue(), level)));
                break;  // You need to break here after updating the progress bars for the selected Pokemon
            }
        }
    }
    
    /**
     * Método de la interfaz que permite calcular las estadísticas reales de un Pokémon basado en
     * las siguientes variables.
     * @param baseStat las estadísticas base de un Pokémon para un stat {HP, ATK, DEF, SPATK, SPDEF, SPD}.
     * @param IV los valores individuales de un Pokémon (genes) para un stat.
     * @param EV los valores de esfuerzo de un Pokémon para un stat.
     * @param level el nivel del Pokémon.
     * @return 
     */
    @Override
    public int calculateModifiedStat(int baseStat, int IV, int EV, int level) {
        return (int) Math.floor((Math.floor(2 * baseStat + IV + Math.floor(EV / 4)) * level / 100 + 5));
    }

    /**
     * Este método inicializa los ChangeListeners y aplica los modelos definidos
     * a cada spinner de IVs.
     * @param spinners la lista de spinners de IVs.
     */
    private void initializeSpinners(JSpinner[] spinners) {
        for (JSpinner spinner : spinners) {
            CustomChangeListener customChangeListener = new CustomChangeListener();
            SpinnerNumberModel model = new SpinnerNumberModel(31, 0, 31, 1);
            spinner.addChangeListener(customChangeListener);
            spinner.setModel(model);
        }
    }
    /**
     * Este método calcula y publica en los finalStatSliders las estadísticas 
     * finales de los Pokémon, basadas en sus características base, nivel, EVs, 
     * IVs y naturaleza. Estos serán actualizados cada vez que se interactúe con los
     * sliders de EVs, los spinners de IVs y los movimientos? eso es raro
     */
    private void initializeEventListeners() {
        // Sliders
        for (JSlider slider : listStatSliders) {
            slider.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    setFinalStatLabels();
                }

                @Override
                public void mouseDragged(java.awt.event.MouseEvent evt) {
                    setFinalStatLabels();
                }
                
                @Override
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    setFinalStatLabels();
                }
            });
        }

        // ComboBoxes
        for (JComboBox cbo : listMoveCBOXS) {
            cbo.addItemListener(new java.awt.event.ItemListener() {
                @Override
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    setFinalStatLabels();
                }
            });
        }

        // Spinners
        for (JSpinner spinner : listIvSPN) {
            spinner.addChangeListener(new javax.swing.event.ChangeListener() {
                @Override
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    setFinalStatLabels();
                }
            });
        }   
    }
    /**
     * Este método halla el objeto naturaleza que está seleccionado en el 
     * combobox.
     * @return el objeto naturaleza en el combobox.
     */
    private Nature matchNature () {
        Nature nature = null;
        for (Nature n : listaNature) {
            if(n.getName().equals(cboNature.getSelectedItem().toString())) {
                nature = n;
                return nature;
            }
        }
        return nature;
    }
    /**
     * Este método halla el objeto pokemon que está seleccionado en el 
     * combobox.
     * @return  el objeto pokemon en el combobox.
     */
    private Pokemon matchPokemon () {
        Pokemon pokemon = null;
        for (Pokemon p : listaPokemon) {
            if(p.getName().equals(cboSpecies.getSelectedItem().toString())) {
                pokemon = p;
                return pokemon;
            }
        }
        return pokemon;
    }
    /**
     * Este método halla el objeto Item que está seleccionado en el 
     * combobox.
     * @return  el objeto item en el combobox.
     */
    private Item matchItem() {
        Item item = null;
        for (Item i : listaItems) {
            if(i.getName().equals(cboItem.getSelectedItem().toString())) {
                item = i;
                return item;
            }
        }
        return item;
    }
    /**
     * Este método halla el objeto tipo pokemon que está seleccionado en el 
     * combobox.
     * @return  el objeto tipo pokemon en el combobox.
     */
    private PokemonType matchTeraType() {
        PokemonType type = null;
        for (PokemonType p : listaPkmnType) {
            if(p.getName().equals(cboTeraType.getSelectedItem().toString())) {
                type = p;
                return type;
            }
        }
        return type;
    }
    /**
     * Este método halla el objeto tipo ability que está seleccionado en el 
     * combobox.
     * @return  el objeto tipo ability en el combobox.
     */
    private Ability matchAbility() {
        Ability ability = null;
        for (Ability a : listaAbility) {
            if(a.getName().equals(cboAbility.getSelectedItem().toString())) {
                ability = a;
                return ability;
            }
        }
        return ability;
    }
    /**
     * Este método halla el objeto move que está seleccionado en el 
     * combobox.
     * @return  el objeto move en el combobox.
     */
    private Move matchMove(JComboBox cboMove) {
        Move Move = null;
        for (Move m : listaMove) {
            if(m.getName().equals(cboMove.getSelectedItem().toString())) {
                Move = m;
                return Move;
            }
        }
        return Move;
    }
    /**
     * Este método recoge todos los objetos y atributos seleccionados en la interfaz
     * gráfica para armar un objeto PokemonPaste, que contiene los datos
     * para poblar la tabla pokemon_paste en la base de datos, que es la única 
     * en la que el usuario puede insertar, borrar y actualizar datos.
     * @return un objeto PokemonPaste
     */
    private PokemonPaste buildPokepaste() {
        PokemonPaste pkpaste = new PokemonPaste();
        pkpaste.setPokemon(matchPokemon());
        pkpaste.setItem(matchItem());
        pkpaste.setNickname(txtNickname.getText());
        pkpaste.setLevel((int) spnLevel.getValue());
        pkpaste.setGender(gender);
        pkpaste.setIsShiny(ckbShiny.isSelected());
        pkpaste.setTeraType(matchTeraType());
        pkpaste.setAbility(matchAbility());
        
        pkpaste.setMove1(matchMove(cboMove1));
        pkpaste.setMove2(matchMove(cboMove2));
        pkpaste.setMove3(matchMove(cboMove3));
        pkpaste.setMove4(matchMove(cboMove4));
        
        pkpaste.setHpEv(sldHP.getValue());
        pkpaste.setAtkEv(sldATK.getValue());
        pkpaste.setDefEv(sldDEF.getValue());
        pkpaste.setSpatkEv(sldSPATK.getValue());
        pkpaste.setSpdefEv(sldSPDEF.getValue());
        pkpaste.setSpdEv(sldSPD.getValue());
        
        pkpaste.setHpIv((int) spnIVHP.getValue());
        pkpaste.setAtkIv((int) spnIVATK.getValue());
        pkpaste.setDefIv((int) spnIVDEF.getValue());
        pkpaste.setSpatkIv((int) spnIVSPATK.getValue());
        pkpaste.setSpdefIv((int) spnIVSPDEF.getValue());
        pkpaste.setSpdIv((int) spnIVSPD.getValue());
        pkpaste.setNature(matchNature());
        pkpaste.setHp(Integer.parseInt(lblFinalHP.getText()));
        pkpaste.setAtk(Integer.parseInt(lblFinalATK.getText()));
        pkpaste.setDef(Integer.parseInt(lblFinalDEF.getText()));
        pkpaste.setSpatk(Integer.parseInt(lblFinalSPATK.getText()));
        pkpaste.setSpdef(Integer.parseInt(lblFinalSPDEF.getText()));
        pkpaste.setSpd(Integer.parseInt(lblFinalSPD.getText()));
        return pkpaste;
    }
    /**
     * Este método permite consultar los datos de la tabla pokemon_paste de la base
     * de datos y guardarlos en la JTable tblPokemon. Los datos de la tabla pokemon_paste
     * son previamente "traducidos" en el método listPokepaste de la clase InteractuarPokepaste
     * en nombres (en lugar de claves primarias int).
     */
    private void llenarTabla() {
        //definicion variables para rescatar datos
        DefaultTableModel tbl = (DefaultTableModel) this.tblPokemon.getModel();
        //para que no se dupliquela información
        tbl.setRowCount(0);
        try (Connection connection = Conexion.getInstance().getConnection()) {
            ArrayList<PokemonPasteInfo> lista =  ip.listPokepaste(connection);
            for (PokemonPasteInfo p : lista) {
                tbl.addRow(new Object[]
                {
                    p.getId(), p.getPokemon(), p.getTeraType(), 
                    p.getAbility(), p.getHp(), p.getAtk(), p.getDef(), p.getSpatk(), 
                    p.getSpdef(), p.getSpd(), p.getLevel(), p.getIsShiny()
                });
            }
           
        // Use the itemList as needed
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    /**
     * Este método llena la tabla tblMoves con todos los movimientos de la base
     * de datos. Se ejecuta al inicio del programa para poder ser consultada en
     * cualquier momento.
     */
    private void fillMoveTbl() {
        DefaultTableModel tbl = (DefaultTableModel) this.tblMoves.getModel();
        tbl.setRowCount(0);
        
        try (Connection connection = Conexion.getInstance().getConnection()) {
            for (MoveInfo m : ip.listMovesInfo(connection)) {
            tbl.addRow(new Object[]
            {
                m.getId(), m.getName(), m.getIdMoveCat(), m.getIdType(), m.getPower(),
                m.getAccuracy(), m.getPp(), m.getEffect(), m.getEffectProb()
            });
            }
            Conexion.getInstance().releaseConnection(connection);
        // Use the itemList as needed
        } catch (SQLException e) {
            System.out.println(e);
        } 
    }
   

    /**
    * Este método permite obtener la descripcion de un movimiento seleccionado.
    * 
    * @param cbo el combobox del cual se quiere sacar el movimiento.
    * @return la descripcion en formato String.
    */
   private String getMoveDescription(JComboBox cbo) {
       String description = "";
       try {
           Move move = matchMove(cbo);
           if (move != null) {
               description = move.getEffect();
           }
       } catch (NullPointerException e) {
           // Handle the exception (e.g., log it or display an error message)
           e.printStackTrace(); // Print the stack trace for debugging purposes
       }
       return description;
   }

   /**
    * Este método permite obtener la descripcion del item seleccionado.
    * 
    * @return la descripcion del item.
    */
   private String getItemDescription() {
       String description = "";
       try {
           Item item = matchItem();
           if (item != null) {
               description = item.getDescription();
           }
       } catch (NullPointerException e) {
           e.printStackTrace();
       }
       return description;
   }

   /**
    * Este método permite obtener la descripcion de la habilidad seleccionada.
    * 
    * @return la descripcion de la habilidad.
    */
   private String getAbilityDescription() {
       String description = "";
       try {
           Ability ability = matchAbility();
           if (ability != null) {
               description = ability.getDescription();
           }
       } catch (NullPointerException e) {
           e.printStackTrace();
       }
       return description;
   }

    /**
     * Este método permite resetear todos los campos combobox para poder seleccionar
     * nuevas opciones.
     */
    private void resetFields(){
        String empty = "";
        for (JComboBox cbo : listMoveCBOXS) {
            cbo.setSelectedItem(empty);
        }
        cboAbility.setSelectedItem(empty);
        cboItem.setSelectedItem(empty);
        cboSpecies.setSelectedItem(empty);
    }
    /**
     * Este método permite obtener un pequeño ícono con la imagen de un Pokémon basado 
     * en su nombre. Se obtiene el nombre del Pokémon seleccionado en la cboSpecies
     * y se busca en la carpeta images del proyecto.
     * @return 
     */
    private ImageIcon getPokemonSprite() {
        String selectedPokemon = cboSpecies.getSelectedItem().toString();
        ImageIcon sprite = new ImageIcon();

        if (selectedPokemon.equalsIgnoreCase(emptyString)) {
            // Set a default image when no Pokemon is selected
            sprite = new ImageIcon("src/images/default.png");
        } else {
            // Load the image based on the selected Pokemon name
            String imagePath = "src/images/" + selectedPokemon.toLowerCase() + ".png";
            File imageFile = new File(imagePath);

            if (imageFile.exists()) {
                sprite = new ImageIcon(imagePath);
            } else {
                // Set a default image if the file doesn't exist
                sprite = new ImageIcon("src/images/default.png");
            }
        }

        // Resize the image if needed
        Image img = sprite.getImage();
        Image resizedImg = img.getScaledInstance(112,84, Image.SCALE_SMOOTH);
        sprite = new ImageIcon(resizedImg);

        return sprite;
    }
     /**
     * Método en progreso que permite recibir un pokepaste en el txtPokepaste
     * e importarlo al sistema para poder interactuar con él y poder subirlo
     * a la base de datos.
     * @param pokepaste 
     */
    public void importPokepaste(String pokepaste) {
        // Split the pokepaste string into lines
        String[] lines = pokepaste.split("\n");

        // Parse the species and nickname if present
        String speciesAndNickname = lines[0];
        String[] speciesAndNicknameParts = speciesAndNickname.split(" @ ");
        String species;
        String nickname = "";
        String item = ""; // Added item variable
     

        if (speciesAndNicknameParts.length == 2) {
            species = speciesAndNicknameParts[0].trim();

            // Further split the speciesAndNicknameParts[0] to separate species and nickname
            String[] speciesParts = speciesAndNicknameParts[0].trim().split("\\(");
            if (speciesParts.length > 1) {
                nickname = speciesParts[0].trim();
                species = speciesParts[1].replace(")", "").trim();
                

            }

            item = speciesAndNicknameParts[1].trim(); // Set item from the second part
        } else {
            species = speciesAndNickname.trim();
        }

        // Set the species, nickname, and item
        cboSpecies.setSelectedItem(species);
        txtNickname.setText(nickname);
        cboItem.setSelectedItem(item); // Set the item
        // Set gender radio buttons
        // Extract gender information from the nickname

        String genderLine = lines[0];
        if (genderLine.contains("(M)")) {
            rbnMale.setSelected(true);
        } else if (genderLine.contains("(F)")) {
            rbnFemale.setSelected(true);
        } else {
            rdnGenderless.setSelected(true);
        }



        // Process each line
        for (String line : lines) {
            if (line.startsWith("Ability:")) {
                // Set ability combobox value
                String ability = line.substring("Ability: ".length()).trim();
                cboAbility.setSelectedItem(ability);
            } else if (line.startsWith("Level:")) {
                // Set level spinner value
                String levelString = line.substring("Level: ".length()).trim();
                int level = Integer.parseInt(levelString);
                spnLevel.setValue(level);
            } else if (line.startsWith("Shiny:")) {
                // Set shiny checkbox value
                boolean isShiny = line.endsWith("Yes");
                ckbShiny.setSelected(isShiny);
             } else if (line.startsWith("Tera Type:")) {
                // Set tera type combobox value
                String teraType = line.substring("Tera Type: ".length()).trim();
                cboTeraType.setSelectedItem(teraType);
            } else if (line.startsWith("EVs:")) {
                // Set EV sliders
                // Parse and set values for HP, Atk, Def, SpA, SpDef, and Spe
                // Example: EVs: 252 HP / 252 Atk / 4 Def
                String evLine = line.substring("EVs: ".length());
                String[] evValues = evLine.split(" / ");
                for (String ev : evValues) {
                    String[] parts = ev.trim().split(" ");
                    int value = Integer.parseInt(parts[0]);
                    String stat = parts[1];
                    switch (stat) {
                        case "HP":
                            sldHP.setValue(value);
                            break;
                        case "Atk":
                            sldATK.setValue(value);
                            break;
                        case "Def":
                            sldDEF.setValue(value);
                            break;
                        case "SpA":
                            sldSPATK.setValue(value);
                            break;
                        case "SpDef":
                            sldSPDEF.setValue(value);
                            break;
                        case "Spe":
                            sldSPD.setValue(value);
                            break;
                        default:
                            // Handle unknown stat
                            break;
                    }
                }
            } else if (line.contains(" Nature")) {
                // Set nature combobox value
                String natureLine = line.replace("Nature", "").trim();
                String[] parts = natureLine.split("\\s+");
                if (parts.length > 0) {
                    String nature = parts[parts.length - 1];
                    cboNature.setSelectedItem(nature);
                }


            } else if (line.startsWith("IVs:")) {
                // Set IV spinners
                // Parse and set values for HP, Atk, Def, SpA, SpDef, and Spe
                // Example: IVs: 31 HP / 31 Atk / 31 Def
                String ivLine = line.substring("IVs: ".length());
                String[] ivValues = ivLine.split(" / ");
                for (String iv : ivValues) {
                    String[] parts = iv.trim().split(" ");
                    int value = Integer.parseInt(parts[0]);
                    String stat = parts[1];
                    switch (stat) {
                        case "HP":
                            spnIVHP.setValue(value);
                            break;
                        case "Atk":
                            spnIVATK.setValue(value);
                            break;
                        case "Def":
                            spnIVDEF.setValue(value);
                            break;
                        case "SpA":
                            spnIVSPATK.setValue(value);
                            break;
                        case "SpDef":
                            spnIVSPDEF.setValue(value);
                            break;
                        case "Spe":
                            spnIVSPD.setValue(value);
                            break;
                        default:
                            // Handle unknown stat
                            break;
                    }
                }
            } else if (line.startsWith("- ")) {
                // Set move combobox values
                String move = line.substring(2).trim();
                // Check which move slot (1, 2, 3, 4) and set the corresponding combobox
                if (cboMove1.getSelectedItem().toString().isEmpty()) {
                    cboMove1.setSelectedItem(move);
                } else if (cboMove2.getSelectedItem().toString().isEmpty()) {
                    cboMove2.setSelectedItem(move);
                } else if (cboMove3.getSelectedItem().toString().isEmpty()) {
                    cboMove3.setSelectedItem(move);
                } else if (cboMove4.getSelectedItem().toString().isEmpty()) {
                    cboMove4.setSelectedItem(move);
                }
            // Add more conditions for other lines as needed
        }
    }

    }  
}