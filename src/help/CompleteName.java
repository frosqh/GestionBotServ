package help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public abstract class CompleteName {
	public static String completeName(HashMap<String,ArrayList<String>> M, String s){
		int i = 0;
		//System.out.println(s);
		for (ArrayList<String> s1: M.values()){
			for (String s2 : s1){
				if (s2.equals(s)){
					Set<String> l = M.keySet();
					String message = (String) l.toArray()[i];
					message += "-" + s;
					return message;
				}
			}
			i++;
		}
		return null;
	}
}
