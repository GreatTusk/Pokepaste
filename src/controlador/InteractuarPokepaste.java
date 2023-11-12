/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import bd.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import modelo.Move;
import modelo.MoveInfo;
import modelo.PokemonPaste;
import modelo.PokemonPasteInfo;

/**
 *
 * @author F776
 */
public class InteractuarPokepaste {

    public InteractuarPokepaste() {
    }

    public boolean insertPokepaste (PokemonPaste p) {
        try {
            Conexion connection = new Conexion();
            Connection ctn = connection.getConnection();
            //Query
            String query = 
            "INSERT INTO pokemon_paste (\n" +
            "    id_pokemon,\n" +
            "    id_item,\n" +
            "    nickname,\n" +
            "    level,\n" +
            "    id_gender,\n" +
            "    is_shiny,\n" +
            "    id_tera_type,\n" +
            "    id_ability,\n" +
            "    move1,\n" +
            "    move2,\n" +
            "    move3,\n" +
            "    move4,\n" +
            "    hp_ev,\n" +
            "    atk_ev,\n" +
            "    def_ev,\n" +
            "    spatk_ev,\n" +
            "    spdef_ev,\n" +
            "    spd_ev,\n" +
            "    hp_iv,\n" +
            "    atk_iv,\n" +
            "    def_iv,\n" +
            "    spatk_iv,\n" +
            "    spdef_iv,\n" +
            "    spd_iv,\n" +
            "    hp,\n" +
            "    atk,\n" +
            "    def,\n" +
            "    spatk,\n" +
            "    spdef,\n" +
            "    spd,\n" +
            "    id_nature\n" +
            ") VALUES (\n" +
            "    ?, -- Replace with the actual Pokemon ID\n" +
            "    ?, -- Replace with the actual Item ID\n" +
            "    ?, -- Replace with the actual nickname\n" +
            "    ?,\n" +
            "    ?, -- Replace with the actual Gender ID\n" +
            "    ?,\n" +
            "    ?, -- Replace with the actual Pokemon Type ID\n" +
            "    ?, -- Replace with the actual Ability ID\n" +
            "    ?, -- Replace with the actual Move ID for move1\n" +
            "    ?, -- Replace with the actual Move ID for move2\n" +
            "    ?, -- Replace with the actual Move ID for move3\n" +
            "    ?, -- Replace with the actual Move ID for move4\n" +
            "    ?, -- Replace with the actual EV for hp_ev\n" +
            "    ?, -- Replace with the actual EV for atk_ev\n" +
            "    ?, -- Replace with the actual EV for def_ev\n" +
            "    ?, -- Replace with the actual EV for spatk_ev\n" +
            "    ?, -- Replace with the actual EV for spdef_ev\n" +
            "    ?, -- Replace with the actual EV for spd_ev\n" +
            "    ?, -- Replace with the actual IV for hp_iv\n" +
            "    ?, -- Replace with the actual IV for atk_iv\n" +
            "    ?, -- Replace with the actual IV for def_iv\n" +
            "    ?, -- Replace with the actual IV for spatk_iv\n" +
            "    ?, -- Replace with the actual IV for spdef_iv\n" +
            "    ?, -- Replace with the actual IV for spd_iv\n" +
            "    ?, -- Replace with the actual hp stat\n" +
            "    ?, -- Replace with the actual atk stat\n" +
            "    ?, -- Replace with the actual def stat\n" +
            "    ?, -- Replace with the actual spatk stat\n" +
            "    ?, -- Replace with the actual spdef stat\n" +
            "    ?, -- Replace with the actual spd stat\n" +
            "    ? -- Replace with the actual Nature ID\n" +
            ");";

            // Definir Prepare statement
            PreparedStatement s = ctn.prepareStatement(query);

            // con el query asignado se le da valores a los ?
            // se avanza desde i=1
            int i = 1;
            s.setInt(i++, p.getPokemon().getId());
            s.setInt(i++, p.getItem().getId());
            s.setString(i++, p.getNickname());
            s.setInt(i++, p.getLevel());
            s.setInt(i++, p.getGender().getId());
            s.setBoolean(i++, p.getIsShiny());
            s.setInt(i++, p.getTeraType().getId());
            s.setInt(i++, p.getAbility().getId());
            s.setInt(i++, p.getMove1().getId());
            s.setInt(i++, p.getMove2().getId());
            s.setInt(i++, p.getMove3().getId());
            s.setInt(i++, p.getMove4().getId());
            s.setInt(i++, p.getHpEv());
            s.setInt(i++, p.getAtkEv());
            s.setInt(i++, p.getDefEv());
            s.setInt(i++, p.getSpatkEv());
            s.setInt(i++, p.getSpdefEv());
            s.setInt(i++, p.getSpdEv());
            s.setInt(i++, p.getHpIv());
            s.setInt(i++, p.getAtkIv());
            s.setInt(i++, p.getDefIv());
            s.setInt(i++, p.getSpatkIv());
            s.setInt(i++, p.getSpdefIv());
            s.setInt(i++, p.getSpdIv());
            s.setInt(i++, p.getHp());
            s.setInt(i++, p.getAtk());
            s.setInt(i++, p.getDef());
            s.setInt(i++, p.getSpatk());
            s.setInt(i++, p.getSpdef());
            s.setInt(i++, p.getSpd());
            s.setInt(i++, p.getNature().getId());

            //ejecutar
            s.executeUpdate();
            //se cierra
            s.close();
            ctn.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            return false;
        }
    }
    
