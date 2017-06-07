package system;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryScanner {
	public List<File> fileList;
	File root;
	
	public DirectoryScanner(File rootFile) {
		fileList = new ArrayList<File>();
		root = rootFile;
	}
	
	public void startScan() {
		List<File> fList = new ArrayList<File>();
		scanDir(root, fList);
		fileList = fList;
		System.out.println("SCAN FINISHED: " + fList.size());
	}
	
	private void scanDir(File curFile, List<File> list) {
		File[] children= curFile.listFiles();
		if (children != null) {
			for (File f : children) {
				if (f.isFile()) {
					list.add(f);
				}
				else {
					scanDir(f, list);
				}
			}
		}
	}
	
	public int getNumFiles() {
		return fileList.size();
	}
}
