package kr.bnnb.play.batch.models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by dysim on 14. 7. 16.
 */
@Entity
@Table(name="job")
public class JobModel extends Model {
	public enum State {
		USE,
		NOT_USE
	}

	public enum IsRunning {
		Y,
		N
	}

	@Id
	public int jobId;
	public String name;
	public String code;
	public String timeExpression;
	public Date regTime;
	public Date modTime;
	public String state;
	public String isRunning;
	public String lastTickTime;

	@Transient
	public Calendar tick;
	@Transient
	public int count;

	public static Finder<Integer, JobModel> find = new Finder<>(Integer.class, JobModel.class);

	public static List<JobModel> findUseList(String lastTickTime) {
		return find.setForUpdate(true)
				.where()
				.eq("state", State.USE.name())
				.eq("is_running", IsRunning.N.name())
				.ne("last_tick_time", lastTickTime)
				.findList();
	}

	public static JobModel findForUpdate(int jobId) {
		return find.setForUpdate(true)
				.where()
				.eq("job_id", jobId)
				.findUnique();
	}
}