    public ArrayList<PokemonPasteInfo> listPokepaste() {
        
        ArrayList<PokemonPasteInfo> listaP = new ArrayList<PokemonPasteInfo>();
        try {
            Conexion connection = new Conexion();
            Connection ctn = connection.getConnection();
            //Query
            String query = 
                    "SELECT\n" +
                    "    pp.id AS paste_id,\n" +
                    "    pp.nickname,\n" +
                    "    pp.level,\n" +
                    "    CASE\n" +
                    "        WHEN pp.is_shiny = 0 THEN 'No'\n" +
                    "        WHEN pp.is_shiny = 1 THEN 'Yes'\n" +
                    "    END AS is_shiny,\n" +
                    "    p.name AS pokemon_name,\n" +
                    "    t.name AS tera_type_name,\n" +
                    "    a.name AS ability_name,\n" +
                    "    i.name AS item_name,\n" +
                    "    g.name AS gender_name,\n" +
                    "    m1.name AS move1_name,\n" +
                    "    m2.name AS move2_name,\n" +
                    "    m3.name AS move3_name,\n" +
                    "    m4.name AS move4_name,\n" +
                    "    n.name AS nature_name,\n" +
                    "    pp.hp,\n" +
                    "    pp.atk,\n" +
                    "    pp.def,\n" +
                    "    pp.spatk,\n" +
                    "    pp.spdef,\n" +
                    "    pp.spd\n" +
                    "FROM\n" +
                    "    pokemon_paste pp\n" +
                    "    INNER JOIN pokemon p ON pp.id_pokemon = p.id\n" +
                    "    INNER JOIN pokemon_type t ON pp.id_tera_type = t.id\n" +
                    "    INNER JOIN ability a ON pp.id_ability = a.id\n" +
                    "    LEFT JOIN item i ON pp.id_item = i.id\n" +
                    "    LEFT JOIN gender g ON pp.id_gender = g.id\n" +
                    "    INNER JOIN move m1 ON pp.move1 = m1.id\n" +
                    "    INNER JOIN move m2 ON pp.move2 = m2.id\n" +
                    "    INNER JOIN move m3 ON pp.move3 = m3.id\n" +
                    "    INNER JOIN move m4 ON pp.move4 = m4.id\n" +
                    "    INNER JOIN nature n ON pp.id_nature = n.id;";
            PreparedStatement stmt = ctn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                PokemonPasteInfo p = new PokemonPasteInfo();
                p.setId(rs.getInt("paste_id"));
                p.setNickname(rs.getString("nickname"));
                p.setLevel(rs.getInt("level"));
                p.setIsShiny(rs.getString("is_shiny"));
                p.setPokemon(rs.getString("pokemon_name"));
                p.setTeraType(rs.getString("tera_type_name"));
                p.setAbility(rs.getString("ability_name"));
                p.setItem(rs.getString("item_name"));
                p.setGender(rs.getString("gender_name"));
                p.setMove1(rs.getString("move1_name"));
                p.setMove2(rs.getString("move2_name"));
                p.setMove3(rs.getString("move3_name"));
                p.setMove4(rs.getString("move4_name"));
                p.setNature(rs.getString("nature_name"));
                p.setHp(rs.getInt("hp"));
                p.setAtk(rs.getInt("atk"));
                p.setDef(rs.getInt("def"));
                p.setSpatk(rs.getInt("spatk"));
                p.setSpdef(rs.getInt("spdef"));
                p.setSpd(rs.getInt("spd"));
                listaP.add(p);
            }
            stmt.close();
            ctn.close();
            
        } catch (SQLException e) {
            System.out.println("Error SQL listar usuario:" + e.getMessage());
            
        } catch (Exception e) {
            System.out.println("Error listar usuario:" + e.getMessage());
        }
        return listaP;
        } 
    
    public ArrayList<MoveInfo> listMovesInfo() {
        
        ArrayList<MoveInfo> listaM = new ArrayList<MoveInfo>();
        try {
            Conexion connection = new Conexion();
            Connection ctn = connection.getConnection();
            //Query
            String query = 
                    "SELECT\n" +
                    "    m.id AS move_id,\n" +
                    "    m.name AS move_name,\n" +
                    "    CONCAT(UPPER(SUBSTRING(mc.name, 1, 1)), LOWER(SUBSTRING(mc.name, 2))) AS move_category,\n" +
                    "    mt.name AS move_type,\n" +
                    "    NVL(m.power, '—') AS power,\n" +
                    "    NVL(m.accuracy, '—') AS accuracy,\n" +
                    "    NVL(m.pp, '—') AS pp,\n" +
                    "    m.effect,\n" +
                    "    NVL(m.effect_prob, '—') AS effect_prob\n" +
                    "FROM\n" +
                    "    move m\n" +
                    "JOIN\n" +
                    "    pokemon_type mt ON m.id_type = mt.id\n" +
                    "JOIN\n" +
                    "    move_cat mc ON m.id_move_cat = mc.id;";
            PreparedStatement stmt = ctn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                MoveInfo m = new MoveInfo();
                m.setId(rs.getInt("move_id"));
                m.setName(rs.getString("move_name"));
                m.setIdMoveCat(rs.getString("move_category"));
                m.setIdType(rs.getString("move_type"));
                m.setPower(rs.getString("power"));
                m.setAccuracy(rs.getString("accuracy"));
                m.setPp(rs.getString("pp"));
                m.setEffect(rs.getString("effect"));
                m.setEffectProb(rs.getString("effect_prob"));
                listaM.add(m);
            }
            stmt.close();
            ctn.close();
            
        } catch (SQLException e) {
            System.out.println("Error SQL listar usuario:" + e.getMessage());
            
        } catch (Exception e) {
            System.out.println("Error listar usuario:" + e.getMessage());
        }
        return listaM;
        }
}

