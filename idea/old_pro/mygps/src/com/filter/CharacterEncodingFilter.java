package com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharacterEncodingFilter implements Filter {
	private String characterEncoding;
	private boolean enabled;
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		characterEncoding = null;	//����ʱ�����Դ
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		if (enabled || characterEncoding != null) {	//������ø�Filter
			request.setCharacterEncoding(characterEncoding);	//����request����
			response.setCharacterEncoding(characterEncoding);	//����response����
		}
		chain.doFilter(request, response);	//ִ����һ��Filter��Servlet
	}
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub
		characterEncoding = config.getInitParameter("characterEncoding");	//���뷽ʽ
		enabled = "true".equalsIgnoreCase(config.getInitParameter("enabled").trim());	//����
	}
	
}
