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
	 * �����ļ� �����ļ�·��
	 * 
	 * @param outputfile
	 * @param lineTxt
	 * @return
	 */
	public static ArrayList<String> CopyFile(String outputfile,
			ArrayList<String> lineTxt) {
		ArrayList<String> lineTxtsc = new ArrayList<String>();
		try {
			File folder = new File(outputfile);// �����ļ���
			if (!folder.exists() && !folder.isDirectory()) {
				folder.mkdirs();
			} else {
				System.out.println(outputfile + "-�ļ����Ѵ���");
			}

			for (int i = 0; i < lineTxt.size(); i++) {
				String newlineTxt = lineTxt.get(i).replace(".java", ".class");
				String test[] = newlineTxt.split("/");
				int len = test.length;

				String project = deploy.getData(test[1]) == null ? test[1]
						: deploy.getData(test[1]);// ������Ŀ��תΪ��������Ŀ��
				System.out.println("�����ļ�������Ŀ����" + project);

				String least = "";
				String programleast = "";// �������ַ
				String programBL = "";// ���ߵ�ַ
				if ("true".equals(deploy.getData("changeline"))) {// ����ת���������·��
					if (newlineTxt.indexOf("src") > 0) {
						String lineresult[] = newlineTxt.split("src");
						least = deploy.getData(project + "output") == null ? "1111"
								: deploy.getData(project + "output") + newadd
										+ lineresult[1];
						if (deploy.getData(project + "weblogic").equals("true")
								|| deploy.getData(project + "weblogic") == "true") {// �Ƿ���weblogic
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
					File folderre = new File(filePath);// �����ļ���
					if (!folderre.exists() && !folderre.isDirectory()) {
						folderre.mkdirs();
					} else {
						System.out.println(filePath + "-�ļ����Ѵ���");
					}
					flag = deploy.copyFile(programBL, copyPath, false);// �жϻ����ļ��Ƿ����
					if (flag == 1) {
						least = "-" + least;
					}
					deploy.copyFile(programleast, copyPath, true);// report��Ŀ�����ļ����ļ���
				} else {
					System.out.println(programBL);
					flag = deploy.copyFile(programBL, outputfile + "\\"
							+ test[len - 1], false);// �жϻ����ļ��Ƿ����
					if (flag == 1) {
						least = "-" + least;
					}
					deploy.copyFile(programleast, outputfile + "\\"
							+ test[len - 1], true);// �����ļ�
				}

				lineTxtsc.add(least);
			}
		} catch (Exception e) {
			System.out.println("-�޷������ļ�");
			e.printStackTrace();
		}
		return lineTxtsc;
	}

	public static void NaturalCopy(String outputfile, ArrayList<String> lineTxt) {
		try {
			File folder = new File(outputfile);// �����ļ���
			if (!folder.exists() && !folder.isDirectory()) {
				folder.mkdirs();
			} else {
				System.out.println(outputfile + "-�ļ����Ѵ���");
			}

			for (int i = 0; i < lineTxt.size(); i++) {
				String newlineTxt = lineTxt.get(i);
				String test[] = newlineTxt.split("/");
				int len = test.length;
				String project = deploy.getData(test[1]) == null ? test[1]
						: deploy.getData(test[1]);// ������Ŀ��תΪ��������Ŀ��
				// String project = deploy.getData("project");//������Ŀ��תΪ��������Ŀ��
				System.out.println("�����ļ�������Ŀ����" + project);

				String programleast = deploy.getData(project + "original")
						+ newlineTxt.replaceAll("/", "\\\\");
				;// �������ַ

				deploy.copyFile(programleast,
						outputfile + "\\" + test[len - 1], true);// �����ļ�

			}
		} catch (Exception e) {
			System.out.println("-�޷������ļ�");
			e.printStackTrace();
		}
	}

	public static void CreateNewFile(String project, String outputfile,
			String filename, ArrayList<String> lineTxt) {
		try {
			File folder = new File(outputfile);// �����ļ���
			if (!folder.exists() && !folder.isDirectory()) {
				folder.mkdirs();
			} else {
			}
			String filePath = outputfile + "\\" + filename;// �����ļ�
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			} else {
				System.out.println(filePath + "-�ļ��Ѵ���");
			}
			FileWriter resultFile = new FileWriter(file);
			PrintWriter NewFile = new PrintWriter(resultFile);
			if (filename.equals("apprestore.sh") || filename == "apprestore.sh") {
				NewFile.print("#1 ��������  20100101\n");
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
							String temp = deploy.getData("appbackup.sh");// ���滻���
							temp = temp.replace("#project#", project);// ��Ŀ��
							temp = temp.replace("#version#",
									deploy.getData("version"));// �汾��
							String scfile = test[len - 1];
							temp = temp.replace("#filename#", scfile);// �����ļ���
							temp = temp.replace("#situation#", lineTxt.get(i));// ����һ��shell
							NewFile.print(temp + "\n");
						}
					} else if (filename.equals("apprestore.sh")) {
						if (!(lineTxt.get(i)).trim().substring(0, 1)
								.equals("-")) {
							String temp = deploy.getData("apprestore.sh");// ���滻���
							temp = temp.replace("#project#", project);// ��Ŀ��
							temp = temp.replace("#version#",
									deploy.getData("version"));// �汾��
							String scfile = test[len - 1];
							temp = temp.replace("#filename#", scfile);// �����ļ���
							temp = temp.replace("#situation#", lineTxt.get(i));// ����һ��shell
							NewFile.print(temp + "\n");
						} else {
							String temp = deploy.getData("rm");// ���滻���
							temp = temp.replace("#project#", project);// ��Ŀ��
							temp = temp.replace("#version#",
									deploy.getData("version"));// �汾��
							String scfile = test[len - 1];
							temp = temp.replace("#filename#", scfile);// �����ļ���
							temp = temp.replace("#situation#", lineTxt.get(i)
									.substring(1));// ����һ��shell
							NewFile.print(temp + "\n");
						}

					} else if (filename.equals("appupdate.sh")) {

						String temp = deploy.getData("appupdate.sh");// ���滻���
						temp = temp.replace("#project#", project);// ��Ŀ��
						temp = temp.replaceAll("#version#",
								deploy.getData("version"));// �汾��
						String scfile = test[len - 1];
						temp = temp.replace("#filename#", scfile);// �����ļ���
						if (!(lineTxt.get(i)).trim().substring(0, 1)
								.equals("-")) {
							temp = temp.replace("#situation#", lineTxt.get(i));// ����һ��shell
						} else {

							temp = temp.replace("#situation#", lineTxt.get(i)
									.substring(1));// ����һ��shell
						}
						NewFile.print(temp + "\n");
					}
				}

			}

			resultFile.close();
			System.out.println(file + "�ļ����ɳɹ�");
		} catch (Exception e) {
			System.out.println("*�޷��������ļ�");
			e.printStackTrace();
		}
	}

	public static void CreateNewFile(String project, String linuxproject,
			String outputfile, String filename, ArrayList<String> lineTxt) {
		try {
			File folder = new File(outputfile);// �����ļ���
			if (!folder.exists() && !folder.isDirectory()) {
				folder.mkdirs();
			} else {
			}
			String filePath = outputfile + "\\" + filename;// �����ļ�
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			} else {
				System.out.println(filePath + "-�ļ��Ѵ���");
			}
			FileWriter resultFile = new FileWriter(file);
			PrintWriter NewFile = new PrintWriter(resultFile);
			if (filename.equals("apprestore.sh") || filename == "apprestore.sh") {
				NewFile.print("#1 ��������  20100101\n");
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
							String temp = deploy.getData("appbackup.sh");// ���滻���
							temp = temp.replace("#project#", project);// ��Ŀ��
							temp = temp.replace("#version#",
									deploy.getData("version"));// �汾��
							String scfile = test[len - 1];
							temp = temp.replace("#filename#", scfile);// �����ļ���
							temp = temp.replace("#situation#", linuxproject
									+ lineTxt.get(i).substring(1));// ����һ��shell
							NewFile.print(temp + "\n");
						}
					} else if (filename.equals("apprestore.sh")) {
						if (!(lineTxt.get(i)).trim().substring(0, 1)
								.equals("-")) {
							String temp = deploy.getData("apprestore.sh");// ���滻���
							temp = temp.replace("#project#", project);// ��Ŀ��
							temp = temp.replace("#version#",
									deploy.getData("version"));// �汾��
							String scfile = test[len - 1];
							temp = temp.replace("#filename#", scfile);// �����ļ���
							temp = temp.replace("#situation#", linuxproject
									+ lineTxt.get(i).substring(1));// ����һ��shell
							NewFile.print(temp + "\n");
						} else {
							String temp = deploy.getData("rm");// ���滻���
							temp = temp.replace("#project#", project);// ��Ŀ��
							temp = temp.replace("#version#",
									deploy.getData("version"));// �汾��
							String scfile = test[len - 1];
							temp = temp.replace("#filename#", scfile);// �����ļ���
							temp = temp.replace("#situation#", linuxproject
									+ lineTxt.get(i).substring(2));// ����һ��shell
							NewFile.print(temp + "\n");
						}

					} else if (filename.equals("appupdate.sh")) {

						String temp = deploy.getData("appupdate.sh");// ���滻���
						temp = temp.replace("#project#", project);// ��Ŀ��
						temp = temp.replaceAll("#version#",
								deploy.getData("version"));// �汾��
						String scfile = test[len - 1];

						if (!(lineTxt.get(i)).trim().substring(0, 1)
								.equals("-")) {
							temp = temp.replace("#filename#", lineTxt.get(i)
									.substring(1));// �����ļ���
							temp = temp.replace("#situation#", linuxproject
									+ lineTxt.get(i).substring(1));// ����һ��shell
						} else {
							temp = temp.replace("#filename#", lineTxt.get(i)
									.substring(2));// �����ļ���
							temp = temp.replace("#situation#", linuxproject
									+ lineTxt.get(i).substring(2));// ����һ��shell
						}
						NewFile.print(temp + "\n");
					}
				}

			}

			resultFile.close();
			System.out.println(file + "�ļ����ɳɹ�");
		} catch (Exception e) {
			System.out.println("*�޷��������ļ�");
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
		outputfile = outputfile.replace("#date#", date);// �ļ����Ŀ¼
		String project = deploy.getData("project");// �����ļ���

		ArrayList<String> lineTxtsc = new ArrayList<String>();
		// NaturalCopy(outputfile, lineTxt);//����Դ����
		lineTxtsc = CopyFile(outputfile + "\\" + deploy.getData("version"),
				lineTxt);// �����ļ�
		// CreateNewFile(project,outputfile,"appbackup.sh",lineTxtsc);//�����ļ�
		// CreateNewFile(project,outputfile,"appupdate.sh",lineTxtsc);//�����ļ�
		// CreateNewFile(project,outputfile,"apprestore.sh",lineTxtsc);//�����ļ�
		TransferFile(project, "20170406", deploy.getData("version"), outputfile);// �ϴ�
																					// ���
																					// �����ļ�
		System.exit(0);

	}

	public static void main(String[] args) {
		String date = deploy.getLimitTime();
		String filePath = deploy.getData("BURINPUT");
		filePath = filePath.replace("#date#", date);
		ArrayList<String> lineTxt = new ArrayList<String>();
		lineTxt = deploy.ReadTxt(filePath, encode_GBK);
		String outputfile = deploy.getData("BUROUTPUT");
		outputfile = outputfile.replace("#date#", date);// �ļ����Ŀ¼
		String project = deploy.getData("project");// �����ļ���

		CreateNewFile(project,"/cadomain/domain_cnap2/applications/DefaultWebApp",outputfile, "appbackup.sh", lineTxt);// �����ļ�
		CreateNewFile(project,"/cadomain/domain_cnap2/applications/DefaultWebApp",outputfile, "appupdate.sh", lineTxt);// �����ļ�
		CreateNewFile(project,"/cadomain/domain_cnap2/applications/DefaultWebApp",outputfile, "apprestore.sh", lineTxt);// �����ļ�
		// TransferFile(project,"20170406",deploy.getData("version"),outputfile);//�ϴ�
		// ��� �����ļ�
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
