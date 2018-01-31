package tokenizedOwnership;

import java.util.HashMap;
import java.util.Iterator;

public class CloneSetEditNumbers {

	HashMap<String,Integer> edNumByEditor;
	int maxEditNum;
	int totalEditNum;
	double csOwnership;


	CloneSetEditNumbers(HashMap<String,Integer> csEditorsNum){
		this.edNumByEditor = csEditorsNum;
		setMaxEditNum();
		setTotalEditNum();
	}

	public HashMap<String, Integer> getEdNumByEditor() {
		return edNumByEditor;
	}
	public void setEdNumByEditor(HashMap<String, Integer> edNumByEditor) {
		this.edNumByEditor = edNumByEditor;
	}
	public int getMaxEditNum() {
		return maxEditNum;
	}
	public void setMaxEditNum() {
		int maxNum=0;
		for(String committer : edNumByEditor.keySet()){
			int i = edNumByEditor.get(committer);
			if(maxNum < i){
				maxNum = i;
			}
		}
			this.maxEditNum = maxNum;
	}
	public int getTotalEditNum() {
		return totalEditNum;
	}
	public void setTotalEditNum() {
		int edNum=0;
		for(String committer : edNumByEditor.keySet()){
			edNum = edNum + edNumByEditor.get(committer);
		}
			this.totalEditNum = edNum;
	}
	public double calCsOwnership() {
		if(totalEditNum != 0){
			return (double)maxEditNum/totalEditNum;
		}
		return 0;
	}

	public void showCommitNumber() {
		Iterator<String> iterator = edNumByEditor.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			Integer value = edNumByEditor.get(key);
			System.out.println(key + ":" + value);
		}
	}

}