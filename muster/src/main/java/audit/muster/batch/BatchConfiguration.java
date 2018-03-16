/**
 * 
 */
package audit.muster.batch;

import java.beans.PropertyEditor;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ChunkListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;

import audit.muster.batch.listener.JobCompletionNotificationListener;
import audit.muster.batch.processor.AuditItemProcessor;
import audit.muster.batch.processor.AuditToTimeSheetItemProcessor;
import audit.muster.bean.Audit;
import audit.muster.bean.TimeSheet;
import audit.muster.jpa.repo.AuditRepository;
import audit.muster.jpa.repo.TimeSheetRepository;


/**
 * @author Prajapati
 *
 */
@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Autowired
    public AuditRepository auditRepository;

    @Autowired
    public TimeSheetRepository timeSheetRepository;
    
    @Autowired
    private Environment env;
    
    @PersistenceContext
    private EntityManager em;
    
    // tag::readerwriterprocessor[]
    @Bean
    public AbstractItemStreamItemReader<Audit> reader() throws MalformedURLException {
        Map<Class<?>, PropertyEditor> customEditors = new HashMap<Class<?>, PropertyEditor>();
        customEditors.put(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy HH:mm"), true));
        FlatFileItemReader<Audit> reader = new FlatFileItemReader<Audit>();
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource("./in/audit-data.csv"));
        reader.setLineMapper(new DefaultLineMapper<Audit>() {{
            setLineTokenizer(new DelimitedLineTokenizer(";") {{
                setNames(new String[] { "keyword", "dateEvent", "source", "eventId", "taskCategory" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Audit>() {{
                setTargetType(Audit.class);
				setCustomEditors(customEditors);
            }});
        }});
        return reader;
    }
    
    @Bean
    public AbstractItemStreamItemReader<Audit> reader2() throws MalformedURLException {
        Map<Class<?>, PropertyEditor> customEditors = new HashMap<Class<?>, PropertyEditor>();
        customEditors.put(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a"), true));
        FlatFileItemReader<Audit> reader = new FlatFileItemReader<Audit>();
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource("./in/EventViewer_LogOnLogOff.csv"));
        reader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());
        reader.setLineMapper(new DefaultLineMapper<Audit>() {{
            setLineTokenizer(new DelimitedLineTokenizer(",") {{
                setNames(new String[] { "keyword", "dateEvent", "source", "eventId", "taskCategory", "comment" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Audit>() {{
                setTargetType(Audit.class);
				setCustomEditors(customEditors);
            }});
        }});
        return reader;
    }

    @Bean
    public AuditItemProcessor processor() {
        return new AuditItemProcessor(auditRepository);
    }

    @Bean
    public JpaItemWriter<Audit> auditWriter() {
    	JpaItemWriter<Audit> writer = new JpaItemWriter<Audit>();
    	writer.setEntityManagerFactory(em.getEntityManagerFactory());
        return writer;
    }

    @Bean
    public JpaItemWriter<TimeSheet> timeSheetWriter() {
    	JpaItemWriter<TimeSheet> writer = new JpaItemWriter<TimeSheet>();
    	writer.setEntityManagerFactory(em.getEntityManagerFactory());
        return writer;
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener) throws MalformedURLException {
        return jobBuilderFactory.get("importEventViewerReport")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .next(step2())
                .end()
                .build();
    }
    @Bean
    public Step step2() throws MalformedURLException {
        return stepBuilderFactory.get("step2")
                .<Audit, TimeSheet> chunk(100)
                .reader(new ItemReader<Audit>() {

					private Iterator<Audit> iterator;

					@Override
					public Audit read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
						if(iterator == null) {
							Iterable<Audit> iterable = auditRepository.findAll();
							iterator = iterable.iterator();
						}
						if(iterator.hasNext()) {
							return iterator.next();
						}
						return null;
					}
				})
                .processor(new AuditToTimeSheetItemProcessor(timeSheetRepository))
                .writer(timeSheetWriter())
                .listener(new ChunkListenerSupport() {
                	@Override
                	public void afterChunk(ChunkContext context) {
                		super.afterChunk(context);
                		log.info("Processing TimeSheet - " + context);
                	}
                })
                .build();
    }
    @Bean
    public Step step1() throws MalformedURLException {
        return stepBuilderFactory.get("step1")
                .<Audit, Audit> chunk(50)
                .reader(reader2())
                .processor(processor())
                .writer(auditWriter())
                .listener(new ChunkListenerSupport() {
                	@Override
                	public void afterChunk(ChunkContext context) {
                		super.afterChunk(context);
                		log.info("Processing Audit - " + context);
                	}
                })
                .build();
    }
    // end::jobstep[]
}
