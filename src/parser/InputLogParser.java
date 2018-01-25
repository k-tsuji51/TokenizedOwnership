package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class InputLogParser {


	String prevMode = "";
	String nowMode = "";
	int lineNumber = 0;

	LineWatcher lineWatcher = new LineWatcher();
	TokenEditNumberByFile tokenEditNumber = new TokenEditNumberByFile();

	InputLogParser(String fileName){
		try {
		File file = new File(fileName);
			BufferedReader br = new BufferedReader(new FileReader(file));
		String str = br.readLine();
		while(str != null){

			nowMode = lineWatcher.whatModeIsThis(str, prevMode);
			System.out.println(nowMode + "," + str);

			str = br.readLine();
			lineNumber++;
			prevMode = nowMode;


		}

		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	public static void main(String args[]){
		System.out.println("Hello,World!");
		String fileName = "input.txt";
		InputLogParser perser = new InputLogParser(fileName);
	}

}
