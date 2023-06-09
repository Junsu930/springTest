package edu.kh.comm.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter(filterName="initFilter", urlPatterns="/*")
public class InitFilter extends HttpFilter implements Filter {
	
	// print 구문 사용하다 걸리면 바보
	// -> Logger 사용
	
	// Logger 객체 생성 (해당 클래스에 대한 log를 출력하는 객체)
	
	private Logger logger = LoggerFactory.getLogger(InitFilter.class);

	// 필터가 생성될 때 실행
	public void init(FilterConfig fConfig) throws ServletException {
		// logger를 이용해서 출력하는 방법
		// trace - debug - info - warn - error 
		
		// debug : 개발의 흐름 파악 (실행이 되었는지, 파라미터가 현재 무엇인지 확인할 때)
		// info : 메소드 실행
		
		// 이 필터가 생성이 되었다라는 정보를 출력하고 싶다면 info가 제일 적절함
		logger.info("초기화 필터 생성");
	}

	public void destroy() {
		logger.info("초기화 필터 파괴");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		ServletContext application = request.getServletContext();
		
		String contextPath = ((HttpServletRequest)request).getContextPath();
		
		application.setAttribute("contextPath", contextPath);
		
		chain.doFilter(request, response);
	}
}
