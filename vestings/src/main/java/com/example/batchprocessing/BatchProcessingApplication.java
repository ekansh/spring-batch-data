package com.example.batchprocessing;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.batchprocessing.repositories.VestingScheduleRepository;
import com.example.batchprocessing.vo.Employee;
import com.example.batchprocessing.vo.VestedVO;
import com.example.batchprocessing.vo.Vesting;

@SpringBootApplication
public class BatchProcessingApplication implements CommandLineRunner{
	private static final Logger log = LoggerFactory.getLogger(BatchProcessingApplication.class);
	@Autowired
	private VestingScheduleRepository vestingScheduleRepository ;
	@Autowired
	private ApplicationArguments arguments;
	public static void main(String[] args) throws Exception {
		SpringApplication.run(BatchProcessingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("fromDate "+arguments.getOptionValues("fromDate"));
		String fromDate = arguments.getOptionValues("fromDate")!=null ?arguments.getOptionValues("fromDate").get(0):"20200401";
		//ALL VESTED STOCKS : solution for phase 1
		List<Vesting> allVestedStocks = vestingScheduleRepository.getVestingScheduleNative( Integer.valueOf(fromDate.replaceAll("-", ""))) ;
		
		List<VestedVO> result = enrichVestedStocksWithName(allVestedStocks);
		for (VestedVO vestedVO : result) {
			log.info(vestedVO.toString());
		}
	}
	private List<VestedVO> enrichVestedStocksWithName(List<Vesting> allVestedStocks) {
		List<Employee> employees = vestingScheduleRepository.getAllEmployees();
		
		Map<String, String>  employeeCache = new HashMap<>();
		employees.stream().forEach(e-> employeeCache.put(e.getEmployeeId(),e.getEmployeeName()));
		
		List<VestedVO> result = new ArrayList<>();
		for (Vesting  vestedStocks : allVestedStocks) {
			VestedVO vestedVO = new VestedVO();
			vestedVO.setAwardId(vestedStocks.getAwardId());
			vestedVO.setEmployeeId(vestedStocks.getEmployeeId());
			vestedVO.setEmployeeName(employeeCache.get(vestedStocks.getEmployeeId()));
			vestedVO.setQuantity(vestedStocks.getVested()==null || vestedStocks.getVested()<0 ? 0: vestedStocks.getVested());
			result.add(vestedVO);
		}
		return result;
	}
}
