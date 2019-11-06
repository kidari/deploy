import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CREATEBUR {
	public final static String encode_GBK = "GBK";
	public final static String oldadd = "\\\\WEB-INF\\\\classes";
	public final static String newadd = "/WEB-INF/classes";
	public final static String ip = "200.31.154.196";
	public final static int port = 22;
	public final static String user = "bea";
	public final static String password = "kfPass@123";

	/**
	 * 复制文件 生成文件路径
	 * 
	 * @param outputfile
	 * @param lineTxt
	 * @return
	 */
	public static ArrayList<String> CopyFile(String outputfile,
			ArrayList<String> lineTxt) {
		ArrayList<String> lineTxtsc = new ArrayList<String>();
		try {
			File folder = new File(outputfile);// 创建文件夹
			if (!folder.exists() && !folder.isDirectory()) {
				folder.mkdirs();
			} else {
				System.out.println(outputfile + "-文件夹已存在");
			}

			for (int i = 0; i < lineTxt.size(); i++) {
				String newlineTxt = lineTxt.get(i).replace(".java", ".class");
				String test[] = newlineTxt.split("/");
				int len = test.length;

				String project = deploy.getData(test[1]) == null ? test[1]
						: deploy.getData(test[1]);// 本地项目名转为服务器项目名
				System.out.println("复制文件所属项目名：" + project);

				String least = "";
				String programleast = "";// 开发版地址
				String programBL = "";// 基线地址
				if ("true".equals(deploy.getData("changeline"))) {// 生成转换后服务器路径
					if (newlineTxt.indexOf("src") > 0) {
						String lineresult[] = newlineTxt.split("src");
						least = deploy.getData(project + "output") == null ? "1111"
								: deploy.getData(project + "output") + newadd
										+ lineresult[1];
						if (deploy.getData(project + "weblogic").equals("true")
								|| deploy.getData(project + "weblogic") == "true") {// 是否部署到weblogic
							programleast = deploy.getData(project + "input")
									+ oldadd
									+ lineresult[1].replaceAll("/", "\\\\");
							programBL = deploy.getData(project + "inputBL")
									+ oldadd
									+ lineresult[1].replaceAll("/", "\\\\");
						} else {
							programleast = deploy.getData(project + "input")
									+ lineresult[1].replaceAll("/", "\\\\");
							programBL = deploy.getData(project + "inputBL")
									+ lineresult[1].replaceAll("/", "\\\\");
						}

					} else if (newlineTxt.indexOf("WebRoot") > 0) {
						String lineresult[] = newlineTxt.split("WebRoot");
						least = deploy.getData(project + "output")
								+ lineresult[1];
						programleast = deploy.getData(project + "input")
								+ lineresult[1].replaceAll("/", "\\\\");
						programBL = deploy.getData(project + "inputBL")
								+ lineresult[1].replaceAll("/", "\\\\");

					} else {
						least = newlineTxt;
					}
				} else {
					least = newlineTxt;
				}
				int flag;
				if ((test[1] == "report" || test[1].equals("report")
						|| test[1] == "kondor" || test[1].equals("kondor"))
						&& newlineTxt.indexOf(".txt") < 0) {
					String newPath = outputfile
							+ "\\"
							+ deploy.getData("version")
							+ newlineTxt.split("src")[1]
									.replaceAll("/", "\\\\");
					String copyPath = newPath;
					String testre[] = newPath.split("\\\\");
					int lenre = testre.length;
					String filePath = newPath.replace("\\" + testre[lenre - 1],
							"");
					File folderre = new File(filePath);// 创建文件夹
					if (!folderre.exists() && !folderre.isDirectory()) {
						folderre.mkdirs();
					} else {
						System.out.println(filePath + "-文件夹已存在");
					}
					flag = deploy.copyFile(programBL, copyPath, false);// 判断基线文件是否存在
					if (flag == 1) {
						least = "-" + least;
					}
					deploy.copyFile(programleast, copyPath, true);// report项目复制文件及文件夹
				} else {
					System.out.println(programBL);
					flag = deploy.copyFile(programBL, outputfile + "\\"
							+ test[len - 1], false);// 判断基线文件是否存在
					if (flag == 1) {
						least = "-" + least;
					}
					deploy.copyFile(programleast, outputfile + "\\"
							+ test[len - 1], true);// 复制文件
				}

				lineTxtsc.add(least);
			}
		} catch (Exception e) {
			System.out.println("-无法复制文件");
			e.printStackTrace();
		}
		return lineTxtsc;
	}

	public static void NaturalCopy(String outputfile, ArrayList<String> lineTxt) {
		try {
			File folder = new File(outputfile);// 创建文件夹
			if (!folder.exists() && !folder.isDirectory()) {
				folder.mkdirs();
			} else {
				System.out.println(outputfile + "-文件夹已存在");
			}

			for (int i = 0; i < lineTxt.size(); i++) {
				String newlineTxt = lineTxt.get(i);
				String test[] = newlineTxt.split("/");
				int len = test.length;
				String project = deploy.getData(test[1]) == null ? test[1]
						: deploy.getData(test[1]);// 本地项目名转为服务器项目名
				// String project = deploy.getData("project");//本地项目名转为服务器项目名
				System.out.println("复制文件所属项目名：" + project);

				String programleast = deploy.getData(project + "original")
						+ newlineTxt.replaceAll("/", "\\\\");
				;// 开发版地址

				deploy.copyFile(programleast,
						outputfile + "\\" + test[len - 1], true);// 复制文件

			}
		} catch (Exception e) {
			System.out.println("-无法复制文件");
			e.printStackTrace();
		}
	}

	public static void CreateNewFile(String project, String outputfile,
			String filename, ArrayList<String> lineTxt) {
		try {
			File folder = new File(outputfile);// 创建文件夹
			if (!folder.exists() && !folder.isDirectory()) {
				folder.mkdirs();
			} else {
			}
			String filePath = outputfile + "\\" + filename;// 创建文件
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			} else {
				System.out.println(filePath + "-文件已存在");
			}
			FileWriter resultFile = new FileWriter(file);
			PrintWriter NewFile = new PrintWriter(resultFile);
			if (filename.equals("apprestore.sh") || filename == "apprestore.sh") {
				NewFile.print("#1 备份日期  20100101\n");
				NewFile.print("#!/bin/bash\n");
				NewFile.print("date_backup=$1\n");

			}
			for (int i = 0; i < lineTxt.size(); i++) {
				if (!(lineTxt.get(i)).trim().substring(0, 1).equals("#")) {
					String test[] = lineTxt.get(i).split("/");
					int len = test.length;
					if (filename.equals("appbackup.sh")) {
						if (!(lineTxt.get(i)).trim().substring(0, 1)
								.equals("-")) {
							String temp = deploy.getData("appbackup.sh");// 被替换语句
							temp = temp.replace("#project#", project);// 项目名
							temp = temp.replace("#version#",
									deploy.getData("version"));// 版本号
							String scfile = test[len - 1];
							temp = temp.replace("#filename#", scfile);// 操作文件名
							temp = temp.replace("#situation#", lineTxt.get(i));// 生成一行shell
							NewFile.print(temp + "\n");
						}
					} else if (filename.equals("apprestore.sh")) {
						if (!(lineTxt.get(i)).trim().substring(0, 1)
								.equals("-")) {
							String temp = deploy.getData("apprestore.sh");// 被替换语句
							temp = temp.replace("#project#", project);// 项目名
							temp = temp.replace("#version#",
									deploy.getData("version"));// 版本号
							String scfile = test[len - 1];
							temp = temp.replace("#filename#", scfile);// 操作文件名
							temp = temp.replace("#situation#", lineTxt.get(i));// 生成一行shell
							NewFile.print(temp + "\n");
						} else {
							String temp = deploy.getData("rm");// 被替换语句
							temp = temp.replace("#project#", project);// 项目名
							temp = temp.replace("#version#",
									deploy.getData("version"));// 版本号
							String scfile = test[len - 1];
							temp = temp.replace("#filename#", scfile);// 操作文件名
							temp = temp.replace("#situation#", lineTxt.get(i)
									.substring(1));// 生成一行shell
							NewFile.print(temp + "\n");
						}

					} else if (filename.equals("appupdate.sh")) {

						String temp = deploy.getData("appupdate.sh");// 被替换语句
						temp = temp.replace("#project#", project);// 项目名
						temp = temp.replaceAll("#version#",
								deploy.getData("version"));// 版本号
						String scfile = test[len - 1];
						temp = temp.replace("#filename#", scfile);// 操作文件名
						if (!(lineTxt.get(i)).trim().substring(0, 1)
								.equals("-")) {
							temp = temp.replace("#situation#", lineTxt.get(i));// 生成一行shell
						} else {

							temp = temp.replace("#situation#", lineTxt.get(i)
									.substring(1));// 生成一行shell
						}
						NewFile.print(temp + "\n");
					}
				}

			}

			resultFile.close();
			System.out.println(file + "文件生成成功");
		} catch (Exception e) {
			System.out.println("*无法创建新文件");
			e.printStackTrace();
		}
	}

	public static void CreateNewFile(String project, String linuxproject,
			String outputfile, String filename, ArrayList<String> lineTxt) {
		try {
			File folder = new File(outputfile);// 创建文件夹
			if (!folder.exists() && !folder.isDirectory()) {
				folder.mkdirs();
			} else {
			}
			String filePath = outputfile + "\\" + filename;// 创建文件
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			} else {
				System.out.println(filePath + "-文件已存在");
			}
			FileWriter resultFile = new FileWriter(file);
			PrintWriter NewFile = new PrintWriter(resultFile);
			if (filename.equals("apprestore.sh") || filename == "apprestore.sh") {
				NewFile.print("#1 备份日期  20100101\n");
				NewFile.print("#!/bin/bash\n");
				NewFile.print("date_backup=$1\n");

			}
			for (int i = 0; i < lineTxt.size(); i++) {
				if (!(lineTxt.get(i)).trim().substring(0, 1).equals("#")) {
					String test[] = lineTxt.get(i).split("/");
					int len = test.length;
					if (filename.equals("appbackup.sh")) {
						if (!(lineTxt.get(i)).trim().substring(0, 1)
								.equals("-")) {
							String temp = deploy.getData("appbackup.sh");// 被替换语句
							temp = temp.replace("#project#", project);// 项目名
							temp = temp.replace("#version#",
									deploy.getData("version"));// 版本号
							String scfile = test[len - 1];
							temp = temp.replace("#filename#", scfile);// 操作文件名
							temp = temp.replace("#situation#", linuxproject
									+ lineTxt.get(i).substring(1));// 生成一行shell
							NewFile.print(temp + "\n");
						}
					} else if (filename.equals("apprestore.sh")) {
						if (!(lineTxt.get(i)).trim().substring(0, 1)
								.equals("-")) {
							String temp = deploy.getData("apprestore.sh");// 被替换语句
							temp = temp.replace("#project#", project);// 项目名
							temp = temp.replace("#version#",
									deploy.getData("version"));// 版本号
							String scfile = test[len - 1];
							temp = temp.replace("#filename#", scfile);// 操作文件名
							temp = temp.replace("#situation#", linuxproject
									+ lineTxt.get(i).substring(1));// 生成一行shell
							NewFile.print(temp + "\n");
						} else {
							String temp = deploy.getData("rm");// 被替换语句
							temp = temp.replace("#project#", project);// 项目名
							temp = temp.replace("#version#",
									deploy.getData("version"));// 版本号
							String scfile = test[len - 1];
							temp = temp.replace("#filename#", scfile);// 操作文件名
							temp = temp.replace("#situation#", linuxproject
									+ lineTxt.get(i).substring(2));// 生成一行shell
							NewFile.print(temp + "\n");
						}

					} else if (filename.equals("appupdate.sh")) {

						String temp = deploy.getData("appupdate.sh");// 被替换语句
						temp = temp.replace("#project#", project);// 项目名
						temp = temp.replaceAll("#version#",
								deploy.getData("version"));// 版本号
						String scfile = test[len - 1];

						if (!(lineTxt.get(i)).trim().substring(0, 1)
								.equals("-")) {
							temp = temp.replace("#filename#", lineTxt.get(i)
									.substring(1));// 操作文件名
							temp = temp.replace("#situation#", linuxproject
									+ lineTxt.get(i).substring(1));// 生成一行shell
						} else {
							temp = temp.replace("#filename#", lineTxt.get(i)
									.substring(2));// 操作文件名
							temp = temp.replace("#situation#", linuxproject
									+ lineTxt.get(i).substring(2));// 生成一行shell
						}
						NewFile.print(temp + "\n");
					}
				}

			}

			resultFile.close();
			System.out.println(file + "文件生成成功");
		} catch (Exception e) {
			System.out.println("*无法创建新文件");
			e.printStackTrace();
		}
	}

	public static void another() {

		String date = deploy.getLimitTime();
		String filePath = deploy.getData("BURINPUT");
		filePath = filePath.replace("#date#", date);
		ArrayList<String> lineTxt = new ArrayList<String>();
		lineTxt = deploy.ReadTxt(filePath, encode_GBK);
		String outputfile = deploy.getData("BUROUTPUT");
		outputfile = outputfile.replace("#date#", date);// 文件输出目录
		String project = deploy.getData("project");// 部署文件夹

		ArrayList<String> lineTxtsc = new ArrayList<String>();
		// NaturalCopy(outputfile, lineTxt);//复制源代码
		lineTxtsc = CopyFile(outputfile + "\\" + deploy.getData("version"),
				lineTxt);// 复制文件
		// CreateNewFile(project,outputfile,"appbackup.sh",lineTxtsc);//生成文件
		// CreateNewFile(project,outputfile,"appupdate.sh",lineTxtsc);//生成文件
		// CreateNewFile(project,outputfile,"apprestore.sh",lineTxtsc);//生成文件
		TransferFile(project, "20170406", deploy.getData("version"), outputfile);// 上传
																					// 打包
																					// 下载文件
		System.exit(0);

	}

	public static void main(String[] args) {
		String date = deploy.getLimitTime();
		String filePath = deploy.getData("BURINPUT");
		filePath = filePath.replace("#date#", date);
		ArrayList<String> lineTxt = new ArrayList<String>();
		lineTxt = deploy.ReadTxt(filePath, encode_GBK);
		String outputfile = deploy.getData("BUROUTPUT");
		outputfile = outputfile.replace("#date#", date);// 文件输出目录
		String project = deploy.getData("project");// 部署文件夹

		CreateNewFile(project,"/cadomain/domain_cnap2/applications/DefaultWebApp",outputfile, "appbackup.sh", lineTxt);// 生成文件
		CreateNewFile(project,"/cadomain/domain_cnap2/applications/DefaultWebApp",outputfile, "appupdate.sh", lineTxt);// 生成文件
		CreateNewFile(project,"/cadomain/domain_cnap2/applications/DefaultWebApp",outputfile, "apprestore.sh", lineTxt);// 生成文件
		// TransferFile(project,"20170406",deploy.getData("version"),outputfile);//上传
		// 打包 下载文件
		System.exit(0);
	}

	public static void TransferFile(String project, String CurrDate,
			String version, String outputfile) {
		try {
			SftpClient sftptest = new SftpClient(ip, user, password, port);
			Shell ssh = new Shell(ip, port, user, password);
			String clear[] = {
					"mkdir -p /home/bea/update/" + project + "/" + CurrDate
							+ "_" + version,
					"cd /home/bea/update/" + project + "/" + CurrDate + "_"
							+ version, "rm -r *", "mkdir -p ./" + version };
			ssh.executeCommands(clear);
			sftptest.putMFile(outputfile + "\\" + version, "/home/bea/update/"
					+ project + "/" + CurrDate + "_" + version + "/" + version,
					false);
			sftptest.putMFile(outputfile, "/home/bea/update/" + project + "/"
					+ CurrDate + "_" + version, false);
			String cmd[] = {
					"cd /home/bea/update/" + project + "/" + CurrDate + "_"
							+ version,
					"tar -cvf " + deploy.getData(project + "dir") + CurrDate
							+ ".tar *" };
			ssh.executeCommands(cmd);
			System.out.println(ssh.getResponse());
			ssh.disconnect();
			sftptest.getFile(outputfile, "/home/bea/update/" + project + "/"
					+ CurrDate + "_" + version, deploy.getData(project + "dir")
					+ CurrDate + ".tar");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
