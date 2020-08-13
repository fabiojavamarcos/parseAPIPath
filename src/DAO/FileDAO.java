package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.API;
import util.DBUtil;

public class FileDAO {
	private static FileDAO instancia;
	private String dbcon;
	private String user;
	private String pswd;
	
	private FileDAO(String dbcon, String user, String pswd){
		this.dbcon = dbcon;
		this.user = user;
		this.pswd = pswd;
	}
	
	public static FileDAO getInstancia(String dbcon, String user, String pswd){
		if (instancia == null){
			instancia = new FileDAO(dbcon, user, pswd);
		}
		return instancia;
	}
	

	public boolean insertAPI(String apiName, String general, String specific) {
		// TODO Auto-generated method stub
		Connection con = DBUtil.getConnection(dbcon, user, pswd);
		int count = 0;
		
		try {
			Statement comandoSql = con.createStatement();
			
			String sql = "insert into \"API_specific\" values ('" + general + "' , '" + specific + "' , '" + apiName + "')";  

			System.out.println(sql);
			
			count = comandoSql.executeUpdate(sql);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if (count >0)
			return true;
		else
			return false;
	}
	
	public ArrayList buscaAPIs(){
		Connection con = DBUtil.getConnection(dbcon, user, pswd);
		boolean achou = false;
		ArrayList <API> apis = new ArrayList();
		try {
			Statement comandoSql = con.createStatement();
			
			String sql = "select * from \"API\" ";
			System.out.println(sql);
			
			ResultSet rs = comandoSql.executeQuery(sql);
			
			
					
			while(rs.next()){
				API api = new API();
				String apiName = rs.getString(1);
				String className = rs.getString(2);
				
				api.setApiName(apiName);
				api.setClassName(className);
				apis.add(api);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return apis;
		
	}
	
}

