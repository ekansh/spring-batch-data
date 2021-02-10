package com.example.batchprocessing.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.batchprocessing.dbres.VestingSchedule;
import com.example.batchprocessing.vo.Employee;
import com.example.batchprocessing.vo.Vesting;

public interface VestingScheduleRepository extends CrudRepository<VestingSchedule, Integer> {
	@Query(value = "SELECT   s1.EMPLOYEE_ID as employeeId , s1.AWARD_ID as awardId, s2.vested as vested \r\n" + 
			"from \r\n" + 
			"(SELECT  EMPLOYEE_ID,   AWARD_ID, FROM VESTING_SCHEDULE  \r\n" + 
			"GROUP BY EMPLOYEE_ID,AWARD_ID ) s1\r\n" + 
			"LEFT JOIN\r\n" + 
			"(SELECT  EMPLOYEE_ID,AWARD_ID, SUM(quantity) as vested FROM VESTING_SCHEDULE  \r\n" + 
			"where AWARD_DATE<=:fromDate GROUP BY EMPLOYEE_ID,AWARD_ID ) s2 \r\n" + 
			" on s1.EMPLOYEE_ID =s2.EMPLOYEE_ID AND s1.AWARD_ID  = s2.AWARD_ID ", nativeQuery = true)
	List<Vesting> getVestingScheduleNative( @Param("fromDate") Integer fromDate);
	
	@Query(value = "select distinct employee_id as employeeId , employee_name as employeeName from VESTING_SCHEDULE  where Employee_name !=''"  ,  nativeQuery = true)
	List<Employee> getAllEmployees( );
 
}
