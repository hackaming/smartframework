package org.smart4j.framework;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ConfigHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.util.ArrayUtil;
import org.smart4j.framework.util.CodecUtil;
import org.smart4j.framework.util.JsonUtil;
import org.smart4j.framework.util.ReflectionUtil;
import org.smart4j.framework.util.StreamUtil;
import org.smart4j.framework.util.StringUtil;
@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet{
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		HelperLoader.init();
		ServletContext servletContext = servletConfig.getServletContext();
		ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
		jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");
		ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
		defaultServlet.addMapping(ConfigHelper.getAppAssetPath()+"*");
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestMethod = req.getMethod().toLowerCase();
		String requestPath = req.getPathInfo();
		Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
		if (null != handler){
			Class<?> controllerClass = handler.getControllerClass();
			Object controllerBean = BeanHelper.getBean(controllerClass);
			Map<String,Object> paramMap = new HashMap<String,Object>();
			Enumeration<String> paramNames = req.getParameterNames();
			while (paramNames.hasMoreElements()){
				String paraName = paramNames.nextElement();
				String paramValue = req.getParameter(paraName);
				paramMap.put(paraName,paramValue);
			}
			String body = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
			if (StringUtil.isNotEmpty(body)){
				String[] params = StringUtils.split(body,"&"); //need add the split into our own StringUtil package, here we use the apache commons package
				if (ArrayUtil.isNotEmpty(params)){
					for (String param:params){
						String[] array = StringUtils.split(param,"=");
						if (ArrayUtil.isNotEmpty(array)&&array.length == 2){
							String paramName = array[0];
							String paramValue = array[1];
							paramMap.put(paramName, paramValue);
						}
					}
				}
			}
			Param param = new Param(paramMap);
			Method actionMethod = handler.getActionMethod();
			Object result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
			if (result instanceof View){
				View view = (View) result;
				String path = view.getPath();
				if (StringUtil.isNotEmpty(path)){
					if (path.startsWith("/")){
						resp.sendRedirect(req.getContextPath()+path);
					} else {
						Map<String,Object> model = view.getModel();
						for (Map.Entry<String,Object> entry:model.entrySet()){
							req.setAttribute(entry.getKey(), entry.getValue());
						}
						req.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(req,resp);
					}
				} else if(result instanceof Data){
					Data data = (Data) result;
					Object model = data.getModel();
					if (null != model){
						resp.setContentType("application/json");
						resp.setCharacterEncoding("UTF-8");
						PrintWriter writer = resp.getWriter();
						String json = JsonUtil.toJson(model);
						writer.write(json);
						writer.flush();
						writer.close();
					}
				}
			}
		}
		
	}
	
}
