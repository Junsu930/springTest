package edu.kh.comm.board.model.service;

import java.util.List;

import edu.kh.comm.board.model.vo.Reply;

public interface ReplyService {

	List<Reply> replyList(int boardNo);


	int replyInsert(String replyContent, int memberNo, int boardNo, int parentReplyNo);


	int replyUpdate(Reply reply);


	int replyDelete(int replyNo);

}
