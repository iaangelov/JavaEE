package morghulis.valar.filters;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import morghulis.valar.services.UserContext;
import morghulis.valar.utils.UserType;

@WebFilter(urlPatterns = { "/rest/admin/*", "/adminPanel.html", "/screeningsManager.html" })
public class AdminFilter implements Filter {

	@Inject
	private UserContext userContext;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (userContext.getCurrentUser() != null) {
			UserType type = userContext.getCurrentUser().getUserType();
			if (type != null && type == UserType.ADMINISTRATOR) {
				chain.doFilter(request, response);
				return;
			}
		}

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String redirectURL = httpServletRequest.getContextPath()
				+ "/index.html";
		httpServletResponse.sendRedirect(redirectURL);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void destroy() {

	}

}
