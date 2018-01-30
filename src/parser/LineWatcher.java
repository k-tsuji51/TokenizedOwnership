
	/*リネームを判別するための変数*/

	RenameDetector renameDetector;

	boolean newMode = false;
	boolean deletedMode = false;
	boolean isRename = false;

	public LineWatcher(){
		case ModeUtil.indexHatena:/*whatmodeに移行予定*/
			mode = whatMode(line,prevMode);
				break;

		if(mode == ModeUtil.changedTokenLocation){
			if(!isRename){
				this.countEditNumber(line);
			}
		}
		else if(mode == ModeUtil.commiterDisplay){
//			System.out.println(getCommitterFromLine(line));
		else if(mode == ModeUtil.newFileMode){/* リネームを判定するための準備処理 */
			if(line.contains("deleted")){
//				System.out.println("deletedmode");
				deletedMode = true;
			}else if(line.contains("new file mode")){
//				System.out.println("newmode");
				newMode = true;
			}
		else if((mode == ModeUtil.diffOrigFileName && deletedMode) || mode == ModeUtil.diffNewFileName && newMode){
			String fileName = line.substring(6);
			System.out.println(fileName);
			renameDetector = new RenameDetector(fileName);
			if((renameDetector.isBeforeRename && deletedMode )||( renameDetector.isAfterRename && newMode)){
				this.isRename = true;
				System.out.println("this is rename commit");
			}
		}

			if(line.startsWith("new file mode") ||line.startsWith("deleted file mode")){
//				tokenEditNumber.showCommitNumber();
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
		isRename = false;
		newMode = false;
		deletedMode = false;
	}