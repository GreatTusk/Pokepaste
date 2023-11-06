
import ActionListeners.SliderMouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author F776
 */
public class MainWindow extends javax.swing.JFrame {
    
    private String gender = "";
    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents(); // Initialize all Swing components first
        // Then, initialize the statSliders array after the Swing components are created
        JSlider[] statSliders = new JSlider[] {sldHP, sldATK, sldDEF, sldSPATK, sldSPDEF, sldSPD};
        JLabel[] statLabels = new JLabel[] {lblHP, lblATK, lblDEF, lblSPATK, lblSPDEF, lblSPD};
        addLimit(statSliders); // Call the method which might use the statSliders array
        setLabels(statLabels, statSliders);
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

    private final String[] pokemonTypes = {
            "Normal", "Fire", "Water", "Electric", "Grass", "Ice", "Fighting",
            "Poison", "Ground", "Flying", "Psychic", "Bug", "Rock", "Ghost",
            "Dragon", "Dark", "Steel", "Fairy"
        };
    
    private final String[] pokemonNatures = {
            "Adamant", "Bashful", "Bold", "Brave", "Calm", "Careful", "Docile",
            "Gentle", "Hardy", "Hasty", "Impish", "Jolly", "Lax", "Lonely",
            "Mild", "Modest", "Naive", "Naughty", "Quiet", "Quirky", "Rash",
            "Relaxed", "Sassy", "Serious", "Timid"
        };
    

    public void submit(JTextField txt) {
        txt.setText(txt.getText());
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
            pokepaste.append('(').append(txtSpecies.getText()).append(')');            
        } else { // else just show the species' name
            pokepaste.append(txtSpecies.getText());        
        }
        if (!gender.isEmpty()) {
            pokepaste.append(" (").append(gender).append(")");
        }
        pokepaste.append(" @ ").append(txtItem.getText());
        pokepaste.append("\n").append("Ability: ").append(txtAbility.getText());
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
        pokepaste.append("\n- ").append(txtMove1.getText());
        pokepaste.append("\n- ").append(txtMove2.getText());
        pokepaste.append("\n- ").append(txtMove3.getText());
        pokepaste.append("\n- ").append(txtMove4.getText());
        
