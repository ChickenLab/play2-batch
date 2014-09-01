package kr.bnnb.play.batch.models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import org.joda.time.DateTime;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dysim on 14. 7. 16.
 */
@Entity
@Table(name="job_history")
public class JobHistoryModel extends Model {
	public enum State {
		NEW,
		RUN,
		FINISH,
		ERROR,
		SUSPEND
	}

	@Id
	public int jobHistoryId;
	public int jobId;
	public Date startTime;
	public Date finishTime;
	public String state;
	public String result;
	public String runIp;

	public static Finder<Integer, JobHistoryModel> find = new Finder<>(Integer.class, JobHistoryModel.class);

	public static List<JobModel> summaryJobHistory() {
		String sql = "SELECT j.job_id, j.name, jh.state, count(*) cnt " +
				"FROM job_history jh join job j on jh.job_id = j.job_id " +
				"WHERE jh.finish_time >= :start " +
				"AND jh.finish_time <= :end " +
				"GROUP BY j.job_id, j.name, jh.state ";
		SqlQuery query = Ebean.createSqlQuery(sql);
		query.setParameter("start", getYesterdayStart());
		query.setParameter("end", getYesterdayEnd());

		List<JobModel> jobList = new ArrayList<>();
		List<SqlRow> rows = query.findList();
		for (SqlRow row : rows) {
			JobModel job = new JobModel();
			job.jobId = row.getInteger("job_id");
			job.name = row.getString("name");
			job.state = row.getString("state");
			job.count = row.getInteger("cnt");
			jobList.add(job);
		}

		return jobList;
	}

	private static String getYesterdayStart() {
		DateTime dt = new DateTime();
		DateTime yesterday = dt.minusDays(1);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		return dateFormat.format(yesterday.toDate().getTime());
	}

	private static String getYesterdayEnd() {
		DateTime dt = new DateTime();
		DateTime yesterday = dt.minusDays(1);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		return dateFormat.format(yesterday.toDate().getTime());
	}
}
