package com.system.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import com.system.busposition.StartThread;


/**
 * ������
 * 
 * @author Alienware-Rocky
 *
 */

public class GPSThreadLietener extends HttpServlet implements ServletContextListener {

    
    public GPSThreadLietener() {
    	
    }

    public void contextDestroyed(ServletContextEvent arg0)  { 
    	System.out.println("----------- GPS����ϵͳֹͣ -----------");
    }

    public void contextInitialized(ServletContextEvent arg0)  { 
    	System.out.println("----------- GPS����ϵͳ���� -----------");
    	
    	Thread startThread = new Thread(new StartThread());	//�������̶߳���
    	startThread.setName("MyThread");
    	startThread.setDaemon(true);	//����Ϊ�ػ��߳�
    	startThread.start();			//�������߳�
    	
    	System.out.println("----------- GPS�źż�����ʼ -----------");
    }
	
}
