package parser;

import java.util.ArrayList;

import clone.CloneFileWithRename;
import clone.CloneSet;
import clone.CloneSetManager;

/* git log -p ... で得られたログファイルの集合を扱うクラス */

public class TokenEditLogFileManager {

	public ArrayList<String> logFileIDList = new ArrayList<String>();


	/*クローンのログファイルは，gitLogP/クローンセットID/クローンファイルID/csID-cfID-renameID.txtという名前で記録している    */

	public TokenEditLogFileManager(CloneSetManager csm){
		logFileIDList = makeLogFileName(csm);
	}



	public ArrayList<String> makeLogFileName(CloneSetManager csm){
		ArrayList<String> fileNameList = new ArrayList<String>();
		int csID = 0;
		int cfID = 0;
		int renameID = 0;
	    for(CloneSet cs : csm.getCloneSetList()){
	    	csID++;
	    	for(CloneFileWithRename cf : cs.cloneFiles){
	    		cfID++;
	    		for(String rfl : cf.getRenameFiles()){
	    			renameID++;
	    			String fileID = "\\\\" + csID + "\\\\" + cfID + "\\\\" + csID + "-"+cfID+"-"+renameID+".txt";
	    			System.out.println(fileID);
	    			fileNameList.add(fileID);
//	    			fileNameList.add(csID + "-"+cfID+"-"+renameID+".txt");
	    		}
	    		renameID=0;
	    	}
	    	cfID=0;
	    }
		return fileNameList;
	}


	public String getcsID(String logFileID){
		return logFileID.substring(0, 0);
	}
	public String getcfID(String logFileID){
		return logFileID.substring(2,2);

	}
}
