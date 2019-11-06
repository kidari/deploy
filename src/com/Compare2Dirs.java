package com;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Compare2Dirs {

	public static void main(String[] args) throws IOException {
		if (args.length < 2) {
			System.out.println("����Ĳ���");
			return;
		}
		String dir1 = args[0];
		String dir2 = args[1];
		// ��ȡpath1,path2�������ļ���·��,�ļ���md5ֵput map
		Map<String, FileMd5> path1Map = listDir(dir1);
		Map<String, FileMd5> path2Map = listDir(dir2);
		// compare path1 map to path2 map �õ�path2û�е��ļ��к��ļ�����md5ֵ��ͬ���ļ�
		List<FileMd5> compareFile1 = compareFile(path1Map, path2Map);
		// compare path2 map to path1 map �õ�path1û�е��ļ��к��ļ�����md5ֵ��ͬ���ļ�
		List<FileMd5> compareFile2 = compareFile(path2Map, path1Map);
		// ���˽��
		List<FileMd5> equalsFile = filterFile(compareFile1, compareFile2);
		// ������ս��
		printResult(equalsFile, compareFile1, compareFile2);
	}

	/**
	 * ��ȡָ��·���µ������ļ�·��
	 */
	private static List<File> listPath(File path) {
		List<File> list = new ArrayList<File>();
		File[] files = path.listFiles();
		Arrays.sort(files);
		for (File file : files) {
			list.add(file);
			if (file.isDirectory()) {
				List<File> _list = listPath(file);
				list.addAll(_list);
			}
		}
		return list;
	}

	/**
	 * ��ȡָ���ļ����µ��ļ���·�����ļ�md5ֵ
	 */
	private static Map<String, FileMd5> listDir(String dir) throws IOException {
		Map<String, FileMd5> map = new HashMap<String, FileMd5>();
		File path = new File(dir);
		Object[] files = listPath(path).toArray();
		Arrays.sort(files);
		for (Object _file : files) {
			File file = (File) _file;
			String key = file.getAbsolutePath().replaceAll("\\\\", "/");
			key = key.replaceAll(dir, "");// ȥ����Ŀ¼
			String md5 = "";// �ļ��е�md5Ĭ��Ϊ��,�����Ƚ�md5ֵ
			if (file.isFile()) {
				String text = FileUtils.readFileToString(file);
				md5 = MD5.md5(text);
			}
			FileMd5 fileMd5 = new FileMd5(file, md5);
			map.put(key, fileMd5);
		}
		return map;
	}

	/**
	 * �Ƚ������ļ��еĲ�ͬ
	 */
	public static List<FileMd5> compareFile(Map<String, FileMd5> path1Map,
			Map<String, FileMd5> path2Map) {
		List<FileMd5> list = new ArrayList<FileMd5>();
		for (String key : path1Map.keySet()) {
			FileMd5 fileMd5 = path1Map.get(key);
			FileMd5 _fileMd5 = path2Map.get(key);

			// �����ļ��л����ļ���ֻҪpath2û����add���ȽϽ������
			if (_fileMd5 == null) {
				list.add(fileMd5);
				continue;
			}

			// �ļ���md5ֵ��ͬ��add���ȽϽ������
			if (fileMd5.getFile().isFile()
					&& !fileMd5.getMd5().equals(_fileMd5.getMd5())) {
				list.add(fileMd5);
			}
		}
		return list;
	}

	/**
	 * ������ͬ
	 */
	public static List<FileMd5> filterFile(List<FileMd5> compareFile1,
			List<FileMd5> compareFile2) {
		List<FileMd5> list = new ArrayList<FileMd5>();
		for (FileMd5 fileMd5 : compareFile1) {
			if (compareFile2.contains(fileMd5)) {
				list.add(fileMd5);// ǧ��Ҫ�ڴ�remove fileMd5
			}
		}
		// remove equals fileMd5
		for (FileMd5 fileMd5 : list) {
			compareFile1.remove(fileMd5);
			compareFile2.remove(fileMd5);
		}
		return list;
	}

	/**
	 * ��ӡ���
	 */
	public static void printResult(List<FileMd5> equalsFile,
			List<FileMd5> compareFile1, List<FileMd5> compareFile2) {
		System.out
				.println("########################�ȽϽ��########################");
		System.out
				.println("########################����ͬ�Ľ��########################");
		printFile(equalsFile);
		System.out
				.println("########################����ͬ�Ľ��########################");
		System.out
				.println("########################path1��Ľ��########################");
		printFile(compareFile1);
		System.out
				.println("########################path1��Ľ��########################");
		System.out
				.println("########################path2��Ľ��########################");
		printFile(compareFile2);
		System.out
				.println("########################path2��Ľ��########################");
	}

	/**
	 * ��ӡ���
	 */
	public static void printFile(List<FileMd5> fileMd5s) {
		for (FileMd5 fileMd5 : fileMd5s) {
			System.out.println(fileMd5.getFile().getAbsolutePath() + " "
					+ fileMd5.getMd5());
		}
	}

}

class FileMd5 {
	public File file;
	public String md5;

	public FileMd5(File file, String md5) {
		this.file = file;
		this.md5 = md5;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	@Override
	public String toString() {
		return file.getAbsolutePath();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		FileMd5 fileMd5 = (FileMd5) obj;
		return this.toString().equals(obj.toString())
				&& this.getMd5().equals(fileMd5.getMd5());
	}
}
