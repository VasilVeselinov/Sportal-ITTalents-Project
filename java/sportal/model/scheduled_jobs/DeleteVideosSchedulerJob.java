package sportal.model.scheduled_jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sportal.model.service.IVideoService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@EnableScheduling
public class DeleteVideosSchedulerJob implements SchedulerJob {

    @Autowired
    private IVideoService videoService;

    @Override
    @Scheduled(cron =  "0 10 3 ? * MON")
    public void scheduledJob() {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.YYYY_HH.mm.ss")));
        this.videoService.deleteAllWhereArticleIdIsNull();
    }
}
