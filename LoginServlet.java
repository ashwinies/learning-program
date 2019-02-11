package com.xlift.asc.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.xlift.asc.model.Engineer;
import com.xlift.asc.service.LoginService;

public class LoginServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4200185243418596003L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Logger log = Logger.getLogger(LoginServlet.class);
		String id = request.getParameter("param").toString();
		log.info("Id" +id);
		if (id != null) {
			String shiptocode = request.getParameter("shipToCode");
			String corporationCode = request.getParameter("corporationCode");
			LoginService loginService = new LoginService();
			String deptCode = loginService.getDepartmentCode(shiptocode,
					corporationCode);
			RequestDispatcher dispatcher = null;
			if (deptCode == null) {
				dispatcher = request.getRequestDispatcher("errorDept.jsp");

			} else {
				List<Engineer> engineerList = (List<Engineer>) loginService
						.getEngineerList(deptCode);
				request.setAttribute("listCategory", engineerList);
				request.setAttribute("deptCode", deptCode);
				request.setAttribute("shipToCode", shiptocode);
				request.setAttribute("corporationCode", corporationCode);
				if (id.equals("1")) {
					dispatcher = request.getRequestDispatcher("addasc.jsp");
				} else {
					dispatcher = request
							.getRequestDispatcher("insertEngineer.jsp");
				}

			}

			dispatcher.forward(request, response);
		}

	}

}