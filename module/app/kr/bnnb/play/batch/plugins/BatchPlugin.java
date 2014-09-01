package kr.bnnb.play.batch.plugins;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import kr.bnnb.play.batch.actors.BatchScheduler;
import play.Application;
import play.Plugin;
import play.libs.Akka;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class BatchPlugin extends Plugin {
	private final Application application;

	public BatchPlugin(Application application) {
		this.application = application;
	}

	@Override
	public void onStart() {
		ActorSystem akkaSystem = Akka.system();

		ActorRef scheduleActor = akkaSystem.actorOf(Props.create(BatchScheduler.class), "batch_scheduler");
		akkaSystem.scheduler().schedule(
				Duration.create(0, TimeUnit.MILLISECONDS),
				Duration.create(1, TimeUnit.MINUTES),
				scheduleActor,
				"tick",
				akkaSystem.dispatcher(),
				null);

		super.onStart();
	}
}
