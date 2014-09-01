package kr.bnnb.play.batch.actors;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import com.avaje.ebean.Ebean;
import kr.bnnb.play.batch.models.JobModel;
import kr.bnnb.play.batch.utils.FixedPeriodCron;
import play.Logger;
import play.libs.Akka;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by dysim on 14. 7. 16.
 */
public class BatchScheduler extends UntypedActor {
	@Override
	public void onReceive(Object message) throws Exception {
		Calendar now = new GregorianCalendar();
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowString = dateFormat.format(now.getTime());
		Logger.debug("BatchScheduler Tick\t" + nowString);

		List<JobModel> prepareList = new ArrayList<>();

		try {
			Ebean.beginTransaction();
			List<JobModel> jobModelList = JobModel.findUseList(nowString);
			for (JobModel job : jobModelList) {
				FixedPeriodCron cron = new FixedPeriodCron(job.timeExpression);
				if (!cron.matches(now)) {
					Logger.debug("JobDismatch\t" + job.jobId + "\t" + job.timeExpression + "\t" + nowString);
					job.lastTickTime = nowString;
					job.update();
					continue;
				}

				job.lastTickTime = nowString;
				job.isRunning = JobModel.IsRunning.Y.name();
				job.update();

				Logger.debug("JobMatch\t" + job.jobId + "\t" + job.timeExpression + "\t" + nowString);
				job.tick = now;
				prepareList.add(job);
			}

			Ebean.commitTransaction();
		} catch (Exception e) {
			Ebean.rollbackTransaction();
			Logger.error(e.getMessage(), e);
			return;
		} finally {
			Ebean.endTransaction();
		}


		for (JobModel job : prepareList) {
			ActorSelection actor = Akka.system().actorSelection("user/" + job.code);
			actor.tell(job, actor.anchor());
		}
	}
}
