package com.system.busposition;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * ���߳�
 * 
 * @author Alienware-Rocky
 *
 */


public class StartThread implements Runnable {
	
	private ThreadPoolExecutor executor;	//�����̳߳�
	private static final int POOL_MAX = 20;
	private boolean flag = true;
	
	public StartThread() {
		
//		this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(POOL_MAX);	//��ʼ���̳߳���������߳���	
		
	}

	@Override
	public void run() {
		ServerSocket serverSocket = null;
		
		
		int GPSprot = 8001;
		System.out.println("-------------�����˿�GPSport: "+GPSprot+"-------------");
		
		/*
		 * �����������������ػ��߳� 
		 */
		NIOServerSocket nss = new NIOServerSocket(GPSprot);
		EchoServer echoServer = new EchoServer();
		try {
			
//			serverSocket = new ServerSocket(GPSprot);	//�����غ�˿�
			//---------------------------------------------------------------//
//			echoServer.setPort(GPSprot);
//			echoServer.init();
			//---------------------------------------------------------------//

//			while(flag) {
							
				nss.start();
				
				
//				try {
//					echoServer.bind();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
				
//				executor.setMaximumPoolSize(POOL_MAX);	//�����̳߳����������
//				executor.setCorePoolSize(4);	//�����̳߳غ����̸߳���
				
				
				// ������һֱ�ȴ��ͻ�������,����������;�����󷵻������Socket����
//				Socket clientSocket = serverSocket.accept();
				
//				System.out.println(executor.getMaximumPoolSize()+" "+executor.getCorePoolSize()+" "+executor.getQueue());

//				executor.submit(new ChildThread(clientSocket));	//�������̣߳������̳߳��ύ�߳�����
//				ChildThread childThread = new ChildThread(clientSocket);
//				new Thread(childThread).start();
				
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (nss != null) {
				nss.shutdown();
			}
//			if (echoServer != null) {
//				echoServer.shutdown();
//			}
		}
	}
}
