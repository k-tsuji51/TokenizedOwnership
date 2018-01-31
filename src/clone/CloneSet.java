package clone;

import java.util.ArrayList;

public class CloneSet{

	int cloneSetID;

	public ArrayList<CloneFileWithRename> cloneFiles = new ArrayList<CloneFileWithRename>();

	CloneSet(int csID){
		cloneSetID = csID;
	}

	public void addFile(String fileName){
		cloneFiles.add(new CloneFileWithRename(fileName));
	}

	public void showCloneFiles(){
		System.out.println(cloneSetID);
		for(CloneFileWithRename cloneFile: cloneFiles){
			cloneFile.showRenameFiles();
		}
	}

	public int getCloneFileNum(){
		return cloneFiles.size();
	}
}
