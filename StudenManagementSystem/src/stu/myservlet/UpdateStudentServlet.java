package stu.myservlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import stu.mybean.Student;
import stu.service.StudentService;

@SuppressWarnings({ "unused", "serial" })
public class UpdateStudentServlet extends HttpServlet {




	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			request.setCharacterEncoding("utf-8");
			
			//��ȡ���޸��˵�ѧ��
			int id = Integer.parseInt(request.getParameter("sid"));
			//��ȡ�޸ĺ������
			
			String name = request.getParameter("sname");
			int age = Integer.parseInt(request.getParameter("sage"));
			String sex = request.getParameter("ssex");
			String home = request.getParameter("shome");
			
			//���޸ĺ�����ݷ�װ��һ��ʵ������
			Student student= new Student(name,age,sex,home);
			
			StudentService service = new StudentService();
			boolean result = service.updateStudentBySid(id, student);

			response.setCharacterEncoding("utf-8");
		    response.setContentType("text/html;charset=utf-8");//������Ӧ����
		      
		    /*if(result){
				
				//out.println();
				
				//response.getWriter().println("�޸ĳɹ���");
				response.sendRedirect("QueryAllStudentServlet");//�޸���Ϻ��ٴ�����ȫ��ѧ����Ϣҳ��
				
			}else{
				response.getWriter().println("�޸�ʧ�ܣ�");
			}*/
		    
		    if(!result){  //����޸�ʧ�ܣ���request���з���һ������
	        	request.setAttribute("uerror","updateError");
	        }else{//���ӳɹ�
	        	request.setAttribute("uerror","noupdateError");
	        }
	                
	        request.getRequestDispatcher("QueryAllStudentServlet").forward(request, response);
	        
	        
		    
			
	}

		
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doGet(request,response);
	}

}
