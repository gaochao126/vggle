package com.jiuyi.vggle.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jiuyi.vggle.common.dict.CacheContainer;
import com.jiuyi.vggle.common.dict.Constants;
import com.jiuyi.vggle.common.util.SysCfg;

/**
 * @author zhb
 * @date 2015年3月26日
 */
public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = -5305744701888018846L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		Constants.applicationContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
        SysCfg.init();
        CacheContainer.init();
	}
}