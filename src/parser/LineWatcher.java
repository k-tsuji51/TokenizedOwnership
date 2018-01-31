package parser;

import util.ModeUtil;

/* 与えられた文字列が，ファイルのどのパーツに当たるのかを判定して返す */
/* 与えられた文字列は，nullではない(事前に弾いておく) */
public class LineWatcher {


	String committerName = "";
	int minus = 0;
	int plus = 0;
	int count = 0;

	public TokenEditNumberByFile tokenEditNumber = new TokenEditNumberByFile();


	/*リネームを判別するための変数*/

	RenameDetector renameDetector;

	boolean newMode = false;
	boolean deletedMode = false;
	boolean isRename = false;

	public LineWatcher(){

	}

	public String whatModeIsThis(String line, String prevMode){
		String mode = "";
		/* prevModeだけで決定するmodeがあるので，prevModeでswitch文を記述する． */
		/* prevModeで決定する:*/

		switch(prevMode){
		case "":
			mode = ModeUtil.commitIDDisplay;
			break;

		case ModeUtil.commitIDDisplay:
			mode = ModeUtil.commiterDisplay;
			break;
		case ModeUtil.commiterDisplay:
			mode = ModeUtil.commitDateDisplay;
			break;
		case ModeUtil.commitDateDisplay:
			mode = ModeUtil.commitComment;
			break;

		case ModeUtil.commitComment:
			mode = whatMode(line,prevMode);
			break;
		case ModeUtil.exeDiffCommand:
			mode = whatMode(line,prevMode);
			break;
		case ModeUtil.newFileMode:
			mode = ModeUtil.indexHatena;
			break;
		case ModeUtil.indexHatena:/*whatmodeに移行予定*/
			mode = whatMode(line,prevMode);
				break;
		case ModeUtil.diffOrigFileName:
			mode = ModeUtil.diffNewFileName;
			break;
		case ModeUtil.diffNewFileName:
			mode = ModeUtil.changedTokenLocation;
			break;
		case ModeUtil.changedTokenLocation:
			mode = whatMode(line,prevMode);
			break;
		}

		if(mode == ModeUtil.changedTokenLocation){
			if(!isRename){
				this.countEditNumber(line);
			}
		}
		else if(mode == ModeUtil.commiterDisplay){
			committerName = getCommitterFromLine(line);
//			System.out.println(getCommitterFromLine(line));
		}
		else if(mode == ModeUtil.newFileMode){/* リネームを判定するための準備処理 */
			if(line.contains("deleted")){
//				System.out.println("deletedmode");
				deletedMode = true;
			}else if(line.contains("new file mode")){
//				System.out.println("newmode");
				newMode = true;
			}
		}
		else if((mode == ModeUtil.diffOrigFileName && deletedMode) || mode == ModeUtil.diffNewFileName && newMode){
			String fileName = line.substring(6);
//			System.out.println(fileName);
			renameDetector = new RenameDetector(fileName);
			if((renameDetector.isBeforeRename && deletedMode )||( renameDetector.isAfterRename && newMode)){
				this.isRename = true;
//				System.out.println("this is rename commit");
			}
		}
		return mode;
	}

	/* modeだけでは特定できない際の処理 */
	private String whatMode(String line, String prevMode){
		String mode = "";

		switch(prevMode){
		case ModeUtil.commitComment:
			/*次のmodeは,commitCommentもしくはexeDiffCommand*/
			if(line.startsWith("diff --git")){
				mode = ModeUtil.exeDiffCommand;
			}
			else mode = ModeUtil.commitComment;
			break;
		case ModeUtil.exeDiffCommand:
			/*次のmodeは，newFileModeもしくはindexHatena*/
			if(line.startsWith("new file mode") ||line.startsWith("deleted file mode")){
				mode = ModeUtil.newFileMode;
			}
			else mode = ModeUtil.indexHatena;
			break;
		case ModeUtil.changedTokenLocation:
			/*次のmodeは，changedTokenLocationもしくはcommitIDDisplay*/
			if(line.startsWith("commit ")){
//				tokenEditNumber.showCommitNumber();
				commitDataInit(); //編集者が変わるので，コミット毎に編集者と編集回数は初期化する．
				mode = ModeUtil.commitIDDisplay;
			}
			else mode = ModeUtil.changedTokenLocation;
			break;
		case ModeUtil.indexHatena:
			if(line.startsWith("---")){
				mode = ModeUtil.diffOrigFileName;
				break;
			}else if(line.startsWith("commit ")){
				commitDataInit(); //編集者が変わるので，コミット毎に編集者と編集回数は初期化する．
				mode = ModeUtil.commitIDDisplay;
//				System.out.println("No changed logs");
				break;
			}else mode = ModeUtil.indexHatena;
		}

		return mode;
	}

	private String getCommitterFromLine(String line){
		String committerLine = "";
		committerLine = line.substring(8, line.length());
		committerLine = committerLine.split("<")[0];
		return committerLine;
	}

	/*トークンの編集回数を数える*/
	public int countEditNumber(String line){
		/*changedtokenは，lineの行頭が3種類ある
		 * - か + か 空白
		 * トークンが編集された場合は，-の行の直後に+の行がくる．
		 * --+の場合は，編集回数は除去1回と置換1回の2回の編集となる
		 * --++の場合も，置換が2回発生したとみなす．*/
		if(line.startsWith("-")){
			minus++;
			count++;
			tokenEditNumber.countCommitNumber(committerName);
		}
		else if(line.startsWith("+")){
			plus++;
			if(minus < plus){
				plus++;
				count++;
				tokenEditNumber.countCommitNumber(committerName);
			};
		}
		else{/*何もついてないとき*/
			minus = 0;
			plus = 0;
		}
		return count;
	}


	private void commitDataInit(){
		this.committerName = "";
		this.count = 0;

		isRename = false;
		newMode = false;
		deletedMode = false;
	}


}
