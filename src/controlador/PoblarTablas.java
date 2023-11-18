/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import modelo.Ability;
import modelo.Item;
import modelo.Move;
import modelo.MoveCat;
import modelo.Nature;
import modelo.Pokemon;
import modelo.PokemonType;

/**
 *
 * @author F776
 */
public class PoblarTablas  {
    /**
     * Este método permite conseguir la información de todos los Pokémon existen
     * tes en la base de datos. Esta se usa para llenar el JComboBox de especies.
     * @param connection la conexión a la base de datos de Oracle.
     * @return un ArrayList que contiene todos los atributos de cada Pokémon.
     */
    public ArrayList<Pokemon> getPokemon(Connection connection) {
        //Query
        String query = "SELECT * FROM pokemon_pokepaste.V_GET_PKMN";
        ArrayList<Pokemon> listaP = new ArrayList<Pokemon>();
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
            
                while(rs.next()) {
                Pokemon p = new Pokemon();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setHp(rs.getInt("hp"));
                p.setAtk(rs.getInt("atk"));
                p.setDef(rs.getInt("def"));
                p.setSpatk(rs.getInt("spatk"));
                p.setSpdef(rs.getInt("spdef"));
                p.setSpd(rs.getInt("spd"));
                p.setBst(rs.getInt("bst"));
                listaP.add(p);
            }
            
        } catch (SQLException e) {
            System.out.println("Error SQL listar pokemon:" + e.getMessage());
            
        } catch (Exception e) {
            System.out.println("Error listar pokemon:" + e.getMessage());
        }
        return listaP;
    }
    
    /**
     * Este método permite obtener el nombre de un objeto de una tabla por su id.
     * Todas las tablas en el modelo tienen una columna llamada "nombre" y una llamada
     * "id" por lo que se puede aplicar para cualquier tabla.
     * @param connection la conexión a la base de datos de Oracle.
     * @param rs el ResultSet necesario para identificar el id 
     * @param table_name el nombre de la tabla de dónde sacar los datos
     * @return un String que contiene el nombre del objeto
     */
    
    private Map<Integer, String> getObjectNameMap(Connection connection, String table_name) throws SQLException {
        String query = "SELECT id, name FROM " + table_name;
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
     * Este método permite conseguir todos los movimientos de los Pokémon que 
     * están registrados en la base de datos. Se encuentran en la tabla move.
     * @param connection la conexión con la base de datos de Oracle.
     * @return un ArrayList conteniendo todos los movimientos y sus atributos.
     */
    
    public ArrayList<Move> getMoves(Connection connection) {
        String query = "SELECT * FROM pokemon_pokepaste.V_GET_MOVES";
        ArrayList<Move> listaM = new ArrayList<>();

        try (
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()
        ) {
            Map<Integer, String> typeMap = getObjectNameMap(connection, "syn_type");
            Map<Integer, String> moveCatMap = getObjectNameMap(connection, "syn_mvcat");

            while (rs.next()) {
                Move m = new Move();
                m.setId(rs.getInt("id"));
                m.setName(rs.getString("name"));
                m.setIdType(new PokemonType(rs.getInt("id_type"), typeMap.get(rs.getInt("id_type"))));
                m.setIdMoveCat(new MoveCat(rs.getInt("id_move_cat"), moveCatMap.get(rs.getInt("id_move_cat"))));
                m.setPower(rs.getInt("power"));
                m.setAccuracy(rs.getInt("accuracy"));
                m.setPp(rs.getInt("pp"));
                m.setEffect(rs.getString("effect"));
                m.setEffectProb(rs.getInt("effect_prob"));
                listaM.add(m);
            }
        } catch (SQLException e) {
            System.out.println("Error SQL listar movimientos:" + e.getMessage());
        }

        return listaM;
    }
    /**
     * Este método permite conseguir todos los items que están registrados en la
     * tabla item de la base de datos.
     * @param connection la conexión con la base de datos de Oracle.
     * @return un ArrayList conteniendo todos los items y sus atributos.
     */
    
    public ArrayList<Item> getItems(Connection connection) {
        // Query
        String query = "SELECT * FROM pokemon_pokepaste.V_GET_ITEMS";
        ArrayList<Item> listaI = new ArrayList<Item>();

        try (PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            
                while (rs.next()) {
                    Item i = new Item();
                    i.setId(rs.getInt("id"));
                    i.setName(rs.getString("name"));
                    i.setDescription(rs.getString("description"));
                    listaI.add(i);
                }
            
        } catch (SQLException e) {
            System.out.println("Error SQL listar item:" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error listar item:" + e.getMessage());
        }

        return listaI;
    }
    
    /**
     * Este método permite conseguir todas las habilidades almacenadas en la tabla
     * ability de la base de datos.
     * @param connection la conexión con la base de datos de Oracle.
     * @return un ArrayList con todas las habilidades y sus atributos.
     */
    
    public ArrayList<Ability> getAbilities(Connection connection) {
        // Query
        String query = "SELECT * FROM pokemon_pokepaste.V_GET_ABILITIES";
        ArrayList<Ability> listaA = new ArrayList<Ability>();

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
          
                while (rs.next()) {
                    Ability a = new Ability();
                    a.setId(rs.getInt("id"));
                    a.setName(rs.getString("name"));
                    a.setDescription(rs.getString("description"));
                    listaA.add(a);
                }
            
        } catch (SQLException e) {
            System.out.println("Error SQL listar habilidad:" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error listar habilidad:" + e.getMessage());
        }

        return listaA;
    }
    
    /**
     * Este método permite conseguir todos los tipos de Pokémon almacenados
     * en la tabla pokemon_type de la base de datos.
     * @param connection la conexión con la base de datos de Oracle.
     * @return un ArrayList con todos los tipos y su atributos.
     */
    
    public ArrayList<PokemonType> getPokemonType(Connection connection) {
        // Query
        String query = "SELECT * FROM pokemon_pokepaste.V_GET_PKMN_TYPES";
        ArrayList<PokemonType> listaPT = new ArrayList<PokemonType>();

        try (PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
          
                while (rs.next()) {
                    PokemonType pt = new PokemonType();
                    pt.setId(rs.getInt("id"));
                    pt.setName(rs.getString("name"));
                    listaPT.add(pt);
                }
           
        } catch (SQLException e) {
            System.out.println("Error SQL listar tipo:" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error listar tipo:" + e.getMessage());
        }

        return listaPT;
    }
    
    /**
     * Este método permite obtener todas las naturalezas de la tabla nature 
     * de la base de datos.
     * @param connection la conexión a la base de datos de Oracle.
     * @return un ArrayList con todas las naturalezas y sus atributos.
     */

    public ArrayList<Nature> getNature(Connection connection) {
        // Query
        String query = "SELECT * FROM pokemon_pokepaste.V_GET_NATURES";
        ArrayList<Nature> listaN = new ArrayList<Nature>();

        try (PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
     
                while (rs.next()) {
                    Nature n = new Nature();
                    n.setId(rs.getInt("id"));
                    n.setName(rs.getString("name"));
                    listaN.add(n);
                }
            
        } catch (SQLException e) {
            System.out.println("Error SQL listar naturaleza:" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error listar naturaleza:" + e.getMessage());
        }

        return listaN;
    }

    
   
}   
