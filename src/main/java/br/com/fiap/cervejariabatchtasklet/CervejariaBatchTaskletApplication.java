package br.com.fiap.cervejariabatchtasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.nio.file.Paths;

//@SpringBootApplication
//@EnableBatchProcessing
public class CervejariaBatchTaskletApplication {

	Logger logger = LoggerFactory.getLogger(CervejariaBatchTaskletApplication.class);

	@Bean
	public Tasklet tasklet(@Value("${file.path}") String path){
		return (contribution, chunkContext) -> {
			File file = Paths.get(path).toFile();

			if(file.delete()) {
				logger.info("Sucesso");
			}
			else {
				logger.info("Deu ruim");
			}

			return RepeatStatus.FINISHED;
		};
	}

	@Bean
	public Step step(StepBuilderFactory stepBuilderFactory, Tasklet tasklet){
		return stepBuilderFactory
				.get("Delete File Step")
				.tasklet(tasklet)
				.build();

	}

	@Bean
	public Job job(JobBuilderFactory jobBuilderFactory,
				   Step step){
		return jobBuilderFactory
				.get("Delete File Job")
				.start(step)
				.build();
	}

	public static void main(String[] args) {

		//SpringApplication.run(CervejariaBatchTaskletApplication.class, args);
	}

}
