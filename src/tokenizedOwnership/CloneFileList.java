package tokenizedOwnership;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import util.StringUtil;

public class CloneFileList {

	String fileName = StringUtil.INPUT_CLONESET_FILELIST;
	File cloneSetList = new File(fileName);

	String removeHead = StringUtil.REMOVE_HEAD;
	ArrayList<String> pathNameList = new ArrayList<String>();



	CloneFileList(){
		pathNameList = makePathNameList();
		try {
			showPathNameList();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public ArrayList<String> makePathNameList(){
		ArrayList<String> pathNameList = new ArrayList<String>();
		try {
		BufferedReader br;
			br = new BufferedReader(new FileReader(cloneSetList));
		String str;
		while((str = br.readLine())!=null){
			if(str.startsWith("#begin{set}") || str.startsWith("#end{set}")){//クローンセットの境目
				pathNameList.add(str);
			}else{
				pathNameList.add(str.replace(removeHead, "").replaceAll("\\\\", "/"));
			}

		}
		br.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return pathNameList;
	}



	public void showPathNameList() throws IOException{
		ArrayList<String> pathNameList = makePathNameList();
		for(String str : pathNameList){
			System.out.println(str);
		}
	}

}
