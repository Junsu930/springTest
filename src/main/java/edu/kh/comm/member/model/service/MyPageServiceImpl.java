package edu.kh.comm.member.model.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.kh.comm.member.model.dao.MyPageDAO;
import edu.kh.comm.member.model.vo.Member;

@Service
public class MyPageServiceImpl implements MyPageService {
	
	@Autowired
	private MyPageDAO dao;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;

	@Override
	public int changePw(Member loginMember ,String currentPw, String newPw) {
		
		String dbPw = dao.checkPw(loginMember);
		
		int result = 0;
		
		if(bcrypt.matches(currentPw, dbPw)) {
			loginMember.setMemberPw(bcrypt.encode(newPw));
			result = dao.changePw(loginMember);
			
		}else{
			return -1;
		}
		
		return result;
	}

	@Override
	public int secession(Member loginMember, String memberPw) {
		
		String dbPw = dao.checkPw(loginMember);
		
		int result = 0;
		
		if(bcrypt.matches(memberPw, dbPw)){
			
			result = dao.secession(loginMember);
			
		}else {
			return -1;
		}
		
		return result;
	}

	@Override
	public int updateInfo(Member loginMember) {
		return dao.updateInfo(loginMember);
	}

	@Override
	public int checkNick(String newNick) {
		return dao.checkNick(newNick);
	}

}
