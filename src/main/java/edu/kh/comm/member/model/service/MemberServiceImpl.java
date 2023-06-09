package edu.kh.comm.member.model.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.kh.comm.member.model.dao.MemberDAO;
import edu.kh.comm.member.model.vo.Member;

@Service // 비지니스로직(데이터 가공, DB연결)을 처리하는 클래스임을 명시 + bean 등록
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDAO dao;
	
	/* Connection을 Service에서 얻어왔던 이유
	 * 
	 * - Service의 메서드 하나는 요청을 처리하는 업무 단위
	 * -> 해당 업무가 끝난 후 트랜잭션을 한 번에 처리하기 위해서
	 *    어쩔 수 없이 Connection을 Service에서 얻어올 수밖에 없었다.
	 * */
	
	// 암호화를 위한 bcrypt 객체 의존성 주입(DI)
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	private Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
	
	
	// 로그인 서비스 구현
	@Override
	public Member login(Member inputMember) {
		
		// 전달 받은 비밀번호를 암호화하여 
		// DB에서 조회한 비밀번호와 비교 (DB에서 비교 X)
		
		// sha 방식 암호화 
		// A회원 / 비밀번호 : 1234 -> 암호화 : abcd
		// B회원 / 비밀번호 : 1234 -> 암호화 : abcd (암호화시 변경된 내용이 같음)
		
		
		
		// Bcrypt 암호화 : 암호화 하기 전에 salt를 추가하여 변형된 상태로 암호화를 진행
		// A회원 / 비밀번호 : 1234 -> 암호화 : abcd
		// B회원 / 비밀번호 : 1234 -> 암호화 : @sef 
		// 매번 암호화되는 비밀번호가 달라져서 DB에서 직접 비교 불가능
		// 이를 비교하는 기능(메서드) 가지고 있어서 이를 활용하면 된다.
		
		// ** Bcrypt 암호화를 사용하기 위해 이를 지원하는 Spring-security 모듈 추가 ** 
		
		/*
		logger.debug(inputMember.getMemberPw() + " / " + bcrypt.encode(inputMember.getMemberPw()));
		logger.debug(inputMember.getMemberPw() + " / " + bcrypt.encode(inputMember.getMemberPw()));
		logger.debug(inputMember.getMemberPw() + " / " + bcrypt.encode(inputMember.getMemberPw()));
		logger.debug(inputMember.getMemberPw() + " / " + bcrypt.encode(inputMember.getMemberPw()));
		logger.debug(inputMember.getMemberPw() + " / " + bcrypt.encode(inputMember.getMemberPw()));
		*/
		logger.debug(inputMember.getMemberPw() + " / " + bcrypt.encode(inputMember.getMemberPw()));
		
		
		
		Member loginMember = dao.login(inputMember);
		
		// loginMember == null : 일치하는 이메일이 없다
		
		if(loginMember != null) { // 일치하는 이메일을 가진 회원 정보가 있다
			
			if(bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())) { // 비밀번호가 일치할 경우
				
				loginMember.setMemberPw(null); // 비교 끝났으면 비밀번호 지우기
				
			}else { // 비밀번호가 일치하지 않을 경우
				loginMember =null;
			}
			
		}
		
		return loginMember;
		
		// Connection을 얻어오거나/반환하거나
		// 트랜잭션 처리를 하는 구문을 작성하지 않아도
		// Spring에서 제어를 하기 때문에 Service구문이 간단해진다.
		
	}


	@Override
	public int emailDupCheck(String memberEmail) {
		return dao.emailDupCheck(memberEmail);
	}


	@Override
	public int nickDupCheck(String memberNickname) {
		return dao.nickDupCheck(memberNickname);
	}


	@Override
	public int signUp(Member inputMember) {
		inputMember.setMemberPw(bcrypt.encode(inputMember.getMemberPw()));
		return dao.signUp(inputMember);
	}


	@Override
	public Member selectOne(String memberEmail) {
		return dao.selectOne(memberEmail);
	}


	@Override
	public List selectAll() {
		return dao.selectAll();
	}
	
	

}
