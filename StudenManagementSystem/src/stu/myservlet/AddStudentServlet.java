package stu.myservlet;
                                 //��̨
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.net.httpserver.HttpServer;

import stu.mybean.Student;
import stu.service.StudentService;

@SuppressWarnings({ "serial", "unused" })
public class AddStudentServlet extends HttpServer {




	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
				   request.setCharacterEncoding("utf-8");

			       int id = Integer.parseInt(request.getParameter("sid"));
			       String name = request.getParameter("sname");
			       int age = Integer.parseInt(request.getParameter("sage"));
			       String sex = request.getParameter("ssex");
			     
			       String home = request.getParameter("shome");
			       
			       Student student = new Student(id,name,age,sex,home);       //�Ѵ�ǰ��ҳ���ȡ����Ϣ��װ��student����
			       
			       StudentService studentService = new StudentService();
			       boolean result=studentService.addStudent(student);
			       /*
			        * out :PrintWriter out= response.getWriter();
			        * session: request.getSession();
			        * application: request.getServiceContext();
			        * 
			        * */
				   response.setCharacterEncoding("utf-8");
			       response.setContentType("text/html;charset=utf-8");//������Ӧ����
			       
			       
			      // PrintWriter out = response.getWriter();
			     /*  if(result){
			    	   
			    	   out.println("���ӳɹ���");
			    	   response.sendRedirect("QueryAllStudentServlet");
			       }else{
			    	  
					out.println("����ʧ�ܣ�");
			       }
			       
			     */
			        if(!result){  //�������ʧ�ܣ���request���з���һ������
			        	request.setAttribute("error","addError");
			        }else{//���ӳɹ�
			        	request.setAttribute("error","noaddError");
			        }
			        
			        
			        
			        request.getRequestDispatcher("QueryAllStudentServlet").forward(request, response);       
			       
	}
	
	

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doGet(request,response);
		
	}

}
