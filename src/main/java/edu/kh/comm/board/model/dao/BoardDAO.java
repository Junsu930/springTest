package edu.kh.comm.board.model.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.comm.board.model.vo.Board;
import edu.kh.comm.board.model.vo.BoardDetail;
import edu.kh.comm.board.model.vo.BoardImage;
import edu.kh.comm.board.model.vo.BoardType;
import edu.kh.comm.board.model.vo.Pagination;
import edu.kh.comm.common.Util;

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

	/** 특정 게시판의 전체 게시글 수 조회
	 * @param boardCode
	 * @return listCount
	 */
	public int getListCount(int boardCode) {
		return sqlSession.selectOne("boardMapper.getListCount",boardCode);
	}

	/** 게시판 목록 조회 DAO
	 * @param pagination
	 * @param boardCode
	 * @return boartList
	 */
	public List<Board> selectBoardList(Pagination pagination, int boardCode) {
		
		// RowBounds 객체 (마이바티스)
		// - 전체 조회 결과에서 
		// 몇 개 행을 건너뛰고(offset) 그 다음 몇 개의 행만(limit) 조회할 것인지 지정

		int offset = (pagination.getCurrentPage() -1) * pagination.getLimit();
		
		RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());
		
		return sqlSession.selectList("boardMapper.selectBoardList", boardCode, rowBounds);
	}

	/*
	public List<BoardImage> boardImage(int boardNo) {
		return sqlSession.selectList("boardMapper.boardImage", boardNo);
	}

	public BoardDetail detail(int boardNo) {
		return sqlSession.selectOne("boardMapper.detail", boardNo);
	}
	*/

	
	/** 게시글 상세 조회
	 * @param boardNo
	 * @return detail
	 */
	public BoardDetail selectBoardDetail(int boardNo) {
		return sqlSession.selectOne("boardMapper.selectBoardDetail",boardNo);
	}

	/** 조회수 증가
	 * @param boardNo
	 * @return result
	 */
	public int count(int boardNo) {
		return sqlSession.update("boardMapper.count", boardNo);
	}

	/** 검색 조건에 맞는 게시글 목록의 전체 개수 조회 DAO
	 * @param paramMap
	 * @return listCount
	 */
	public int searchCount(Map<String, Object> paramMap) {
		return sqlSession.selectOne("boardMapper.searchCount", paramMap);
	}

	/** 검색 조건에 맞는 게시글 목록 조회(페이징 처리)
	 * @param paramMap
	 * @param pagination
	 * @return
	 */
	public List<Board> searchBoardList(Map<String, Object> paramMap, Pagination pagination) {

		int offset = (pagination.getCurrentPage() -1) * pagination.getLimit();
		
		RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());
		
		return sqlSession.selectList("boardMapper.searchBoardList", paramMap, rowBounds);
	}

	/** 글 등록 
	 * @param map
	 * @return result
	 */
	public int insertBoard(Map<String, Object> map) {
		
		
		return sqlSession.insert("boardMapper.insertBoard", map);
	}

	/** 이미지 등록
	 * @param map
	 * @return
	 */
	public int insertImage(Map<String, Object> map) {
		
		
		return sqlSession.insert("boardMapper.insertImage", map);
	}
}
