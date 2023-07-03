package com.example.libraryJSP.DAO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CommentsDAO {
    public JSONArray getListCommentById (int idProduct){
        JSONArray response = new JSONArray();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = DBUntil.openConnection();
            pstmt = con.prepareStatement("select c.id,c.idCustomer, c.content,c.userName,ac.name from comments c,account ac  where idProduct = ? and c.idCustomer = ac.id ");
            pstmt.setInt(1,idProduct);
            rs = pstmt.executeQuery();
            while(rs.next()){
                JSONObject item = new JSONObject();
                item.put("id",rs.getInt("id"));
                item.put("idCustomer",rs.getInt("idCustomer"));
                item.put("content",rs.getString("content"));
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

    public boolean insertComment(int idProduct,int idCustomer,String comment, String userName){
        boolean checkInsert = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = DBUntil.openConnection();
            pstmt = con.prepareStatement("insert into comments(idProduct,idCustomer,content,userName) values (?,?,?,?)");
            pstmt.setInt(1,idProduct);
            pstmt.setInt(2,idCustomer);
            pstmt.setString(3,comment);
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
    public boolean deleteComment(int id){
        boolean checkDelete = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = DBUntil.openConnection();
            pstmt = con.prepareStatement("delete FROM comments where id = ? ");
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
}
