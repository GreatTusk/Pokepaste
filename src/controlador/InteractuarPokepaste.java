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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import modelo.Ability;
import modelo.Gender;
import modelo.Item;
import modelo.Move;
import modelo.MoveInfo;
import modelo.Nature;
import modelo.Pokemon;
import modelo.PokemonPaste;
import modelo.PokemonPasteInfo;
import modelo.PokemonType;

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
    /**
     * Este método permite actualizar el registro seleccionado en la tabla tblPokepaste.
     * La sentencia update permite actualizar solamente el Pokémon previamente seleccionado.
     * @param connection la conexión con la base de datos
     * @param p el objeto Pokepaste desde el que se obtienen los atributos
     * @param id la clave única de un registro en la tabla pokemon_paste de la base
     * de datos.
     * @return true si se ejecuta con éxito, false si no
     */
    public boolean updatePokepaste(Connection connection, PokemonPaste p, int id) {
        String query =
                "UPDATE syn_pkmn_paste\n" +
                "SET\n" +
                "  id_pokemon = ?,\n" +
                "  id_item = ?,\n" +
                "  nickname = ?,\n" +
                "  \"level\" = ?,\n" +
                "  id_gender = ?,\n" +
                "  is_shiny = ?,\n" +
                "  id_tera_type = ?,\n" +
                "  id_ability = ?,\n" +
                "  move1 = ?,\n" +
                "  move2 = ?,\n" +
                "  move3 = ?,\n" +
                "  move4 = ?,\n" +
                "  hp_ev = ?,\n" +
                "  atk_ev = ?,\n" +
                "  def_ev = ?,\n" +
                "  spatk_ev = ?,\n" +
                "  spdef_ev = ?,\n" +
                "  spd_ev = ?,\n" +
                "  hp_iv = ?,\n" +
                "  atk_iv = ?,\n" +
                "  def_iv = ?,\n" +
                "  spatk_iv = ?,\n" +
                "  spdef_iv = ?,\n" +
                "  spd_iv = ?,\n" +
                "  hp = ?,\n" +
                "  atk = ?,\n" +
                "  \"def\" = ?,\n" +
                "  spatk = ?,\n" +
                "  spdef = ?,\n" +
                "  spd = ?,\n" +
                "  id_nature = ?\n" +
                "WHERE id = " + id 
                ;
        try (PreparedStatement stmt = connection.prepareStatement(
            query
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
        System.out.println("Error Update Pokepaste SQL:" + e.getMessage());
        return false;
    } catch (Exception e) {
        System.out.println("Error Update Pokepaste:" + e.getMessage());
        return false;
    }
    }
    
    /**
     * Este método permite guardar en un Map la combinación de id y nombre de una
     * tabla cualquiera. Se usa para acelerar el almacenamiento de consultas.
     * @param connection la conexión a la base de datos
     * @param table_name el nombre de la tabla de la que se sacará la combinación de id y nombre
     * @param id el id del objeto del que se le quiere sacar el nombre. Solo se retornará una fila.
     * @return un Map con la combinación de id y nombre. La clave es el id y el value el nombre.
     * @throws SQLException la consulta podría generar un error del código SQL
     */
    private Map<Integer, String> getObjectNameMap(Connection connection, String table_name, int id) throws SQLException {
        String query = "SELECT id, name FROM " + table_name + " WHERE id = " + id;
        Map<Integer, String> objectNameMap = new HashMap<>();

        try (
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                objectNameMap.put(rs.getInt("id"), rs.getString("name"));
            }
        }

        return objectNameMap;
    }
    
    /**
     * Este método permite armar un objeto PokemonPaste a base un registro de la tabla
     * pokemon_paste en la base de datos. A partir de este objeto, se puede generar un
     * pokepaste e importarlo en los componentes Swing del programa.
     * @param connection la conexión con la base de datos
     * @param id el id del registro a partir del que crear el PokemonPaste
     * @return un PokemonPaste
     */
    public PokemonPaste getPokepasteAtId(Connection connection, int id) {
        
        int pokemonId;
        int itemId;
        int genderId;
        int typeId;
        int abilityId;
        int moveId1, moveId2, moveId3, moveId4;
        int natureId;
        
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT \n" +
                "    ID_POKEMON,\n" +
                "    ID_ITEM,\n" +
                "    NICKNAME,\n" +
                "    \"level\",\n" +
                "    ID_GENDER,\n" +
                "    IS_SHINY,\n" +
                "    ID_TERA_TYPE,\n" +
                "    ID_ABILITY,\n" +
                "    MOVE1,\n" +
                "    MOVE2,\n" +
                "    MOVE3,\n" +
                "    MOVE4,\n" +
                "    HP_EV,\n" +
                "    ATK_EV,\n" +
                "    DEF_EV,\n" +
                "    SPATK_EV,\n" +
                "    SPDEF_EV,\n" +
                "    SPD_EV,\n" +
                "    HP_IV,\n" +
                "    ATK_IV,\n" +
                "    DEF_IV,\n" +
                "    SPATK_IV,\n" +
                "    SPDEF_IV,\n" +
                "    SPD_IV,\n" +
                "    HP,\n" +
                "    ATK,\n" +
                "    \"def\",\n" +
                "    SPATK,\n" +
                "    SPDEF,\n" +
                "    SPD,\n" +
                "    ID_NATURE\n" +
                "FROM \n" +
                "    syn_pkmn_paste"
                        + " WHERE id = " + id);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                // extracción ids de la tabla
                pokemonId = rs.getInt("ID_POKEMON");
                itemId = rs.getInt("id_item");
                genderId = rs.getInt("id_gender");
                typeId = rs.getInt("id_tera_type");
                abilityId = rs.getInt("id_ability");
                moveId1 = rs.getInt("move1");
                moveId2 = rs.getInt("move2");
                moveId3 = rs.getInt("move3");
                moveId4 = rs.getInt("move4");
                natureId = rs.getInt("id_nature");
                // creación de mapas para sacar nombres
                Map<Integer, String> pokemon = getObjectNameMap(connection, "syn_pkmn", pokemonId);
                Map<Integer, String> item = getObjectNameMap(connection, "syn_itm", itemId);
                Map<Integer, String> gender = getObjectNameMap(connection, "syn_gdr", genderId);
                Map<Integer, String> type = getObjectNameMap(connection, "syn_type", typeId);
                Map<Integer, String> ability = getObjectNameMap(connection, "syn_abi", abilityId);
                Map<Integer, String> move1 = getObjectNameMap(connection, "syn_move", moveId1);
                Map<Integer, String> move2 = getObjectNameMap(connection, "syn_move", moveId2);
                Map<Integer, String> move3 = getObjectNameMap(connection, "syn_move", moveId3);    
                Map<Integer, String> move4 = getObjectNameMap(connection, "syn_move", moveId4);    
                Map<Integer, String> nature = getObjectNameMap(connection, "syn_nat", natureId);    

                PokemonPaste p = new PokemonPaste();

                p.setPokemon(new Pokemon(pokemonId, pokemon.get(pokemonId)));
                p.setItem(new Item(itemId, item.get(itemId)));
                p.setNickname(rs.getString("nickname"));
                p.setLevel(rs.getInt("level"));
                p.setGender(new Gender(genderId, gender.get(genderId)));
                p.setIsShiny(rs.getBoolean("is_shiny"));
                p.setTeraType(new PokemonType(typeId, type.get(typeId)));
                p.setAbility(new Ability(abilityId, ability.get(abilityId)));
                p.setMove1(new Move(moveId1, move1.get(moveId1)));
                p.setMove2(new Move(moveId2, move2.get(moveId2)));
                p.setMove3(new Move(moveId3, move3.get(moveId3)));
                p.setMove4(new Move(moveId4, move4.get(moveId4)));
                p.setHpEv(rs.getInt("hp_ev"));
                p.setAtkEv(rs.getInt("atk_ev"));
                p.setDefEv(rs.getInt("def_ev"));
                p.setSpatkEv(rs.getInt("spatk_ev"));
                p.setSpdefEv(rs.getInt("spdef_ev"));
                p.setSpdEv(rs.getInt("spd_ev"));
                p.setHpIv(rs.getInt("hp_Iv"));
                p.setAtkIv(rs.getInt("atk_Iv"));
                p.setDefIv(rs.getInt("def_Iv"));
                p.setSpatkIv(rs.getInt("spatk_Iv"));
                p.setSpdefIv(rs.getInt("spdef_Iv"));
                p.setSpdIv(rs.getInt("spd_Iv"));
                p.setHp(rs.getInt("hp"));
                p.setAtk(rs.getInt("atk"));
                p.setDef(rs.getInt("def"));
                p.setSpatk(rs.getInt("spatk"));
                p.setSpdef(rs.getInt("spdef"));
                p.setSpd(rs.getInt("spd"));
                p.setNature(new Nature(natureId, nature.get(natureId)));
                return p;
            }
        } catch (SQLException e) {
            System.out.println("Error SQL creación objeto pokepaste:" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error creación objeto pokepaste:" + e.getMessage());
        }
        return null;
    }

}