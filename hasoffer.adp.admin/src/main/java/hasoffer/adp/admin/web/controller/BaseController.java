package hasoffer.adp.admin.web.controller;


import hasoffer.adp.base.utils.Constants;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	protected String basePath;
	protected Integer page;
	protected Integer size;


	public BaseController() {
		super();
	}

	@ModelAttribute
	protected void initRequestResponseSession(HttpServletRequest request, HttpServletResponse response,
                                              RedirectAttributes redirectAttributes) {
		this.request = request;
		this.response = response;
		this.session = request.getSession();

		String path = request.getContextPath();
		basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;

		String page_str = request.getParameter("page");
		String size_str = request.getParameter("size");

		if (!StringUtils.isEmpty(page_str)) {
			page = Integer.parseInt(page_str);
		}else{
            page = Constants.DEFAULT_PAGE;
        }
		if (!StringUtils.isEmpty(size_str)) {
            size = Integer.parseInt(size_str);
		}else{
            size = Constants.DEFAULT_PAGE_SIZE;
        }
	}


}
