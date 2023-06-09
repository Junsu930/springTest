package edu.kh.comm.member.model.dao;


import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.comm.member.model.vo.Member;

@Repository // 영속성을 가지는 DB/파일과 연결되는 클래스임을 명시 + bean으로 등록
public class MemberDAO {
	
	// DAO는 DB랑 연결하기 위한 Connection이 공통적으로 필요하다
	// -> 필드에 선언
	// + Mybatis 영속성 프레임워크를 이용하려면 Connection을 이용해 만들어진 객체
	// SqlSessionTemplate을 사용
	private Logger logger = LoggerFactory.getLogger(MemberDAO.class);
	
	@Autowired // root-context.xml에서 생성된 SqlSessionTemplate bean을 의존성 주입(DI)
	private SqlSessionTemplate sqlSession;
	

	public Member login(Member inputMember) {
		
		// 1행 조회(파라미터X) 방법
		//int count = sqlSession.selectOne("memberMapper.test1");
		
		//logger.debug(count+"");
	
		// 1행 조회(파라미터 O) 방법
		//String memberNickname = sqlSession.selectOne("memberMapper.test2", inputMember.getMemberEmail());
		//logger.debug(memberNickname);
		
		// 1행 조회(파라미터 VO인 경우)
		// String memberTel = sqlSession.selectOne("memberMapper.test3", inputMember);
																// memberEmail, memberPw
		// logger.debug(memberTel);
		
		// 1행 조회(파라미터 VO, 반환되는 결과도 VO)
		Member loginMember = sqlSession.selectOne("memberMapper.login", inputMember);
		
		
		return loginMember;
	}


	public int emailDupCheck(String memberEmail) {
		return sqlSession.selectOne("memberMapper.emailDupCheck", memberEmail);
		
		
	}


	public int nickDupCheck(String memberNickname) {
		return sqlSession.selectOne("memberMapper.nickDupCheck", memberNickname);
	}


	public int signUp(Member inputMember) {
		int result = sqlSession.insert("memberMapper.signUp", inputMember);
		
		// INSERT, UPDATE, DELETE를 수행하기 위한 메서드 존재함
		
		// * insert() / update() /delete() 메서드 반환값은 int 고정
		// -> resultType 생략 가능. 묵시적으로 _int
		
		return result;
	}


	public Member selectOne(String memberEmail) {
		Member selectMember = sqlSession.selectOne("memberMapper.selectOne", memberEmail);
		return selectMember;
	}


	public List selectAll() {
		return sqlSession.selectList("memberMapper.selectAll");
		
	}

}
