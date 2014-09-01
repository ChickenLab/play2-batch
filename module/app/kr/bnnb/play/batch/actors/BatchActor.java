package kr.bnnb.play.batch.actors;

import akka.actor.UntypedActor;
import kr.bnnb.play.batch.models.JobHistoryModel;
import kr.bnnb.play.batch.models.JobModel;
import org.joda.time.DateTime;
import play.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Created by dysim on 14. 7. 15.
 */
abstract class BatchActor extends UntypedActor {
	public class BatchResult {
		private String result;
		private JobHistoryModel.State state;

		public BatchResult(JobHistoryModel.State state, String result) {
			this.state = state;
			this.result = result;
		}
	}

	public class BatchSuspendException extends Exception {}

	private int jobHistoryId;
	private JobModel job;
	private BatchResult batchResult;

	abstract public BatchResult process() throws Exception;

	@Override
	final public void onReceive(Object message) throws Exception {
		if (!(message instanceof JobModel)) {
			return;
		}

		job = (JobModel)message;
		preProcess();

		try {
			batchResult = process();
			if (batchResult.state == JobHistoryModel.State.NEW ||
				batchResult.state == JobHistoryModel.State.RUN) {
				batchResult.state = JobHistoryModel.State.ERROR;
			}
		} catch (BatchSuspendException e) {
			Logger.error(e.getMessage(), e);
			batchResult = createResult(JobHistoryModel.State.SUSPEND, e.getMessage());
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			batchResult = createResult(JobHistoryModel.State.ERROR, e.getMessage());
		}

		postProcess();
	}

	private void preProcess() {
		JobHistoryModel jobHistoryModel = new JobHistoryModel();
		jobHistoryModel.startTime = new Date();
		jobHistoryModel.jobId = job.jobId;
		jobHistoryModel.state = JobHistoryModel.State.RUN.name();
		jobHistoryModel.runIp = "";
		jobHistoryModel.save();

		this.jobHistoryId = jobHistoryModel.jobHistoryId;
	}


	private void postProcess() {
		JobHistoryModel jobHistoryModel = JobHistoryModel.find.byId(jobHistoryId);
		jobHistoryModel.finishTime = new Date();
		jobHistoryModel.state = batchResult.state.name();
		jobHistoryModel.result = batchResult.result;
		try {
			 String address = InetAddress.getLocalHost().getHostAddress();
			String name = InetAddress.getLocalHost().getHostName();

			jobHistoryModel.runIp = InetAddress.getLocalHost().getHostAddress() + "/" + name;
		} catch (UnknownHostException e) {
			Logger.error(e.getMessage(), e);
		}

		jobHistoryModel.update();

		JobModel jobModel = JobModel.findForUpdate(jobHistoryModel.jobId);
		jobModel.isRunning = JobModel.IsRunning.N.name();
		jobModel.update();
	}

	public BatchResult createResult(JobHistoryModel.State state, String result) {
		return new BatchResult(state, result);
	}

	public DateTime getTick() {
		return new DateTime(job.tick.getTime());
	}
}
