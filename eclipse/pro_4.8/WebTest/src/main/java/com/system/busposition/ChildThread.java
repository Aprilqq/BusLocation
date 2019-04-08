package com.system.busposition;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 
 * ���߳�ģ��
 * 		BIO���
 * @author Alienware-Rocky
 *
 */

public class ChildThread implements Runnable {

	private Socket socket; // ����ͨ�ŷ�������
	private volatile static boolean flag = true;

	public ChildThread(Socket socket) {

		this.socket = socket;

	}

	@Override
	public void run() {

		InputStreamReader reader = null;
		PrintWriter writer = null;

		try {

			//OutputStream ops = socket.getOutputStream(); // �������ͨ��
			//writer = new PrintWriter(new OutputStreamWriter(ops ,"UTF-8")); // ��װ
			
			InputStream ips = socket.getInputStream(); // ��������ͨ��
			reader = new InputStreamReader(ips, "UTF-8");
//			reader = new BufferedReader(new InputStreamReader(ips, "UTF-8")); // ��ȡ�ͻ��˵������������а�װ

			while (true) {
				
				// �ճ��ӳ�
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				/*
				 * ����������� �ж��Ƿ��Զ�̿ͻ��˶Ͽ����ӡ�
				 */
				try {
					socket.sendUrgentData(0xff);
				} catch (Exception e1) {
					System.out.println("�߳�" + Thread.currentThread().getName() + "��������");
					break;
				}
				
//				char[] rDataLen = new char[4];
//				reader.read(rDataLen, 0, 4);
//				int lenth = Integer.parseInt(new String(rDataLen));
//				char[] rContentTxt = new char[lenth];
//				reader.read(rContentTxt, 0, lenth);
//				new String(rDataLen) + new String(rContentTxt);
				String message = null;
				byte[] buf = new byte[1024];
	            int len=0;
	            len = ips.read(buf);
	            message = new String(buf, 0, len);
//	            while((len=reader.read(buf))!=-1){
//	                System.out.println(new String(buf,0,len) +" "+buf+" "+len);  //����
//		            len=reader.read(buf);
//		            message = new String(buf,0,len);
//		            System.out.println(message);
//	                System.out.println(buf+" "+len);
//	            }
					
//				do {
//					message += reader.read();
//				} while (reader.read() != -1);
//				while ((line = reader.readLine()) != null) {
//					message += line;		//��ȡ�ն˶���Ϣ
//				}
//				socket.setSoTimeout(3000);
//				message = reader.readLine();
				
				
				//-------------------------------------------------------------------------//
//				synchronized (this) {
				
//				WriteToMysql write = new WriteToMysql(message+"->"+Thread.currentThread().getName());
//				write.connect();
//				write.write();
				
//				}
				
				//-------------------------------------------------------------------------//
				System.out.println("�ͻ�����Ϣ��" + message + "-->" + Thread.currentThread().getName() + ":" + socket.getPort());
				System.out.println(Thread.currentThread().getName() + "~socket.isConnected:" + socket.isConnected());
				
				//writer.println("����");	// ���ն˷�����Ϣ
				//writer.flush();				// ˢ�������
				
			}

//			socket.shutdownOutput();	// �ر������ͨ��
			socket.shutdownInput();		// �ر�������ͨ��

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if (writer != null) {
				writer.close();
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println(socket);
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

}
