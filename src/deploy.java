
import java.io.*;
import java.util.*;

public class deploy {
	public final static String encode_GB2312 = "GB2312";
	public final static String encode_GBK = "GBK";
	public final static String encode_UTF8 = "UTF8";
	public final static String encode_ISO8859_1 = "8859_1";

	/**
	 * ˽������������ȫ�����ò����Ƿ��ʼ��
	 */
	private static boolean is_inited = false;

	/**
	 * ˽������������ϵͳȫ�����ò���
	 */
	private static HashMap deployHashMap = null;

	/**
	 * ��������������Ӧ�����ø�Ŀ¼
	 */
	public static String ConfigHome = "E:\\workspaces\\deploy\\src\\";// deploy.txt����Ŀ¼

	public deploy() {
		if (!is_inited) {
			is_inited = init();
		}
	}

	public static boolean init() {
		if (!is_inited) {
			try {
				deployHashMap = new HashMap();
				File file = new File(ConfigHome + "deploy.txt");
				if (file.exists()) {
					java.io.BufferedReader br = new BufferedReader(
							new FileReader(file));
					String rc = "";
					while ((rc = br.readLine()) != null) {
						if (!rc.trim().equals("") && rc.indexOf("=") > 0
								&& rc.trim().substring(0, 1) != "#") {
							String DataName = rc.substring(0, rc.indexOf("="));
							String DataValue = rc
									.substring(rc.indexOf("=") + 1);
							if (DataValue != null
									&& !DataValue.trim().equals("")) {
								deployHashMap.put(DataName, DataValue);
							}
						}
					}
				} else {
					throw new Exception(ConfigHome + "deploy.txt �����ļ������ڡ�");
				}
				System.out.println(deploy.getCurrTimestamp() + " �����ļ���");
			} catch (Exception ce) {
				System.out.println(getCurrTimestamp() + "--------------------");
				ce.printStackTrace();
				deployHashMap = null;
				return false;
			}
		}
		return true;
	}

	/**
	 * �������������³�ʼ��
	 * 
	 * @return boolean
	 */
	public static boolean reinit() {
		is_inited = false;
		is_inited = init();
		return is_inited;
	}

	/**
	 * ������������deployHashMap�в�ѯ������Ϣ����̬����
	 * 
	 * @param tagName
	 *            ��������
	 * @return String ����ֵ
	 */
	public static String getData(String tagName) {
		if (deployHashMap == null) {
			if (init()) {
				return (String) deployHashMap.get(tagName);
			} else {
				System.out.println(getCurrTimestamp() + "���ó�ʼ��δ�ɹ���");
				return new String("");
			}
		} else {
			return (String) deployHashMap.get(tagName);
		}
	}

	/**
	 * ������������ȡ��������
	 * 
	 * @return HashMap ��������
	 */
	public static HashMap getHashMap() {
		return deployHashMap;
	}

	/**
	 * ������������ȡ��Ӧ��ʽʱ��
	 * 
	 * @return 2016��9��6��
	 */
	public static String getLimitTime() {

		Calendar now = Calendar.getInstance();
		return now.get(Calendar.YEAR) + "��" + (now.get(Calendar.MONTH) + 1)
				+ "��" + now.get(Calendar.DAY_OF_MONTH) + "��";

	}

	/**
	 * ��������������ϵͳ��ǰʱ���޷ָ�����Date
	 * 
	 * @param SplitChar
	 *            ���ڵķָ� ������- / ��մ�"" ��"������"
	 * @return String ϵͳ��ǰʱ���ַ���
	 */
	public static String getCurrDate(String SplitChar) {
		java.text.SimpleDateFormat sdf = null;
		if (SplitChar.equals("������")) {
			sdf = new java.text.SimpleDateFormat("yyyy��MM��dd��");
		} else {
			sdf = new java.text.SimpleDateFormat("yyyy" + SplitChar + "MM"
					+ SplitChar + "dd");
		}
		java.util.Date d = new java.util.Date();
		d.setTime(System.currentTimeMillis());
		return sdf.format(d);
	}

	/**
	 * ������������ȡ�����ı����� ��/* ��//����������
	 * 
	 * @return ���д���ArrayList
	 */
	public static ArrayList<String> ReadTxt(String filePath, String encoding) {
		ArrayList<String> lineTxt = new ArrayList<String>();
		try {

			File file = new File(filePath);
			if (file.isFile() && file.exists()) {// �ж��ļ��Ƿ����
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// �����ʽ
				java.io.BufferedReader br = new BufferedReader(read);
				String rc = "";
				while ((rc = br.readLine()) != null) {
					if (!rc.trim().equals("")
							&& rc.trim().substring(0, 1) != "/") {
						String DataValue = rc;
						if (DataValue != null && !DataValue.trim().equals("")) {
							if (!lineTxt.contains(DataValue))
								lineTxt.add(DataValue);
						}
					}

				}

				System.out.println(filePath + "�ļ���ȡ���");
			} else {
				System.out.println("�Ҳ���ָ�����ļ�");
			}

		} catch (Exception e) {
			System.out.println("��ȡ�ļ����ݳ���");
			e.printStackTrace();
		}
		return lineTxt;

	}

	/**
	 * ��������������ϵͳ��ǰʱ��Timestamp,��java.sql.Timestamp����Ϊ���ظ�ʽ
	 * 
	 * @return String ϵͳ��ǰʱ���ַ���
	 */
	public static int copyFile(String oldPath, String newPath, boolean tran_flag) {
		int flag = 0;
		try {

			int bytesum = 0;
			int byteread = 0;

			File oldfile = new File(oldPath);
			if (oldfile.exists()) {// �ļ�����ʱ
				if (tran_flag) {
					InputStream inStream = new FileInputStream(oldPath);// ��ȡԭ�ļ�
					FileOutputStream outStream = new FileOutputStream(newPath);
					byte[] buffer = new byte[1444];
					while ((byteread = inStream.read(buffer)) != -1) {
						bytesum += byteread;// �ֽ��� �ļ���С
						outStream.write(buffer, 0, byteread);
					}
					System.out.println(oldPath + "�ļ����Ƴɹ����ֽ�����" + bytesum);
					inStream.close();
				}
			} else {
				if (tran_flag) {
					System.out.println(oldPath + "*�ļ�������");
				}
				flag = 1;
			}
		} catch (Exception e) {
			System.out.println("*�����ļ�����");
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * ��������������ϵͳ��ǰʱ��Timestamp,��java.sql.Timestamp����Ϊ���ظ�ʽ
	 * 
	 * @return String ϵͳ��ǰʱ���ַ���
	 */
	public static String getCurrTimestamp() {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SS");
		java.util.Date d = new java.util.Date();
		d.setTime(System.currentTimeMillis());
		String time = sdf.format(d);
		if (time.length() < 23) {
			for (int i = 0; i < (23 - time.length()); i++) {
				time += "0";
			}
		}
		return time;
	}

	public static void main(String[] args) throws Exception {
		deploy.init();

	}
}
