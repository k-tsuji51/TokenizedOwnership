package clone;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class RenameFileList {/*リネームファイルを読んで，順番に記憶しておく．*/

	/*inputファイルの，左側に記述されたファイル名がリネーム前，右側がリネーム後になっている．
	 * 上に行くほど過去のリネームになっている（tacコマンドを用いてそうなるようにしておく．）*/

	static ArrayList<ArrayList<String>> changedNameListArray = new ArrayList<ArrayList<String>>();

	static String renameInput = util.StringUtil.REVERSED_RENAMELIST;
	static String outputFile = "";

	static Logger logger = Logger.getLogger ("RenameFileList");

	public RenameFileList(String renameFileListName){
		makeRenameList(renameFileListName);
	}


	public void makeRenameList(String fileName){
		try {
			BufferedReader renameBR = new BufferedReader(new FileReader(renameInput));
//			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));

			String str;

			while((str = renameBR.readLine()) != null){
				String prevName = str.split("\t")[1];
				String nowName = str.split("\t")[2];
				ArrayList<String> newRenameList = renameListSearchByFileName(prevName);
				if(newRenameList == null){/*リネームリストの新規登録*/
					newRenameList = new ArrayList<>();
					newRenameList.add(prevName);
				}
				/*既存リネームリストに追加*/
				newRenameList.add(nowName);
				changedNameListArray.add(newRenameList);
			}
			renameBR.close();
//	        pw.close();
		} catch (FileNotFoundException e) {
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
	}

	public ArrayList<String> renameListSearchByFileName(String fileName){/*ファイル名を入力とし，そのリストを返すメソッド*/
		for(ArrayList<String> renameNameList : changedNameListArray){
			if(renameNameList.contains(fileName)){
				return renameNameList;
			}
		}
		return null;
	}

	public void showRenameList(){
		for(ArrayList<String> changedNameList : changedNameListArray){
			for(String changedName : changedNameList){
				System.out.println(changedName);
			}
			System.out.println();
		}
	}
}
