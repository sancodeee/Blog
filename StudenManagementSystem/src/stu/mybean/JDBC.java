package stu.mybean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class JDBC {  //���ݿ�������
	public static Connection getConnection(){
		String url="jdbc:mysql://localhost:3306/test";
		String username="root";
		String pwd="root";
		
		
		Connection conn=null;
		try{
			conn=DriverManager.getConnection(url, username, pwd);//�����ݿ⽨������
			System.out.println("link ok!");
			
		}catch(SQLException e){
			e.printStackTrace();
			
		}
		return conn;
	}
}
	
