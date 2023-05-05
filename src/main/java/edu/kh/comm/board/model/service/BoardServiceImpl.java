package edu.kh.comm.board.model.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.comm.board.model.dao.BoardDAO;
import edu.kh.comm.board.model.vo.Board;
import edu.kh.comm.board.model.vo.BoardDetail;
import edu.kh.comm.board.model.vo.BoardType;
import edu.kh.comm.board.model.vo.Pagination;
import edu.kh.comm.common.Util;
import edu.kh.comm.member.model.vo.Member;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardDAO dao;

	// 게시판 코드, 이름 조회
	@Override
	public List<BoardType> selectBoardType() {
		
		return dao.selectBoardType();
	}

	// 게시글 목록 조회
	@Override
	public Map<String, Object> selectBoardList(int cp, int boardCode) {
		
		// 1) 게시판 이름 조회 -> 인터셉터 application에 올려둔 boardTypeList 쓸 수 있다
		// 2) 페이지네이션 객체 생성(listCount)
		int listCount = dao.getListCount(boardCode);
		
		Pagination pagination = new Pagination(cp, listCount);
		
		// 3) 게시글 목록 조회
		List<Board> boardList = dao.selectBoardList(pagination, boardCode);
		
		// map 만들어 담기
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		return map;
	}
	/*
	@Override
	public List<BoardImage> boardImage(int boardNo) {
		
		return dao.boardImage(boardNo);
	}

	@Override
	public BoardDetail detail(int boardNo) {
		return dao.detail(boardNo);
	}
	*/

	// 게시글 상세 조회 서비스 구현
	@Override
	public BoardDetail selectBoardDetail(int boardNo) {
		return dao.selectBoardDetail(boardNo);
	}

	@Override
	public int count(int boardNo) {
		return dao.count(boardNo);
	}

	@Override
	public Map<String, Object> searchBoardList(Map<String, Object> paramMap) {
		System.out.println("으아아아 호출됨");
		// 검색 조건에 맞는 게시글 목록의 전체 개수 조회 (페이지네이션을 위해서)
		int listCount = dao.searchCount(paramMap);
		System.out.println(listCount);
		// 페이지네이션 객체 생성 
		Pagination pagination = new Pagination((int)paramMap.get("cp"), listCount);
		
		// 검색 조건에 맞는 게시글 목록 조회
		List<Board> boardList = dao.searchBoardList(paramMap, pagination);
		// map에 담기 
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		return map;
	}

	@Override
	public int insertBoard(Map<String, Object> map, Member loginMember) {
		
		
		int result = 0;

		
		map.put("loginMember", loginMember);
		
		System.out.println("service단에서 image리스트" + map.get("imageFiles"));
		
		result = dao.insertBoard(map);

		return result;
	}

	/** 이미지 db저장 메서드
	 *
	 */
	@Override
	public int insertImage(Map<String, Object> map) throws Exception{
		int result = 0;
		// 이미지 파일 가져오기
		List<MultipartFile> ImageList = (ArrayList<MultipartFile>)map.get("uploadImage");
		
		System.out.println(ImageList);
		
			
		for(MultipartFile files : ImageList) {
			
			String renameImage =  Util.fileRename(files.getOriginalFilename()); // 변경된 파일명 저장
			
			map.put("rename",  (map.get("webPath") + renameImage));
			map.put("fileName", renameImage);
			map.put("field", files.getName());
			result = dao.insertImage(map);
			
			if(result>0) {
				files.transferTo(new File(map.get("folderPath") + renameImage));
			}
		}

		return result;
	}

	@Override
	public int updateBoard(Map<String, Object> map, Member loginMember) {
			int result = 0;
	
			
			map.put("loginMember", loginMember);

			
			result = dao.updateBoard(map);
	
			return result;
	}

	@Override
	public int deleteImage(Map<String, Object> map) {
		
		int result = 0;
		
		String deleteImage = (String)map.get("deleteList");
		
		String[] deleteList = deleteImage.split(",");
		
		for(String num : deleteList) {
			map.put("deleteNum", num);
			
			result = dao.deleteImage(map);
		}
		
		return result;
	}

	@Override
	public int deleteBoard(int boardCode, int boardNo) {
		
		Map<String,Object> map = new HashMap<>();
		
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		
		return dao.deleteBoard(map);
	}


}