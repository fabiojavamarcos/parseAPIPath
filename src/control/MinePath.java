package control;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Locale;

import DAO.FileDAO;
import model.API;

public class MinePath {
	private String user = "";
	private String pswd = "";
	private String db = "";
	private String project = "";
	
	public MinePath (String [] args) {
		user = args[0];
		pswd = args[1];
		project = args[2];
		db = args[3];

		readData();
		
	}

	private void readData() {
		// TODO Auto-generated method stub
		FileDAO fd = FileDAO.getInstancia(db,user,pswd);
		
		ArrayList <API> apis = fd.buscaAPIs();
		
		for(API item : apis) {
		    
			String apiName = item.getApiName();
			
			processPath(apiName);
		} 
	}
	

	private void processPath(String apiName) {
		// TODO Auto-generated method stub
		String apiNamePath = apiName.replace(".", "/");
		Path path = Paths.get(apiNamePath);
		
		if (path.getNameCount()>4) {
			System.out.println("count:"+path);
		}
		String general = null;
		String specific = null;
		Path classNamePath= path.getFileName();
		String className = classNamePath.toString();
		
		for (Path p: path) {
			String name = p.toString();
		    System.out.println(name);
		    boolean found = false;
		    
			String[] locales = Locale.getISOCountries();

			for (String countryCode : locales) {

				Locale obj = new Locale("", countryCode);
				if (name.equals(obj.getCountry())) {
					found = true;
					System.out.println("Country Code = " + obj.getCountry()
					+ ", Country Name = " + obj.getDisplayCountry());
					break;
				}
			}
			if (!found) {
		    
			    if (name.equals("com")||name.equals(project)||name.equals("java")||name.equals("org")|| name.equals("sun")||name.equals(className)) {
			    	
			    	System.out.println("skip:"+name);
			    } else {
			    	if (general==null) {
			    		general = name.toString();
			    	} else {
			    		if (specific==null) {
			    			specific = name.toString();
			    		} else {
					    	System.out.println("skip:"+name);

			    			//general = specific;
			    			//specific = name.toString();
			    		}
			    		
			    	}
			    }
			}
		}
		if (specific==null&&general!=null) {
			specific = general;
		}
		if (general==null) {
			general = "undefined";
			specific = "undefined";
		}
		insertAPI(apiName, general, specific);
	}

	private boolean insertAPI(String apiName, String general, String specific) {
		// TODO Auto-generated method stub
		
		FileDAO fd = FileDAO.getInstancia(db,user,pswd);
		boolean found = fd.insertAPI(apiName, general, specific);
		if (!found) {
			System.out.println("api: "+apiName+" - "+general+" - "+specific+"NOT inserted in database!!!");
		} else {
			System.out.println("api: "+apiName+" - "+general+" - "+specific+" inserted in database!!!");
		}
		return found;
	}


}
