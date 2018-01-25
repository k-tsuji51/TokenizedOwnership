package clone;

import java.util.ArrayList;

public class CloneFileWithRename {/* リネームの情報を含んだクローンファイルの情報 */

	String fileName = "";

	ArrayList<String> renames = new ArrayList<String>();

	CloneFileWithRename(String fileName){
		this.fileName = fileName;
		renames.add(fileName);
	}


	public void addRename(String newRename){
		renames.add(newRename);
	}

	ArrayList<String> getRenameFiles(){
		return renames;
	}

	public void showRenameFiles(){
		for(String rename: renames){
			System.out.println(rename);
		}
		System.out.println();
	}

}
