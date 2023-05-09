package edu.kh.comm.board.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.comm.board.model.vo.Reply;

@Repository
public class ReplyDAO {

	@Autowired
	private  SqlSessionTemplate sqlSession;

	/** 댓글 반환 메서드
	 * @param boardNo
	 * @return list
	 */
	public List<Reply> replyList(int boardNo) {
		return sqlSession.selectList("replyMapper.selectReply", boardNo);
	}

	/** 댓글 작성 메서드
	 * @param replyContent
	 * @param memberNo
	 * @param boardNo
	 * @return result
	 */
	public int replyInsert(Reply reply) {

		return sqlSession.insert("replyMapper.insertReply",reply);
	}

	/** 댓글 수정 메서드
	 * @param reply
	 * @return
	 */
	public int replyUpdate(Reply reply) {
		return sqlSession.update("replyMapper.updateReply", reply);
	}

	public int replyDelete(int replyNo) {
		return sqlSession.update("replyMapper.deleteReply", replyNo);
	}

}
