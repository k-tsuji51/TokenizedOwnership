package tokenizedOwnership;

import clone.CloneSetManager;
import parser.InputLogParser;
import parser.TokenEditLogFileManager;
import parser.TokenEditNumberByFile;

public class TokenizedOwnership {

	static final String cloneListFileName = util.StringUtil.CLONESET_FILELIST;
	static TokenEditLogFileManager logFiles;

//	static CloneSetManager cloneSetManager;
	public static TokenEditNumberByFile tokenEditNumber;


	public static void main(String[] args){
		CloneSetManager csm = new CloneSetManager(cloneListFileName);
		csm.printCloneSetListToTextFile();

		logFiles = new TokenEditLogFileManager(csm);
		InputLogParser parser = new InputLogParser();
//		cloneSetManager = new CloneSetManager(util.StringUtil.CLONESET_FILELIST);

		for(String logFileID : logFiles.logFileIDList){
			System.out.println(logFileID);
			if(isNewCloneSet(logFileID)) System.out.println("new clone set");
			if(isNewFile(logFileID)){
				parser = new InputLogParser();
			}
			String fileName = util.StringUtil.DIRECTORY_LOGFILES + "\\\\" + logFiles.getcsID(logFileID) + "\\\\" + logFiles.getcfID(logFileID) + "\\\\" +  logFileID.replaceAll("/", "\\\\");
			TokenEditNumberByFile token = parser.testEditCount(fileName);
			token.showCommitNumber();
		}

	}



	public static boolean isNewCloneSet(String logFileID){
		return logFileID.matches(".*[0-9]*-1-1.*");
	}

	public static boolean isNewFile(String logFileID){
		return logFileID.matches(".*[0-9]*-[0-9]*-1.*");
	}




	/*データ集計用のメソッドを作る．*/
}
