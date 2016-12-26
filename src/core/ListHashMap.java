package core;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class ListHashMap {
	public static ArrayList<String> recupList(String s){
		String[] sl2 = s.split("\n");
		ArrayList<String> l = new ArrayList<String>();
		for (String s2:sl2){
			l.add(s2);
		}
		return l;
	}
	
	public static HashMap<String,ArrayList<String>> ListToHash(ArrayList<String> l){
		HashMap<String,ArrayList<String>> M = new HashMap<String,ArrayList<String>>();
		for (String s:l){
			String[] k = s.split("-");
			if (M.containsKey(k[0])){
				M.get(k[0]).add(k[1]);
			}
			else{
				M.put(k[0],new ArrayList<String>());
				M.get(k[0]).add(k[1]);
			}
		}
		return M;
	}
}

