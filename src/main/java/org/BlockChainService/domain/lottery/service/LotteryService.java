package org.BlockChainService.domain.lottery.service;

import java.util.ArrayList;
import java.util.List;

import org.BlockChainService.domain.com.dto.EthInputVO;
import org.BlockChainService.domain.com.dto.EthResultVO;
import org.BlockChainService.domain.com.dto.Function;
import org.BlockChainService.domain.com.dto.Transaction;
import org.BlockChainService.domain.service.HttpService;
import org.BlockChainService.domain.utils.CommonUtils;
import org.BlockChainService.domain.utils.FunctionEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LotteryService {

	@Value("${ethereum.contract.address}")
	private String contractAddress;
	
	@Autowired
	private HttpService httpService;
	
	/**
	 * get the Contract owner address
	 * @return
	 */
	public String getOwner()
	{
		return etherCall(
				new Transaction.Builder()
				.addTo(contractAddress)
				.addData(FunctionEncoder.encode(new Function("getOwner")))
				.build());
	}
	

	/**
	 * bet(byte challenges) is contract function
	 * @param challenges check the + 3 block hash 
	 * @param from is the account Value, use this contract
	 * @return
	 */
	public String bet(byte challenges, String from, String value)
	{
		List<Object> list = new ArrayList<>();
		list.add(challenges);
		
		return etherCall(
				new Transaction.Builder()
				.addTo(contractAddress)
				.addData(FunctionEncoder.encode(new Function("bet", list)))
				.addFrom(from)
				.addvalue(value)
				.build());
	}

	// distribute()
	/**
	 * distribute 
	 * @return
	 */
	public String distribute()
	{
		return etherCall(
				new Transaction.Builder()
				.addTo(contractAddress)
				.addData(FunctionEncoder.encode(new Function("distribute")))
				.build());
	}

	
	//betAndDistribute(byte challenges)
	/**
	 * 배팅 후 즉시 결과를 확인한다.
	 * @param challenges
	 * @param from
	 * @param value
	 * @return
	 */
	public String betAndDistribute(byte challenges, String from, String value)
	{
		List<Object> list = new ArrayList<>();
		list.add(challenges);
			
		return etherCall(
				new Transaction.Builder()
				.addTo(contractAddress)
				.addData(FunctionEncoder.encode(new Function("betAndDistribute", list)))
				.addFrom(from)
				.addvalue(value)
				.build());
	}
	
	/**
	 * 
	 * @param answer
	 * @return
	 */
	public String setAnswerForTest(byte answer)
	{
		List<Object> list = new ArrayList<>();
		list.add(answer);
		
		return etherCall(
				new Transaction.Builder()
				.addTo(contractAddress)
				.addData(FunctionEncoder.encode(new Function("setAnswerForTest", list)))
				.build());
	}
	
	// getPot()
	/**
	 * get the pot money
	 * @return 
	 */
	public String getPot()
	{
		return etherCall(
				new Transaction.Builder()
				.addTo(contractAddress)
				.addData(FunctionEncoder.encode(new Function("getPot")))
				.build());
	}

	/**
	 * 
	 * @param index is number you wanted know
	 * @return
	 */
	public String getBetInfo(int index)
	{
		List<Object> list = new ArrayList<>();
		list.add(index);
		
		return etherCall(
				new Transaction.Builder()
				.addTo(contractAddress)
				.addData(FunctionEncoder.encode(new Function("getBetInfo")))
				.build());
	}

	public String createFilter()
	{
		// BET : 0x100791de9f40bf2d56ffa6dc5597d2fd0b2703ea70bc7548cd74c04f5d215ab7
		// WIN : 0x8219079e2d6c1192fb0ff7f78e6faaf5528ad6687e69749205d87bd4b156912b
		// LOSE : 0x857de42f5f3a8f5de6cedde05ecd2843e3d33c293b4a75322a56e345f8bc75b4
		// DRAW : 0x72ec2e949e4fad9380f9d5db3e2ed0e71cf22c51d8d66424508bdc761a3f4b0e
		// REFUND : 0x59c0185881271a0f53d43e6ab9310091408f9e0ff9ae2512613de800f26b8de4 
		
	}
	
	
	private String etherCall(Transaction transaction)
	{	
		EthInputVO<?, EthResultVO> gethInputVO = new EthInputVO<>("eth_call", java.util.Arrays.asList(transaction, "latest"), EthResultVO.class);	
		return httpService.callGethFunction(CommonUtils.getJsonString(gethInputVO), EthResultVO.class).getResult();
	}
}
