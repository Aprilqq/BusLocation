package org.zstuky.wechat.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zstuky.wechat.message.resp.Article;
import org.zstuky.wechat.message.resp.NewsMessage;
import org.zstuky.wechat.message.resp.TextMessage;
import org.zstuky.wechat.util.MessageUtil;

import com.hibernatespring.*;
//import com.myeclipse.persistencelayer.PersistenceLayerLinyx;
import com.persistencelayer.PersistenceLayerLiuhx;
//import com.myeclipse.persistencelayer.PersistenceLayerHuangzy;
/** 
 * ���ķ����� 
 *  
 * @author liufeng 
 * @date 2013-05-20 
 */  
public class CoreService {
	
	static ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml"); 
    /** 
     * ����΢�ŷ��������� 
     *  
     * @param request 
     * @return 
     */  
    public static String processRequest(HttpServletRequest request) {  
        String respMessage = null;
        //��վ��ַ
        String path = request.getContextPath();
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
      //  PersistenceLayerHuangzy persistenceLayer3 = (PersistenceLayerHuangzy) ctx.getBean("persistenceLayerHuangzy"); 
        try {  
            // Ĭ�Ϸ��ص��ı���Ϣ����  
            String respContent = "�������쳣�����Ժ��ԣ�";  
  
            // xml�������  
            Map<String, String> requestMap = MessageUtil.parseXml(request);  
  
            // ���ͷ��ʺţ�open_id��  
            String fromUserName = requestMap.get("FromUserName");  
            // �����ʺ�  
            String toUserName = requestMap.get("ToUserName");  
            // ��Ϣ����  
            String msgType = requestMap.get("MsgType");  
  
            // Ĭ�ϵĻظ���Ϣ  
            TextMessage textMessage = new TextMessage();  
            textMessage.setToUserName(fromUserName);  
            textMessage.setFromUserName(toUserName);  
            textMessage.setCreateTime(new Date().getTime());  
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);  
            textMessage.setFuncFlag(0);
 
            // �ı���Ϣ  
          //�����û���ָ����Ϣ
            String content = requestMap.get("Content"); 
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
            	//Ĭ�ϻظ����˵�
            	respContent = getMainMenu();
                textMessage.setContent(respContent);  
                respMessage = MessageUtil.textMessageToXml(textMessage);
                
                //��ָ����зָ�
                String[] sourceStrArray = content.split("\\+");
                
                PersistenceLayerLiuhx persistenceLayer2 = (PersistenceLayerLiuhx) ctx.getBean("persistenceLayerLiuhx");
                
                if(sourceStrArray[0].equalsIgnoreCase("c1"))
                {
                	//����ظ�����Ϣ����֤������Ϣ
            		int flag2 = persistenceLayer2.testJobNumber(fromUserName,sourceStrArray[1]);
        			if (flag2==0) {
        				respContent = "���ظ��Ĺ��ź���֤�벻��ȷ�������Ǹ�ʽ�������������룡";  
                        textMessage.setContent(respContent);  
                        respMessage = MessageUtil.textMessageToXml(textMessage);
        			}
        			else {
        				respContent = "����ͨ����֤��лл��ע��";  
                        textMessage.setContent(respContent);  
                        respMessage = MessageUtil.textMessageToXml(textMessage);
        			}
                }
                else  if(sourceStrArray[0].equalsIgnoreCase("c2"))
                {
                	   //����ظ�����Ϣ�����İ칫�绰
            		int flag2 = persistenceLayer2.updateTelphone(fromUserName,sourceStrArray[1]);
        			if (flag2==1) {
        				respContent = "���İ칫�绰�Ѿ��ɹ�����";  
                        textMessage.setContent(respContent);  
                        respMessage = MessageUtil.textMessageToXml(textMessage);
        			}
        			else {
        				respContent = "����ʧ�ܣ����Ժ�";  
                        textMessage.setContent(respContent);  
                        respMessage = MessageUtil.textMessageToXml(textMessage);
        			}
                }
                else if(sourceStrArray[0].equalsIgnoreCase("c3"))
                {
                	   //����ظ�����Ϣ�������ֻ�����
            		int flag2 = persistenceLayer2.updateMobile(fromUserName,sourceStrArray[1]);
        			if (flag2==1) {
        				respContent = "�����ֻ������Ѿ��ɹ�����";  
                        textMessage.setContent(respContent);  
                        respMessage = MessageUtil.textMessageToXml(textMessage);
        			}
        			else {
        				respContent = "����ʧ�ܣ����Ժ����ԣ�";  
                        textMessage.setContent(respContent);  
                        respMessage = MessageUtil.textMessageToXml(textMessage);
        			}
                }
                else{ 
                	respContent = "��û�а���Ҫ��������룬���������룡��"; 
                    textMessage.setContent(respContent);  
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }
            	/*respContent = getMainMenu();
                textMessage.setContent(respContent);  
                respMessage = MessageUtil.textMessageToXml(textMessage);
                //�����û���ָ����Ϣ
                String content = requestMap.get("Content"); 
                // ����ͼ����Ϣ  
                NewsMessage newsMessage = new NewsMessage();  
                newsMessage.setToUserName(fromUserName);  
                newsMessage.setFromUserName(toUserName);  
                newsMessage.setCreateTime(new Date().getTime());  
                newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);  
                newsMessage.setFuncFlag(0);      
                List<Article> articleList = new ArrayList<Article>();
                
                // ѧԺ�����ĸ���
                if ("A00".equals(content)) {
                	Article article1 = new Article();  
                    article1.setTitle("�����쵼");  
                    article1.setDescription("");  
                    article1.setPicUrl(basePath+"images/KyMen.jpg");  
                    article1.setUrl("http://www.ky.zstu.edu.cn/mypage/index.php?name=leader");
                    
                    Article article2 = new Article();  
                    article2.setTitle("ѧԺλ��ͼ");  
                    article2.setDescription("");  
                    article2.setPicUrl(basePath+"images/KyTi.jpg");  
                    article2.setUrl("http://cn.bing.com/ditu/default.aspx?rtp=adr.~pos.30.28429_120.0224_%e6%b5%99%e6%b1%9f%e7%9c%81%e6%9d%ad%e5%b7%9e%e5%b8%82%e6%96%87%e4%b8%80%e8%a5%bf%e8%b7%af960%e5%8f%b7_%e6%b5%99%e6%b1%9f%e7%90%86%e5%b7%a5%e5%a4%a7%e5%ad%a6%e7%a7%91%e6%8a%80%e4%b8%8e%e8%89%ba%e6%9c%af%e5%ad%a6%e9%99%a2-%e7%a7%91%e5%ad%a6%e9%a6%86_&where1=%e6%b5%99%e6%b1%9f%e7%9c%81%e6%9d%ad%e5%b7%9e%e5%b8%82%e6%96%87%e4%b8%80%e8%a5%bf%e8%b7%af960%e5%8f%b7&mode=T&FORM=LLDP#JnE9LiUyNXU2ZDU5JTI1dTZjNWYlMjV1NzcwMSUyNXU2NzZkJTI1dTVkZGUlMjV1NWUwMiUyNXU2NTg3JTI1dTRlMDAlMjV1ODk3ZiUyNXU4ZGVmOTYwJTI1dTUzZjclN2Vzc3QuMCU3ZXBnLjEmYmI9MzAuMjg5MzM5MTM4NzQxMiU3ZTEyMC4wMzMyODk3Njg2JTdlMzAuMjc5MjQwNjAxNDAwOCU3ZTEyMC4wMTE1MTAyMzE0");
                    
                    Article article3 = new Article();  
                    article3.setTitle("ѧԺ��ͼ");  
                    article3.setDescription("");  
                    article3.setPicUrl(basePath+"images/KyKe.jpg");  
                    article3.setUrl("http://www.ky.zstu.edu.cn/mypage/index.php?name=pics");
  
                    articleList.add(article1);  
                    articleList.add(article2);  
                    articleList.add(article3); 

                    newsMessage.setArticleCount(articleList.size());  
                    newsMessage.setArticles(articleList);  
                    respMessage = MessageUtil.newsMessageToXml(newsMessage); 
                }
                // �칫����ĸ���
                else if ("B00".equals(content)) {
                	Article article1 = new Article();  
                    article1.setTitle("�칫�绰");  
                    article1.setDescription("");  
                    article1.setPicUrl(basePath+"images/KyMen.jpg");  
                    article1.setUrl("http://www.ky.zstu.edu.cn/mypage/index.php?name=zuzhi");
                    
                    Article article2 = new Article();  
                    article2.setTitle("У������");  
                    article2.setDescription("");  
                    article2.setPicUrl(basePath+"images/KyZhong.jpg");  
                    article2.setUrl("http://www.zucc.edu.cn/index.php?c=index&a=tlist&catid=58");
                    
                    Article article3 = new Article();  
                    article3.setTitle("ѧԺУ��");  
                    article3.setDescription("");  
                    article3.setPicUrl(basePath+"images/KyMei.jpg");  
                    article3.setUrl("http://www.zucc.edu.cn/index.php?c=index&a=tlist&catid=55");
  
                    articleList.add(article1);  
                    articleList.add(article2);  
                    articleList.add(article3); 

                    newsMessage.setArticleCount(articleList.size());  
                    newsMessage.setArticles(articleList);  
                    respMessage = MessageUtil.newsMessageToXml(newsMessage); 
                }
                // ���˲������ظ����˲����Ĳ˵�
                else if ("3".equals(content)) {
                	//respContent = getPersonalMenu();
                    //textMessage.setContent(respContent);  
                    //respMessage = MessageUtil.textMessageToXml(textMessage);
                }*/
                
            }  
            // ͼƬ��Ϣ  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {  
                respContent = "�����͵���ͼƬ��Ϣ��"; 
                textMessage.setContent(respContent);  
                respMessage = MessageUtil.textMessageToXml(textMessage);
            }  
            // ����λ����Ϣ  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {  
                respContent = "�����͵��ǵ���λ����Ϣ��"; 
                textMessage.setContent(respContent);  
                respMessage = MessageUtil.textMessageToXml(textMessage);
            }  
            // ������Ϣ  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {  
                respContent = "�����͵���������Ϣ��"; 
                textMessage.setContent(respContent);  
                respMessage = MessageUtil.textMessageToXml(textMessage);
            }  
            // ��Ƶ��Ϣ  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {  
                respContent = "�����͵�����Ƶ��Ϣ��"; 
                textMessage.setContent(respContent);  
                respMessage = MessageUtil.textMessageToXml(textMessage);
            }  
            // �¼�����  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {  
                // �¼�����  
                String eventType = requestMap.get("Event");  
                // ����  
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {  
                    respContent = "лл���Ĺ�ע��";  
                    textMessage.setContent(respContent);  
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }  
                // ȡ������  
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {  
                    // TODO ȡ�����ĺ��û����ղ������ںŷ��͵���Ϣ����˲���Ҫ�ظ���Ϣ  
                }  
                // �Զ���˵�����¼�  
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
                	// �¼�KEYֵ���봴���Զ���˵�ʱָ����KEYֵ��Ӧ  
                	String eventKey = requestMap.get("EventKey"); 
                	// ����ͼ����Ϣ  
                    NewsMessage newsMessage = new NewsMessage();  
                    newsMessage.setToUserName(fromUserName);  
                    newsMessage.setFromUserName(toUserName);  
                    newsMessage.setCreateTime(new Date().getTime());  
                    newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);  
                    newsMessage.setFuncFlag(0);   
                    
                    
                	if ("C00".equalsIgnoreCase(eventKey)) {
                		PersistenceLayerLiuhx persistenceLayer2 = (PersistenceLayerLiuhx) ctx.getBean("persistenceLayerLiuhx");
                		int flag = persistenceLayer2.getWeChatOpenIdFlag(fromUserName);
                    	if (flag==0) {
            				respContent = "��ظ����Ĺ��ţ���֤�롣�磺c1+20082254,123456,����20082254�ǹ��ţ�123456����֤�룡";  
                            textMessage.setContent(respContent);  
                            respMessage = MessageUtil.textMessageToXml(textMessage);
            			}
            			else {
            				respContent = "���Ѿ��󶨣������ظ��󶨣�";  
                            textMessage.setContent(respContent);  
                            respMessage = MessageUtil.textMessageToXml(textMessage);
            			}
            			
            			
                    }	//end of �����Ĺ���
                    
                    // ���İ칫�绰
                    else if ("C03".equalsIgnoreCase(eventKey)) {
                		//PersistenceLayerLinyx persistenceLayer = (PersistenceLayerLinyx) ctx.getBean("persistenceLayerLinyx");
                		PersistenceLayerLiuhx persistenceLayer2 = (PersistenceLayerLiuhx) ctx.getBean("persistenceLayerLiuhx");
                		String jobNumber = persistenceLayer2.getJobNumberByOpenID(fromUserName);
                		String telephone = persistenceLayer2.getTelephone(fromUserName);
            			if (jobNumber==null || jobNumber.equalsIgnoreCase("")) {
            				respContent = "����΢���˻���δ�빤�Ű󶨣�";  
                            textMessage.setContent(respContent);  
                            respMessage = MessageUtil.textMessageToXml(textMessage);
            			}
            			else {
            				respContent = "��ظ����������°칫�绰����:c2+85857153,�����ڵİ칫�绰��:"+telephone;  
                            textMessage.setContent(respContent);  
                            respMessage = MessageUtil.textMessageToXml(textMessage);

            			}
                    }	//end of ���İ칫�绰
                    
                    // �����ֻ�����
                    else if ("C04".equalsIgnoreCase(eventKey)) {
                		//PersistenceLayerLinyx persistenceLayer = (PersistenceLayerLinyx) ctx.getBean("persistenceLayerLinyx");
                		PersistenceLayerLiuhx persistenceLayer2 = (PersistenceLayerLiuhx) ctx.getBean("persistenceLayerLiuhx");
                		String jobNumber = persistenceLayer2.getJobNumberByOpenID(fromUserName);
                		String mobile = persistenceLayer2.getMobile(fromUserName);
                		if (jobNumber==null || jobNumber.equalsIgnoreCase("")) {
            				respContent = "����΢���˻���δ�빤�Ű󶨣�";  
                            textMessage.setContent(respContent);  
                            respMessage = MessageUtil.textMessageToXml(textMessage);
            			}
            			else {
            				respContent = "��ظ������������ֻ����룡��:c3+15868483883,�����ڵİ칫�绰��:"+mobile;  
                            textMessage.setContent(respContent);  
                            respMessage = MessageUtil.textMessageToXml(textMessage);
                         
            			}
                    }	//end of �����ֻ�����
               /*     
                    List<Article> articleList = new ArrayList<Article>();
                    // ѧԺ�����ĸ���
                    if ("A00".equals(eventKey)) {
                    	Article article1 = new Article();  
                        article1.setTitle("�����쵼");  
                        article1.setDescription("");  
                        article1.setPicUrl(basePath+"images/KyMen.jpg");  
                        article1.setUrl("http://www.ky.zstu.edu.cn/mypage/index.php?name=leader");
                        
                        Article article2 = new Article();  
                        article2.setTitle("ѧԺλ��ͼ");  
                        article2.setDescription("");  
                        article2.setPicUrl(basePath+"images/KyTi.jpg");  
                        article2.setUrl("http://cn.bing.com/ditu/default.aspx?rtp=adr.~pos.30.28429_120.0224_%e6%b5%99%e6%b1%9f%e7%9c%81%e6%9d%ad%e5%b7%9e%e5%b8%82%e6%96%87%e4%b8%80%e8%a5%bf%e8%b7%af960%e5%8f%b7_%e6%b5%99%e6%b1%9f%e7%90%86%e5%b7%a5%e5%a4%a7%e5%ad%a6%e7%a7%91%e6%8a%80%e4%b8%8e%e8%89%ba%e6%9c%af%e5%ad%a6%e9%99%a2-%e7%a7%91%e5%ad%a6%e9%a6%86_&where1=%e6%b5%99%e6%b1%9f%e7%9c%81%e6%9d%ad%e5%b7%9e%e5%b8%82%e6%96%87%e4%b8%80%e8%a5%bf%e8%b7%af960%e5%8f%b7&mode=T&FORM=LLDP#JnE9LiUyNXU2ZDU5JTI1dTZjNWYlMjV1NzcwMSUyNXU2NzZkJTI1dTVkZGUlMjV1NWUwMiUyNXU2NTg3JTI1dTRlMDAlMjV1ODk3ZiUyNXU4ZGVmOTYwJTI1dTUzZjclN2Vzc3QuMCU3ZXBnLjEmYmI9MzAuMjg5MzM5MTM4NzQxMiU3ZTEyMC4wMzMyODk3Njg2JTdlMzAuMjc5MjQwNjAxNDAwOCU3ZTEyMC4wMTE1MTAyMzE0");
                        
                        Article article3 = new Article();  
                        article3.setTitle("ѧԺ��ͼ");  
                        article3.setDescription("");  
                        article3.setPicUrl(basePath+"images/KyKe.jpg");  
                        article3.setUrl("http://www.ky.zstu.edu.cn/mypage/index.php?name=pics");
      
                        articleList.add(article1);  
                        articleList.add(article2);  
                        articleList.add(article3); 

                        newsMessage.setArticleCount(articleList.size());  
                        newsMessage.setArticles(articleList);  
                        respMessage = MessageUtil.newsMessageToXml(newsMessage); 
                    }
                    // �칫����ĸ���
                    else if ("B00".equals(eventKey)) {
                    	Article article1 = new Article();  
                        article1.setTitle("�칫�绰");  
                        article1.setDescription("");  
                        article1.setPicUrl(basePath+"images/KyMen.jpg");  
                        article1.setUrl("http://www.ky.zstu.edu.cn/mypage/index.php?name=tele");
                        
                        Article article2 = new Article();  
                        article2.setTitle("У������");  
                        article2.setDescription("");  
                        article2.setPicUrl(basePath+"images/KyZhong.jpg");  
                        article2.setUrl("http://www.ky.zstu.edu.cn/mypage/index.php?name=xichengbus");
                        
                        Article article3 = new Article();  
                        article3.setTitle("ѧԺУ��");  
                        article3.setDescription("");  
                        article3.setPicUrl(basePath+"images/KyMei.jpg");  
                        article3.setUrl("http://www.ky.zstu.edu.cn/mypage/index.php?name=xiaoli");
      
                        articleList.add(article1);  
                        articleList.add(article2);  
                        articleList.add(article3); 

                        newsMessage.setArticleCount(articleList.size());  
                        newsMessage.setArticles(articleList);  
                        respMessage = MessageUtil.newsMessageToXml(newsMessage); 
                    }
                    
                    //��ʾ������������
                    else if ("B01".equalsIgnoreCase(eventKey)) {
                    	String serviceType="֪ͨ����";
                    	List<TService> listService;
                    	listService=persistenceLayer3.findByProperty(serviceType);
                    	int serviceSize=listService.size();
                    	if (serviceSize>=4) {
                			TService service1=listService.get(serviceSize-1);
                			String serviceTitle1=service1.getServiceTitle();
                			long serviceId1=service1.getServiceId();
                			
                			TService service2=listService.get(serviceSize-2);
                			String serviceTitle2=service2.getServiceTitle();
                			long serviceId2=service2.getServiceId();
                			
                			TService service3=listService.get(serviceSize-3);
                			String serviceTitle3=service3.getServiceTitle();
                			long serviceId3=service3.getServiceId();
                			
                			TService service4=listService.get(serviceSize-4);
                			String serviceTitle4=service4.getServiceTitle();
                			long serviceId4=service4.getServiceId();
                        	
                			Article article1 = new Article();  
                            article1.setTitle(serviceTitle1);  
                            article1.setDescription("��ʾ��һ��֪ͨ�������µ�һ��֪ͨ");  
                            article1.setPicUrl(basePath+"images/KyMen.jpg");  
                            article1.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId1);
                            Article article2 = new Article();  
                            article2.setTitle(serviceTitle2);  
                            article2.setDescription("��ʾ�ڶ���֪ͨ�������µĶ���֪ͨ");  
                            article2.setPicUrl(basePath+"images/KyZhong.jpg");  
                            article2.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId2);
                            Article article3 = new Article();  
                            article3.setTitle(serviceTitle3);  
                            article3.setDescription("��ʾ������֪ͨ�������µ�����֪ͨ");  
                            article3.setPicUrl(basePath+"images/KyMei.jpg");  
                            article3.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId3); 
                            Article article4 = new Article();  
                            article4.setTitle(serviceTitle4);  
                            article4.setDescription("��ʾ������֪ͨ�������µ�����֪ͨ");  
                            article4.setPicUrl(basePath+"images/KyMei.jpg");  
                            article4.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId4);

                            articleList.add(article1);  
                            articleList.add(article2);  
                            articleList.add(article3); 
                            articleList.add(article4); 
                            newsMessage.setArticleCount(articleList.size());  
                            newsMessage.setArticles(articleList);  
                            respMessage = MessageUtil.newsMessageToXml(newsMessage); 
                		    }
                		else if(serviceSize==3) {
                			TService service1=listService.get(serviceSize-1);
                			String serviceTitle1=service1.getServiceTitle();
                			long serviceId1=service1.getServiceId();
                			
                			TService service2=listService.get(serviceSize-2);
                			String serviceTitle2=service2.getServiceTitle();
                			long serviceId2=service2.getServiceId();
                			
                			TService service3=listService.get(serviceSize-3);
                			String serviceTitle3=service3.getServiceTitle();
                			long serviceId3=service3.getServiceId();
                			
                			Article article1 = new Article();  
                            article1.setTitle(serviceTitle1);  
                            article1.setDescription("��ʾ��һ��֪ͨ�������µ�һ��֪ͨ");  
                            article1.setPicUrl(basePath+"images/KyMen.jpg");  
                            article1.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId1);
                            Article article2 = new Article();  
                            article2.setTitle(serviceTitle2);  
                            article2.setDescription("��ʾ�ڶ���֪ͨ�������µĶ���֪ͨ");  
                            article2.setPicUrl(basePath+"images/KyZhong.jpg");  
                            article2.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId2);
                            Article article3 = new Article();  
                            article3.setTitle(serviceTitle3);  
                            article3.setDescription("��ʾ������֪ͨ�������µ�����֪ͨ");  
                            article3.setPicUrl(basePath+"images/KyMei.jpg");  
                            article3.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId3); 

                            articleList.add(article1);  
                            articleList.add(article2);  
                            articleList.add(article3); 
                            newsMessage.setArticleCount(articleList.size());  
                            newsMessage.setArticles(articleList);  
                            respMessage = MessageUtil.newsMessageToXml(newsMessage); 
                		    }
                		else if(serviceSize==2){
                			TService service1=listService.get(serviceSize-1);
                			String serviceTitle1=service1.getServiceTitle();
                			long serviceId1=service1.getServiceId();
                			TService service2=listService.get(serviceSize-2);
                			String serviceTitle2=service2.getServiceTitle();
                			long serviceId2=service2.getServiceId();
                			Article article1 = new Article();  
                            article1.setTitle(serviceTitle1);  
                            article1.setDescription("��ʾ��һ��֪ͨ�������µ�һ��֪ͨ");  
                            article1.setPicUrl(basePath+"images/KyMen.jpg");  
                            article1.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId1);
                            Article article2 = new Article();  
                            article2.setTitle(serviceTitle2);  
                            article2.setDescription("��ʾ�ڶ���֪ͨ�������µĶ���֪ͨ");  
                            article2.setPicUrl(basePath+"images/KyZhong.jpg");  
                            article2.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId2);

                            articleList.add(article1);  
                            articleList.add(article2);  
                            newsMessage.setArticleCount(articleList.size());  
                            newsMessage.setArticles(articleList);  
                            respMessage = MessageUtil.newsMessageToXml(newsMessage); 
                		    }
                		else if(serviceSize==1){
                			TService service1=listService.get(serviceSize-1);
                			String serviceTitle1=service1.getServiceTitle();
                			long serviceId1=service1.getServiceId();
                			Article article1 = new Article();  
                            article1.setTitle(serviceTitle1);  
                            article1.setDescription("��ʾ��һ��֪ͨ�������µ�һ��֪ͨ");  
                            article1.setPicUrl(basePath+"images/KyMen.jpg");  
                            article1.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId1);

                            articleList.add(article1);  
                            newsMessage.setArticleCount(articleList.size());  
                            newsMessage.setArticles(articleList);  
                            respMessage = MessageUtil.newsMessageToXml(newsMessage); 

                		}
                		else{
                			Article article1 = new Article();  
                            article1.setTitle("��ʱû��֪ͨ�����Щ������ע��лл��");  
                            articleList.add(article1);  
                            newsMessage.setArticleCount(articleList.size());  
                            newsMessage.setArticles(articleList);  
                            respMessage = MessageUtil.newsMessageToXml(newsMessage); 
                		}
                    } //end of ��������
                        
                    //�������
                    else if ("B02".equals(eventKey)) {
                    	String serviceType="�������";
                    	List<TService> listService;
                    	listService=persistenceLayer3.findByProperty(serviceType);
                    	int serviceSize=listService.size();
            			TService service1=listService.get(serviceSize-1);
            		    //String serviceTitle1=service1.getServiceTitle();
            			long serviceId1=service1.getServiceId();

                       	Article article1 = new Article();  
                        article1.setTitle("����һ�ܻ����ѯ");  
                        article1.setDescription("�ף�ֻ�ܲ�ѯ���һ�ܵĻ��飬�����鿴ȫ�Ľ��в�ѯ�������ѯ����������ݣ����¼�����칫����ҳ���в�ѯ");  
                        article1.setPicUrl(basePath+"images/KyHuiYi.jpg");  
                        article1.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId1);  
     
                        articleList.add(article1);  

                        newsMessage.setArticleCount(articleList.size());  
                        newsMessage.setArticles(articleList);  
                        respMessage = MessageUtil.newsMessageToXml(newsMessage); 
                    }
                    
                    //��ʾ������������
                    else if ("B03".equalsIgnoreCase(eventKey)) {
                    	String serviceType="���Ź���";
                    	List<TService> listService;
                    	listService=persistenceLayer3.findByProperty(serviceType);
                    	int serviceSize=listService.size();
                    	if (serviceSize>=4) {
                			TService service1=listService.get(serviceSize-1);
                			String serviceTitle1=service1.getServiceTitle();
                			long serviceId1=service1.getServiceId();
                			
                			TService service2=listService.get(serviceSize-2);
                			String serviceTitle2=service2.getServiceTitle();
                			long serviceId2=service2.getServiceId();
                			
                			TService service3=listService.get(serviceSize-3);
                			String serviceTitle3=service3.getServiceTitle();
                			long serviceId3=service3.getServiceId();
                			
                			TService service4=listService.get(serviceSize-4);
                			String serviceTitle4=service4.getServiceTitle();
                			long serviceId4=service4.getServiceId();
                        	
                			Article article1 = new Article();  
                            article1.setTitle(serviceTitle1);  
                            article1.setDescription("��ʾ��һ�����ţ������µ�һ������");  
                            article1.setPicUrl(basePath+"images/KyMen.jpg");  
                            article1.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId1);
                            Article article2 = new Article();  
                            article2.setTitle(serviceTitle2);  
                            article2.setDescription("��ʾ�ڶ������ţ������µĶ�������");  
                            article2.setPicUrl(basePath+"images/KyZhong.jpg");  
                            article2.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId2);
                            Article article3 = new Article();  
                            article3.setTitle(serviceTitle3);  
                            article3.setDescription("��ʾ���������ţ������µ���������");  
                            article3.setPicUrl(basePath+"images/KyMei.jpg");  
                            article3.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId3); 
                            Article article4 = new Article();  
                            article4.setTitle(serviceTitle4);  
                            article4.setDescription("��ʾ���������ţ������µ���������");  
                            article4.setPicUrl(basePath+"images/KyMei.jpg");  
                            article4.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId4);

                            articleList.add(article1);  
                            articleList.add(article2);  
                            articleList.add(article3); 
                            articleList.add(article4); 
                            newsMessage.setArticleCount(articleList.size());  
                            newsMessage.setArticles(articleList);  
                            respMessage = MessageUtil.newsMessageToXml(newsMessage); 
                		   }
                		else if(serviceSize==3) {
                			TService service1=listService.get(serviceSize-1);
                			String serviceTitle1=service1.getServiceTitle();
                			long serviceId1=service1.getServiceId();
                			
                			TService service2=listService.get(serviceSize-2);
                			String serviceTitle2=service2.getServiceTitle();
                			long serviceId2=service2.getServiceId();
                			
                			TService service3=listService.get(serviceSize-3);
                			String serviceTitle3=service3.getServiceTitle();
                			long serviceId3=service3.getServiceId();
                			
                			Article article1 = new Article();  
                            article1.setTitle(serviceTitle1);  
                            article1.setDescription("��ʾ��һ�����ţ������µ�һ������");  
                            article1.setPicUrl(basePath+"images/KyMen.jpg");  
                            article1.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId1);
                            Article article2 = new Article();  
                            article2.setTitle(serviceTitle2);  
                            article2.setDescription("��ʾ�ڶ������ţ������µĶ�������");  
                            article2.setPicUrl(basePath+"images/KyZhong.jpg");  
                            article2.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId2);
                            Article article3 = new Article();  
                            article3.setTitle(serviceTitle3);  
                            article3.setDescription("��ʾ���������ţ������µ���������");  
                            article3.setPicUrl(basePath+"images/KyMei.jpg");  
                            article3.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId3); 

                            articleList.add(article1);  
                            articleList.add(article2);  
                            articleList.add(article3); 
                            newsMessage.setArticleCount(articleList.size());  
                            newsMessage.setArticles(articleList);  
                            respMessage = MessageUtil.newsMessageToXml(newsMessage); 
                		   }
                		else if(serviceSize==2){
                			TService service1=listService.get(serviceSize-1);
                			String serviceTitle1=service1.getServiceTitle();
                			long serviceId1=service1.getServiceId();
                			TService service2=listService.get(serviceSize-2);
                			String serviceTitle2=service2.getServiceTitle();
                			long serviceId2=service2.getServiceId();
                			Article article1 = new Article();  
                            article1.setTitle(serviceTitle1);  
                            article1.setDescription("��ʾ��һ�����ţ������µ�һ������");  
                            article1.setPicUrl(basePath+"images/KyMen.jpg");  
                            article1.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId1);
                            Article article2 = new Article();  
                            article2.setTitle(serviceTitle2);  
                            article2.setDescription("��ʾ�ڶ������ţ������µĶ�������");  
                            article2.setPicUrl(basePath+"images/KyZhong.jpg");  
                            article2.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId2);

                            articleList.add(article1);  
                            articleList.add(article2);  
                            newsMessage.setArticleCount(articleList.size());  
                            newsMessage.setArticles(articleList);  
                            respMessage = MessageUtil.newsMessageToXml(newsMessage); 
                		   }
                		else if(serviceSize==1){
                			TService service1=listService.get(serviceSize-1);
                			String serviceTitle1=service1.getServiceTitle();
                			long serviceId1=service1.getServiceId();
                			Article article1 = new Article();  
                            article1.setTitle(serviceTitle1);  
                            article1.setDescription("��ʾ��һ�����ţ������µ�һ������");  
                            article1.setPicUrl(basePath+"images/KyMen.jpg");  
                            article1.setUrl(basePath+"OfficeService/serviceView.jsp?id=" + serviceId1);

                            articleList.add(article1);  
                            newsMessage.setArticleCount(articleList.size());  
                            newsMessage.setArticles(articleList);  
                            respMessage = MessageUtil.newsMessageToXml(newsMessage); 

                		   }
                		else{
                			Article article1 = new Article();  
                            article1.setTitle("��ʱû�����ţ����Щ������ע��лл��");  
                            articleList.add(article1);  
                            newsMessage.setArticleCount(articleList.size());  
                            newsMessage.setArticles(articleList);  
                            respMessage = MessageUtil.newsMessageToXml(newsMessage); 
                		}
                    }
                    
                    // �࿼��ѯ
                    else if ("C05".equalsIgnoreCase(eventKey)) {
                		PersistenceLayerLinyx persistenceLayer = (PersistenceLayerLinyx) ctx.getBean("persistenceLayerLinyx");
                		String jobNumber = persistenceLayer.getJobNumberByOpenID(fromUserName);
            			if (jobNumber==null || jobNumber.equalsIgnoreCase("")) {
            				respContent = "����΢���˻���δ�빤�Ű󶨣�";  
                            textMessage.setContent(respContent);  
                            respMessage = MessageUtil.textMessageToXml(textMessage);
            			}
            			else {
	                    	Article article = new Article();  
	                        article.setTitle("�࿼������Ϣ��ѯ");  
	                        article.setDescription("�ף�ֻ�ܲ�ѯ��ǰ����֮��ļ࿼����Ŷ��");  
	                        article.setPicUrl(basePath+"images/KyJianKao.jpg");  
	                        article.setUrl(basePath+"Invigilation/invigilationTask.jsp?jobNumber=" + jobNumber);  
	                        //article.setUrl(basePath+"Invigilation/invigilationTask.jsp?jobNumber=20010888");
	                        articleList.add(article);  
	                        // ����ͼ����Ϣ����  
	                        newsMessage.setArticleCount(articleList.size());  
	                        // ����ͼ����Ϣ������ͼ�ļ���  
	                        newsMessage.setArticles(articleList);  
	                        // ��ͼ����Ϣ����ת����xml�ַ���  
	                        respMessage = MessageUtil.newsMessageToXml(newsMessage);
            			}
                    }	//end of �࿼  // ֵ���ѯ
                    else if ("C06".equalsIgnoreCase(eventKey)) {
                		PersistenceLayerLinyx persistenceLayer = (PersistenceLayerLinyx) ctx.getBean("persistenceLayerLinyx");
                		String jobNumber = persistenceLayer.getJobNumberByOpenID(fromUserName);
            			if (jobNumber==null || jobNumber.equalsIgnoreCase("")) {
            				respContent = "����΢���˻���δ�빤�Ű󶨣�";  
                            textMessage.setContent(respContent);  
                            respMessage = MessageUtil.textMessageToXml(textMessage);
            			}
            			else {
	                    	Article article = new Article();  
	                        article.setTitle("ֵ��������Ϣ��ѯ");  
	                        article.setDescription("�ף�ֻ�ܲ�ѯ��ǰ����֮���ֵ������Ŷ��");  
	                        article.setPicUrl(basePath+"images/KyZhiBan.jpg");  
	                        article.setUrl(basePath+"Duty/dutyTask.jsp?jobNumber=" + jobNumber);  
	                        articleList.add(article);  
	                        // ����ͼ����Ϣ����  
	                        newsMessage.setArticleCount(articleList.size());  
	                        // ����ͼ����Ϣ������ͼ�ļ���  
	                        newsMessage.setArticles(articleList);  
	                        // ��ͼ����Ϣ����ת����xml�ַ���  
	                        respMessage = MessageUtil.newsMessageToXml(newsMessage);
            			}
                    }	//end of ֵ��
                    */
                    
                    // �����Ĺ���ֵ���ѯ
                    //else 
                    

                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return respMessage;  
    }
    
    /**
     * �ֶ����˵�
     * @return
     */
    private static String getMainMenu() {  
        StringBuffer buffer = new StringBuffer();  
        buffer.append("��ظ�����ѡ�����").append("\n\n");  
        buffer.append("1  ѧԺ����").append("\n");  
        buffer.append("2  �칫����").append("\n");  
        buffer.append("3  ���˲���").append("\n");   
        return buffer.toString();  
    }
    
    
}