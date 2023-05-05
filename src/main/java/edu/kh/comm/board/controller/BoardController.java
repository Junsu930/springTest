package edu.kh.comm.board.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.comm.board.model.service.BoardService;
import edu.kh.comm.board.model.vo.BoardDetail;
import edu.kh.comm.common.Util;
import edu.kh.comm.member.model.vo.Member;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private BoardService service;
	
	// 게시글 목록 조회
	// 주소변수 @PathVariable을 통해 주소 부분에 변수 사용 가능
	// -> 자동으로 request scope에 등록됨 => jsp에서 ${value} EL 작성 가능
	
	// PathVariable : 요청 자원을 식별하는 경우
	// QueryString : 정렬, 검색 등의 필터링 옵셩
	@GetMapping("/list/{boardCode}")
	public String boardList(@PathVariable("boardCode") int boardCode,
							@RequestParam(value="cp", required=false, defaultValue="1") int cp,
							Model model ,
							@RequestParam Map<String, Object> paramMap
							// key, query, cp
							) {
		
		// 게시글 목록 조회 서비스 호출
		// 1) 게시판 이름 조회 -> 인터셉터 application에 올려둔 boardTypeList 쓸 수 있다
		// 2) 페이지네이션 객체 생성(listCount)
		// 3) 게시글 목록 조회
		
		Map<String,Object> map = null;
		
		if(paramMap.get("key")==null) {// 검색이 아닌 경우
			
			map = service.selectBoardList(cp, boardCode);
			
		}else { // 검색인 경우
			paramMap.put("cp", cp);
			paramMap.put("boardCode", boardCode);
			
			map = service.searchBoardList(paramMap);
		}
		
		model.addAttribute("map", map);
		
		return "board/boardList";
	}
	
	/*
	@GetMapping("/list/detail")
	public String boardDetail(@RequestParam Map<String, Object> map, Model model) {
		
		List<BoardImage> imgList = service.boardImage(Integer.parseInt((String)map.get("no")));
		
		BoardDetail detail = service.detail(Integer.parseInt((String)map.get("no")));
		
		detail.setImageList(imgList);
		
		model.addAttribute("cp", map.get("cp"));
		
		model.addAttribute("detail", detail);
		
		model.addAttribute("type", map.get("type"));
		
		return "board/boardDetail";
	}
	 */
	
	// 게시글 상세 조회
	@GetMapping("/detail/{boardCode}/{boardNo}")
	public String boardDetail( @PathVariable("boardCode") int boardCode, 
								@PathVariable("boardNo") int boardNo,
								@RequestParam(value="cp", required=false, defaultValue="1") int cp,
								Model model, HttpSession session, HttpServletResponse resp,
								HttpServletRequest req){
		
		// 게시글 상세 조회 서비스 호출
		BoardDetail detail = service.selectBoardDetail(boardNo);
		
		if(detail != null) {
			
			
			// 쿠키를 이용한 조회수 중복 증가 방지 코드 + 본인의 글은 조회수 증가 금지
			
			// @ModelAttribute("loginMember") Member loginMember (사용 불가)
			// @ModelAttribute는 별도의 required 속성이 없어서 무조건 필수 !! 
			// -> 세선에 loginMember가 없으면 예외 발생
			
			// 해결 방법 HttpSession을 이용
			// -> session.getAttribute("loginMember")
			
			Member loginMember = (Member)session.getAttribute("loginMember");
			
			int memberNo = 0;
			if(loginMember != null) {
				memberNo= loginMember.getMemberNo();
			}
			
			// 글쓴이와 현재 클라이언트가 같은지 아닌지
			if(detail.getMemberNo() != memberNo) {
				
				// 쿠키가 있는지 없는지
				// 있다면 쿠키 이름 "readBoardNo" 있는지?
				
				//없다면 만들고 있으면 게시글 번호를 추가
				
				Cookie cookie = null; // 기존에 존재하던 쿠키를 저장하는 변수
				
				Cookie[] cArr = req.getCookies(); // 쿠키 얻어오기
				
				if(cArr != null && cArr.length>0) {// 얻어온 쿠키가 있을 경우
					for(Cookie c: cArr) {
						
						// 얻어온 쿠키 중 이름이 "readBoardNo"가 있으면 얻어오기 
						if(c.getName().equals("readBoardNo")) {
							cookie = c;
						}
					}
				}
				int result = 0;
				if(cookie == null) { // 기존에 "readBoarNo" 이름의 쿠키가 없던 경우
					cookie = new Cookie("readBoardNo", boardNo+"");
					// 조회수 증가하는 서비스 호출
					result = service.count(boardNo);
					
					
				}else { // 기존에 "readBoardNo" 이름의 쿠키가 있던 경우
					// "readBoardNo" : "1/2/4/5/3/19".. + boardNo
					// -> 쿠키에 저장된 값 뒤쪽에 현재 조회된 게시글 번호 추가 
					
					String[] temp = cookie.getValue().split("/");
					List<String> list = Arrays.asList(temp); // 배열 -> List변환
					
					if(list.indexOf(boardNo+"") == -1) { // 기존 값에 같은 글번호가 없다면 추가
						cookie.setValue(cookie.getValue()+ "/" + boardNo);
						result = service.count(boardNo);
						
						
					}
				}
				
				if(result > 0 ) {
					// 조회된 데이터 db와 동기화
					detail.setReadCount(detail.getReadCount()+1);
					cookie.setPath(req.getContextPath());
					resp.addCookie(cookie);
					
					//+ 쿠키 maxAge1시간
				}
			}
			
			
			
			/* 내가 짠 코드
			int countResult = 0;
			
			Member loginMember = (Member)session.getAttribute("loginMember");
			Cookie cookie[] = req.getCookies();
			Cookie cooked = null;
			
			for(Cookie inCooked :cookie) {
				if(inCooked.getName().equals("readBoardNo")) {
					cooked= inCooked;
				}
			}
			
			
			// 상세 조회 성공 시 
			// 세션이 있는지 없는지 
			// 세션이 있으면 memberNo 세팅
			
			if(loginMember != null) {
				session.setAttribute("memberNo", loginMember.getMemberNo());
				if(loginMember.getMemberNo() != detail.getMemberNo()) {
					 // 로그인 멤버가 없을 경우
					// 쿠키가 없을 경우 
					System.out.println("로그인 멤버 있음");
					if(cooked == null) {
						System.out.println("쿠키가 없다");
						// 조회수를 증가시켜준다. 
						countResult = service.count(boardNo);
						System.out.println("조회수 증가" + countResult);
						if(countResult > 0) {
							
							Cookie boardCookie = new Cookie("readBoardNo", boardNo+"");
							boardCookie.setPath("/");
							boardCookie.setMaxAge(60 * 60);
							resp.addCookie(boardCookie);
						}
					}else {
						// 쿠키가 이미 존재할 경우
						// 조회된 넘버들 
						String cookedVal = cooked.getValue();
						ArrayList<String> cookedList = new ArrayList<String>(Arrays.asList(cookedVal.split("/")));
						String boardNos = detail.getBoardNo() + "";
						System.out.println(cookedList);
						if(!cookedList.contains(boardNos)) {
							countResult = service.count(boardNo);
							if(countResult > 0) {
								System.out.println("증가시킴");
								cooked.setPath("/");
								cooked.setValue(cookedVal+"/"+boardNos);
								resp.addCookie(cooked);
							}
						}
					}
				}
			}else { // 로그인 멤버가 없을 경우
				// 쿠키가 없을 경우 
				System.out.println("로그인 멤버 없음");
				if(cooked == null) {
					System.out.println("쿠키가 없다");
					// 조회수를 증가시켜준다. 
					countResult = service.count(boardNo);
					System.out.println("조회수 증가" + countResult);
					if(countResult > 0) {
						
						Cookie boardCookie = new Cookie("readBoardNo", boardNo+"");
						boardCookie.setPath("/");
						boardCookie.setMaxAge(60 * 60);
						resp.addCookie(boardCookie);
					}
				}else {
					// 쿠키가 이미 존재할 경우
					// 조회된 넘버들 
					System.out.println("쿠키가 존재함");
					
					String cookedVal = cooked.getValue();
					System.out.println(cookedVal);
					ArrayList<String> cookedList = new ArrayList<String>(Arrays.asList(cookedVal.split("/")));
					String boardNos = detail.getBoardNo() + "";
					System.out.println(cookedList);
					if(! cookedList.contains(boardNos)) {
						
						countResult = service.count(boardNo);
						if(countResult > 0) {
							System.out.println("증가시킴");
							
							cooked.setPath("/");
							cooked.setValue(cookedVal+"/"+boardNos);
							resp.addCookie(cooked);
						}
					}
				}
			}
		}
	
		
				// 글쓴이와 현재 클라이언트가 같은지 아닌지 (
					//같지 않으면 -> 조회수 증가)
						// 쿠기가 있는지 없는지
							// 쿠키가 있다면 쿠키 이름이 readBoardNo
							// 없다면 만들어라
							// 있다면 쿠키에 저장된 값 뒤쪽에 현재 조회된 게시글 번호를 추가 
				// 이미 조회된 데이터 DB와 동기화 
			
				// + 쿠키 maxAge 1시간 (60 * 60)
				// -> 단, 기존 쿠키값에 중복되는 번호 없어야 함
				detail = service.selectBoardDetail(boardNo);
				model.addAttribute("detail", detail);
				model.addAttribute("cp", cp);
		*/
	}
		
		model.addAttribute("detail", detail);
		return "/board/boardDetail";
	}
	
	// 게시글 작성 화면 전환
	@GetMapping("/write/{boardCode}")
	public String boardWriteForm(@PathVariable("boardCode") int boardCode,
			String mode, @RequestParam(value="no", required=false, defaultValue="0") int boardNo,
			Model model) {
		
		if(mode.equals("update")) {
			// 게시글 상세 조회 서비스 호출(boardNo)
			BoardDetail detail = service.selectBoardDetail(boardNo);
			// -> 개행문자가 <br>로 되어있는 상태 - > textarea 출력 예정이기 때문에 \n으로 변경
		
			detail.setBoardContent(Util.newLineClear(detail.getBoardContent()));
			
			model.addAttribute("detail", detail);
			
			
			
		}
		
		model.addAttribute("boardCode", boardCode);
		return "board/boardWriteForm";
	}
	
	// 게시글 작성 완료
	@PostMapping("/write")
	public String write(@RequestParam Map<String, Object> map, @RequestParam("0") MultipartFile image0,
			@RequestParam("1") MultipartFile image1, @RequestParam("2") MultipartFile image2,
			@RequestParam("3") MultipartFile image3, @RequestParam("4") MultipartFile image4, HttpServletRequest req,
			HttpSession session) {
		
		List<MultipartFile> fileList = new ArrayList<>();
		MultipartFile[] filedummy = {image0, image1, image2, image3, image4};
		
		for(MultipartFile file :filedummy) {
			if(file.getSize()!=0) {
				fileList.add(file);
			}
		}
		System.out.println(map);
		System.out.println("파일리스트"+fileList);
		

		// 경로 작성하기
		
		// 1)웹 접근 경로 (/comm/resources/images/memberProfile/)
		String webPath = "/resources/images/board/";
		
		// 2)서버 저장 폴더 경로 
		// C:\workspace\7_framework\comm\src\main\webapp\resources\images\board
		String folderPath = req.getSession().getServletContext().getRealPath(webPath);
		
		//  경로 2개, 이미지, delete, 회원번호 map에 담기
		map.put("webPath", webPath);
		map.put("folderPath", folderPath);
		map.put("uploadImage", fileList);
		
		
		if(map.get("mode").equals("insert")) { // insert일 경우
			
			Member loginMember = (Member)session.getAttribute("loginMember");
			int result = service.insertBoard(map, loginMember);
			
			System.out.println("결과값" + result);
			
			if(result > 0 && fileList.size() != 0) {
				System.out.println("파일이 있다잉");
				
				
				try {
					int fileResult = service.insertImage(map);
					System.out.println("파일 삽입 결과 : " +fileResult);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}else { // update일 경우
			if(map.get("deleteList") != null || !map.get("deleteList").equals("")) {// 이미지 수정이 있을 경우 
				
				int deleteResult = service.deleteImage(map);
				System.out.println("삭제됐슝"+ deleteResult);
						
				
			}
			Member loginMember = (Member)session.getAttribute("loginMember");
			int result = service.updateBoard(map, loginMember);
			
			System.out.println("결과값" + result);
			
			if(result > 0 && fileList.size() != 0) {
				
				try {
					int fileResult = service.insertImage(map);
					System.out.println("파일 삽입 결과 : " +fileResult);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
	
		return "redirect:../board/list/" + map.get("type");
	}
	
	@GetMapping("delete/{boardCode}/{boardNo}")
	public String deleteBoard(@PathVariable("boardCode") int boardCode, 
			@PathVariable("boardNo") int boardNo, HttpServletRequest req){
		
		int result = service.deleteBoard(boardCode, boardNo);
		String referer = req.getHeader("Referer");
		
		if(result>0) {
			return "redirect:/board/list/" + boardCode;
			
		}else {
			return "redirect:"+referer;
		}
		
		
		
	}
	
}

