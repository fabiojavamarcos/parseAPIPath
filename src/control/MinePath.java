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
	private String tableName = null;
	private String project = "";
	private String language =  "";
	private ArrayList <String> projects = new ArrayList(); 
	
	public MinePath (String [] args) {
		user = args[0];
		pswd = args[1];
		db = args[2];
		tableName = args[3];
		language = args[4];
		
		for (int x = 5; x < args.length; x++) {
			projects.add(args[x]);
			
		}
		
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
		
		int h = apiName.lastIndexOf(".java");
		String extension = "";
		if (h == apiName.length()-5) { // 5 positions before the length means this is an extension
			//apiName = apiName.replaceAll(".java",  "");
			apiName = replaceLast(apiName,".java","");
			extension = ".java";
		}
		h = apiName.lastIndexOf(".cpp");
		if (h == apiName.length()-4) { // 4 positions before the length means this is an extension
			//apiName = apiName.replaceAll(".cpp",  "");
			apiName = replaceLast(apiName,".cpp","");
			extension = ".cpp";
		}
		h = apiName.lastIndexOf(".cs");
		if (h == apiName.length()-3) { // 3 positions before the length means this is an extension
			//apiName = apiName.replaceAll(".cs",  "");
			apiName = replaceLast(apiName,".cpp","");
			extension = ".cpp";
		}
		h = apiName.lastIndexOf(".h");
		if (h == apiName.length()-2) {// 2 positions before the length means this is an extension
			//apiName = apiName.replaceAll(".h",  "");
			apiName = replaceLast(apiName,".h","");
			extension = ".h";
		}
		
		String apiNamePath = apiName.replace(".", "/");
		Path path = Paths.get(apiNamePath);
		
		if (path.getNameCount()>0) {
			System.out.println("count:"+path);
		}
		String general = null;
		String specific = null;
		String aux = null;
		
		Path classNamePath= path.getFileName();
		String className = classNamePath.toString();
		
		for (Path p: path) {
			String name = p.toString();
		    System.out.println("name:"+ name);
		    boolean found = false;
		    
		    if (name.equals("presto")) {
		    	System.out.println("Debug");
		    }
		    
			String[] locales = Locale.getISOCountries();

			for (String countryCode : locales) {

				Locale obj = new Locale("", countryCode);
				if (name.equals(obj.getCountry())) {
					found = true;
					System.out.println("Skip Country Code = " + obj.getCountry()
					+ ", Country Name = " + obj.getDisplayCountry());
					break;
				}
			}
			if (!found) {
				
				boolean foundProj = false;
				for (int j = 0; j<projects.size(); j++) {
					project = projects.get(j);
					if (name.equals(project)) {
						foundProj = true;
				    	System.out.println("prject name found! Skip:"+name);

						break;

					}
					
				}
				if (!foundProj) {
		    
				    if (name.equals("com")||name.equals("java")||name.equals("org")|| name.equals("sun")||name.equals(className)||name.equals("facebook")||name.equals("microsoft")||name.equals("amazonaws")||name.equals("google")||name.equals("apache")) {
				    	
				    	System.out.println("skip:"+name);
				    	aux = name;
				    } else {
				    	if (general==null) {
				    		general = name.toString();
					    	System.out.println("general: "+name);
	
				    	} else {
				    		if (specific==null) {
				    			specific = name.toString();
						    	System.out.println("specific: "+name);
	
				    		} else {
						    	
						    	System.out.println("general and specific have been set:"+name);
	
				    			//general = specific;
				    			//specific = name.toString();
				    		}
				    		
				    	}
				    }
				}
			}
		}
		if (specific==null&&general!=null) {
			specific = general;
		}
		if (general==null) {
	    	System.out.println("using aux:"+aux);

			general = "undefined";
			specific = "undefined";
			if (language.equals("cpp")) {
				general = className;
			}
			//specific = aux;
		}
		if (extension.equals(""))
			insertAPI(apiName, general, specific);
		else 
			insertAPI(apiName+extension, general, specific);

	}
	String replaceLast(String string, String substring, String replacement)
	{
	  int index = string.lastIndexOf(substring);
	  if (index == -1)
	    return string;
	  return string.substring(0, index) + replacement
	          + string.substring(index+substring.length());
	}
	private boolean insertAPI(String apiName, String general, String specific) {
		// TODO Auto-generated method stub
		
		FileDAO fd = FileDAO.getInstancia(db,user,pswd);
		boolean found = fd.insertAPI(apiName, general, specific, tableName);
		if (!found) {
			System.out.println("api: "+apiName+" - "+general+" - "+specific+"NOT inserted in database!!!");
		} else {
			System.out.println("api: "+apiName+" - "+general+" - "+specific+" inserted in database!!!");
		}
		return found;
	}


}
