package tokenizedOwnership;

import clone.CloneSetManager;
import parser.InputLogParser;
import parser.TokenEditLogFileManager;
import parser.TokenEditNumberByFile;

public class TokenizedOwnership {

	static final String cloneListFileName = util.StringUtil.CLONESET_FILELIST;
	static TokenEditLogFileManager logFiles;

	public static TokenEditNumberByFile tokenEditNumber;

	static InputLogParser parser = new InputLogParser();

	public static void main(String[] args){
		CloneSetManager csm = new CloneSetManager(cloneListFileName);
		csm.printCloneSetListToTextFile();

		logFiles = new TokenEditLogFileManager(csm);

		for(String logFileID : logFiles.logFileIDList){
			TokenEditNumberByFile token = new TokenEditNumberByFile();
			String fileName = util.StringUtil.DIRECTORY_LOGFILES + "\\\\" + logFiles.getcsID(logFileID) + "\\\\" + logFiles.getcfID(logFileID) + "\\\\" +  logFileID.replaceAll("/", "\\\\");
			token = parser.testEditCount(fileName);

		}

	}


}
