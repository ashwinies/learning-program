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


public class AddEngineerServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4816085727751551900L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		Logger log = Logger.getLogger(AddAscServlet.class);
	 response.setContentType("text/html;charset=UTF-8");
	 String shipToCode = request.getParameter("shipToCode");
	 String engineers[] = request.getParameterValues("category");
		List<String> filePaths = Arrays
				.asList(new String[] { "D:/ModifySetUpFile/settings.xml",
						"D:/ModifySetUpFile/testsettings.xml" });
		
	 try{
	 DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
     DocumentBuilder documentBuilder;
		
			
				documentBuilder = documentBuilderFactory.newDocumentBuilder();
				 PrintWriter out = response.getWriter();
			Document document = null;
			for (String path : filePaths) {
			document = documentBuilder.parse(path);
			
				 Element root = document.getDocumentElement();
				 System.out.println(root.getNodeName());
				 NodeList nList = document.getElementsByTagName("corporation");
				 log.info(nList);
				 XPath xPath = XPathFactory.newInstance().newXPath();
			        String expression = "//corporations/corporation/ascList/asc/ship_to_code[text()='"+shipToCode+"']";
			        log.info(expression);
			        Node nameTag = (Node) xPath.compile(expression).evaluate(document, XPathConstants.NODE);
			        log.info("GOT BLOCK!!");
			        log.info("parent node " + nameTag.getParentNode().getNodeName());
					
			        for (int i = 0; i < nameTag.getParentNode().getChildNodes().getLength(); i++) {
			        	log.info("Inside loop i element "+nameTag.getParentNode().getChildNodes().item(i).getNodeName());
			           if (nameTag.getParentNode().getChildNodes().item(i).getNodeName().equals("engineerList")) {
			        	   log.info("GOT BLOCK@@@");
			              
			                    for (String engineer : engineers) {
					   	     		
					   	    		 String[] id=engineer.split(",");
					   	    		 
					   	    		  String engId = id[0];
					   	    		  String engName = id[1];
					   	    		 
			                    Node engNode=document.createElement("engineer");
			                    Node engineerName = document.createElement("engineer_name");
			                    engineerName.setTextContent(engName);
			                    engNode.appendChild(engineerName);
			                    Node engineerCode = document.createElement("engineer_code");
			                    engineerCode.setTextContent(engId);
			                    engNode.appendChild(engineerName);
			                    engNode.appendChild(engineerCode);
			                    nameTag.getParentNode().getChildNodes().item(i).appendChild(engNode);
			                    
			                    }
				               
				               

			            }
			        }
			        TransformerFactory tFactory = TransformerFactory.newInstance();
			        Transformer transformer = tFactory.newTransformer();
			        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			        DOMSource source = new DOMSource(document);
			        StreamResult result = new StreamResult(path);
			        transformer.transform(source, result);
			}
			         out.println("<html>");
					 out.println("<head>");		
					 out.println("<title>Registration Successful</title>");		
					 out.println("</head>");
					 out.println("<body>");
					 out.println("<center>");
					 out.println("<h2>Thanks for Registering Engineer(Technician) details:</h2>");
					 out.println("<h2>To Register new ASC or shipToCode <a href=home.jsp>Click here</a></h2>");
		
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
	
}
