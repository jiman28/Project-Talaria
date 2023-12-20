package com.noelwon.model.ccc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity, Integer>{
	List<ReplyEntity> findByboardEntity(BoardEntity boardEntity);
	List<ReplyEntity> findBycompanyEntity(CompanyEntity companyEntity);
	List<ReplyEntity> findByTradeEntity(TradeEntity tradeEntity);
}
