package clone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
	String outputFileName = util.StringUtil.RENAMED_CLONESET_OUTPUT;


	ArrayList<CloneSet> cloneSetList = new ArrayList<CloneSet>();

	public ArrayList<CloneSet> getCloneSetList() {
		return cloneSetList;
	}


	public void setCloneSetList(ArrayList<CloneSet> cloneSetList) {
		this.cloneSetList = cloneSetList;
	}


	public CloneSetManager(String cloneFileList) {
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

	public void printCloneSetListToTextFile(){
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(outputFileName)));
			for(CloneSet cs : cloneSetList){
				pw.println("#CloneSet" + cs.cloneSetID);
				for(CloneFileWithRename cloneRename : cs.cloneFiles){
					for(String fileName : cloneRename.renames){
						pw.println(fileName);
					}
					pw.println();
				}
			}

			pw.close();
		} catch (IOException e) {
			logger.info(e.getMessage());
		}


	}



}
