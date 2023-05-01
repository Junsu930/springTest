package edu.kh.comm.member.model.dao;

import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.comm.member.model.vo.Member;

@Repository
public class MyPageDAO {

	@Autowired // root-context.xml에서 생성된 SqlSessionTemplate bean을 의존성 주입(DI)
	private SqlSessionTemplate sqlSession;
	
	public int changePw(Member loginMember) {
		
		return sqlSession.update("myPageMapper.changePw", loginMember);
	}

	public String checkPw(Member loginMember) {
		
		return sqlSession.selectOne("myPageMapper.checkPw", loginMember.getMemberNo());
	}

	public int secession(Member loginMember) {
		return sqlSession.update("myPageMapper.secession", loginMember.getMemberNo());
	}

	public int updateInfo(Member loginMember) {
		return sqlSession.update("myPageMapper.updateMember", loginMember);
	}

	public int checkNick(String newNick) {
		return sqlSession.selectOne("myPageMapper.checkNick", newNick);
	}

	
	/** 프로필 이미지 수정
	 * @param map
	 * @return
	 */
	public int updateProfile(Map<String, Object> map) {
		return sqlSession.update("myPageMapper.updateProfile", map);
	}

}
