package edu.kh.comm.board.model.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.comm.board.model.vo.BoardDetail;
import edu.kh.comm.board.model.vo.BoardType;
import edu.kh.comm.member.model.vo.Member;

public interface BoardService{

	/** 게시판 코드, 이름 조회
	 * @return boardTypeList
	 */
	List<BoardType> selectBoardType();

	/** 게시글 목록 조회 서비스
	 * @param cp
	 * @param boardCode
	 * @return map
	 */
	Map<String, Object> selectBoardList(int cp, int boardCode);

	//List<BoardImage> boardImage(int boardNo);

	//BoardDetail detail(int boardNo);
 
	/** 게시글 상세 조회 서비스
	 * @param boardNo
	 * @return BoardDetail
	 */
	BoardDetail selectBoardDetail(int boardNo);

	/** 조회수 증가 메서드
	 * @param boardNo
	 * @return result
	 */
	int count(int boardNo);

	/** 검색 게시글 목록 조회 서비스
	 * @param paramMap
	 * @return map
	 */
	Map<String, Object> searchBoardList(Map<String, Object> paramMap);

	/** 게시글 삽입 서비스
	 * @param detail
	 * @param imageList
	 * @param webPath
	 * @param folderPath
	 * @return boardNo
	 * @throws Exception
	 */
	int insertBoard(BoardDetail detail, List<MultipartFile> imageList, String webPath, String folderPath) throws Exception;

	/** 게시글 수정 서비스
	 * @param detail
	 * @param imageList
	 * @param webPath
	 * @param folderPath
	 * @param deleteList
	 * @return result
	 */
	int updateBoard(BoardDetail detail, List<MultipartFile> imageList, String webPath, String folderPath,
			String deleteList) throws IOException;

	/** 게시글 삭제
	 * @param boardCode
	 * @param boardNo
	 * @return
	 */
	int deleteBoard(int boardCode, int boardNo);

	/* 내가 짠 코드 
	int insertBoard(Map<String, Object> map, Member loginMember);

	int insertImage(Map<String, Object> map) throws Exception;

	int updateBoard(Map<String, Object> map, Member loginMember);

	int deleteImage(Map<String, Object> map);

	int deleteBoard(int boardCode, int boardNo);
	*/
}
