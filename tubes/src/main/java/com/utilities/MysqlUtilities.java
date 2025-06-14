package com.utilities;

import java.sql.*;

public class MysqlUtilities {
    private static Connection koneksi;
    
    public static Connection getConnection(){
        if (koneksi == null){
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/perpus";
                String user = "root";
                String password = "root";
                koneksi = DriverManager.getConnection(url, user, password);
                if(koneksi !=null){
                    System.out.println("koneksi berhasil");
                }
            }catch(ClassNotFoundException one){
                System.out.println("Gagal load driver : " + one.getMessage());
            }catch(SQLException sqle){
                System.out.println("Gagal Koneksi : " + sqle.getMessage() );
            }
        }
        return koneksi;
    }    
    
}
