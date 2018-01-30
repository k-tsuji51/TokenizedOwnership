package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/* リネームコミットかどうかを判定するクラス */
public class RenameDetector {

	public String fileName = util.StringUtil.REVERSED_RENAMELIST;
	public boolean isBeforeRename = false;
	public boolean isAfterRename = false;


	/*リネームのリストファイルから，入力で与えられたファイルがあるかどうかを調べる．リネーム前，後，もしくは両方にファイルが存在するのかどうかをチェックする．*/
	public RenameDetector(String fileName){
		try {
			File file = new File(this.fileName);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String str;
			while (((str = br.readLine()) != null) && !(isBeforeRename && isAfterRename)) {
				String[] renames = str.split("\t");
				if(renames[1].matches(".*"+fileName+".*")){
					isBeforeRename = true;

				}if(renames[2].matches(".*"+fileName+".*")){
					isAfterRename = true;

				}
				// System.out.println(nowMode + "," + str);
			}
			br.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}
}