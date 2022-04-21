package pacote;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.regex.*;
import java.sql.*;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;

import org.apache.commons.io.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class DataSciencer
 */
@WebServlet("/datasciencer.do")
public class DataSciencer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private File arquivo1=null;
	private File arquivo2=null;
	private File arquivoExclui=null;

	
	private File diretorio;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
		String path = "/datasciencer/files/";
		diretorio = new File(path);
		diretorio.mkdirs();
	}
	/**
     * @see HttpServlet#HttpServlet()
     */
    public DataSciencer() {
        super();
        // TODO Auto-generated constructor stub
    }

    private void processUploadedFile(FileItem item) throws Exception {
		String fileName = item.getName();
		System.out.println(item.getFieldName()+"\t"+item.getName());
		File uploadedFile = new File(diretorio+"/"+fileName);
	    item.write(uploadedFile);
	    System.out.print(uploadedFile.getAbsoluteFile());
	
	    
	}
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = null;
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		//PrintWriter out = response.getWriter();
		
		String content[][]=null;
		
		
			
		int numColumns=0;
		int numRows=0;
		
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			return;
		}
		
		String action = "";
		
		
		List<String[]> results = new ArrayList<String[]>();
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(diretorio);
		
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items ) {
			    if (!item.isFormField()) {
			        processUploadedFile(item);
			    } else {
				   	//para inputs que nao sao 'file', isFormField() é verdadeiro
			    	String nomeDoCampo = item.getFieldName();
					String valorDoCampo = item.getString();
					
					if (nomeDoCampo.equals("action")) {
						action = valorDoCampo;
					}
					
			    }
			}
	    } catch (Exception e) {
	    	   //out.println(e.getMessage());
	    	System.err.printf("Erro na abertura do arquivo: %s.\n",
	          e.getMessage());
	     
	    }
		System.out.println("action="+action);
		
		rd = request.getRequestDispatcher("/appResp/resp.jsp");
        rd.forward(request, response);
	}

}
