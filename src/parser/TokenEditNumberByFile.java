package parser;

import java.util.HashMap;
import java.util.Iterator;

//あるファイルの編集者と編集回数を格納，表示できるクラス

public class TokenEditNumberByFile {
	String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	HashMap<String,Integer> commitNumber = new HashMap<String,Integer>();


	//コミッターの編集回数をカウントしていく
	public void countCommitNumber(String committer) {
		int count = 1;
		if(commitNumber.containsKey(committer)){
			count += commitNumber.get(committer);
		}
		commitNumber.put(committer, count);
	}

	public TokenEditNumberByFile(){
	}

	public void showCommitNumber() {
		Iterator<String> iterator = commitNumber.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			Integer value = commitNumber.get(key);
			System.out.println(key + ":" + value);
		}
	}





}
