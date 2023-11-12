package vista;


import ActionListeners.CustomChangeListener;
import ActionListeners.SliderMouseListener;
import Interface.IPokemonCalculator;
import bd.Conexion;
import controlador.InteractuarPokepaste;
import controlador.PoblarTablas;
import java.awt.Component;
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
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
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
    private boolean isShiny;
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
    private JLabel[] listStatLabels;
    private JLabel[] listFinalStatLabels;
    private JSpinner[] listIvSPN;
    private JComboBox[] listMoveCBOXS;
    private JSlider[] listStatSliders;
    
    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        this.listaMove = tb.getMoves();
        this.listaNature = tb.getNature();
        this.listaPkmnType = tb.getPokemonType();
        this.listaAbility = tb.getAbilities();
        this.listaPokemon = tb.getPokemon();
        this.listaItems = tb.getItems();
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
        initializeEventListeners();
    }

    
    private void setLabels(JLabel[] statLabels, JSlider[] statSliders) {
        for (int i = 0; i < statLabels.length; i++) {
            int index = i; // Final or effectively final variable
            statLabels[i].setText(String.valueOf(statSliders[i].getValue()));
            statSliders[i].addChangeListener(e -> {
                statLabels[index].setText(String.valueOf(statSliders[index].getValue()));
            });
        }
    }

    public String generatePokepaste(){
        int hp = sldHP.getValue();
        int atk = sldATK.getValue();
        int def = sldDEF.getValue();
        int spatk = sldSPATK.getValue();
        int spdef = sldSPDEF.getValue();
        int spd = sldSPD.getValue();
        int evSum = hp + atk + def + spatk + spdef + spd;
        lblEVSum.setText(String.valueOf(evSum));
        
        StringBuilder pokepaste = new StringBuilder();
        
        if (!txtNickname.getText().isEmpty()) { // if the pokemon has a nickname
            pokepaste.append(txtNickname.getText()).append(' ');
        } else { // else just show the species' name
            pokepaste.append(cboSpecies.getSelectedItem().toString());        
        }
        if (gender.getId()!=3) {
            pokepaste.append(" (").append(gender.getName().charAt(0)).append(")");
        }
        pokepaste.append(" @ ").append(cboItem.getSelectedItem().toString());
        pokepaste.append("\n").append("Ability: ").append(cboAbility.getSelectedItem().toString());
        if (ckbShiny.isSelected()) {
            pokepaste.append("\nShiny: Yes");
        }
        pokepaste.append("\nTera Type: ").append(cboTeraType.getSelectedItem());
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
        pokepaste.append("\n").append(cboNature.getSelectedItem()).append(" Nature");
        //
        
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
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtPokepaste = new javax.swing.JTextArea();
        lblEVSum = new javax.swing.JLabel();
        btnProbarConexion = new javax.swing.JButton();
        jtpPokemonGeneration = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
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
        lblPokemonSprite = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
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
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPokemon = new javax.swing.JTable();
        btnConsultar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblMoves = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pokemon Generator");
        setIconImage(greatTuskIcon.getImage());
        setName("mainFrame"); // NOI18N
        setResizable(false);
        setSize(new java.awt.Dimension(1920, 1080));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Pokepaste:");

        txtPokepaste.setColumns(20);
        txtPokepaste.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 18)); // NOI18N
        txtPokepaste.setRows(5);
        jScrollPane1.setViewportView(txtPokepaste);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(212, 212, 212)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(82, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(36, 36, 36)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(81, 81, 81))
        );

        btnProbarConexion.setText("Probar Conexion");
        btnProbarConexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProbarConexionActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));

        jLabel2.setText("Nickname:");

        jLabel3.setText("Species:");

        jLabel4.setText("Move 1:");

        jLabel5.setText("Move 2:");

        jLabel6.setText("Move 4:");

        jLabel7.setText("Move 3:");

        jLabel8.setText("Items:");

        jLabel10.setText("Ability:");

        jLabel11.setText("Tera Type:");

        cboTeraType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTeraTypeActionPerformed(evt);
            }
        });

        jLabel12.setText("Nature:");

        cboNature.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboNatureItemStateChanged(evt);
            }
        });

        bgpGender.add(rbnFemale);
        rbnFemale.setText("Female");
        rbnFemale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbnFemaleActionPerformed(evt);
            }
        });

        bgpGender.add(rbnMale);
        rbnMale.setText("Male");
        rbnMale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbnMaleActionPerformed(evt);
            }
        });

        bgpGender.add(rdnGenderless);
        rdnGenderless.setSelected(true);
        rdnGenderless.setText("Random");
        rdnGenderless.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdnGenderlessActionPerformed(evt);
            }
        });

        cboSpecies.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboSpeciesItemStateChanged(evt);
            }
        });

        cboMove1.setSelectedItem(new String(""));

        cboAbility.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        btnGeneratePokepaste.setText("Generate Pokepaste");
        btnGeneratePokepaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGeneratePokepasteActionPerformed(evt);
            }
        });

        spnLevel.setModel(new javax.swing.SpinnerNumberModel(100, 1, 100, 1));
        spnLevel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnLevelStateChanged(evt);
            }
        });

        jLabel20.setText("Level:");

        ckbShiny.setText("Shiny");
        ckbShiny.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckbShinyActionPerformed(evt);
            }
        });

        btnSavePokepaste.setText("Save Pokepaste");
        btnSavePokepaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSavePokepasteActionPerformed(evt);
            }
        });

        btnImportPaste.setText("Import Pokepaste");
        btnImportPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportPasteActionPerformed(evt);
            }
        });

        btnAbilityDesc.setText("See description");
        btnAbilityDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbilityDescActionPerformed(evt);
            }
        });

        btnMove1Desc.setText("See description");
        btnMove1Desc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMove1DescActionPerformed(evt);
            }
        });

        btnMove2Desc.setText("See description");
        btnMove2Desc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMove2DescActionPerformed(evt);
            }
        });

        btnMove3Desc.setText("See description");
        btnMove3Desc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMove3DescActionPerformed(evt);
            }
        });

        btnMove4Desc.setText("See description");
        btnMove4Desc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMove4DescActionPerformed(evt);
            }
        });

        btnItemDesc.setText("See description");
        btnItemDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemDescActionPerformed(evt);
            }
        });

        btnResetFields.setText("Reset fields");
        btnResetFields.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetFieldsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(rbnFemale)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbnMale)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdnGenderless)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 719, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ckbShiny)
                            .addComponent(btnSavePokepaste, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnImportPaste, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnResetFields, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnGeneratePokepaste, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(64, 64, 64))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNickname)
                            .addComponent(cboTeraType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboNature, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboItem, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboMove4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboMove3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboMove2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboMove1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboAbility, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboSpecies, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(spnLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnAbilityDesc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnMove1Desc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnMove2Desc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnMove3Desc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnMove4Desc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnItemDesc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(lblPokemonSprite)))
                        .addGap(158, 158, 158))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNickname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ckbShiny))
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(cboSpecies, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblPokemonSprite))
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(cboAbility, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAbilityDesc))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cboMove1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMove1Desc))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cboMove2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMove2Desc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cboMove3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMove3Desc))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cboMove4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMove4Desc))
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cboItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnItemDesc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cboTeraType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboNature, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spnLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(btnImportPaste))
                .addGap(18, 18, 18)
                .addComponent(btnGeneratePokepaste)
                .addGap(18, 18, 18)
                .addComponent(btnResetFields)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbnFemale)
                    .addComponent(rbnMale)
                    .addComponent(rdnGenderless)
                    .addComponent(btnSavePokepaste))
                .addGap(29, 29, 29))
        );

        jtpPokemonGeneration.addTab("Pokémon", jPanel2);

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblDEF.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        lblSPDEF.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        sldATK.setMajorTickSpacing(36);
        sldATK.setMaximum(252);
        sldATK.setPaintLabels(true);
        sldATK.setValue(0);

        sldDEF.setMajorTickSpacing(36);
        sldDEF.setMaximum(252);
        sldDEF.setPaintLabels(true);
        sldDEF.setValue(0);

        lblSPATK.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        sldSPD.setMajorTickSpacing(36);
        sldSPD.setMaximum(252);
        sldSPD.setPaintLabels(true);
        sldSPD.setValue(0);

        lblSPD.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        lblHP.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        sldSPDEF.setMajorTickSpacing(36);
        sldSPDEF.setMaximum(252);
        sldSPDEF.setPaintLabels(true);
        sldSPDEF.setValue(0);

        lblATK.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        sldHP.setMajorTickSpacing(36);
        sldHP.setMaximum(252);
        sldHP.setPaintLabels(true);
        sldHP.setValue(0);
        sldHP.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                sldHPMouseDragged(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("EVs: ");

        jLabel13.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel13.setText("HP:");

        sldSPATK.setMajorTickSpacing(36);
        sldSPATK.setMaximum(252);
        sldSPATK.setPaintLabels(true);
        sldSPATK.setValue(0);

        jLabel14.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel14.setText("ATK:");

        jLabel15.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel15.setText("DEF:");

        jLabel16.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel16.setText("SPATK:");

        jLabel17.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel17.setText("SPDEF:");

        jLabel18.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel18.setText("SPD:");

        pgbHP.setMaximum(255);
        pgbHP.setString("");
        pgbHP.setStringPainted(true);

        pgbATK.setMaximum(255);
        pgbATK.setString("");
        pgbATK.setStringPainted(true);

        pgbDEF.setMaximum(255);
        pgbDEF.setString("");
        pgbDEF.setStringPainted(true);

        pgbSPATK.setMaximum(255);
        pgbSPATK.setString("");
        pgbSPATK.setStringPainted(true);

        pgbSPDEF.setMaximum(255);
        pgbSPDEF.setRequestFocusEnabled(false);
        pgbSPDEF.setString("");
        pgbSPDEF.setStringPainted(true);

        pgbSPD.setMaximum(255);
        pgbSPD.setString("");
        pgbSPD.setStringPainted(true);

        jLabel19.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel19.setText("IVs");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(pgbDEF, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(pgbSPATK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(57, 57, 57)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblSPATK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblDEF, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(sldSPATK, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(sldDEF, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(pgbHP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(pgbATK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(57, 57, 57)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblHP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblATK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(sldHP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(sldATK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pgbSPDEF, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pgbSPD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(57, 57, 57)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblSPD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblSPDEF, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(sldSPD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(sldSPDEF, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spnIVHP, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spnIVATK, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spnIVDEF, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spnIVSPATK, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spnIVSPDEF, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spnIVSPD, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblFinalSPATK, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblFinalDEF, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblFinalATK, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblFinalHP, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblFinalSPDEF, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblFinalSPD, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(340, 340, 340)
                                .addComponent(jLabel9))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(819, 819, 819)
                                .addComponent(jLabel19)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {sldATK, sldDEF, sldHP, sldSPATK, sldSPD, sldSPDEF});

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {spnIVATK, spnIVDEF, spnIVHP, spnIVSPATK, spnIVSPD, spnIVSPDEF});

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lblFinalATK, lblFinalDEF, lblFinalHP, lblFinalSPATK, lblFinalSPD, lblFinalSPDEF});

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {pgbATK, pgbDEF, pgbHP, pgbSPATK, pgbSPD, pgbSPDEF});

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel13, jLabel14, jLabel15, jLabel16, jLabel17, jLabel18});

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel9, lblATK, lblDEF, lblHP, lblSPATK, lblSPD, lblSPDEF});

        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel19))
                .addGap(43, 43, 43)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spnIVHP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sldHP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFinalHP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pgbHP, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblHP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblATK, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sldATK, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(spnIVATK, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblFinalATK, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pgbATK, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDEF, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(pgbDEF, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(sldDEF, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblFinalDEF, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(spnIVDEF, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sldSPATK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSPATK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(spnIVSPATK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblFinalSPATK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pgbSPATK, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pgbSPDEF, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGap(8, 8, 8)
                                    .addComponent(lblFinalSPDEF, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(lblSPDEF, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(spnIVSPDEF, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8))
                            .addComponent(sldSPDEF, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblFinalSPD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sldSPD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSPD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(spnIVSPD, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pgbSPD, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(186, Short.MAX_VALUE))
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {sldATK, sldDEF, sldHP, sldSPATK, sldSPD, sldSPDEF});

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {spnIVATK, spnIVDEF, spnIVHP, spnIVSPATK, spnIVSPD, spnIVSPDEF});

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lblFinalATK, lblFinalDEF, lblFinalHP, lblFinalSPATK, lblFinalSPD, lblFinalSPDEF});

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {pgbATK, pgbDEF, pgbHP, pgbSPATK, pgbSPD, pgbSPDEF});

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel13, jLabel14, jLabel15, jLabel16, jLabel17, jLabel18});

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel9, lblATK, lblDEF, lblHP, lblSPATK, lblSPD, lblSPDEF});

        jtpPokemonGeneration.addTab("EV's", jPanel4);

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
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1263, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(btnConsultar)
                .addGap(32, 32, 32)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(159, Short.MAX_VALUE))
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
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1251, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(176, Short.MAX_VALUE))
        );

        jtpPokemonGeneration.addTab("Moves Table", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(536, 536, 536)
                        .addComponent(btnProbarConexion))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1340, 1340, 1340)
                                .addComponent(lblEVSum, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jtpPokemonGeneration, javax.swing.GroupLayout.PREFERRED_SIZE, 1263, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(382, 382, 382)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1586, 1586, 1586)
                        .addComponent(lblEVSum, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(btnProbarConexion)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtpPokemonGeneration, javax.swing.GroupLayout.PREFERRED_SIZE, 778, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            Conexion connection = new Conexion();
            Connection ctn = connection.getConnection();
            System.out.println("Bien");
        } catch (Exception e) {
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
        isShiny = this.ckbShiny.isSelected();
    }//GEN-LAST:event_ckbShinyActionPerformed

    private void cboNatureItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboNatureItemStateChanged
        // TODO add your handling code here:
        setFinalStatLabels();

    }//GEN-LAST:event_cboNatureItemStateChanged

    private void btnSavePokepasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSavePokepasteActionPerformed
        // TODO add your handling code here:
        PokemonPaste buildPokepaste = buildPokepaste();
        ip.insertPokepaste(buildPokepaste);
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
        setValuesFromPokepaste(txtPokepaste.getText());
    }//GEN-LAST:event_btnImportPasteActionPerformed

    private void btnAbilityDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbilityDescActionPerformed
        // TODO add your handling code here:
        String abilityDescription = getAbilityDescription();
    
        // Show a pop-up message with the ability description
        JOptionPane.showMessageDialog(this, abilityDescription, "Ability Description", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnAbilityDescActionPerformed

    private void btnMove1DescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMove1DescActionPerformed
        // TODO add your handling code here:
        String moveDesc = getMoveDescription(cboMove1);
        // Show a pop-up message with the ability description
        JOptionPane.showMessageDialog(this, moveDesc, "Move Description", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnMove1DescActionPerformed

    private void btnMove2DescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMove2DescActionPerformed
        // TODO add your handling code here:
        String moveDesc = getMoveDescription(cboMove2);
        // Show a pop-up message with the ability description
        JOptionPane.showMessageDialog(this, moveDesc, "Move Description", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnMove2DescActionPerformed

    private void btnMove3DescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMove3DescActionPerformed
        // TODO add your handling code here:
        String moveDesc = getMoveDescription(cboMove3);
        // Show a pop-up message with the ability description
        JOptionPane.showMessageDialog(this, moveDesc, "Move Description", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnMove3DescActionPerformed

    private void btnMove4DescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMove4DescActionPerformed
        // TODO add your handling code here:
        String moveDesc = getMoveDescription(cboMove4);
        // Show a pop-up message with the ability description
        JOptionPane.showMessageDialog(this, moveDesc, "Move Description", JOptionPane.INFORMATION_MESSAGE);
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
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
     * @param def ...
     * @param spatk ...
     * @param spdef ...
     * @param spd ...
     * @return devuelve la lista de stats ya modificados tras pasar por el switch.
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

    
    private void initializeSpinners(JSpinner[] spinners) {
        for (JSpinner spinner : spinners) {
            CustomChangeListener customChangeListener = new CustomChangeListener();
            SpinnerNumberModel model = new SpinnerNumberModel(31, 0, 31, 1);
            spinner.addChangeListener(customChangeListener);
            spinner.setModel(model);
        }
    }
    
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
        ArrayList<PokemonPasteInfo> lista =  ip.listPokepaste();
        for (PokemonPasteInfo p : lista) {
            tbl.addRow(new Object[]
            {
                p.getId(), p.getPokemon(), p.getTeraType(), 
                p.getAbility(), p.getHp(), p.getAtk(), p.getDef(), p.getSpatk(), 
                p.getSpdef(), p.getSpd(), p.getLevel(), p.getIsShiny()
            });
            
        }
    }
    
    private void fillMoveTbl() {
        DefaultTableModel tbl = (DefaultTableModel) this.tblMoves.getModel();
        tbl.setRowCount(0);
        for (MoveInfo m : ip.listMovesInfo()) {
            tbl.addRow(new Object[]
            {
                m.getId(), m.getName(), m.getIdMoveCat(), m.getIdType(), m.getPower(),
                m.getAccuracy(), m.getPp(), m.getEffect(), m.getEffectProb()
            });
        }
    }
    /**
     * Método en progreso que permite recibir un pokepaste en el txtPokepaste
     * e importarlo al sistema para poder interactuar con él y poder subirlo
     * a la base de datos.
     * @param pokepaste 
     */
    private void setValuesFromPokepaste(String pokepaste) {
        // Split the pokepaste into lines
        String[] lines = pokepaste.split("\n");

        // Parse the species and nickname if present
        String speciesAndNickname = lines[0];
        String[] speciesAndNicknameParts = speciesAndNickname.split(" @ ");
        String species;
        String nickname = "";
        String item = ""; // Added item variable
        if (speciesAndNicknameParts.length == 2) {
            species = speciesAndNicknameParts[0].trim();
            item = speciesAndNicknameParts[1].trim(); // Set item from the second part
        } else {
            species = speciesAndNickname.trim();
        }

        // Set the species, nickname, and item
        cboSpecies.setSelectedItem(species);
        txtNickname.setText(nickname);
        cboItem.setSelectedItem(item); // Set the item

        // Parse and set other values
        for (String line : lines) {
            if (line.startsWith("Ability: ")) {
                cboAbility.setSelectedItem(line.substring("Ability: ".length()));
            } else if (line.startsWith("Tera Type: ")) {
                cboTeraType.setSelectedItem(line.substring("Tera Type: ".length()));
            } else if (line.startsWith("Shiny: ")) {
                ckbShiny.setSelected(line.endsWith("Yes"));
            } else if (line.startsWith("EVs: ")) {
                parseAndSetEVs(line.substring("EVs: ".length()));
            } else if (line.endsWith(" Nature")) {
                cboNature.setSelectedItem(line.replace(" Nature", ""));
            } else if (line.startsWith("- ")) {
                parseAndSetMove(line.substring(2));
            }
        }
    }

    /**
     * Método que permite obtener los valores de los EVs del pokepaste.
     * @param evLine 
     */
    private void parseAndSetEVs(String evLine) {
        String[] evs = evLine.split(" / ");
        for (String ev : evs) {
            String[] evParts = ev.trim().split(" ");
            int value = Integer.parseInt(evParts[0]);
            String stat = evParts[1];
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
                // Handle additional stats if needed
            }
        }


    }
    
    /**
     * Método que permite obtener los movimientos del pokepaste.
     * @param move 
     */
    private void parseAndSetMove(String move) {
        // Determine which move slot to set based on the current state
        
        if (cboMove1.getSelectedItem().toString().isEmpty()) {
            cboMove1.setSelectedItem(move);
        } else if (cboMove2.getSelectedItem().toString().isEmpty()) {
            cboMove2.setSelectedItem(move);
        } else if (cboMove3.getSelectedItem().toString().isEmpty()) {
            cboMove3.setSelectedItem(move);
        } else if (cboMove4.getSelectedItem().toString().isEmpty()) {
            cboMove4.setSelectedItem(move);
        }
    }
    /**
     * Este método permite obtener la descripcion de un movimiento seleccionado.
     * @param cbo el combobox del cual se quiere sacar el movimiento.
     * @return la descripcion en formato String.
     */
    private String getMoveDescription(JComboBox cbo ) {
        Move move = matchMove(cbo);
        String description = move.getEffect();
        return description;
    }
    /**
     * Este método permite obtener la descripcion del item seleccionado.
     * @return la descripcion del item.0
     */
    private String getItemDescription() {
        Item item = matchItem();
        String description = item.getDescription();
        return description;
    }
    /**
     * Este método permite obtener la descripcion de la habilidad seleccionada.
     * @return 
     */
    private String getAbilityDescription() {
        Ability ability = matchAbility();    
        String description = ability.getDescription();
        return description;
    }
    
    private void resetFields(){
        for (JComboBox cbo : listMoveCBOXS) {
            cbo.setSelectedItem("");
        }
    }
    
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
        Image resizedImg = img.getScaledInstance(56,42, Image.SCALE_SMOOTH);
        sprite = new ImageIcon(resizedImg);

        return sprite;
    }
}


    
    




