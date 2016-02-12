package eu.freme.bservices.example.loggingfilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class ExampleLoggingFilter extends GenericFilterBean {

	Logger logger = Logger.getLogger(ExampleLoggingFilter.class);

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		if (req instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) req;
			String uri = httpRequest.getRequestURI();
			logger.info("Detect HTTP request to endpoint " + uri);
		}

		chain.doFilter(req, res);
	}
}
