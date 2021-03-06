package controllers;

import kr.bnnb.play.batch.models.JobHistoryModel;
import kr.bnnb.play.batch.models.JobModel;
import play.data.*;
import static play.data.Form.*;
import play.mvc.*;

import views.html.*;

import java.util.Date;
import java.util.List;

public class Application extends Controller {

    public static Result index() {
		return redirect(routes.Application.list());
    }

	public static Result list() {
		List<JobModel> jobModelList = JobModel.find.findList();
		return ok(list.render(jobModelList));
	}

	public static Result historyList(int id) {
		List<JobHistoryModel> historyModelList = JobHistoryModel.findByJobId(id);
		return ok(historyList.render(historyModelList));
	}

	public static Result create() {
		Form<JobModel> jobForm	= form(JobModel.class);
		return ok(createForm.render(jobForm));
	}

	public static Result save() {
		Form<JobModel> jobForm = form(JobModel.class).bindFromRequest();
		if (jobForm.hasErrors()) {
			return badRequest(createForm.render(jobForm));
		}

		JobModel.saveNewJob(jobForm.get());
		flash("success", "Job " + jobForm.get().name + " has been created");
		return redirect(routes.Application.list());
	}

	public static Result edit(int id) {
		Form<JobModel> jobForm = form(JobModel.class).fill(JobModel.find.byId(id));
		return ok(editForm.render(id, jobForm));
	}

	public static Result update(int id) {
		Form<JobModel> jobForm = form(JobModel.class).bindFromRequest();
		if (jobForm.hasErrors()) {
			return badRequest(editForm.render(id, jobForm));
		}

		JobModel.updateJob(id, jobForm.get());

		flash("success", "Job " + jobForm.get().name + " has been updated");
		return redirect(routes.Application.list());
	}

	public static Result markAsUse(int jobId) {
		JobModel job = JobModel.markAsUse(jobId);
		if (job == null) {
			flash("fail", "Job does not exists");
		} else {
			flash("success", "Job " + job.name + " state is USE");
		}

		return redirect(routes.Application.list());
	}

	public static Result markAsNotUse(int jobId) {
		JobModel job = JobModel.markAsNotUse(jobId);
		if (job == null) {
			flash("fail", "Job does not exists");
		} else {
			flash("success", "Job " + job.name + " state is NOT USE");
		}
		return redirect(routes.Application.list());
	}
}
