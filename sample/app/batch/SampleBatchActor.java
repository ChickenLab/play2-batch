package batch;

import kr.bnnb.play.batch.actors.BatchActor;
import kr.bnnb.play.batch.models.JobHistoryModel;
import play.Logger;

/**
 * Created by dysim on 14. 9. 1.
 */
public class SampleBatchActor extends BatchActor {
	@Override
	public BatchResult process() throws Exception {
		Logger.debug("SampleBatchActor Process");
		return createResult(JobHistoryModel.State.FINISH, "SampleBatchActor Result");
	}
}
