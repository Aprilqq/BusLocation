package com.system.busposition;

import java.io.IOException;
import java.net.InetSocketAddress;
//import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * NIO���
 * 
 * @author Alienware-Rocky
 *
 */

public class NIOServerSocket {
	private Selector selector;					// ѡ����
	private ServerSocketChannel socketChannel;	// ͨ�Źܵ�
//	private static ServerSocket serverSocket;	// ͨ�ŷ���
	private int port;

	public NIOServerSocket(int port) {
		this.port = port;
	}

	
	/*
	 * 	NIO��������ʼ��
	 */
	public void init() throws IOException {
		
		selector = Selector.open();						// ����ͨ��ѡ����������
		socketChannel = ServerSocketChannel.open();		// ����ServerSocketChannelͨ��������
		socketChannel.configureBlocking(false);			// ���÷�����ģʽ��trueΪ������
		
	}
	
	/*
	 * 	����������
	 */
	public void start() {
		
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			
//			serverSocket = socketChannel.socket();							// 
			socketChannel.bind(new InetSocketAddress(port));				// ���ŵ��󶨵�ָ���˿�
			socketChannel.register(selector, SelectionKey.OP_ACCEPT);		// �ѷ�����ͨ��ע�ᵽ��·�������ϣ����Ҽ��������¼�
			
			while (true) {
				
				int channels = selector.select();		// ����ѡ������ʼ�����˿ڣ�����ȡ����ͨ��
				if (channels <= 0) {					// �ж��Ƿ���ڿ��õ�ͨ��
					continue;
				}
				
				Set<SelectionKey> selectionKeys = selector.selectedKeys();	// ������е�keys
				Iterator<SelectionKey> iterator = selectionKeys.iterator();	// ʹ��iterator�������е�keys
				SelectionKey key = null;
				
				try {
					
					while (iterator.hasNext()) {		// ѭ������SelectionKeySet�е����е�SelectionKey
						
						key = iterator.next();			// ѡ��һ��Ԫ��
						iterator.remove();				// ������Ҫ�ֶ��Ӽ������Ƴ���ǰ��key
						
						try {
							
							handleEvent(selector,key);	// �Զ����¼�����
							
						} catch (Exception e) {
							if (key != null) {
								key.cancel();
								if (key.channel() != null)
									key.channel().close();
							}
						}
						
					}
					
				} catch (Exception e) {
					System.err.println("ͨ������ʧ�ܣ�");
					e.printStackTrace();
				} finally {
					if (selectionKeys != null) {
						selectionKeys.clear();
					}
				}
			}
		} catch (IOException e) {
			System.err.println("��������ʧ��! ");
			e.printStackTrace();
		} finally {
			shutdown();
		}
	}
	
	public void shutdown() {
		if (socketChannel != null) {
			try {
				socketChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (selector != null) {
			try {
				selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void handleEvent(Selector selector, SelectionKey key) throws IOException {

		if (key.isValid()) {		// �������Ч�ģ���ִ����һ��
			// �����½����������Ϣ
			if (key.isAcceptable()) {
				
				ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				SocketChannel sc = ssc.accept();		// �������������ͨ��
				System.out.println("�ͻ�����������...");
				sc.configureBlocking(false);			// ���÷�����ģʽ
				sc.register(selector, SelectionKey.OP_READ);	// ע���ͨ��Ϊ�ɶ���д״̬
				System.out.println("Client: " + sc.getRemoteAddress() + " --> ���ӳɹ�! ");
				
			}else if (key.isReadable()) {		// �ж�ͨ���Ƿ�ɶ�
				
				SocketChannel sc = (SocketChannel) key.channel();		// ��ȡ֮ǰע���socketͨ������
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);		// ���û�����
				int readBytes = sc.read(readBuffer);					// ��ͨ���е����ݶ���������
				
				if (readBytes > 0) {
					
					readBuffer.flip();									// ����������ж�ȡ����ȡ֮ǰ��Ҫ���и�λ����(��position ��limit���и�λ)
					byte[] bytes = new byte[readBuffer.remaining()];	// ���ݻ����������ݳ��ȴ�����Ӧ��С��byte���飬���ջ�����������
					readBuffer.get(bytes);		// ���ջ�������
					String request = new String(bytes, "GBK"); 		// ���յ������룬�����ñ����ʽ
					System.out.println("Client: " + sc.getRemoteAddress() + " --> msg: " + request);
					
					//-------------------------------------------------------------------------//
					
					WriteToMysql write = new WriteToMysql(request);
					write.connect();
					write.write();
					
					//-------------------------------------------------------------------------//
					
					String response = " ���ճɹ�! ";
					doWrite(sc, response);
					
				} else if (readBytes < 0) {
					// �Զ���·�ر�
					key.cancel();
					sc.close();
				} else
					; // ����0�ֽڣ�����
				
				readBuffer.clear();
			}
		}
	}

	public static void doWrite(SocketChannel channel, String response) throws IOException {
		
		if (response != null && response.trim().length() > 0) {
			
			byte[] bytes = response.getBytes();
			ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			channel.write(writeBuffer);
			writeBuffer.clear();
			writeBuffer.compact();		// Ϊ�����ڳ�����ռ�
		}
	}
}