        return String.valueOf(pokepaste);
    }
    
    public void confirmTxt(java.awt.event.KeyEvent evt, JTextField txt) {
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
        // Perform actions when Enter key is pressed
        // For example, you can call a method or perform an action here
        submit(txt);// 
        }
        // Update the txtPokepaste field
        String generatePoketext = generatePokepaste();
        txtPokepaste.setText(generatePoketext);
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
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtNickname = new javax.swing.JTextField();
        txtSpecies = new javax.swing.JTextField();
        txtMove1 = new javax.swing.JTextField();
        txtMove2 = new javax.swing.JTextField();
        txtMove3 = new javax.swing.JTextField();
        txtMove4 = new javax.swing.JTextField();
        txtItem = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtAbility = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cboTeraType = new javax.swing.JComboBox<>(pokemonTypes);
        jLabel12 = new javax.swing.JLabel();
        cboNature = new javax.swing.JComboBox<>(pokemonNatures);
        rbnFemale = new javax.swing.JRadioButton();
        rbnMale = new javax.swing.JRadioButton();
        rdnGenderless = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtPokepaste = new javax.swing.JTextArea();
        lblEVSum = new javax.swing.JLabel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pokemon Generator");
        setIconImage(greatTuskIcon.getImage());
        setName("mainFrame"); // NOI18N
        setResizable(false);
        setSize(new java.awt.Dimension(1920, 1080));

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));

        jLabel2.setText("Nickname:");

        jLabel3.setText("Species:");

        jLabel4.setText("Move 1:");

        jLabel5.setText("Move 2:");

        jLabel6.setText("Move 4:");

        jLabel7.setText("Move 3:");

        jLabel8.setText("Items:");

        txtNickname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNicknameKeyPressed(evt);
            }
        });

        txtSpecies.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSpeciesKeyPressed(evt);
            }
        });

        txtMove1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMove1KeyPressed(evt);
            }
        });

        txtMove2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMove2KeyPressed(evt);
            }
        });

        txtMove3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMove3KeyPressed(evt);
            }
        });

        txtMove4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMove4KeyPressed(evt);
            }
        });

        txtItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtItemKeyPressed(evt);
            }
        });

        jLabel10.setText("Ability:");

        txtAbility.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAbilityKeyPressed(evt);
            }
        });

        jLabel11.setText("Tera Type:");

        cboTeraType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTeraTypeActionPerformed(evt);
            }
        });

        jLabel12.setText("Nature:");

        cboNature.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboNatureActionPerformed(evt);
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
        rdnGenderless.setText("Genderless");
        rdnGenderless.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdnGenderlessActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
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
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNickname)
                            .addComponent(txtSpecies)
                            .addComponent(txtAbility)
                            .addComponent(txtMove1)
                            .addComponent(txtMove2)
                            .addComponent(txtMove3)
                            .addComponent(txtMove4)
                            .addComponent(txtItem)
                            .addComponent(cboTeraType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboNature, 0, 269, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(rbnFemale)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbnMale)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdnGenderless)))
                .addContainerGap(68, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNickname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtSpecies, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtAbility, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMove1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtMove2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMove3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(txtMove4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cboTeraType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboNature, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbnFemale)
                    .addComponent(rbnMale)
                    .addComponent(rdnGenderless))
                .addContainerGap(29, Short.MAX_VALUE))
        );

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

        jLabel9.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Sum of EVs: ");

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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(6, 6, 6)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sldDEF, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                    .addComponent(sldSPATK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sldSPDEF, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sldHP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sldSPD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sldATK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblHP, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblATK, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDEF, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSPATK, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSPDEF, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSPD, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(223, 223, 223))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel9)
                .addGap(42, 42, 42)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sldHP, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHP, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sldATK, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblATK, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDEF, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(sldDEF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sldSPATK, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSPATK, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sldSPDEF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSPDEF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sldSPD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSPD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(120, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1340, 1340, 1340)
                        .addComponent(lblEVSum, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(146, 146, 146)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(284, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(317, 317, 317)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(196, 196, 196)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 601, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(131, 131, 131)
                .addComponent(lblEVSum, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void addLimit(JSlider[] statSliders) {
        SliderMouseListener sliderMouseListener = new SliderMouseListener(statSliders);
        for (JSlider statSlider : statSliders) {
            statSlider.addMouseListener(sliderMouseListener);
            statSlider.addMouseMotionListener(sliderMouseListener);
        }
    }

    private void txtNicknameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNicknameKeyPressed
        // TODO add your handling code here:
        confirmTxt(evt, txtNickname);  
    }//GEN-LAST:event_txtNicknameKeyPressed

    private void txtSpeciesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSpeciesKeyPressed
        // TODO add your handling code here:
        confirmTxt(evt, txtSpecies); 
    }//GEN-LAST:event_txtSpeciesKeyPressed

    private void txtAbilityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAbilityKeyPressed
        // TODO add your handling code here:
        confirmTxt(evt, txtAbility); 
    }//GEN-LAST:event_txtAbilityKeyPressed

    private void txtMove1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMove1KeyPressed
        // TODO add your handling code here:
        confirmTxt(evt, txtMove1); 
    }//GEN-LAST:event_txtMove1KeyPressed

    private void txtMove2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMove2KeyPressed
        // TODO add your handling code here:
        confirmTxt(evt, txtMove2); 
    }//GEN-LAST:event_txtMove2KeyPressed

    private void txtMove3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMove3KeyPressed
        // TODO add your handling code here:
        confirmTxt(evt, txtMove3); 
    }//GEN-LAST:event_txtMove3KeyPressed

    private void txtMove4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMove4KeyPressed
        // TODO add your handling code here:
        confirmTxt(evt, txtMove4); 
    }//GEN-LAST:event_txtMove4KeyPressed

    private void txtItemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemKeyPressed
        // TODO add your handling code here:
        confirmTxt(evt, txtItem); 
    }//GEN-LAST:event_txtItemKeyPressed

    private void rbnFemaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnFemaleActionPerformed
        // TODO add your handling code here:
        gender = "F";
    }//GEN-LAST:event_rbnFemaleActionPerformed

    private void rbnMaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnMaleActionPerformed
        // TODO add your handling code here:
        gender = "M";
    }//GEN-LAST:event_rbnMaleActionPerformed

    private void rdnGenderlessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdnGenderlessActionPerformed
        // TODO add your handling code here:
        gender = "";
    }//GEN-LAST:event_rdnGenderlessActionPerformed

    private void cboTeraTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTeraTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboTeraTypeActionPerformed

    private void cboNatureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboNatureActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboNatureActionPerformed
    
    
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
    private javax.swing.JComboBox<String> cboNature;
    private javax.swing.JComboBox<String> cboTeraType;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblATK;
    private javax.swing.JLabel lblDEF;
    private javax.swing.JLabel lblEVSum;
    private javax.swing.JLabel lblHP;
    private javax.swing.JLabel lblSPATK;
    private javax.swing.JLabel lblSPD;
    private javax.swing.JLabel lblSPDEF;
    private javax.swing.JRadioButton rbnFemale;
    private javax.swing.JRadioButton rbnMale;
    private javax.swing.JRadioButton rdnGenderless;
    private javax.swing.JSlider sldATK;
    private javax.swing.JSlider sldDEF;
    private javax.swing.JSlider sldHP;
    private javax.swing.JSlider sldSPATK;
    private javax.swing.JSlider sldSPD;
    private javax.swing.JSlider sldSPDEF;
    private javax.swing.JTextField txtAbility;
    private javax.swing.JTextField txtItem;
    private javax.swing.JTextField txtMove1;
    private javax.swing.JTextField txtMove2;
    private javax.swing.JTextField txtMove3;
    private javax.swing.JTextField txtMove4;
    private javax.swing.JTextField txtNickname;
    private javax.swing.JTextArea txtPokepaste;
    private javax.swing.JTextField txtSpecies;
    // End of variables declaration//GEN-END:variables
    
    
}
