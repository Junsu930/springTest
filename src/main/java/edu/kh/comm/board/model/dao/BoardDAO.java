package edu.kh.comm.board.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.comm.board.model.vo.BoardType;

@Repository
public class BoardDAO {

	@Autowired
	private  SqlSessionTemplate sqlSession;

	/** 게시판 코드, 이름 조회
	 * @return boardTypeList
	 */
	public List<BoardType> selectBoardType() {
		
		return sqlSession.selectList("boardMapper.selectBoardType");
	}
}
