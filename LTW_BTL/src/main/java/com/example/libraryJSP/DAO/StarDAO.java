package com.example.libraryJSP.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

public class StarDAO {
	 public boolean insertStar(int star,int idProduct,int idCustomer,String userName){
	        boolean checkInsert = false;
	        Connection con = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        try{
	            con = DBUntil.openConnection();
	            pstmt = con.prepareStatement("insert into star(star,idProduct,idCustomer,userName) values (?,?,?,?)");
	            pstmt.setInt(1, star);
	            pstmt.setInt(2,idProduct);
	            pstmt.setInt(3,idCustomer);
	            pstmt.setString(4,userName);
	            int a = pstmt.executeUpdate();
	            if(a>0) checkInsert = true;
	        }catch (Exception e){
	            e.printStackTrace();
	        }finally {
	            DBUntil.closeAll(con,pstmt,rs);
	        }
	        return checkInsert;
	    }
	 public JSONArray getListStarById (int idProduct){
	        JSONArray response = new JSONArray();
	        Connection con = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        try{
	            con = DBUntil.openConnection();
	            pstmt = con.prepareStatement("select c.id,c.idCustomer,c.star,c.userName,ac.name from star c,account ac  where idProduct = ? and c.idCustomer = ac.id ");
	            pstmt.setInt(1,idProduct);
	            rs = pstmt.executeQuery();
	            while(rs.next()){
	                JSONObject item = new JSONObject();
	                item.put("id",rs.getInt("id"));
	                item.put("idCustomer",rs.getInt("idCustomer"));
	                item.put("star",rs.getInt("star"));
	                item.put("name",rs.getString("name"));
	                item.put("userName",rs.getString("userName"));
	                response.put(item);
	            }
	        }catch (Exception e){
	            e.printStackTrace();
	        }finally {
	            DBUntil.closeAll(con,pstmt,rs);
	        }
	        return response;
	    }
	 public JSONObject checkCustomer(int idProduct,int idCustomer ){
	        JSONObject response = new JSONObject();
	        Connection con = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        try{
	            con = DBUntil.openConnection();
	            pstmt = con.prepareStatement("select * from star where idProduct = ? and idCustomer = ?");
	            pstmt.setInt(1,idProduct);
	            pstmt.setInt(2,idCustomer);
	            rs = pstmt.executeQuery();
	            if(rs.next()){
	                response.put("id",rs.getInt("id"));
	                response.put("idProduct",rs.getInt("idProduct"));
	                response.put("idCustomer",rs.getInt("idCustomer"));
	            }
	        }catch (Exception e){
	            e.printStackTrace();
	        }finally {
	            DBUntil.closeAll(con,pstmt,rs);
	        }
	        return response;
	    }
	 public JSONArray getListStar(int idProduct){
	        JSONArray response = new JSONArray();
	        Connection con = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        try{
	            con = DBUntil.openConnection();
	            pstmt = con.prepareStatement("select * from star where idProduct = ?");
	            pstmt.setInt(1,idProduct);
	            rs = pstmt.executeQuery();
	            while (rs.next()){
	                JSONObject item = new JSONObject();
	                item.put("star",rs.getInt("star"));
	                response.put(item);
	            }
	        }catch (Exception e){
	            e.printStackTrace();
	        }finally {
	            DBUntil.closeAll(con,pstmt,rs);
	        }
	        return response;
	    }
}
