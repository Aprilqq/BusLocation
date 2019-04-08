package com.system.busposition;

import java.util.StringTokenizer;

//import org.junit.Test;

/**
 * 
 * @author Alienware
 *
 */

public class ParseGPS {
	private char lats;
	private char lngs;
	private double speed;
	private boolean valid;
	/*
	 * �������ڽ��յ����ֽڴ���RMC���Ƽ���λ��Ϣ
	 *
	 * $GPRMC,013946,A,3202.1855,N,11849.0769,E,0.05,218.30,111105,4.5,W,A*20..
	 * $GPRMC,092927.000,A,2235.9058,N,11400.0518,E,0.000,74.11,151216,,D*49
	 * ������⣺$GPRMC,<1>,<2>,<3>,<4>,<5>,<6>,<7>,<8>,<9>,<10>,<11>,<12>*hh
	 * <1> UTC ʱ�䣬hhmmss(ʱ����)��ʽ 
	 * <2> ��λ״̬��A=��Ч��λ��V=��Ч��λ 
	 * <3>γ��ddmm.mmmm(�ȷ�)��ʽ(ǰ���0Ҳ��������) 
	 * <4> γ�Ȱ���N(������)��S(�ϰ���) 
	 * <5>����dddmm.mmmm(�ȷ�)��ʽ(ǰ���0Ҳ��������) 
	 * <6> ���Ȱ���E(����)��W(����) 
	 * <7>��������(000.0~999.9�ڣ�ǰ���0Ҳ��������) 
	 * <8>���溽��(000.0~359.9�ȣ����汱Ϊ�ο���׼��ǰ���0Ҳ��������) 
	 * <9> UTC ���ڣ�ddmmyy(������)��ʽ 
	 * <10>��ƫ��(000.0~180.0�ȣ�ǰ���0Ҳ��������) 
	 * <11> ��ƫ�Ƿ���E(��)��W(��) 
	 * <12>ģʽָʾ(��NMEA01833.00�汾�����A=������λ��D=��֣�E=���㣬N=������Ч)
	 *
	 *
	 * ����ֵ 0 ��ȷ 1У��ʧ�� 2��GPRMC��Ϣ 3��Ч��λ 4��ʽ���� 5У�����
	 */
	public int parseGPRMC(String by) {
		if (by == null || by.isEmpty())// �жϷǿ�
			return 4;
		if (checksum(by.getBytes()) == false)// ����У��Ͳ�������е�У��ͱȽ�
			return 5;
		StringTokenizer str = new StringTokenizer(new String(by), ",");
		String temp = null;
		temp = str.nextToken();// ȡ��һ���Ӵ������
		if (!temp.equals("$GPRMC"))// ȷ����$GPRMC
			return 2;
		temp = str.nextToken();// ʱ��
		short hour = (short) Integer.parseInt(temp.substring(0, 2));
		short minute = (short) Integer.parseInt(temp.substring(2, 4));
		short second = (short) Float.parseFloat(temp.substring(4));
		temp = str.nextToken();// ��λ״̬
		if (temp.length() != 1)// ��
			return 42;
		else if (temp.charAt(0) == 'V')// ΪA����Ч ΪV����Ч
			return 3;
		temp = str.nextToken();// γ��
		double lat = Double.parseDouble(temp.substring(0, 2));// γ��-��
		lat += Double.parseDouble(temp.substring(2)) / 60;// γ��-��
		temp = str.nextToken();// γ�Ȱ���
		if (temp.length() != 1)
			return 44;
		else if (temp.charAt(0) == 'N')
			lats = 'N';
		else if (temp.charAt(0) == 'S')
			lats = 'S';
		else // ������Ϣ
			return 45;
		temp = str.nextToken();// ����
		double lng = Double.parseDouble(temp.substring(0, 3));// ����-��
		lng += Double.parseDouble(temp.substring(3)) / 60;// ����-��
		temp = str.nextToken();// ���Ȱ���
		if (temp.length() != 1)
			return 47;
		else if (temp.charAt(0) == 'E')
			lngs = 'E';
		else if (temp.charAt(0) == 'W')
			lngs = 'W';
		else
			return 48;
		temp = str.nextToken();// ��������
		if (!temp.isEmpty()) {
			speed = Double.parseDouble(temp) * 1.852;// �ٶȵ�λת��Ϊǧ��ÿСʱ
		}
		this.valid = true;// ת���ɹ���������������Ч
		System.out.println("ʱ�䣺"+hour+":"+minute+":"+second);
		System.out.println("��λ��"+lat+","+lng);
		System.out.println("�ٶȣ�"+speed+"km/h");
		return 0;
	}

	private boolean checksum(byte[] b) {
		byte chk = 0;// У���
		byte cb = b[1];// ��ǰ�ֽ�
		int i = 0;
		if (b[0] != '$')
			return false;
		for (i = 2; i < b.length; i++)// ����У���
		{
			if (b[i] == '*')
				break;
			cb = (byte) (cb ^ b[i]);
		}
		if (i != b.length - 3)// У��λ������
			return false;
		i++;
		byte[] bb = new byte[2];// ���ڴ��������λ
		bb[0] = b[i++];
		bb[1] = b[i];
		try {
			chk = (byte) Integer.parseInt(new String(bb), 16);// ����λת��Ϊһ���ֽ�
		} catch (Exception e)// ����λ�޷�ת��Ϊһ���ֽڣ���ʽ����
		{
			return false;
		}
		System.out.println("У����Ϣ");
		System.out.println(" ԭ�ģ�" + chk);
		System.out.println(" ���㣺" + cb);
		return chk == cb;// �������У��������������Ƿ�һ��
	}
	
	/*@Test
	public void Test() {
		ParseGPS parseGPS = new ParseGPS();
		String sign = "$GPRMC,024813.640,A,3158.4608,N,11848.3737,E,10.05,324.27,150706,A*50";
		switch (parseGPS.parseGPRMC(sign)) {
		case 0:
			System.out.println("��λ�ɹ���");
			break;
		case 1:
			System.err.println("У��ʧ��");
			break;
		case 2:
			System.err.println("��GPRMC��Ϣ");
			break;
		case 3:
			System.err.println("��Ч��λ");
			break;
		case 4:
			System.err.println("��ʽ����");
			break;
		case 5:
			System.err.println("У�����");
			break;
		default:
			System.err.println("��λ��Ϣ��Ч");
			break;
		}
	}*/
}