package stu.service;
import java.util.List;

import stu.mybean.Student;
import stu.dao.StudentDao;

//ҵ���߼��㣺�߼��Ե���ɾ�Ĳ�

public class StudentService {
	StudentDao studentDao = new StudentDao();
	
		//����ѧ��service
	
	public boolean addStudent(Student student){
		
		if(!studentDao.isExist(student.getSid())){
			
			return studentDao.addStudent(student);
		}else{
			System.out.println("�����Ѵ��ڣ�");
			return false;
		}
	}
		
		//ɾ��
	public boolean deleteStudentBySid(int sid){
		if(studentDao.isExist(sid)){
			return studentDao.deleteStudentBySid(sid);
		}else{
			return false;
		}
		
	}
	
	//�޸�
	public boolean updateStudentBySid(int sid,Student student){
		if(studentDao.isExist(sid)){
			return studentDao.updateStudentBySid(sid, student);
		}
		return false;
	}
	
		//����ѧ�Ų�һ����
	public Student queryStudentBySid(int sid){
		return studentDao.queryStudentBySid(sid);
	}
	
	//��ѯȫ��ѧ��
	public List<Student> queryAllStudents(int page){
		return studentDao.queryAllStudents(page);//
	}
	
	//��ѯ�ܼ�¼��
	public int findCount(){
		return studentDao.findCount();
	}
		
}
