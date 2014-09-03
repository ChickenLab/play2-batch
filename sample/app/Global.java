import akka.actor.ActorSystem;
import akka.actor.Props;
import batch.SampleBatchActor;
import play.Application;
import play.GlobalSettings;
import play.libs.Akka;

/**
 * Created by dysim on 14. 9. 3.
 */
public class Global extends GlobalSettings {
	@Override
	public void onStart(Application application) {
		super.onStart(application);

		ActorSystem akkaSystem = Akka.system();
		akkaSystem.actorOf(Props.create(SampleBatchActor.class), "sample");
	}
}
