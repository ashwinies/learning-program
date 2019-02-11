package com.xlift.asc.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AddAscServlet extends HttpServlet {

	/**
	 * @author ashwni
	 */
	private static final long serialVersionUID = 267627092379721736L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Logger log = Logger.getLogger(AddAscServlet.class);
		response.setContentType("text/html;charset=UTF-8");
		String shipToCode = request.getParameter("shipToCode");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String ascName = request.getParameter("name");
		String countryCode = request.getParameter("countryCode");
		String currency = request.getParameter("currency");
		String engineers[] = request.getParameterValues("category");
		String defaultUser = request.getParameter("defaultUser");
		String corporationCode = request.getParameter("corporationCode");
		String timegap = request.getParameter("timegap");
		String language = request.getParameter("languageCode");
		String deptCode = request.getParameter("deptCode");
		String deptGrp = request.getParameter("deptGrpType");
		List<String> timeZones = Arrays.asList(new String[] { "1", "2", "3",
				"0" });
		List<String> defaultFlagFields = Arrays.asList(new String[] {
				"inOutManagementFlag", "swapInOutManagementFlag",
				"visitFeeCodeFlag", "freightFeeCodeFlag",
				"materialFeeCodeFlag", "repairLevelOption" });
		List<String> filePaths = Arrays
				.asList(new String[] { "D:/ModifySetUpFile/settings.xml",
						"D:/ModifySetUpFile/testsettings.xml" });
		
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder documentBuilder;

				documentBuilder = documentBuilderFactory.newDocumentBuilder();
				PrintWriter out = response.getWriter();
				Document document = null;
				for (String path : filePaths) {

				document = documentBuilder.parse(path);
				Element root = document.getDocumentElement();
				log.info(root.getNodeName());
				NodeList nList = document.getElementsByTagName("corporation");
				log.info(nList);
				XPath xPath = XPathFactory.newInstance().newXPath();
				corporationCode=corporationCode.toUpperCase();
				String expression = "//corporations/corporation/corporation_code[text()='"
						+ corporationCode + "']";
				log.info(expression);
				Node nameTag = (Node) xPath.compile(expression).evaluate(
						document, XPathConstants.NODE);
				log.info("parent node " + nameTag.getParentNode().getNodeName());
				for (int i = 0; i < nameTag.getParentNode().getChildNodes()
						.getLength(); i++) {
					log.info("Inside loop i element "
							+ nameTag.getParentNode().getChildNodes().item(i)
									.getNodeName());
					if (nameTag.getParentNode().getChildNodes().item(i)
							.getNodeName().equals("ascList")) {
						log.info("GOT BLOCK");

						Node node = document.createElement("asc");
						Node engListNode = document
								.createElement("engineerList");
						Node timeZoneList = document
								.createElement("timeZoneList");

						nameTag.getParentNode().getChildNodes().item(i)
								.appendChild(node);

						Node usernode = document.createElement("username");
						usernode.setTextContent(userName);
						node.appendChild(usernode);

						Node pwd = document.createElement("password");
						pwd.setTextContent(password);
						node.appendChild(pwd);

						Node name = document.createElement("name");
						name.setTextContent(ascName);
						node.appendChild(name);

						Node ship = document.createElement("ship_to_code");
						ship.setTextContent(shipToCode);
						node.appendChild(ship);

						Node departmentCode = document
								.createElement("department_code");
						departmentCode.setTextContent(deptCode);
						node.appendChild(departmentCode);

						Node languageCode = document
								.createElement("language_code");
						languageCode.setTextContent(language);
						node.appendChild(languageCode);

						Node country = document.createElement("country_code");
						country.setTextContent(countryCode.toUpperCase());
						node.appendChild(country);

						Node timeGap = document.createElement("time_gap");
						timeGap.setTextContent(timegap);
						node.appendChild(timeGap);

						Node dateFormat = document.createElement("date_format");
						dateFormat.setTextContent("ddmmyyyy");
						node.appendChild(dateFormat);

						Node cur = document.createElement("currency_code");
						cur.setTextContent(currency.toUpperCase());
						node.appendChild(cur);

						Node deptGrpType = document
								.createElement("dept_group_type");
						deptGrpType.setTextContent(deptGrp.toUpperCase());
						node.appendChild(deptGrpType);

						for (String engineer : engineers) {

							String[] id = engineer.split(",");

							String engId = id[0];
							String engName = id[1];

							Node engNode = document.createElement("engineer");
							Node engineerName = document
									.createElement("engineer_name");
							engineerName.setTextContent(engName);
							engNode.appendChild(engineerName);
							Node engineerCode = document
									.createElement("engineer_code");
							engineerCode.setTextContent(engId);
							engNode.appendChild(engineerCode);
							engListNode.appendChild(engNode);

						}
						node.appendChild(engListNode);
						for (String time : timeZones) {

							Node timeZone = document.createElement("timeZone");
							timeZone.setTextContent(time);
							timeZoneList.appendChild(timeZone);

						}
						node.appendChild(timeZoneList);
						
							Node defaultUserId = document.createElement("defaultUserId");
							defaultUserId.setTextContent(defaultUser);
							node.appendChild(defaultUserId);
						
						
						
						for (String field : defaultFlagFields) {
							Node defaultFlagNode = createDefaultFlagFields(
									field, "false", document);
							node.appendChild(defaultFlagNode);
						}

					}
				}
				TransformerFactory tFactory = TransformerFactory.newInstance();
				Transformer transformer = tFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(
						"{http://xml.apache.org/xslt}indent-amount", "4");
				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(
						path);
				transformer.transform(source, result);
				}
				out.println("<html>");
				out.println("<head>");
				out.println("<title>Registration Successful</title>");
				out.println("</head>");
				out.println("<body>");
				out.println("<center>");
				out.println("<h2>Thanks for Registering ASC details:</h1>");
				out.println("<h2>To Register new ASC or Engineer(technician) <a href=home.jsp>Click here</a></h2>");

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			}
		

	}

	private Node createDefaultFlagFields(String defaultFlag, String value,
			Document document) {
		Node inOutMgmtFlag = document.createElement(defaultFlag);
		inOutMgmtFlag.setTextContent(value);
		return inOutMgmtFlag;
	}

}
