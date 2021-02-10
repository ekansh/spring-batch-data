package com.example.batchprocessing.stepsconfig;


import org.springframework.batch.item.ItemProcessor;

import com.example.batchprocessing.constants.Constants;
import com.example.batchprocessing.csv.VestingScheduleCSV;
import com.example.batchprocessing.dbres.VestingSchedule;


public class VestingScheduleItemProcessor implements ItemProcessor<VestingScheduleCSV,VestingSchedule> {


	@Override
	public VestingSchedule process(final VestingScheduleCSV item) throws Exception {
		VestingSchedule vestingSchedule = new VestingSchedule();
		vestingSchedule.setAwardDate(Integer.valueOf(item.getAwardDate().replaceAll("-", "")));
		if(Constants.CANCEL.equalsIgnoreCase(item.getEventType())) {
			vestingSchedule.setQuantity( -1*item.getQuantity() );
		}else if(Constants.VEST.equalsIgnoreCase(item.getEventType())) {
			vestingSchedule.setQuantity(  item.getQuantity());
		}
		vestingSchedule.setAwardId(item.getAwardId());
		vestingSchedule.setEmployeeId(item.getEmployeeId());
		vestingSchedule.setEmployeeName(item.getEmployeeName());
		vestingSchedule.setEventType(item.getEventType());
		return vestingSchedule;
	}

}
