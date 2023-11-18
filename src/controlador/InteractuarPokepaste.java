/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    
    /**
     * Este método permite insertar toda la información contenida en un Pokepaste
     * en la tabla pokemon_paste de la base de datos.
     * @param connection la conexión con la base de datos de Oracle.
     * @param p el pokepaste de donde sacar la información.
     * @return true si se insertó con éxito. false si la inserción falló.
     */
    public boolean insertPokepaste(Connection connection, PokemonPaste p) {
        try (PreparedStatement stmt = connection.prepareStatement(
            "INSERT INTO syn_pkmn_paste (\n" +
            "    id_pokemon, \n" +
            "    id_item, \n" +
            "    nickname, \n" +
            "    \"level\", \n" +
            "    id_gender, \n" +
            "    is_shiny, \n" +
            "    id_tera_type, \n" +
            "    id_ability, \n" +
            "    move1, \n" +
            "    move2, \n" +
            "    move3, \n" +
            "    move4, \n" +
            "    hp_ev, \n" +
            "    atk_ev, \n" +
            "    def_ev, \n" +
            "    spatk_ev, \n" +
            "    spdef_ev, \n" +
            "    spd_ev, \n" +
            "    hp_iv, \n" +
            "    atk_iv, \n" +
            "    def_iv, \n" +
            "    spatk_iv, \n" +
            "    spdef_iv, \n" +
            "    spd_iv, \n" +
            "    hp, \n" +
            "    atk, \n" +
            "    \"def\", \n" +
            "    spatk, \n" +
            "    spdef, \n" +
            "    spd, \n" +
            "    id_nature\n" +
            ") VALUES (" +
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
            ")"
         )) {

        // Set parameter values
        int i = 1;
        stmt.setInt(i++, p.getPokemon().getId());
        stmt.setInt(i++, p.getItem().getId());
        stmt.setString(i++, p.getNickname());
        stmt.setInt(i++, p.getLevel());
        stmt.setInt(i++, p.getGender().getId());
        stmt.setBoolean(i++, p.getIsShiny());
        stmt.setInt(i++, p.getTeraType().getId());
        stmt.setInt(i++, p.getAbility().getId());
        stmt.setInt(i++, p.getMove1().getId());
        stmt.setInt(i++, p.getMove2().getId());
        stmt.setInt(i++, p.getMove3().getId());
        stmt.setInt(i++, p.getMove4().getId());
        stmt.setInt(i++, p.getHpEv());
        stmt.setInt(i++, p.getAtkEv());
        stmt.setInt(i++, p.getDefEv());
        stmt.setInt(i++, p.getSpatkEv());
        stmt.setInt(i++, p.getSpdefEv());
        stmt.setInt(i++, p.getSpdEv());
        stmt.setInt(i++, p.getHpIv());
        stmt.setInt(i++, p.getAtkIv());
        stmt.setInt(i++, p.getDefIv());
        stmt.setInt(i++, p.getSpatkIv());
        stmt.setInt(i++, p.getSpdefIv());
        stmt.setInt(i++, p.getSpdIv());
        stmt.setInt(i++, p.getHp());
        stmt.setInt(i++, p.getAtk());
        stmt.setInt(i++, p.getDef());
        stmt.setInt(i++, p.getSpatk());
        stmt.setInt(i++, p.getSpdef());
        stmt.setInt(i++, p.getSpd());
        stmt.setInt(i++, p.getNature().getId());

        // Execute update
        stmt.executeUpdate();
        return true;
    } catch (SQLException e) {
        System.out.println("Error Insert Pokepaste SQL:" + e.getMessage());
        return false;
    } catch (Exception e) {
        System.out.println("Error Insert Pokepaste:" + e.getMessage());
        return false;
    }
}

    /**
     * Este método permite hacer un select en la tabla pokemon_paste que permite
     * devolver toda la información de un Pokepaste en un formato representativo
     * (devuelve nombres de los atributos en lugar de claves primarias.)
     * @param connection la conexión a la base de datos de Oracle.
     * @return un ArrayList con la información de un Pokepaste en formato representativo.
     */
    public ArrayList<PokemonPasteInfo> listPokepaste(Connection connection) {
        ArrayList<PokemonPasteInfo> listaP = new ArrayList<PokemonPasteInfo>();
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM pokemon_pokepaste.V_GET_POKEPASTE");
                ResultSet rs = stmt.executeQuery()) {  
            while (rs.next()) {
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
        } catch (SQLException e) {
            System.out.println("Error SQL listar pokepaste:" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error listar pokepaste:" + e.getMessage());
        }
        return listaP;
    }

    /**
     * Este método permite hacer un select en la tabla move que permite obtener 
     * toda la información de un movimiento en formato representativo (con nombres
     * en lugar de claves primarias).
     * @param connection la conexión a la base de datos de Oracle.
     * @return un ArrayList con todos los movimientos y sus atributos en un formato
     * representativo.
     */
    public ArrayList<MoveInfo> listMovesInfo(Connection connection) {
        ArrayList<MoveInfo> listaM = new ArrayList<MoveInfo>();
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM pokemon_pokepaste.V_GET_MOVE_TABLE");
                ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
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
        } catch (SQLException e) {
            System.out.println("Error SQL listar movimientos:" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error listar movimientos:" + e.getMessage());
        }
        return listaM;
    }

}