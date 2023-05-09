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

	/** 게시글 삽입 DAO
	 * @param detail
	 * @return boardNo
	 */
	public int insertBoard(BoardDetail detail) {
		int result = sqlSession.insert("boardMapper.insertBoard", detail); // 0 또는 1
		
		if(result > 0) result = detail.getBoardNo();
		
		// 게시글 삽입 성공 시 <selectKey> 태그를 이용해서 세팅된 boardNo 값을 반환함 
		
		return result;
	}

	/** 게시글 이미지 삽입(리스트) DAO
	 * @param boardImageList
	 * @return result
	 */
	public int insertBoardImageList(List<BoardImage> boardImageList) {
		
		return sqlSession.insert("boardMapper.insertBoardImageList", boardImageList);
	}

	/** 게시글 수정 DAO
	 * @param detail
	 * @return result
	 */
	public int updateBoard(BoardDetail detail) {
		
		return sqlSession.update("boardMapper.updateBoard", detail);
	}

	/** 게시글 이미지 삭제
	 * @param map
	 * @return result
	 */
	public int deleteBoardImage(Map<String, Object> map) {
		return sqlSession.delete("boardMapper.deleteBoardImage", map);
	}

	/** 게시글 이미지 1개 수정
	 * @param img
	 * @return result
	 */
	public int updateBoardImage(BoardImage img) {
		return sqlSession.update("boardMapper.updateBoardImage", img);
	}

	/** 게시글 이미지 1개 삽입
	 * @param img
	 * @return result
	 */
	public int insertBoardImage(BoardImage img) {
		return sqlSession.insert("boardMapper.insertBoardImage", img);
	}

	public int deleteBoard(Map<String, Object> map) {
		return sqlSession.update("boardMapper.deleteBoard", map);
	}


	/* 내가 짠 코드
	public int insertBoard(Map<String, Object> map) {
		
		
		return sqlSession.insert("boardMapper.insertBoard", map);
	}

	public int insertImage(Map<String, Object> map) {
		
		
		return sqlSession.insert("boardMapper.insertImage", map);
	}

	public int updateBoard(Map<String, Object> map) {
		return sqlSession.update("boardMapper.updateBoard", map);
	}

	public int deleteImage(Map<String, Object> map) {
		return sqlSession.delete("boardMapper.deleteImage", map);
	}

	public int deleteBoard(Map<String, Object> map) {
		return sqlSession.update("boardMapper.deleteBoard", map);
	}
	*/
}
