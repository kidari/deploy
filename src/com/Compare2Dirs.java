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
			System.out.println("错误的参数");
			return;
		}
		String dir1 = args[0];
		String dir2 = args[1];
		// 获取path1,path2的所有文件夹路径,文件的md5值put map
		Map<String, FileMd5> path1Map = listDir(dir1);
		Map<String, FileMd5> path2Map = listDir(dir2);
		// compare path1 map to path2 map 得到path2没有的文件夹和文件及其md5值不同的文件
		List<FileMd5> compareFile1 = compareFile(path1Map, path2Map);
		// compare path2 map to path1 map 得到path1没有的文件夹和文件及其md5值不同的文件
		List<FileMd5> compareFile2 = compareFile(path2Map, path1Map);
		// 过滤结果
		List<FileMd5> equalsFile = filterFile(compareFile1, compareFile2);
		// 输出最终结果
		printResult(equalsFile, compareFile1, compareFile2);
	}

	/**
	 * 获取指定路径下的所有文件路径
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
	 * 获取指定文件夹下的文件夹路径和文件md5值
	 */
	private static Map<String, FileMd5> listDir(String dir) throws IOException {
		Map<String, FileMd5> map = new HashMap<String, FileMd5>();
		File path = new File(dir);
		Object[] files = listPath(path).toArray();
		Arrays.sort(files);
		for (Object _file : files) {
			File file = (File) _file;
			String key = file.getAbsolutePath().replaceAll("\\\\", "/");
			key = key.replaceAll(dir, "");// 去掉根目录
			String md5 = "";// 文件夹的md5默认为空,即不比较md5值
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
	 * 比较两个文件夹的不同
	 */
	public static List<FileMd5> compareFile(Map<String, FileMd5> path1Map,
			Map<String, FileMd5> path2Map) {
		List<FileMd5> list = new ArrayList<FileMd5>();
		for (String key : path1Map.keySet()) {
			FileMd5 fileMd5 = path1Map.get(key);
			FileMd5 _fileMd5 = path2Map.get(key);

			// 不管文件夹还是文件，只要path2没有则add到比较结果集中
			if (_fileMd5 == null) {
				list.add(fileMd5);
				continue;
			}

			// 文件的md5值不同则add到比较结果集中
			if (fileMd5.getFile().isFile()
					&& !fileMd5.getMd5().equals(_fileMd5.getMd5())) {
				list.add(fileMd5);
			}
		}
		return list;
	}

	/**
	 * 过滤相同
	 */
	public static List<FileMd5> filterFile(List<FileMd5> compareFile1,
			List<FileMd5> compareFile2) {
		List<FileMd5> list = new ArrayList<FileMd5>();
		for (FileMd5 fileMd5 : compareFile1) {
			if (compareFile2.contains(fileMd5)) {
				list.add(fileMd5);// 千万不要在此remove fileMd5
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
	 * 打印结果
	 */
	public static void printResult(List<FileMd5> equalsFile,
			List<FileMd5> compareFile1, List<FileMd5> compareFile2) {
		System.out
				.println("########################比较结果########################");
		System.out
				.println("########################均不同的结果########################");
		printFile(equalsFile);
		System.out
				.println("########################均不同的结果########################");
		System.out
				.println("########################path1多的结果########################");
		printFile(compareFile1);
		System.out
				.println("########################path1多的结果########################");
		System.out
				.println("########################path2多的结果########################");
		printFile(compareFile2);
		System.out
				.println("########################path2多的结果########################");
	}

	/**
	 * 打印结果
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
