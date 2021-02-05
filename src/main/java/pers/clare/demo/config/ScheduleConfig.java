package pers.clare.demo.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pers.clare.core.scheduler.*;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class ScheduleConfig implements InitializingBean {
    @Autowired
    Scheduler scheduler;


    @Override
    public void afterPropertiesSet() throws Exception {
        Job job = new Job("Test","test","test",true,"+08:00","0/10 * * * * ?",true,new HashMap<>());

        scheduler.handler(job.getGroup(),job.getName(),(eventJob)->{
           System.out.println(eventJob);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        scheduler.add(job);

    }

    @Bean
    public JobStore jobStore(
            DataSource dataSource
    ) {
        return new JdbcJobStore(dataSource);
    }

    @Bean
    public Scheduler scheduler(
            @Autowired JobStore jobStore
            , @Value("${cache.notify.topic:default}") String topic
           , @Autowired(required = false) ScheduleMQService scheduleMQService
    ) {
        return new Scheduler(jobStore,topic,scheduleMQService);
    }

}
