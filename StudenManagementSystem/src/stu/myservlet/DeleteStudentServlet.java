package stu.myservlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import stu.service.StudentService;

@SuppressWarnings({ "unused", "serial" })
public class DeleteStudentServlet extends HttpServlet {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
			//ɾ��
		request.setCharacterEncoding("utf-8");
		//����ǰ�˴�����ѧ��
		int id=Integer.parseInt(request.getParameter("sid"));
		
		StudentService service = new StudentService();
		boolean result = service.deleteStudentBySid(id);
		
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");     //������Ӧ����
	                                           
	      
		if(result){
			
			//out.println();
			//response.getWriter().println("ɾ���ɹ���");
			response.sendRedirect("QueryAllStudentServlet");//���²�ѯ����ѧ��
		}else{
			response.getWriter().println("ɾ��ʧ�ܣ�");
		}
		
		//PrintWriter out = response.getWriter();
		
		
		/*if(!result){  //���ɾ��ʧ�ܣ���request���з���һ������
        	request.setAttribute("derror","deleteError");
        }else{
        	request.setAttribute("derror","nodeleteError");
        }
        
        
        
        request.getRequestDispatcher("QueryAllStudentServlet").forward(request, response);*/
		
		
	
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		 doGet(request,response);
	}

}
