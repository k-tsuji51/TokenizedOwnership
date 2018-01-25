package clone;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/*ファイルからクローンセットの読み出しを行う*/
public class CloneSetManager {

	/*入力するクローンセットが記述されたファイル名と，中にあるファイルリストのファイル名を先頭から何文字削ぐかを指定する*/
	/*wildfly:47*/
	static final String cloneListFileName = util.StringUtil.CLONESET_FILELIST;
	static final int removeHead = 47;

	static final String renameFileList = util.StringUtil.REVERSED_RENAMELIST;

	static Logger logger = Logger.getLogger("loggerName");

	ArrayList<CloneSet> cloneSetList = new ArrayList<CloneSet>();

	CloneSetManager(String cloneFileList) {
		loadCloneList(cloneFileList);

		RenameFileList renameList = new RenameFileList(renameFileList);
		String origFileName;

		for(CloneSet cs : cloneSetList){
			for(CloneFileWithRename cloneRename :cs.cloneFiles){
				origFileName = cloneRename.fileName;
				if(renameList.renameListSearchByFileName(origFileName) != null){
					cloneRename.renames = renameList.renameListSearchByFileName(origFileName);
				}
			}
		}

		this.showCloneSet();
	}


	public static void main(String[] args){
		CloneSetManager csm = new CloneSetManager(cloneListFileName);
	}

	private void loadCloneList(String cloneListFileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(cloneListFileName));
			// PrintWriter pw = new PrintWriter(new BufferedWriter(new
			// FileWriter(outputFile)));
			String str;

			CloneSet cs = null;
			int csID = 0;

			Handler handler = new FileHandler("sample.log");
			logger.addHandler(handler);
			Formatter formatter = new SimpleFormatter();
			handler.setFormatter(formatter);


			while ((str = br.readLine()) != null) {
				if (str.matches("#begin\\{set\\}")) {
					csID++;
					cs = new CloneSet(csID);
				} else if (str.matches("#end\\{set\\}")) {
					cloneSetList.add(cs);
				} else{
					cs.addFile(str.replace('\\', '/').substring(removeHead));
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			logger.log(Level.INFO,e.getMessage());
		} catch (IOException e) {
			logger.log(Level.INFO,e.getMessage());
		}
	}

	public void showCloneSet(){
		for(CloneSet cs:cloneSetList){
			cs.showCloneFiles();
		}
	}

}