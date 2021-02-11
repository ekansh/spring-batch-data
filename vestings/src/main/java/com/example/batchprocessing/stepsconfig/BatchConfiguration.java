package com.example.batchprocessing.stepsconfig;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.AbstractLineTokenizer;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.BindException;

import com.example.batchprocessing.csv.VestingScheduleCSV;
import com.example.batchprocessing.dbres.VestingSchedule;

// tag::setup[]
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	private static final Logger log = LoggerFactory.getLogger(BatchConfiguration.class);
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	// end::setup[]
	 @Autowired
	private ApplicationArguments args;
	
	// tag::readerwriterprocessor[]
	@Bean
	public FlatFileItemReader<VestingScheduleCSV> reader() {
		log.info("input file = "+args.getOptionValues("inputFile"));
		String inputFile = args.getOptionValues("inputFile")!=null ? args.getOptionValues("inputFile").get(0):"";
		
		CustomLineTokenizer lineTokenizer = new CustomLineTokenizer();
		lineTokenizer.setNames(new String[] {"eventType", "employeeId", "employeeName", "awardId", "awardDate", "quantity"});
		
		DefaultLineMapper<VestingScheduleCSV> lineMapper = new DefaultLineMapper<>();
		  lineMapper.setFieldSetMapper(new BeanWrapperFieldSetMapper<VestingScheduleCSV>() {{
				setTargetType(VestingScheduleCSV.class);
			}});
		  lineMapper.setLineTokenizer(lineTokenizer);
		
		return new FlatFileItemReaderBuilder<VestingScheduleCSV>()
			.name("vestingScheduleItemReader")
			.resource(new FileSystemResource(inputFile))
			.lineMapper(lineMapper)
			.build();
	}

	@Bean
	public VestingScheduleItemProcessor processor() {
		return new VestingScheduleItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<VestingSchedule> writer(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<VestingSchedule>()
			.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
			.sql("INSERT INTO VESTING_SCHEDULE (EVENT_TYPE, EMPLOYEE_ID, EMPLOYEE_NAME, AWARD_ID, AWARD_DATE, QUANTITY )"
					+ " VALUES (:eventType, :employeeId, :employeeName, :awardId, :awardDate, :quantity)")
			.dataSource(dataSource)
			.build();
	}
	// end::readerwriterprocessor[]

	// tag::jobstep[]
	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {

		return jobBuilderFactory.get("importUserJob")
			.incrementer(new RunIdIncrementer())
			.listener(listener)
			.flow(step1)
			.end()
			.build();
	}

	@Bean
	public Step step1(JdbcBatchItemWriter<VestingSchedule> writer) {
		return stepBuilderFactory.get("step1")
			.<VestingScheduleCSV, VestingSchedule> chunk(10)
			.reader(reader())
			.processor(processor())
			.writer(writer)
			.build();
	}
	// end::jobstep[]
}
