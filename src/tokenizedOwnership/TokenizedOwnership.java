package tokenizedOwnership;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import clone.CloneSetManager;
import parser.InputLogParser;
import parser.TokenEditLogFileManager;
import parser.TokenEditNumberByFile;

public class TokenizedOwnership {

	static final String cloneListFileName = util.StringUtil.CLONESET_FILELIST;
	static TokenEditLogFileManager logFiles;

	// static CloneSetManager cloneSetManager;
	public static TokenEditNumberByFile tokenEditNumber;

	/* データ集計用変数 */
	static ArrayList<TokenEditNumberByFile> editNumbers = new ArrayList<>();

	public static void main(String[] args) {
		CloneSetManager csm = new CloneSetManager(cloneListFileName);
		csm.printCloneSetListToTextFile();

		logFiles = new TokenEditLogFileManager(csm);
		InputLogParser parser = new InputLogParser();
		// cloneSetManager = new
		// CloneSetManager(util.StringUtil.CLONESET_FILELIST);
		TokenEditNumberByFile prevToken = null;

		for (String logFileID : logFiles.logFileIDList) {
			// System.out.println(logFileID);
			// if(isNewCloneSet(logFileID)) System.out.println("new clone set");
			if (isNewFile(logFileID)) {
				parser = new InputLogParser();
				parser.lineWatcher.tokenEditNumber.setFileID(logFileID);
				if (prevToken != null)
					editNumbers.add(prevToken);
			}
			String fileName = util.StringUtil.DIRECTORY_LOGFILES + "\\\\" + logFiles.getcsID(logFileID) + "\\\\"
					+ logFiles.getcfID(logFileID) + "\\\\" + logFileID.replaceAll("/", "\\\\");
			prevToken = parser.testEditCount(fileName);
			// prevToken.showCommitNumber();
		}
		editNumbers.add(prevToken);
		printCSVFile(csm);
	}

	public static boolean isNewCloneSet(String logFileID) {
		return logFileID.matches(".*[0-9]*-1-1.*");
	}

	public static boolean isNewFile(String logFileID) {
		return logFileID.matches(".*[0-9]*-[0-9]*-1.*");
	}

	/* データ集計用のメソッドを作る． */
	public static void printCSVFile(CloneSetManager csm) {
		String outputFileName = util.StringUtil.CSV_OUTPUT;
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(outputFileName)));

			String cloneFileName;
			String cloneSetID = "";
			int cloneFileNum = 0;
			ArrayList<HashMap<String, Integer>> cloneSetCommitNumbers = new ArrayList<HashMap<String, Integer>>();
			for (TokenEditNumberByFile tokenFile : editNumbers) {/*
																	 * リネームの編集回数をマージした後の
																	 * ，
																	 * 各クローンファイルを処理する
																	 * クローンセット単位での最大編集者は以降で求める
																	 */
				// System.out.println(tokenFile.getFileID());
				cloneSetID = getCloneSetID(tokenFile.getFileID());
				cloneFileNum = csm.cloneSetNumBycsID(Integer.parseInt(cloneSetID));
				/* クローンセットごとに初期化 */
				if (isNewCloneSet(tokenFile.getFileID())) {
					System.out.print("CloneSet" + cloneSetID);
					pw.print("CloneSet" + cloneSetID);
					cloneSetCommitNumbers = new ArrayList<HashMap<String, Integer>>();
				}
				cloneSetCommitNumbers.add(tokenFile.getCommitNumber());
				System.out.print("," + getFileNameFromID(tokenFile.getFileID(), csm) + ",");
				pw.print("," + getFileNameFromID(tokenFile.getFileID(), csm) + ",");
				// tokenFile.showCommitNumber();
				CloneSetEditNumbers csENum = new CloneSetEditNumbers(tokenFile.getCommitNumber());
				// showTotalCommitNumber(mergeEditNumber(cloneSetCommitNumbers));
				// System.out.println("maxEditNum = "+csENum.getMaxEditNum()+"
				// totalEditNum = "+csENum.getTotalEditNum());
				System.out.print(csENum.getMaxEditNum() + "," + csENum.getTotalEditNum());
				pw.print(csENum.getMaxEditNum() + "," + csENum.getTotalEditNum());
				/* クローンファイルの数分だけ読み込みが終わったら，cs-ownershipを出力したい */
				if (cloneFileNum == cloneSetCommitNumbers.size()) {
					csENum = new CloneSetEditNumbers(mergeEditNumber(cloneSetCommitNumbers));
					System.out.print("," + csENum.calCsOwnership());
					pw.print("," + csENum.calCsOwnership());
				}
				System.out.println();
				pw.println();
			}
			pw.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	/* ファイルIDから，クローンセットIDを求める */
	public static String getCloneSetID(String fileID) {
		return fileID.split("\\\\\\\\")[1];
	}

	/* ファイルIDとクローンセットマネジャーから，ファイル名を求める */
	public static String getFileNameFromID(String fileID, CloneSetManager csm) {
		String[] IDArray = fileID.split("\\\\\\\\");
		// System.out.println(fileID);
		int cloneID = Integer.parseInt(IDArray[1]);
		int cloneFileID = Integer.parseInt(IDArray[2]);
		return csm.cloneSetList.get(cloneID - 1).cloneFiles.get(cloneFileID - 1).fileName;
	}

	public static HashMap<String, Integer> mergeEditNumber(ArrayList<HashMap<String, Integer>> cloneEditNum) {// 配列中のハッシュマップを，コミッターごとに合計して，新たなハッシュマップを返す
		HashMap<String, Integer> m = new HashMap<String, Integer>();
		for (HashMap<String, Integer> cfEditNum : cloneEditNum) {
			for (String committer : cfEditNum.keySet()) {
				if (m.containsKey(committer)) {
					m.put(committer, m.get(committer) + cfEditNum.get(committer));
				} else {
					m.put(committer, cfEditNum.get(committer));
				}
			}
		}
		return m;
	}

	/* 特定のmapに対して，その中身を標準出力に． */
	public static void showTotalCommitNumber(HashMap<String, Integer> commitNumber) {
		Iterator<String> iterator = commitNumber.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Integer value = commitNumber.get(key);
			System.out.println(key + ":" + value);
		}
	}

}
