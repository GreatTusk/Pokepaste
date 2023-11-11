/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import bd.Conexion;
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import modelo.Ability;
import modelo.Gender;
import modelo.Item;
import modelo.Move;
import modelo.MoveCat;
import modelo.Nature;
import modelo.Pokemon;
import modelo.PokemonPaste;
import modelo.PokemonType;

/**
 *
 * @author F776
 */
public class PoblarTablas {
    
    public ArrayList<Pokemon> getPokemon() {
        ArrayList<Pokemon> listaP = new ArrayList<Pokemon>();
        try {
            Conexion connection = new Conexion();
            Connection ctn = connection.getConnection();
            //Query
            String query = "SELECT * FROM POKEMON ORDER BY id;";
            PreparedStatement stmt = ctn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
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
            stmt.close();
            ctn.close();
        } catch (SQLException e) {
            System.out.println("Error SQL listar pokemon:" + e.getMessage());
            
        } catch (Exception e) {
            System.out.println("Error listar pokemon:" + e.getMessage());
        }
        return listaP;
    }
    
    public String getObjectName(ResultSet rs, String table_name){
        String name = null;
        try {
            Conexion connection = new Conexion();
            Connection ctn = connection.getConnection();
            //Query
            String query = "SELECT name FROM "+ table_name +" WHERE id =" + rs.getInt("id") +";";
            PreparedStatement stmt = ctn.prepareStatement(query);
            ResultSet rsName = stmt.executeQuery();
            if (rsName.next()) {
                name = rsName.getString("name");
            }
            stmt.close();
            ctn.close();
            return name;
            
        } catch (SQLException e) {
            System.out.println("Error SQL listar nombre objeto:" + e.getMessage());
            return null;
            
        } catch (Exception e) {
            System.out.println("Error listar nombre objeto:" + e.getMessage());
            return null;
        }
    }
    
    public ArrayList<Move> getMoves() {
        ArrayList<Move> listaM = new ArrayList<Move>();
        try {
            Conexion connection = new Conexion();
            Connection ctn = connection.getConnection();
            //Query
            String query = "SELECT * FROM move ORDER BY name;";
            PreparedStatement stmt = ctn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Move m = new Move();
                m.setId(rs.getInt("id"));
                m.setName(rs.getString("name"));
                m.setIdType(new PokemonType(rs.getInt("id_type"), getObjectName(rs, "pokemon_type")));
                m.setIdMoveCat(new MoveCat(rs.getInt("id_move_cat"), getObjectName(rs,"move_cat")));
                m.setPower(rs.getInt("power"));
                m.setAccuracy(rs.getInt("accuracy"));
                m.setPp(rs.getInt("pp"));
                m.setEffect(rs.getString("effect"));
                m.setEffectProb(rs.getInt("effect_prob"));
                listaM.add(m);
            }
            stmt.close();
            ctn.close();
        } catch (SQLException e) {
            System.out.println("Error SQL listar movimientos:" + e.getMessage());
            
        } catch (Exception e) {
            System.out.println("Error listar movimientos:" + e.getMessage());
        }
        return listaM;
    }
    
    public ArrayList<Item> getItems() {
        ArrayList<Item> listaI = new ArrayList<Item>();
        try {
            Conexion connection = new Conexion();
            Connection ctn = connection.getConnection();
            //Query
            String query = "SELECT * FROM item ORDER BY name;";
            PreparedStatement stmt = ctn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Item i = new Item();
                i.setId(rs.getInt("id"));
                i.setName(rs.getString("name"));
                i.setDescription(rs.getString("description"));
                listaI.add(i);
            }
            stmt.close();
            ctn.close();
        } catch (SQLException e) {
            System.out.println("Error SQL listar item:" + e.getMessage());
            
        } catch (Exception e) {
            System.out.println("Error listar item:" + e.getMessage());
        }
        return listaI;
    }
    
    public ArrayList<Ability> getAbilities() {
        ArrayList<Ability> listaA = new ArrayList<Ability>();
        try {
            Conexion connection = new Conexion();
            Connection ctn = connection.getConnection();
            //Query
            String query = "SELECT * FROM ability ORDER BY name;";
            PreparedStatement stmt = ctn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Ability a = new Ability();
                a.setId(rs.getInt("id"));
                a.setName(rs.getString("name"));
                a.setDescription(rs.getString("description"));
                listaA.add(a);
            }
            stmt.close();
            ctn.close();
        } catch (SQLException e) {
            System.out.println("Error SQL listar habilidad:" + e.getMessage());
            
        } catch (Exception e) {
            System.out.println("Error listar habilidad:" + e.getMessage());
        }
        return listaA;
    }
    
    public ArrayList<PokemonType> getPokemonType() {
        ArrayList<PokemonType> listaPT = new ArrayList<PokemonType>();
        try {
            Conexion connection = new Conexion();
            Connection ctn = connection.getConnection();
            //Query
            String query = "SELECT * FROM pokemon_type ORDER BY name;";
            PreparedStatement stmt = ctn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                PokemonType pt = new PokemonType();
                pt.setId(rs.getInt("id"));
                pt.setName(rs.getString("name"));
                listaPT.add(pt);
            }
            stmt.close();
            ctn.close();
        } catch (SQLException e) {
            System.out.println("Error SQL listar tipo:" + e.getMessage());
            
        } catch (Exception e) {
            System.out.println("Error listar tipo:" + e.getMessage());
        }
        return listaPT;
    }
    
    public ArrayList<Nature> getNature() {
        ArrayList<Nature> listaN = new ArrayList<Nature>();
        try {
            Conexion connection = new Conexion();
            Connection ctn = connection.getConnection();
            //Query
            String query = "SELECT * FROM nature ORDER BY id;";
            PreparedStatement stmt = ctn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Nature n = new Nature();
                n.setId(rs.getInt("id"));
                n.setName(rs.getString("name"));
                listaN.add(n);
            }
            stmt.close();
            ctn.close();
        } catch (SQLException e) {
            System.out.println("Error SQL listar naturaleza:" + e.getMessage());
            
        } catch (Exception e) {
            System.out.println("Error listar naturaleza:" + e.getMessage());
        }
        return listaN;
    }
    
   
}   
