package com.example.libraryJSP.DAO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CartDAO {
    public JSONArray getListCartByCustomer(int idCustomer){
        JSONArray response = new JSONArray();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = DBUntil.openConnection();
            pstmt = con.prepareStatement("select c.id ,c.idProduct, c.quantity, b.title, b.photo,b.price from cart c, book b where c.idProduct = b.id  and c.idCustomer = ? ");
            pstmt.setInt(1,idCustomer);
            rs = pstmt.executeQuery();
            while (rs.next()){
                JSONObject item = new JSONObject();
                item.put("id",rs.getInt("id"));
                item.put("idProduct",rs.getInt("idProduct"));
                item.put("quantity",rs.getInt("quantity"));
                item.put("title",rs.getString("title"));
                item.put("photo",rs.getString("photo"));
                item.put("price",rs.getDouble("price"));
                response.put(item);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUntil.closeAll(con,pstmt,rs);
        }
        return response;
    }
    public JSONArray getListCartAll(){
        JSONArray response = new JSONArray();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = DBUntil.openConnection();
            pstmt = con.prepareStatement("select c.quantity,b.price,b.title,b.author,d.username from cart c, book b, account d where c.idProduct = b.id and c.idCustomer=d.id");
            rs = pstmt.executeQuery();
            while (rs.next()){
                JSONObject item = new JSONObject();
                item.put("quantity",rs.getInt("quantity"));
                item.put("price",rs.getDouble("price"));
                item.put("username",rs.getString("username"));
                item.put("title",rs.getString("title"));
                item.put("author",rs.getString("author"));
                response.put(item);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUntil.closeAll(con,pstmt,rs);
        }
        return response;
    }
    public boolean insertCart(int quantity, int idCustomer, int idProduct){
        boolean checkInsert = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = DBUntil.openConnection();
            pstmt = con.prepareStatement("insert into cart (quantity,idCustomer,idProduct) values (?,?,?)");
            pstmt.setInt(1,quantity);
            pstmt.setInt(2,idCustomer);
            pstmt.setInt(3,idProduct);
            int a = pstmt.executeUpdate();
            if(a>0) checkInsert = true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUntil.closeAll(con,pstmt,rs);
        }
        return checkInsert;
    }

    public JSONObject checkDuplicateBook(int idCustomer , int idProduct){
        JSONObject response = new JSONObject();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = DBUntil.openConnection();
            pstmt = con.prepareStatement("select * from cart where idCustomer = ? and idProduct = ?");
            pstmt.setInt(1,idCustomer);
            pstmt.setInt(2,idProduct);
            rs = pstmt.executeQuery();
            if(rs.next()){
                response.put("id",rs.getInt("id"));
                response.put("idProduct",rs.getInt("idProduct"));
                response.put("quantity",rs.getInt("quantity"));

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUntil.closeAll(con,pstmt,rs);
        }
        return response;
    }

    public boolean updateCart(int quantity,int numberPro, int id){
        boolean check = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = DBUntil.openConnection();
            pstmt = con.prepareStatement("update cart set quantity = ? where id = ?");
            pstmt.setInt(1,numberPro + quantity);
            pstmt.setInt(2,id);
            int a = pstmt.executeUpdate();
            if(a>0) check = true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUntil.closeAll(con,pstmt,rs);
        }
        return check;
    }

    public boolean deleteCart(int id){
        boolean checkDelete = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = DBUntil.openConnection();
            pstmt = con.prepareStatement("delete FROM cart where id = ? ");
            pstmt.setInt(1,id);
            int a = pstmt.executeUpdate();
            if(a>0) checkDelete = true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUntil.closeAll(con,pstmt,rs);
        }
        return checkDelete;
    }
    public static int getQuantity(int id ){
    	int response=0;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = DBUntil.openConnection();
            pstmt = con.prepareStatement("SELECT SUM(quantity) AS total_quantity FROM cart WHERE idProduct = ?");
            pstmt.setInt(1,id);
            rs = pstmt.executeQuery();
            if(rs.next()){
                response=rs.getInt("total_quantity");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUntil.closeAll(con,pstmt,rs);
        }
        return response;
    }
}
