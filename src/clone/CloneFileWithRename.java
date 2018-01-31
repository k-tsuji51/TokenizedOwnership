package clone;

import java.util.ArrayList;

import parser.TokenEditNumberByFile;

public class CloneFileWithRename {/* リネームの情報を含んだクローンファイルの情報 */

	public String fileName = "";
	public ArrayList<String> renames = new ArrayList<String>();

	/*トークン編集回数を数える変数*/
	TokenEditNumberByFile tokenEditNum;

	public TokenEditNumberByFile getTokenEditNum() {
		return tokenEditNum;
	}


	public void setTokenEditNum(TokenEditNumberByFile tokenEditNum) {
		this.tokenEditNum = tokenEditNum;
	}


	CloneFileWithRename(String fileName){
		this.fileName = fileName;
		renames.add(fileName);
	}


	public void addRename(String newRename){
		renames.add(newRename);
	}

	public ArrayList<String> getRenameFiles(){
		return renames;
	}

	public void showRenameFiles(){
		for(String rename: renames){
			System.out.println(rename);
		}
		System.out.println();
	}

}
